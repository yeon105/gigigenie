package com.gigigenie.domain.ai.controller;

import com.gigigenie.domain.ai.dto.AnswerResponseDTO;
import com.gigigenie.domain.ai.dto.QuestionRequestDTO;
import com.gigigenie.domain.ai.service.ChatService;
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

    @PostMapping("/ask")
    public Mono<AnswerResponseDTO> ask(@RequestBody QuestionRequestDTO requestDto) {
        return chatService.getAnswer(requestDto);
    }

}

