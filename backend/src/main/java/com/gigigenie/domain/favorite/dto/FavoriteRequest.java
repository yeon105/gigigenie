package com.gigigenie.domain.favorite.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FavoriteRequest {
    private Integer memberId;
    private Integer productId;
}
