package com.gigigenie.domain.chat.repository;

import com.gigigenie.domain.chat.entity.LangchainEmbedding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LangchainEmbeddingRepository extends JpaRepository<LangchainEmbedding, Long> {
}
