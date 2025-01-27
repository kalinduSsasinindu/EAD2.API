package com.example.ead2project.service.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class OrderCountItemServiceDto {
    private String date;
    private int count;
} 