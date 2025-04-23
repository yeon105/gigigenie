package com.gigigenie.domain.product.controller;

import com.gigigenie.domain.product.dto.HistoryRequest;
import com.gigigenie.domain.product.dto.QueryHistoryDTO;
import com.gigigenie.domain.product.service.QueryHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/chat/history")
@RestController
public class QueryHistoryController {
    private final QueryHistoryService historyService;

    @Operation(summary = "이전 대화내용 불러오기")
    @GetMapping
    public ResponseEntity<List<QueryHistoryDTO>> getHistories(@RequestParam("memberId") Integer memberId, @RequestParam("productId") Integer productId) {
        List<QueryHistoryDTO> dtoList = historyService.getHistories(memberId, productId);
        return ResponseEntity.ok(dtoList);
    }

    @Operation(summary = "대화내용 저장")
    @PostMapping("/save")
    public void save(@RequestBody HistoryRequest request) {
        historyService.save(request);
    }

}
