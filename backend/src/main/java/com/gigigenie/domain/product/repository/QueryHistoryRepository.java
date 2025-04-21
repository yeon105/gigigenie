package com.gigigenie.domain.product.repository;

import com.gigigenie.domain.product.entity.QueryHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueryHistoryRepository extends JpaRepository<QueryHistory, Integer> {
}
