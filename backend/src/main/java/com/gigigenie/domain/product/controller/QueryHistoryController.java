package com.gigigenie.domain.product.controller;

import com.gigigenie.domain.product.dto.HistoryRequest;
import com.gigigenie.domain.product.service.QueryHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/chat/history")
@RestController
public class QueryHistoryController {
    private final QueryHistoryService historyService;

    @Operation(summary = "대화내용 저장")
    @PostMapping("/save")
    public void save(@RequestBody HistoryRequest request) {
        historyService.save(request);
    }

}
