package com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.theme;

import lombok.Data;

@Data
public class Theme {
    private String id;
    private String templateId;
    private boolean isCustomTemplate;
    private Colors colors;
    private Typography typography;
    private Spacing spacing;
    private BreakPoint breakpoint;
} 