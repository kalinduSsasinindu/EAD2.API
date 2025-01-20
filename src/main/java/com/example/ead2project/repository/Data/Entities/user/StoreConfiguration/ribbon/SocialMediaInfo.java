package com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.ribbon;

import lombok.Data;
import java.util.List;

@Data
public class SocialMediaInfo {
    private List<SocialMediaLink> links;
    private SocialMediaStyle style;
} 