package com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.body;

import com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.ComponentStyle;

import lombok.Data;

@Data
public class DynamicPage {
    private String id;
    private String key;
    private String type;
    private BodyContent content;
    private ComponentStyle style;
    private int order;
    private boolean isActive;
} 