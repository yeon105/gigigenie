package com.gigigenie.domain.ai.dto;

import lombok.Data;
import java.util.List;

@Data
public class EmbeddingResponse {
    private List<DataItem> data;

    @Data
    public static class DataItem {
        private List<Float> embedding;
    }

    public List<Float> getEmbedding() {
        return (data != null && !data.isEmpty()) ? data.get(0).getEmbedding() : null;
    }
}
