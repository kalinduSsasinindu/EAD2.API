package com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.ribbon;

import lombok.Data;

@Data
public class SocialMediaLink {
    private String platform;
    private String url;
    private String icon;
    private int order;
    private boolean isActive;
} 