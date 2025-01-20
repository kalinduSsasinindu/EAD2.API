package com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.footer;

import lombok.Data;

@Data
public class FooterLink {
    private String text;
    private String url;
    private int order;
    private boolean isActive;
} 