package com.example.ead2project.serviceInterface.Dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ShippingRateServiceDto {
    private String name;
    private BigDecimal rate;
    private boolean isDefault;
} 