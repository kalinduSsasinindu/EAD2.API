package com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.menu;

import com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.Meta;

import lombok.Data;

@Data
public class MenuCollection {
    private String id;
    private String displayName;
    private String code;
    private String imageUrl;
    private String description;
    private int order;
    private boolean isActive;
    private Meta meta;
} 