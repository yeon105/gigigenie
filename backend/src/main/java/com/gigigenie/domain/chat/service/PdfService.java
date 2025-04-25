package com.gigigenie.domain.chat.service;

import com.gigigenie.domain.chat.client.SummaryClient;
import com.gigigenie.domain.chat.entity.LangchainCollection;
import com.gigigenie.domain.chat.entity.LangchainEmbedding;
import com.gigigenie.domain.chat.repository.LangchainCollectionRepository;
import com.gigigenie.domain.chat.repository.LangchainEmbeddingRepository;
import com.gigigenie.domain.chat.util.PdfTextExtractor;
import com.gigigenie.domain.chat.util.TextSplitter;
import com.gigigenie.domain.notification.service.NotificationService;
import com.gigigenie.domain.product.entity.Category;
import com.gigigenie.domain.product.entity.Product;
import com.gigigenie.domain.product.repository.CategoryRepository;
import com.gigigenie.domain.product.repository.ProductRepository;
import com.gigigenie.util.files.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PdfService {

    private final PdfTextExtractor extractor;
    private final EmbeddingService embeddingService;
    private final SummaryClient summaryClient;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final LangchainCollectionRepository collectionRepository;
    private final LangchainEmbeddingRepository embeddingRepository;
    private final CustomFileUtil fileUtil;
    private final NotificationService notificationService;

    @Transactional
    public Map<String, Object> processPdf(MultipartFile file, Integer categoryId, int chunkSize,
                                          int chunkOverlap, String name, MultipartFile image, Integer memberId) {
        Optional<Product> existingProduct = productRepository.findByModelName(name);
        if (existingProduct.isPresent()) {
            return Map.of(
                    "status", "exists",
                    "message", "이미 등록된 모델입니다.",
                    "model_name", existingProduct.get().getModelName()
            );
        }

        String fileKey = fileUtil.uploadS3File(file);
        log.info("PDF 파일 S3 업로드 완료: {}", fileKey);

        String imageUrl = null;

        if (image != null && !image.isEmpty()) {
            String imageKey = fileUtil.uploadS3File(image);
            imageUrl = fileUtil.getS3Url(imageKey);
            log.info("이미지 S3 업로드 완료: {}, URL: {}", imageKey, imageUrl);
        }

        String text = extractor.extract(file);
        String summary = summaryClient.summarize(text);
        log.info("생성된 요약: {}", summary);

        List<Float> summaryEmbedding = embeddingService.createEmbedding(summary).block();
        log.info("summaryEmbedding: {}", summaryEmbedding);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = Product.builder()
                .category(category)
                .modelName(name)
                .modelImage(imageUrl)
                .createdAt(LocalDateTime.now())
                .featureSummary(summary)
                .featureEmbedding(summaryEmbedding)
                .build();

        productRepository.save(product);

        if (memberId != null) {
            notificationService.addNotification(
                    memberId,
                    name + " 제품이 성공적으로 등록되었습니다.",
                    "제품 등록 완료"
            );
        }

        List<String> chunks = TextSplitter.split(text, chunkSize, chunkOverlap);

        String collectionName = "product_" + product.getId() + "_embeddings";
        UUID collectionUuid = UUID.randomUUID();

        LangchainCollection collection = LangchainCollection.builder()
                .uuid(collectionUuid)
                .name(collectionName)
                .cmetadata(Map.of(
                        "product_id", product.getId(),
                        "model_name", product.getModelName(),
                        "created_at", product.getCreatedAt().toString()
                ))
                .build();
        collectionRepository.save(collection);

        List<LangchainEmbedding> embeddingEntities = Collections.synchronizedList(new ArrayList<>());

        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < chunks.size(); i++) {
            final int index = i;
            final String chunk = chunks.get(i);

            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    List<Float> vector = embeddingService.createEmbedding(chunk).block();
                    LangchainEmbedding embedding = LangchainEmbedding.builder()
                            .collection(collection)
                            .embedding(vector)
                            .document(chunk)
                            .cmetadata(Map.of(
                                    "chunk_index", index,
                                    "source", file.getOriginalFilename(),
                                    "product_id", product.getId()
                            ))
                            .build();

                    embeddingEntities.add(embedding);
                } catch (Exception e) {
                    log.warn("임베딩 실패 (index: {}): {}", index, chunk.length() > 50 ? chunk.substring(0, 50) + "..." : chunk);
                }
            }, executor);

            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        executor.shutdown();

        embeddingRepository.saveAll(embeddingEntities);

        return Map.of(
                "status", "success",
                "collection_name", collectionName,
                "collection_uuid", collectionUuid.toString(),
                "chunks_saved", embeddingEntities.size(),
                "product_id", product.getId(),
                "summary", summary
        );
    }

}
