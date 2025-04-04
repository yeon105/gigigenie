package com.gigigenie.controller;

import com.gigigenie.entity.LangchainEmbedding;
import com.gigigenie.service.LangchainEmbeddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/embeddings")
@RequiredArgsConstructor
public class LangchainEmbeddingController {

    private final LangchainEmbeddingService embeddingService;

    @GetMapping("/{collectionId}")
    public ResponseEntity<List<LangchainEmbedding>> getEmbeddings(@PathVariable Long collectionId) {
        List<LangchainEmbedding> embeddings = embeddingService.getEmbeddingsByCollection(collectionId);
        return ResponseEntity.ok(embeddings);
    }

    @PostMapping("/save")
    public ResponseEntity<LangchainEmbedding> saveEmbedding(@RequestBody LangchainEmbedding embedding) {
        LangchainEmbedding savedEmbedding = embeddingService.saveEmbedding(embedding);
        return ResponseEntity.ok(savedEmbedding);
    }
}
