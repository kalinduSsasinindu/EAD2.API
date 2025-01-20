package com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.footer;

import com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.ComponentStyle;

import lombok.Data;

@Data
public class NewsLetter {
    private String id;
    private boolean enabled;
    private String title;
    private String description;
    private ComponentStyle style;
} 