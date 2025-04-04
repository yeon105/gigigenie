package com.gigigenie.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmbeddingRequestDTO {
    private String collectionName; // ✅ 컬렉션 이름 추가
    private int chunkSize;
    private int chunkOverlap;
    private List<List<Float>> embeddingList; // ✅ 다차원 리스트로 변경
}
