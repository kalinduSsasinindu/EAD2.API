package com.example.ead2project.repository.Data.Entities.Order;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TimeLineDetails {
    private LocalDateTime createdAt;
    private String comment;
    private List<String> images;
    private List<String> imgUrls;
} 