package com.example.ead2project.repository.Data.Entities.user.StoreConfiguration;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MetaData {
    private LocalDateTime lastUpdated;
    private boolean isActive;
    private String environment;
} 