package com.example.test_pgvector.repository;

import com.example.test_pgvector.entity.Items;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemsRepository extends JpaRepository<Items, Integer> {
}
