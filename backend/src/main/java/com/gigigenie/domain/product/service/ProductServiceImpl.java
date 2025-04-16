package com.gigigenie.domain.product.service;

import com.gigigenie.domain.chat.client.EmbeddingClient;
import com.gigigenie.domain.product.dto.ProductResponse;
import com.gigigenie.domain.product.entity.Product;
import com.gigigenie.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final EmbeddingClient embeddingClient;

    @Override
    public List<ProductResponse> list() {
        List<Product> products = productRepository.findAllWithCategory();
        return products.stream().map(product -> (
                ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getModelName())
                        .icon(product.getCategory().getCategoryIcon())
                        .build())).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> findSimilarProducts(String query, Integer limit) {
        try {
            List<Float> queryEmbedding = embeddingClient.embed(query);

            if (queryEmbedding == null || queryEmbedding.isEmpty()) {
                log.error("Failed to generate embedding for query: {}", query);
                return List.of();
            }

            String vectorString = convertToVectorString(queryEmbedding);
            log.info("Generated embedding for query: {} (vector size: {})", query, queryEmbedding.size());

            float similarityThreshold = 0.5f;
            List<ProductResponse> products = productRepository.findSimilarProducts(vectorString, limit, similarityThreshold);

            if (products.isEmpty()) {
                log.info("No similar products found for query: {} with threshold {}", query, similarityThreshold);
                return List.of();
            }

            log.info("Found {} similar products for query: {} with threshold {}",
                    products.size(), query, similarityThreshold);

            return products;
        } catch (Exception e) {
            log.error("Error finding similar products for query: {}", query, e);
            return List.of();
        }
    }

    /**
     * List<Float>를 PostgreSQL vector 문자열 형식으로 변환
     * 예: [0.1, 0.2, 0.3] -> "[0.1,0.2,0.3]"
     */
    private String convertToVectorString(List<Float> embedding) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < embedding.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(embedding.get(i));
        }
        sb.append("]");
        return sb.toString();
    }
}
