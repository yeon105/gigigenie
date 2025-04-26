package com.gigigenie.domain.prompt.config;

import com.gigigenie.domain.prompt.entity.PromptTemplate;
import com.gigigenie.domain.prompt.repository.PromptRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
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
                            당신은 전자제품 사용설명서 안내를 위한 챗봇입니다.
                            
                            사용자의 질문: %s
                            
                            아래는 검색된 전자제품 사용설명서 문서들의 내용입니다:
                            %s
                            
                            위 사용설명서 내용을 바탕으로 사용자의 질문에 대해 정확도가 높고 사용자가 쉽게 이해할 수 있게 답변해주세요.
                            전문 용어는 가능한 쉽게 설명하고, 필요시 단계별 사용법을 제공해주세요.
                            
                            답변은 마크다운 형식으로 작성해주세요. 다음 마크다운 요소를 활용하세요:
                            - 중요 내용: **굵은 글씨**로 강조
                            - 단계별 설명: 1. 2. 3. 번호 목록 사용
                            - 제목: ## 헤더 사용 (예: ## 제품 사용법)
                            - 주의사항: > 인용구 사용 (예: > 주의: 물에 닿지 않게 하세요)
                            
                            응답은 순수 텍스트로 제공하고 HTML 태그를 사용하지 마세요. 마크다운 구문만 사용하세요.
                            """)
                    .description("Gemini 검색 결과 응답용 프롬프트")
                    .active(true)
                    .lastUpdated(LocalDateTime.now())
                    .build();

            PromptTemplate geminiHistoryPrompt = PromptTemplate.builder()
                    .id("gemini_answer_with_history")
                    .template("""
                            당신은 전자제품 사용설명서 안내를 위한 챗봇입니다.
                            
                            다음은 사용자와의 이전 대화 이력입니다:
                            %s
                            
                            사용자의 최신 질문: %s
                            
                            아래는 검색된 전자제품 사용설명서 문서들의 내용입니다:
                            %s
                            
                            이전 대화 이력과 검색된 사용설명서 문서를 참고하여 사용자의 최신 질문에 대해 정확하고 일관된 답변을 제공해주세요.
                            이전 대화에서 언급했던 정보와 관련해 추가 질문을 하는 경우, 이전 맥락을 유지하여 답변해주세요.
                            전문 용어는 가능한 쉽게 설명하고, 필요시 단계별 사용법을 제공해주세요.
                            
                            답변은 마크다운 형식으로 작성해주세요. 다음 마크다운 요소를 활용하세요:
                            - 중요 내용: **굵은 글씨**로 강조
                            - 단계별 설명: 1. 2. 3. 번호 목록 사용
                            - 제목: ## 헤더 사용 (예: ## 제품 사용법)
                            - 부제목: ### 소제목 사용 (예: ### 청소 방법)
                            - 주의사항: > 인용구 사용 (예: > 주의: 물에 닿지 않게 하세요)
                            - 목록: - 또는 * 사용
                            
                            응답은 순수 텍스트로 제공하고 HTML 태그를 사용하지 마세요. 마크다운 구문만 사용하세요.
                            """)
                    .description("Gemini 대화 이력 포함 검색 결과 응답용 프롬프트")
                    .active(true)
                    .lastUpdated(LocalDateTime.now())
                    .build();

            promptRepository.save(summaryPrompt);
            promptRepository.save(geminiPrompt);
            promptRepository.save(geminiHistoryPrompt);

            log.info("Prompt 초기 데이터 생성 완료");
        }
    }
}