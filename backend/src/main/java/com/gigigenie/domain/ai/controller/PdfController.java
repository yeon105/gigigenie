package com.gigigenie.domain.ai.controller;

import com.gigigenie.domain.ai.service.PdfService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    private final PdfService pdfService;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPdf(
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "langchain_pg_collection") String collection,
            @RequestParam(defaultValue = "210") int chunkSize,
            @RequestParam(defaultValue = "50") int chunkOverlap
    ) {
        if (!file.getOriginalFilename().endsWith(".pdf")) {
            return ResponseEntity.badRequest().body("PDF 파일만 지원합니다.");
        }

        var result = pdfService.processPdf(file, collection, chunkSize, chunkOverlap);
        return ResponseEntity.ok(result);
    }
}
