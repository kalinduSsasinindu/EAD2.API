package com.example.ead2project.repository.Data.Entities.user;

import lombok.Data;
import java.util.List;

@Data
public class StoreSettings {
    private String name;
    private String type;
    private String companyName;
    private String companyEmail;
    private String address;
    private List<ShippingRate> shippingRates;
} 