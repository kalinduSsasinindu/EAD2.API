package com.example.ead2project.serviceInterface.Dto;

import lombok.Data;
import java.util.List;

@Data
public class UserIntegrationServiceDto {
    private List<CourierSettingsServiceDto> courierSettings;
    private List<ECommerceSettingsServiceDto> eCommerceSettings;
    private int defaultCourierId;
    private int defaultECommerceId;
} 