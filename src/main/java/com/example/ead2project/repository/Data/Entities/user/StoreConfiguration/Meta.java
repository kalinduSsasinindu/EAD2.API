package com.example.ead2project.repository.Data.Entities.user.StoreConfiguration;

import lombok.Data;
import java.util.List;

@Data
public class Meta {
    private String seoTitle;
    private String seoDescription;
    private List<String> keywords;
} 