package com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.footer;

import lombok.Data;
import java.util.List;

import com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.ComponentStyle;
import com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.ribbon.SocialMediaInfo;

@Data
public class Footer {
    private String id;
    private ComponentStyle style;
    private List<FooterSection> sections;
    private SocialMediaInfo socialMedia;
    private NewsLetter newsLetter;
    private Copyright copyright;
} 