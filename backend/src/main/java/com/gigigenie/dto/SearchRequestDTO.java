package com.gigigenie.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchRequestDTO {
    private String query;
    private String collectionName;
    private int topK;

    public String getQuery() {
        return query;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public int getTopK() {
        return topK;
    }

}


