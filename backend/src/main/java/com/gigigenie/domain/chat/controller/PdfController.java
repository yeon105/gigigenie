package com.gigigenie.domain.chat.controller;

import com.gigigenie.domain.chat.service.PdfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    private final PdfService pdfService;

    @Operation(
            summary = "PDF 파일 업로드 및 임베딩 처리",
            description = "PDF 업로드 → 텍스트 추출 → 임베딩 → vectorDB 저장"
    )
    @PostMapping("/upload")
    public ResponseEntity<?> uploadPdf(
            @Parameter(description = "업로드할 PDF 파일", required = true)
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "카테고리 이름", required = true)
            @RequestParam("category") String category,
            @Parameter(description = "청크 크기 (기본값: 210)")
            @RequestParam(defaultValue = "210") int chunkSize,
            @Parameter(description = "청크 오버랩 (기본값: 50)")
            @RequestParam(defaultValue = "50") int chunkOverlap,
            @Parameter(description = "제품 이름", required = true)
            @RequestParam("name") String name
    ) {
        if (!Objects.requireNonNull(file.getOriginalFilename()).endsWith(".pdf")) {
            return ResponseEntity.badRequest().body("PDF 파일만 지원합니다.");
        }

        var result = pdfService.processPdf(file, category, chunkSize, chunkOverlap, name);
        return ResponseEntity.ok(result);
    }
}
