package com.example.ead2project.repository.Data.Entities.user.StoreConfiguration;

import lombok.Data;

@Data
public class ComponentStyle {
    private String id;
    private String fontStyle;
    private String fontFamily;
    private Integer fontSize;
    private Integer fontWeight;
    private String foregroundColor;
    private String backgroundColor;
    private String hoverColor;
    private String activeColor;
    private String mobileBreakPoint;
    private Integer height;
    private Integer width;
    private Integer mobileHeight;
    private Integer mobileWidth;
} 