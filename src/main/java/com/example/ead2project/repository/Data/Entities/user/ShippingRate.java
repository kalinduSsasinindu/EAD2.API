package com.example.ead2project.repository.Data.Entities.user;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ShippingRate {
    private String name;
    private BigDecimal rate;
    private boolean isDefault;
} 