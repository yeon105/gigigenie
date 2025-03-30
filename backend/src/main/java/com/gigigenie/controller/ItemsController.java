package com.example.test_pgvector.controller;

import com.example.test_pgvector.dto.MultipartInputStreamFileResource;
import com.example.test_pgvector.service.ItemsService;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
public class ItemsController {
    private final ItemsService itemsService;
    private final RestTemplate restTemplate;

    public ItemsController(ItemsService itemsService) {
        this.itemsService = itemsService;
        this.restTemplate = new RestTemplate();
    }

    @PostMapping("/uploadPdf")
    public ResponseEntity<String> uploadPdf(@RequestParam("file") MultipartFile file) throws IOException {
        try {
            String fastApiUrl = "http://127.0.0.1:8000/upload_pdf/";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.exchange(fastApiUrl, HttpMethod.POST, requestEntity, Map.class);

            List<Double> embedding = (List<Double>) response.getBody().get("embedding");
            itemsService.saveEmbedding(embedding);

            return ResponseEntity.ok("PDF 처리 및 벡터 저장 완료.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("PDF 처리 오류: " + e.getMessage());
        }
    }

}
