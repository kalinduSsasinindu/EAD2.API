package com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.menu;

import lombok.Data;
import java.util.List;

@Data
public class MenuItem {
    private String id;
    private String name;
    private int type;
    private int order;
    private boolean isActive;
    private String url;
    private String key;
    private List<MenuCollection> collections;
} 