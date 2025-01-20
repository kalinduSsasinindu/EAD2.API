package com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.banner;

import lombok.Data;
import java.util.List;

@Data
public class BannerConfig {
    private List<BannerImage> images;
    private boolean isAutoChange;
    private int changeTime;
    private boolean allowNextImage;
} 