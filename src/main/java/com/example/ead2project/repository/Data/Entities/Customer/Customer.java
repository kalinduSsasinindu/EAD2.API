package com.example.ead2project.repository.Data.Entities.Customer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.example.ead2project.repository.Data.Entities.Base.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "customers")
public class Customer extends BaseEntity {
    
    @Field("Email")
    private String email;
    
    @Field("FirstName")
    private String firstName;
    
    @Field("LastName")
    private String lastName;
    
    @Field("Phone")
    private String phone;
    
    private DefaultAddress defaultAddress;
} 