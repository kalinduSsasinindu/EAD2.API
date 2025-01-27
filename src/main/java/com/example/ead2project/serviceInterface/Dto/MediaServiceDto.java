package com.example.ead2project.serviceInterface.Dto;

import lombok.Data;
import java.util.List;

@Data
public class MediaServiceDto {
    private String productId;
    private List<String> newMediaBase64;
    private List<MediaUpdate> mediaUpdates;
} 