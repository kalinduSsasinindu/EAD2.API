package com.example.ead2project.service.dto;

import lombok.Data;
import lombok.Builder;
import java.util.List;

@Data
@Builder
public class PagedOrdersResultServiceDto {
    private int totalRecords;
    private List<OrderServiceDto> orders;
} 