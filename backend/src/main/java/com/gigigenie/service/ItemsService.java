package com.gigigenie.service;

import com.gigigenie.repository.ItemsRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemsService {
    private final ItemsRepository itemsRepository;
    private final JdbcTemplate jdbcTemplate;

    public ItemsService(ItemsRepository itemsRepository, JdbcTemplate jdbcTemplate) {
        this.itemsRepository = itemsRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveEmbedding(List<Double> embedding) {
        String vectorString = vectorToString(embedding);

        String sql = "INSERT INTO items (embedding) VALUES (?::vector)";
        jdbcTemplate.update(sql, vectorString);
    }

    private String vectorToString(List<Double> embedding) {
        return embedding.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "[", "]"));
    }

}
