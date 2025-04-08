package com.gigigenie.domain.chat.service;

import com.gigigenie.domain.chat.client.EmbeddingClient;
import com.gigigenie.domain.chat.entity.Category;
import com.gigigenie.domain.chat.entity.Embedding;
import com.gigigenie.domain.chat.repository.CategoryRepository;
import com.gigigenie.domain.chat.repository.EmbeddingRepository;
import com.gigigenie.domain.chat.util.PdfTextExtractor;
import com.gigigenie.domain.chat.util.TextSplitter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
@Service
public class PdfService {

    private final PdfTextExtractor extractor;
    private final EmbeddingClient embeddingClient;
    private final EmbeddingRepository embeddingRepository;
    private final CategoryRepository categoryRepository;

    public Map<String, Object> processPdf(MultipartFile file, String category, int chunkSize, int chunkOverlap, String name) {
        String text = extractor.extract(file);
        List<String> chunks = TextSplitter.split(text, chunkSize, chunkOverlap);

        Category cg = categoryRepository.findByCategory(category)
                .orElseGet(() -> categoryRepository.save(
                        Category.builder()
                                .name(name)
                                .category(category)
                                .build()
                ));

        List<Embedding> embeddingEntities = Collections.synchronizedList(new ArrayList<>());

        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < chunks.size(); i++) {
            final int index = i;
            final String chunk = chunks.get(i);

            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    List<Float> embedding = embeddingClient.embed(chunk);

                    Map<String, Object> metadata = new HashMap<>();
                    metadata.put("source", file.getOriginalFilename());
                    metadata.put("chunk_index", index);
                    metadata.put("category", category);
                    metadata.put("product_name", name);

                    Embedding embeddingEntity = new Embedding();
                    embeddingEntity.setCategory(cg);
                    embeddingEntity.setDocument(chunk);
                    embeddingEntity.setCmetadata(metadata);
                    embeddingEntity.setEmbedding(embedding);

                    embeddingEntities.add(embeddingEntity);
                } catch (Exception e) {
                    System.out.println("임베딩 실패 → 생략된 청크: " + chunk.substring(0, Math.min(50, chunk.length())));
                }
            }, executor);

            futures.add(future);
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        embeddingRepository.saveAll(embeddingEntities);
        executor.shutdown();

        return Map.of(
                "status", "success",
                "chunks", chunks.size(),
                "category", category
        );
    }


}
