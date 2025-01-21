package com.example.ead2project.serviceInterface.Dto;

import lombok.Data;

@Data
public class UserServiceDto {
    private String id;
    private String clientId;
    private String email;
    private UserIntegrationServiceDto userIntegration;
    private boolean isDeleted;
    private StoreSettingsServiceDto storeSettings;
    private PaymentSettingsServiceDto paymentSettings;
} 