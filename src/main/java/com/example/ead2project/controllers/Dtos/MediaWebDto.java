package com.example.ead2project.controllers.Dtos;

import lombok.Data;
import java.util.List;

@Data
public class MediaWebDto {
    private String productId;
    private List<String> newMediaBase64;
    private List<MediaUpdateRequestWebDto> mediaUpdates;
}
