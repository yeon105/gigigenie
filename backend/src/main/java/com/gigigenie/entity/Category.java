package com.gigigenie.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId; // 카테고리 ID

    @Column(nullable = false, length = 10)
    private String categoryName; // 카테고리 이름

    @Column
    private String categoryIcon; // 아이콘 URL
}

