package com.example.ead2project.serviceInterface.Dto;

import lombok.Data;

@Data
public class UserServiceDto {
    private String id;
    private String clientId;
    private String email;
    private boolean isDeleted;
    private UserIntegrationServiceDto userIntegration;
    private StoreSettingsServiceDto storeSettings;
    private PaymentSettingsServiceDto paymentSettings;
} 