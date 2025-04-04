package com.gigigenie.service;

import com.gigigenie.entity.LangchainEmbedding;
import com.gigigenie.repository.LangchainEmbeddingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LangchainEmbeddingService {

    private final LangchainEmbeddingRepository embeddingRepository;

    public List<LangchainEmbedding> getEmbeddingsByCollection(Long collectionId) {
        return embeddingRepository.findByCollection_Id(collectionId);
    }

    public LangchainEmbedding saveEmbedding(LangchainEmbedding embedding) {
        return embeddingRepository.save(embedding);
    }
}
