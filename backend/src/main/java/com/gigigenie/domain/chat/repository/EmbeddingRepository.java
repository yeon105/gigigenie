package com.gigigenie.domain.chat.repository;

import com.gigigenie.domain.chat.entity.Embedding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmbeddingRepository extends JpaRepository<Embedding, Integer> {
}