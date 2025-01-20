package com.example.ead2project.repository.Data.Entities.user;

import lombok.Data;
import java.util.List;

@Data
public class PaymentSettings {
    private List<PaymentOption> paymentOptions;
} 