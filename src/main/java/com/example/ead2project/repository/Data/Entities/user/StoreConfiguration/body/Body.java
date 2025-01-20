package com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.body;

import lombok.Data;
import java.util.List;

@Data
public class Body {
    private String id;
    private List<DynamicPage> pages;
    private Layout layout;
} 