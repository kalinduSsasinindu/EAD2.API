package com.example.ead2project.service.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderServiceDto {
    private String id;
    private String clientId;
    private String financialStatus;
    private String fulfillmentStatus;
    private String name;
    private BigDecimal totalPrice;
    private BigDecimal totalOutstanding;
    private ShippingAddressServiceDto shippingAddress;
    private List<String> trackingIds;
    private String trackingError;
    private String deliveryStatus;
    private LocalDateTime createdAt;
    private int lineItemCount;
    private boolean isCancelled;
    private Integer isExchange;
    private Integer weight;
} 