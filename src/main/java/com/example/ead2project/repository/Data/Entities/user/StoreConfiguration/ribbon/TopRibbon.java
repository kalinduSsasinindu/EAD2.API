package com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.ribbon;

import com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.ComponentStyle;

import lombok.Data;

@Data
public class TopRibbon {
    private String id;
    private Logo logo;
    private ComponentStyle style;
    private Announcements announcements;
    private SocialMediaInfo socialMedia;
} 