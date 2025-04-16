package com.gigigenie.domain.chat.service;

import com.gigigenie.domain.chat.dto.AnswerResponseDTO;
import com.gigigenie.domain.chat.dto.QuestionRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final WebClient webClient;

    public Mono<AnswerResponseDTO> getAnswer(QuestionRequestDTO dto) {
        log.info("Sending question to FastAPI - Query: {}, Collection: {}", dto.getQuery(), dto.getCollection_name());

        return webClient.post()
                .uri("/ai/chat/ask")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .map(res -> {
                    log.info("Received answer from FastAPI");
                    return new AnswerResponseDTO((String) res.get("answer"));
                });
    }
}

