package com.gigigenie.controller;

import com.gigigenie.dto.DocumentDataDTO;
import com.gigigenie.dto.EmbeddingRequestDTO;
import com.gigigenie.service.EmbeddingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class EmbeddingController {
    private final EmbeddingService embeddingService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadEmbeddings(@RequestBody EmbeddingRequestDTO request) {
        try {
            log.info("📌 Received embedding request: {}", request);

            if (request.getEmbeddingList() == null || request.getEmbeddingList().isEmpty()) {
                return ResponseEntity.badRequest().body("❌ 잘못된 데이터 형식입니다.");
            }

            embeddingService.processEmbedding(request);
            return ResponseEntity.ok("✅ 임베딩 데이터 처리 완료!");
        } catch (Exception e) {
            log.error("❌ 임베딩 처리 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ 임베딩 처리 중 오류 발생: " + e.getMessage());
        }
    }

    @GetMapping("/collections")
    public ResponseEntity<List<String>> listCollections() {
        List<String> collections = embeddingService.getCollections();
        return ResponseEntity.ok(collections);
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchDocuments(@RequestParam String query,
                                             @RequestParam String collectionName,
                                             @RequestParam(defaultValue = "5") int topK) {
        List<DocumentDataDTO> results = embeddingService.searchDocuments(query, collectionName, topK);
        return ResponseEntity.ok(results);
    }
}
