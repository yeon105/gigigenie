package com.gigigenie.domain.product.service;

import com.gigigenie.domain.product.dto.ProductResponse;

import java.util.List;

public interface ProductService {

    List<ProductResponse> list();

    List<ProductResponse> findSimilarProducts(String query, Integer limit);

}
