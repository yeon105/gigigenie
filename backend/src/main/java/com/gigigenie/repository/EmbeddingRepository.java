package com.gigigenie.repository;

import com.gigigenie.entity.LangchainEmbedding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface EmbeddingRepository extends JpaRepository<LangchainEmbedding, UUID> {

    @Query("SELECT DISTINCT e.collection.name FROM LangchainEmbedding e")
    List<String> findDistinctCollections();

    @Query(value = """
        SELECT * FROM langchain_pg_embedding
        WHERE collection_id = (SELECT uuid FROM langchain_pg_collection WHERE name = :collectionName)
        ORDER BY embedding <-> (SELECT embedding FROM langchain_pg_embedding WHERE document LIKE CONCAT('%', :query, '%') LIMIT 1)
        LIMIT :topK
    """, nativeQuery = true)
    List<LangchainEmbedding> findTopKSimilarDocuments(@Param("query") String query,
                                                      @Param("collectionName") String collectionName,
                                                      @Param("topK") int topK);
}
