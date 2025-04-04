package com.gigigenie.service;

import com.gigigenie.dto.DocumentDataDTO;
import com.gigigenie.dto.EmbeddingRequestDTO;
import com.gigigenie.entity.LangchainCollection;
import com.gigigenie.entity.LangchainEmbedding;
import com.gigigenie.repository.EmbeddingRepository;
import com.gigigenie.repository.LangchainCollectionRepository;
import com.gigigenie.util.PDFProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmbeddingService {
    private final EmbeddingRepository embeddingRepository;
    private final LangchainCollectionRepository collectionRepository;
    private final PDFProcessor pdfProcessor;

    public LangchainCollection findOrCreateCollection(String collectionName) {
        return collectionRepository.findByName(collectionName)
                .orElseGet(() -> {
                    LangchainCollection newCollection = new LangchainCollection();
                    newCollection.setUuid(UUID.randomUUID());
                    newCollection.setName(collectionName);
                    return collectionRepository.save(newCollection);
                });
    }

    public void processEmbedding(EmbeddingRequestDTO request) {
        LangchainCollection collection = findOrCreateCollection(request.getCollectionName());

        // ✅ 벡터 데이터 변환 (List<List<Float>> → float[][])
        List<float[]> floatList = request.getEmbeddingList().stream()
                .map(list -> list.stream().map(Float::floatValue).toArray(Float[]::new)) // Float[] 변환
                .map(fArray -> {
                    float[] primitiveArray = new float[fArray.length];
                    for (int i = 0; i < fArray.length; i++) {
                        primitiveArray[i] = fArray[i];
                    }
                    return primitiveArray;
                }) // Float[] → float[]
                .toList();

        float[][] embeddingArray = floatList.toArray(new float[0][]);

        // ✅ 문서 개수와 벡터 개수가 일치하는지 확인
        if (embeddingArray.length == 0) {
            throw new IllegalArgumentException("❌ 임베딩 데이터가 없습니다.");
        }

        for (float[] embedding : embeddingArray) {
            LangchainEmbedding entity = new LangchainEmbedding();
            entity.setCollection(collection);
            entity.setEmbedding(embedding);
            entity.setDocument("임시 문서 내용"); // 🔹 필요 시 수정
            entity.setCmetadata("{}"); // 🔹 필요 시 수정
            embeddingRepository.save(entity);
        }

        log.info("✅ {}개의 임베딩 데이터 저장 완료!", embeddingArray.length);
    }

    public List<String> getCollections() {
        return embeddingRepository.findDistinctCollections();
    }

    public List<DocumentDataDTO> searchDocuments(String query, String collectionName, int topK) {
        return embeddingRepository.findTopKSimilarDocuments(query, collectionName, topK)
                .stream()
                .map(LangchainEmbedding::toDTO)
                .collect(Collectors.toList());
    }
}
