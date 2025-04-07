package com.gigigenie.domain.ai.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String category;
    private String name;
}
