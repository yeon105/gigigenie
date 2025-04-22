package com.gigigenie.domain.prompt.config;

import com.gigigenie.domain.prompt.entity.PromptTemplate;
import com.gigigenie.domain.prompt.repository.PromptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PromptInitializer implements CommandLineRunner {

    private final PromptRepository promptRepository;

    @Override
    public void run(String... args) {
        if (promptRepository.count() == 0) {
            PromptTemplate summaryPrompt = PromptTemplate.builder()
                    .id("summary")
                    .template("""
                            다음은 전자제품의 사용설명서입니다. 이 제품이 어떤 제품인지 사람에게 설명하듯 핵심 특징만 뽑아 자연어로 요약해줘. 
                            예시 출력:
                            "iOS 기반 스마트폰이며, 트리플 카메라를 탑재했고 2022년 애플에서 출시됨"
                            [문서 입력]
                            %s
                            """)
                    .description("제품 설명서 요약용 프롬프트")
                    .active(true)
                    .lastUpdated(LocalDateTime.now())
                    .build();

            PromptTemplate geminiPrompt = PromptTemplate.builder()
                    .id("gemini_answer")
                    .template("""
                            사용자의 질문: %s
                            
                            아래는 검색된 문서들의 내용입니다:
                            %s

                            위 내용을 바탕으로 사용자의 질문에 대해 정확도가 높고 사용자가 쉽게 이해할 수 있게 간단히 요약해서 설명해주세요.
                            """)
                    .description("Gemini 검색 결과 응답용 프롬프트")
                    .active(true)
                    .lastUpdated(LocalDateTime.now())
                    .build();

            promptRepository.save(summaryPrompt);
            promptRepository.save(geminiPrompt);
        }
    }
}