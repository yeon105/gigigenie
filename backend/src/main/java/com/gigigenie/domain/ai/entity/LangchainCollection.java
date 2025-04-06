package com.gigigenie.domain.ai.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.Map;

@Builder
@Entity
@Table(name = "langchain_pg_collection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LangchainCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uuid;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> cmetadata;

    private String name;
}
