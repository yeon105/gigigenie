package com.gigigenie.domain.product.repository;

import com.gigigenie.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query(value =
            "SELECT p.product_id FROM product p " +
                    "WHERE p.feature_embedding IS NOT NULL " +
                    "ORDER BY p.feature_embedding <=> CAST(:embedding AS vector) " +
                    "LIMIT :limit",
            nativeQuery = true)
    List<Integer> findSimilarProductIds(@Param("embedding") String embedding, @Param("limit") int limit);

}
