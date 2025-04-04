package com.gigigenie.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;

@Converter(autoApply = true)
public class EmbeddingConverter implements AttributeConverter<float[], String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(float[] attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("임베딩 데이터를 JSON 문자열로 변환하는 중 오류 발생", e);
        }
    }

    @Override
    public float[] convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, float[].class);
        } catch (IOException e) {
            throw new RuntimeException("JSON 문자열을 임베딩 데이터로 변환하는 중 오류 발생", e);
        }
    }
}
