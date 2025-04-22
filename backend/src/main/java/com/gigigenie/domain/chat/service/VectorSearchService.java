package com.gigigenie.domain.chat.service;

import com.gigigenie.domain.chat.dto.SearchResultDTO;
import com.gigigenie.domain.chat.entity.LangchainCollection;
import com.gigigenie.domain.chat.repository.LangchainCollectionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VectorSearchService {

    @PersistenceContext
    private EntityManager entityManager;

    private final LangchainCollectionRepository collectionRepository;
    private final EmbeddingService embeddingService;

    public Mono<List<SearchResultDTO>> searchSimilarDocuments(String query, String collectionName, int topK) {
        return embeddingService.createEmbedding(query)
                .flatMap(embedding -> {
                    Optional<LangchainCollection> collectionOpt = collectionRepository.findByName(collectionName);

                    if (collectionOpt.isEmpty()) {
                        log.error("Collection not found: {}", collectionName);
                        return Mono.error(new RuntimeException("Collection not found: " + collectionName));
                    }

                    LangchainCollection collection = collectionOpt.get();
                    List<SearchResultDTO> results = performVectorSearch(embedding, collection, topK);
                    return Mono.just(results);
                });
    }

    private List<SearchResultDTO> performVectorSearch(List<Float> queryEmbedding, LangchainCollection collection, int topK) {
        String vectorString = queryEmbedding.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "[", "]"));

        String sql = "SELECT e.id, e.document, e.cmetadata, " +
                "1 - (e.embedding <=> cast(:queryVector as vector)) as similarity " +
                "FROM langchain_pg_embedding e " +
                "WHERE e.collection_id = :collectionId " +
                "ORDER BY similarity DESC " +
                "LIMIT :topK";

        List<Object[]> results = entityManager.createNativeQuery(sql)
                .setParameter("queryVector", vectorString)
                .setParameter("collectionId", collection.getUuid())
                .setParameter("topK", topK)
                .getResultList();

        List<SearchResultDTO> searchResults = new ArrayList<>();

        for (Object[] row : results) {
            try {
                Long id = row[0] instanceof Number ? ((Number) row[0]).longValue() : null;
                String document = row[1] instanceof String ? (String) row[1] : "";
                Map<String, Object> metadata = row[2] instanceof Map ? (Map<String, Object>) row[2] : Map.of();
                double score = row[3] instanceof Number ? ((Number) row[3]).doubleValue() : 0.0;

                SearchResultDTO result = new SearchResultDTO();
                result.setContent(document);
                result.setMetadata(metadata);
                result.setScore(score);

                searchResults.add(result);
            } catch (Exception e) {
                log.error("Error processing search result row: {}", e.getMessage());
            }
        }

        return searchResults;
    }
}