package com.gigigenie.domain.chat.repository;

import com.gigigenie.domain.chat.entity.LangchainCollection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LangchainCollectionRepository extends JpaRepository<LangchainCollection, UUID> {
    Optional<LangchainCollection> findByName(String name);
}
