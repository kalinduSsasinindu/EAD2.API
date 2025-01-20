package com.example.ead2project.repository.Data.Entities.user;

import lombok.Data;

@Data
public class CourierSettings {
    private String addApiUrl;
    private String addApiKey;
    private String clientId;
    private String webHookApiUrl;
    private Integer type;
} 