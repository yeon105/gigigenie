package com.gigigenie.domain.chat.service;

import com.gigigenie.domain.chat.dto.AnswerResponseDTO;
import com.gigigenie.domain.chat.dto.QuestionRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final WebClient webClient;

    public Mono<AnswerResponseDTO> getAnswer(QuestionRequestDTO dto) {
        return webClient.post()
                .uri("/api/chat/ask")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .map(res -> new AnswerResponseDTO((String) res.get("answer")));
    }
}

