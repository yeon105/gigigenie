package com.gigigenie.domain.product.repository;

import com.gigigenie.domain.product.dto.ProductResponse;
import com.gigigenie.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Product p JOIN FETCH p.category")
    List<Product> findAllWithCategory();

    @Query(value =
            "SELECT p.product_id as id, p.model_name as name, p.model_image as image, c.category_icon as icon " +
                    "FROM product p " +
                    "JOIN category c ON p.category_id = c.category_id " +
                    "JOIN (SELECT product_id, feature_embedding FROM product " +
                    "      WHERE feature_embedding IS NOT NULL) as p_inner ON p.product_id = p_inner.product_id " +
                    "WHERE 1 - (p_inner.feature_embedding <=> CAST(:embedding AS vector)) >= :threshold " +
                    "ORDER BY p_inner.feature_embedding <=> CAST(:embedding AS vector) " +
                    "LIMIT :limit",
            nativeQuery = true)
    List<ProductResponse> findSimilarProducts(@Param("embedding") String embedding,
                                              @Param("limit") int limit,
                                              @Param("threshold") float threshold);

    @Query("SELECT p FROM Product p WHERE p.modelName = :name")
    Optional<Product> findByModelName(@Param("name") String name);
}
