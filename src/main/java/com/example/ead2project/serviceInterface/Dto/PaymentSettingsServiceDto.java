package com.example.ead2project.serviceInterface.Dto;

import lombok.Data;
import java.util.List;

@Data
public class PaymentSettingsServiceDto {
    private List<PaymentOptionServiceDto> paymentOptions;
} 