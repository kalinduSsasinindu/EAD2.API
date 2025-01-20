package com.example.ead2project.repository.Data.Entities.Order;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.example.ead2project.repository.Data.Entities.Base.BaseEntity;
import com.example.ead2project.repository.Data.Entities.Customer.CustomerInfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "orders")
public class Order extends BaseEntity {
    
    @Field("FinancialStatus")
    private String financialStatus;
    
    @Field("FulfillmentStatus")
    private String fulfillmentStatus;
    
    @Field("Name")
    private String name;
    
    @Field("Note")
    private String note;
    
    @Field("PaymentMethod")
    private String[] paymentMethod;
    
    @Field("Phone")
    private String phone;
    
    @Field("SubtotalPrice")
    private BigDecimal subtotalPrice;
    
    @Field("TotalLineItemsPrice")
    private BigDecimal totalLineItemsPrice;
    
    @Field("TotalPrice")
    private BigDecimal totalPrice;
    
    @Field("TotalShippingPrice")
    private BigDecimal totalShippingPrice;
    
    @Field("TotalDiscountPrice")
    private BigDecimal totalDiscountPrice;
    
    private ShippingAddress shippingAddress;
    
    private CustomerInfo customer;
    
    private List<LineItem> lineItems = new ArrayList<>();
    
    private PaymentInfo paymentInfo;
    
    @Field("isCancelled")
    private boolean isCancelled;
    
    private List<TimeLineDetails> timeLineDetails;
    
    @Field("Tags")
    private List<String> tags;
    
    @Field("isDrafted")
    private boolean isDrafted;
    
    public BigDecimal getTotalOutstanding() {
        BigDecimal totalPaid = paymentInfo != null ? paymentInfo.getTotalPaidAmount() : BigDecimal.ZERO;
        return totalPrice.subtract(totalPaid);
    }
} 