package com.gigigenie.domain.chat.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SummaryClient {

    private final WebClient webClient;
    private final String CHAT_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String PROMPT_TEMPLATE = """
            다음은 전자제품의 사용설명서입니다. 이 제품이 어떤 제품인지 사람에게 설명하듯 핵심 특징만 뽑아 자연어로 요약해줘. 
            예시 출력:
            "iOS 기반 스마트폰이며, 트리플 카메라를 탑재했고 2022년 애플에서 출시됨"
            [문서 입력]
            %s
            """;

    public SummaryClient(@Value("${openai.api.key}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl(CHAT_API_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Mono<String> summarizeAsync(String text) {
        String limitedText = text.length() > 3000 ? text.substring(0, 3000) : text;
        String prompt = String.format(PROMPT_TEMPLATE, limitedText);

        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4");
        requestBody.put("messages", List.of(message));
        requestBody.put("temperature", 0.3);

        return webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    if (response != null && response.containsKey("choices")) {
                        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                        if (!choices.isEmpty()) {
                            Map<String, Object> choice = choices.get(0);
                            Map<String, Object> messageObj = (Map<String, Object>) choice.get("message");
                            return (String) messageObj.get("content");
                        }
                    }
                    return "요약 생성 실패";
                })
                .onErrorReturn("요약 생성 중 오류 발생");
    }

    public String summarize(String text) {
        return summarizeAsync(text).block();
    }
}