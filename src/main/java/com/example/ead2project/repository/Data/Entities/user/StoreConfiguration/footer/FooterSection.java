package com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.footer;

import lombok.Data;
import java.util.List;

@Data
public class FooterSection {
    private String id;
    private String title;
    private List<FooterLink> links;
} 