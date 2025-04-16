package com.gigigenie.domain.chat.service;

import com.gigigenie.domain.chat.client.EmbeddingClient;
import com.gigigenie.domain.chat.client.SummaryClient;
import com.gigigenie.domain.chat.entity.LangchainCollection;
import com.gigigenie.domain.chat.entity.LangchainEmbedding;
import com.gigigenie.domain.chat.repository.LangchainCollectionRepository;
import com.gigigenie.domain.chat.repository.LangchainEmbeddingRepository;
import com.gigigenie.domain.chat.util.PdfTextExtractor;
import com.gigigenie.domain.chat.util.TextSplitter;
import com.gigigenie.domain.product.entity.Category;
import com.gigigenie.domain.product.entity.Product;
import com.gigigenie.domain.product.repository.CategoryRepository;
import com.gigigenie.domain.product.repository.ProductRepository;
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
@Service
public class PdfService {

    private final PdfTextExtractor extractor;
    private final EmbeddingClient embeddingClient;
    private final SummaryClient summaryClient;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final LangchainCollectionRepository collectionRepository;
    private final LangchainEmbeddingRepository embeddingRepository;

    @Transactional
    public Map<String, Object> processPdf(MultipartFile file, Integer categoryId, int chunkSize, int chunkOverlap, String name) {
        Optional<Product> existingProduct = productRepository.findByModelName(name);
        if (existingProduct.isPresent()) {
            return Map.of(
                    "status", "exists",
                    "message", "이미 등록된 모델입니다.",
                    "model_name", existingProduct.get().getModelName()
            );
        }

        String text = extractor.extract(file);
        String summary = summaryClient.summarize(text);
        log.info("생성된 요약: {}", summary);

        List<Float> summaryEmbedding = embeddingClient.embed(summary);
        log.info("summaryEmbedding: {}", summaryEmbedding);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = Product.builder()
                .category(category)
                .modelName(name)
                .createdAt(LocalDateTime.now())
                .featureSummary(summary)
                .featureEmbedding(summaryEmbedding)
                .build();

        productRepository.save(product);

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
                    List<Float> vector = embeddingClient.embed(chunk);
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
