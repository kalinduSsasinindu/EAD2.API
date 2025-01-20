package com.example.ead2project.repository.Data.Entities.product;

import lombok.Data;
import java.util.List;

@Data
public class VariantOption {
    private String name;
    private List<String> values;
} 