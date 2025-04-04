package com.gigigenie.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DocumentDataDTO {
    private String document;
    private String metadata;

    public String getDocument() {
        return document;
    }
    public String getMetadata() {
        return metadata;
    }
}
