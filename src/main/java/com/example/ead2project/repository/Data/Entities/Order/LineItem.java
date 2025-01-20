package com.example.ead2project.repository.Data.Entities.Order;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;
import java.math.BigDecimal;

@Data
public class LineItem {
    
    @Field("FulfillableQuantity")
    private Integer fulfillableQuantity;
    
    @Field("FulfillmentStatus")
    private String fulfillmentStatus;
    
    @Field("Name")
    private String name;
    
    @Field("Price")
    private BigDecimal price;
    
    @Field("ProductId")
    private String productId;
    
    @Field("Quantity")
    private Integer quantity;
    
    @Field("Title")
    private String title;
    
    @Field("VariantTitle")
    private String variantTitle;
    
    @Field("ImageUrl")
    private String imageUrl;
    
    @Field("VariantId")
    private Integer variantId;
} 