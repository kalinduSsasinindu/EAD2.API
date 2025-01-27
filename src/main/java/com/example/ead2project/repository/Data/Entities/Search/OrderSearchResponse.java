package com.example.ead2project.repository.Data.Entities.Search;

import com.example.ead2project.repository.Data.Entities.Order.ShippingAddress;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document
public class OrderSearchResponse {
    @Id
    @Field("_id")
    private String id;
    
    private String financialStatus;
    private String fulfillmentStatus;
    private String name;
    private BigDecimal totalPrice;
    private BigDecimal totalOutstanding;
    private ShippingAddress shippingAddress;
    private List<String> trackingIds;
    private String trackingError;
    private String deliveryStatus;
    private LocalDateTime createdAt;
    private int lineItemCount;
    private boolean isCancelled;
    private String clientId;
    private Integer isExchange;
} 