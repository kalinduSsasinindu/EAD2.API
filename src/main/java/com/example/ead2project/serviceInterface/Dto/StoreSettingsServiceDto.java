package com.example.ead2project.serviceInterface.Dto;

import lombok.Data;
import java.util.List;

@Data
public class StoreSettingsServiceDto {
    private String name;
    private String type;
    private String companyName;
    private String companyEmail;
    private String address;
    private List<ShippingRateServiceDto> shippingRates;
} 