package com.gigigenie.repository;

import com.gigigenie.entity.LangchainCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LangchainCollectionRepository extends JpaRepository<LangchainCollection, Long> {
    Optional<LangchainCollection> findByName(String name);
}

