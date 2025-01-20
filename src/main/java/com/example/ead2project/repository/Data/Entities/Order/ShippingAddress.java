package com.example.ead2project.repository.Data.Entities.Order;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class ShippingAddress {
    
    @Field("FirstName")
    private String firstName;
    
    @Field("LastName")
    private String lastName;
    
    @Field("Phone")
    private String phone;
    
    @Field("Address1")
    private String address1;
    
    @Field("Address2")
    private String address2;
    
    private String city;
} 