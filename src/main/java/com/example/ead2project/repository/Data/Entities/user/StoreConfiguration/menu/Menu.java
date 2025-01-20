package com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.menu;

import lombok.Data;
import java.util.List;

import com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.ComponentStyle;

@Data
public class Menu {
    private String id;
    private List<MenuItem> items;
    private ComponentStyle style;
} 