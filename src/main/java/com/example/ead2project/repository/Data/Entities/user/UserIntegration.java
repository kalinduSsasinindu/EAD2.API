package com.example.ead2project.repository.Data.Entities.user;

import lombok.Data;
import java.util.List;

@Data
public class UserIntegration {
    private CourierSettings fardar;
    private CourierSettings fardarDomestic;
    private CourierSettings royalExpress;
    private List<CourierSettings> courierSettings;
    private List<ECommerceSettings> eCommerceSettings;
    private ECommerceSettings shopify;
    private ECommerceSettings wooCommerce;
    private Integer defaultCourierId;
    private Integer defaultECommerceId;
} 