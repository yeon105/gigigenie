package com.gigigenie.domain.chat.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmbeddingService {

    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;
    private final String EMBEDDING_API_URL = "https://api.openai.com/v1/embeddings";
    private final String MODEL = "text-embedding-3-small";

    @Value("${openai.api.key}")
    private String openaiApiKey;

    private WebClient getWebClient() {
        return webClientBuilder
                .baseUrl(EMBEDDING_API_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + openaiApiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Data
    public static class EmbeddingRequest {
        private String model;
        private List<String> input;

        public EmbeddingRequest(String model, List<String> input) {
            this.model = model;
            this.input = input;
        }
    }

    @Data
    public static class EmbeddingResponse {
        private String object;
        private List<EmbeddingData> data;
        private String model;
        private Usage usage;

        @Data
        public static class EmbeddingData {
            private String object;
            private List<Float> embedding;
            private int index;
        }

        @Data
        public static class Usage {
            @JsonProperty("prompt_tokens")
            private int promptTokens;
            @JsonProperty("total_tokens")
            private int totalTokens;
        }
    }

    public Mono<List<Float>> createEmbedding(String text) {
        EmbeddingRequest request = new EmbeddingRequest(
                MODEL,
                List.of(text)
        );

        return getWebClient().post()
                .uri("")
                .bodyValue(request)
                .retrieve()
                .onStatus(status -> status.isError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new RuntimeException("API 오류: " + errorBody))))
                .bodyToMono(String.class)
                .flatMap(responseBody -> {
                    try {
                        EmbeddingResponse response = objectMapper.readValue(responseBody, EmbeddingResponse.class);
                        if (response.getData() == null || response.getData().isEmpty()) {
                            return Mono.error(new RuntimeException("빈 임베딩 응답"));
                        }
                        return Mono.just(response.getData().get(0).getEmbedding());
                    } catch (JsonProcessingException e) {
                        log.error("임베딩 응답 파싱 실패", e);
                        return Mono.error(e);
                    }
                })
                .doOnError(e -> log.error("임베딩 생성 오류: {}", e.getMessage()))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)).jitter(0.3));
    }
}