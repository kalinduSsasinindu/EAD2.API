package com.example.ead2project.repository.Data.Entities.Customer;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class CustomerInfo {
    
    @Field("Email")
    private String email;
    
    @Field("FirstName")
    private String firstName;
    
    @Field("LastName")
    private String lastName;
    
    @Field("Phone")
    private String phone;
} 