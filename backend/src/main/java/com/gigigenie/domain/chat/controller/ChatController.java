package com.gigigenie.domain.chat.controller;

import com.gigigenie.domain.chat.dto.AnswerResponseDTO;
import com.gigigenie.domain.chat.dto.QuestionRequestDTO;
import com.gigigenie.domain.chat.dto.SearchResponseDTO;
import com.gigigenie.domain.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "제품 사용설명서 기반 질의응답")
    @PostMapping("/ask")
    public Mono<AnswerResponseDTO> ask(@RequestBody QuestionRequestDTO requestDto) {
        return chatService.getAnswer(requestDto);
    }

    @PostMapping("/search")
    public Mono<SearchResponseDTO> search(@RequestBody QuestionRequestDTO requestDto) {
        return chatService.getSearchResults(requestDto);
    }
}
