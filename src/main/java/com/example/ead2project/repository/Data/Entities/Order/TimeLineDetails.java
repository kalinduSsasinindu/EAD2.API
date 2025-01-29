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
    public static Object builder() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'builder'");
    }
} 