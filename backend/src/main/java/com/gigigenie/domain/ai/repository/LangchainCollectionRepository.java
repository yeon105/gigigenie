package com.gigigenie.domain.ai.repository;

import com.gigigenie.domain.ai.entity.LangchainCollection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LangchainCollectionRepository extends JpaRepository<LangchainCollection, Long> {
    Optional<LangchainCollection> findByName(String name);
}