package com.gigigenie.domain.product.controller;

import com.gigigenie.domain.product.dto.ProductResponse;
import com.gigigenie.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ProductController {

    private final ProductService productService;

    @GetMapping("/product/list")
    public ResponseEntity<List<ProductResponse>> list() {
        List<ProductResponse> list = productService.list();
        return ResponseEntity.ok(list);
    }
}
