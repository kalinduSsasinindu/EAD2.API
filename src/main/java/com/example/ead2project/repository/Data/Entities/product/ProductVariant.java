package com.example.ead2project.repository.Data.Entities.product;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductVariant {
    private Integer variantId;
    private String sku;
    private String name;
    private BigDecimal price;
    private Integer availableQuantity;
    private Integer committedQuantity;
    private boolean isActive;
    private String barcode;
    
    public Integer getOnHandQuantity() {
        int available = availableQuantity != null ? availableQuantity : 0;
        int committed = committedQuantity != null ? committedQuantity : 0;
        return available + committed;
    }
} 