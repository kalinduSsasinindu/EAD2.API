package com.example.ead2project.repository.Data.Entities.Order;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PaymentInfo {
    private List<Payment> payments;
    
    public BigDecimal getTotalPaidAmount() {
        if (payments == null || payments.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return payments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
} 