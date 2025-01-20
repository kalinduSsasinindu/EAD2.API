package com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.ribbon;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AnnouncementItem {
    private String text;
    private String link;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isActive;
} 