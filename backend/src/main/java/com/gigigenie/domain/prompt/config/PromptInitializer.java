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
                            당신은 전자제품 사용설명서 분석 전문가입니다. 다음 사용설명서를 분석하여 검색 시스템에 최적화된 특징 요약을 생성해주세요.
                            
                            [작업 지침]
                            1. 제품의 기본 정보 파악: 브랜드, 모델명, 제품 유형, 대상 사용자 등
                            2. 주요 사양 추출: 성능, 크기, 무게, 전력, 용량 등 모든 수치 정보
                            3. 핵심 부품 및 기능 식별: 제품의 주요 부품, 특수 기능, 차별화 요소
                            4. 사용 환경 및 목적 파악: 어디서 어떻게 사용하는 제품인지
                            5. 유지보수 및 관리방법: 청소, 교체, 충전, 보관 등 관련 정보
                            6. 특수 기술 용어 추출: 제품별 고유 기술이나 브랜드 용어
                            
                            [출력 형식]
                            아래 형식에 맞게 제품 특징을 요약해주세요:
                            
                            제품기본: [브랜드] [모델명] [제품 유형]에 대한 사용설명서입니다.
                            
                            주요사양: [모든 기술적 사양, 정확한 수치와 단위 포함]
                            
                            핵심기능: [제품의 주요 기능 및 부품에 대한 상세 설명, 각 기능의 특징과 장점 포함]
                            
                            사용목적: [제품이 사용되는 환경, 상황, 용도]
                            
                            유지관리: [제품 관리 방법, 부품 교체/세척 주기, 보관 방법 등]
                            
                            주요용어: [제품 관련 특수 용어와 그 의미]
                            
                            검색키워드: [사용자가 이 제품을 찾을 때 사용할 만한 다양한 검색어 15-20개, 쉼표로 구분]
                            
                            [입력 문서]
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