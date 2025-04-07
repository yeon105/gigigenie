package com.gigigenie.domain.ai.repository;

import com.gigigenie.domain.ai.entity.Embedding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmbeddingRepository extends JpaRepository<Embedding, Long> {}