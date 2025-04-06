package com.gigigenie.domain.ai.repository;

import com.gigigenie.domain.ai.entity.LangchainEmbedding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LangchainEmbeddingRepository extends JpaRepository<LangchainEmbedding, Long> {}