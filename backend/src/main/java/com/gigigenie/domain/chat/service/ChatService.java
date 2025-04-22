package com.gigigenie.domain.chat.service;

import com.gigigenie.domain.chat.dto.AnswerResponseDTO;
import com.gigigenie.domain.chat.dto.QuestionRequestDTO;
import com.gigigenie.domain.chat.dto.SearchResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final VectorSearchService vectorSearchService;
    private final GeminiService geminiService;

    public Mono<AnswerResponseDTO> getAnswer(QuestionRequestDTO dto) {
        String collectionName = "product_" + dto.getProductId() + "_embeddings";
        log.info("질문 처리 중 - 쿼리: {}, 컬렉션: {}", dto.getQuery(), collectionName);
        return processQuestion(dto)
                .map(AnswerResponseDTO::new);
    }

    public Mono<SearchResponseDTO> getSearchResults(QuestionRequestDTO dto) {
        String collectionName = "product_" + dto.getProductId() + "_embeddings";
        log.info("전체 검색 처리 중 - 쿼리: {}, 컬렉션: {}", dto.getQuery(), collectionName);
        return processQuestionWithResults(dto);
    }

    private Mono<String> processQuestion(QuestionRequestDTO dto) {
        String collectionName = "product_" + dto.getProductId() + "_embeddings";
        return vectorSearchService.searchSimilarDocuments(dto.getQuery(), collectionName, dto.getTop_k())
                .flatMap(searchResults ->
                        geminiService.generateAnswer(dto.getQuery(), searchResults)
                                .doOnNext(answer -> log.info("쿼리에 대한 답변 생성: {}", dto.getQuery()))
                );
    }

    private Mono<SearchResponseDTO> processQuestionWithResults(QuestionRequestDTO dto) {
        String collectionName = "product_" + dto.getProductId() + "_embeddings";
        return vectorSearchService.searchSimilarDocuments(dto.getQuery(), collectionName, dto.getTop_k())
                .flatMap(searchResults ->
                        geminiService.generateAnswer(dto.getQuery(), searchResults)
                                .map(answer -> {
                                    log.info("쿼리에 대한 답변 생성: {}", dto.getQuery());
                                    return new SearchResponseDTO(dto.getQuery(), answer, searchResults);
                                })
                );
    }
}