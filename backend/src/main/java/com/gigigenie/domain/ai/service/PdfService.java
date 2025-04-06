package com.gigigenie.domain.ai.service;

import com.gigigenie.domain.ai.client.EmbeddingClient;
import com.gigigenie.domain.ai.util.PdfTextExtractor;
import com.gigigenie.domain.ai.util.TextSplitter;
import com.gigigenie.domain.ai.entity.LangchainCollection;
import com.gigigenie.domain.ai.entity.LangchainEmbedding;
import com.gigigenie.domain.ai.repository.LangchainCollectionRepository;
import com.gigigenie.domain.ai.repository.LangchainEmbeddingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PdfService {

    private final PdfTextExtractor extractor;
    private final EmbeddingClient embeddingClient;
    private final LangchainEmbeddingRepository embeddingRepository;
    private final LangchainCollectionRepository collectionRepository;

    public Map<String, Object> processPdf(MultipartFile file, String collection, int chunkSize, int chunkOverlap) {
        String text = extractor.extract(file);
        List<String> chunks = TextSplitter.split(text, chunkSize, chunkOverlap);

        int index = 0; // 청크 인덱스

        for (String chunk : chunks) {
            try {
                List<Float> embedding = embeddingClient.embed(chunk);

                LangchainCollection col = collectionRepository.findByName(collection)
                        .orElseGet(() -> collectionRepository.save(
                                LangchainCollection.builder()
                                        .name(collection)
                                        .cmetadata(new HashMap<>())
                                        .build()
                        ));

                Map<String, Object> metadata = new HashMap<>();
                metadata.put("source", file.getOriginalFilename());
                metadata.put("chunk_index", index);
                metadata.put("collection", collection);

                LangchainEmbedding embeddingEntity = new LangchainEmbedding();
                embeddingEntity.setCollection(col);
                embeddingEntity.setDocument(chunk);
                embeddingEntity.setCmetadata(metadata);
                embeddingEntity.setEmbedding(embedding);

                embeddingRepository.save(embeddingEntity);
                index++;

            } catch (Exception e) {
                System.out.println("임베딩 실패 → 생략된 청크: " + chunk.substring(0, Math.min(50, chunk.length())));
            }
        }

        return Map.of(
                "status", "success",
                "chunks", chunks.size(),
                "collection", collection
        );
    }

}
