package com.gigigenie.domain.chat.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gigigenie.domain.product.entity.Product;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "embedding")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Embedding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonBackReference
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @JdbcTypeCode(SqlTypes.OTHER)
    @Column(columnDefinition = "vector")
    private List<Float> embedding;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String document;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> cmetadata;

}
