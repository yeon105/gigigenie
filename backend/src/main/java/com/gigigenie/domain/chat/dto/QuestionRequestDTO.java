package com.gigigenie.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequestDTO {
    private String query;

    @JsonProperty("collection_name")
    private String collection_name = "embedding";

    @JsonProperty("top_k")
    private int top_k = 3;
}


