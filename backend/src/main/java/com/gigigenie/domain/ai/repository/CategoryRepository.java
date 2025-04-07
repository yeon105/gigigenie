package com.gigigenie.domain.ai.repository;

import com.gigigenie.domain.ai.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCategory(String category);
}