package com.gigigenie.domain.product.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "category_name", nullable = false, length = 50)
    private String categoryName;

    @Size(max = 255)
    @NotNull
    @Column(name = "category_icon", nullable = false)
    private String categoryIcon;

    @JsonManagedReference
    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

}