package com.gigigenie.repository;

import com.gigigenie.entity.LangchainEmbedding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LangchainEmbeddingRepository extends JpaRepository<LangchainEmbedding, Long> {
    List<LangchainEmbedding> findByCollection_Id(Long collectionId);
}
