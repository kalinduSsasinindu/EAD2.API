package com.example.ead2project.serviceInterface.Dto;

import lombok.Data;

@Data
public class CourierSettingsServiceDto {
    private String addApiUrl;
    private String addApiKey;
    private String clientId;
    private String webHookApiUrl;
    private int type;
} 