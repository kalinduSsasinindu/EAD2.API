package com.example.ead2project.repository.Data.Entities.user.StoreConfiguration;


import com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.banner.BannerConfig;
import com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.body.Body;
import com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.footer.Footer;
import com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.menu.Menu;
import com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.ribbon.TopRibbon;
import com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.seo.SEO;
import com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.theme.Theme;

import lombok.Data;

@Data
public class StoreWebConfiguration {
    private String version;
    private MetaData metadata;
    private Menu menu;
    private TopRibbon topRibbon;
    private BannerConfig banner;
    private Body body;
    private Footer footer;
    private Theme theme;
    private SEO seo;
} 