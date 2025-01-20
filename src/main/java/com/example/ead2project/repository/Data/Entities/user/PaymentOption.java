package com.example.ead2project.repository.Data.Entities.user;

import lombok.Data;

@Data
public class PaymentOption {
    private boolean isDefault;
    private Integer paymentType;
} 