package com.example.ead2project.repository.Data.Entities.user;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.StoreWebConfiguration;

@Data
@Document(collection = "users")
public class User {
    
    @Id
    private String id;
    
    @Field("ClientId")
    private String clientId;
    
    @Field("Email")
    private String email;
    
    @Field("isDeleted")
    private boolean isDeleted;
    
    @Field("UserIntegration")
    private UserIntegration userIntegration;
    

    @Field("StoreSettings")
    private StoreSettings storeSettings;
    
    @Field("PaymentSettings")
    private PaymentSettings paymentSettings;
    
    @Field("StoreWebConfiguration")
    private StoreWebConfiguration storeWebConfiguration;
} 