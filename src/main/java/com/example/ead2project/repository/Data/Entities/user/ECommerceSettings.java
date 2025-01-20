package com.example.ead2project.repository.Data.Entities.user;

import lombok.Data;

@Data
public class ECommerceSettings {
    private String orderApiUrl;
    private String orderApiKey;
    private Integer type;
} 