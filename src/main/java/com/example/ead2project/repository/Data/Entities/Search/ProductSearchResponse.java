package com.example.ead2project.repository.Data.Entities.Search;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

import com.example.ead2project.repository.Data.Entities.product.ProductVariant;

@Data
public class ProductSearchResponse {
    private String id;
    private String title;
    private String description;
    private BigDecimal amount;
    private List<String> imgUrls;
    private List<ProductVariant> variants;
} 