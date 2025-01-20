package com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.ribbon;

import lombok.Data;
import java.util.List;

@Data
public class Announcements {
    private boolean enabled;
    private List<AnnouncementItem> items;
} 