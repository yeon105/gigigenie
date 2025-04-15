package com.gigigenie.domain.chat.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "langchain_pg_collection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LangchainCollection {

    @Id
    @Column(nullable = false)
    private UUID uuid;

    @Column(nullable = false)
    private String name;

    @Type(JsonType.class)
    @Column(name = "cmetadata", columnDefinition = "jsonb")
    private Map<String, Object> cmetadata;
}
