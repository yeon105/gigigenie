package com.gigigenie.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "favorite")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriteId; // 즐겨찾기 ID

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 회원 ID (FK)

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // 제품 ID (FK)
}

