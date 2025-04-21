package com.gigigenie.domain.product.controller;

import com.gigigenie.domain.product.dto.HistoryRequest;
import com.gigigenie.domain.product.service.QueryHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/history")
@RestController
public class QueryHistoryController {
    private final QueryHistoryService historyService;

    @PostMapping("/save")
    public void save(@RequestBody HistoryRequest request) {
        historyService.save(request);
    }

}
