package com.gigigenie.domain.product.controller;

import com.gigigenie.domain.product.dto.ProductResponse;
import com.gigigenie.domain.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "제품 전체 조회")
    @GetMapping("/product/list")
    public ResponseEntity<List<ProductResponse>> list() {
        List<ProductResponse> list = productService.list();
        return ResponseEntity.ok(list);
    }

    @Operation(
            summary = "질문 기반 제품 검색",
            description = "사용자 질문과 유사한 제품을 최대 3개까지 반환합니다"
    )
    @GetMapping("/product/search")
    public ResponseEntity<List<ProductResponse>> searchSimilarProducts(@RequestParam String query) {
        List<ProductResponse> products = productService.findSimilarProducts(query, 3);
        return ResponseEntity.ok(products);
    }
}
