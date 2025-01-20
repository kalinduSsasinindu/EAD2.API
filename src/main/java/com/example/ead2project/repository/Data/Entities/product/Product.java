package com.example.ead2project.repository.Data.Entities.product;

import com.example.ead2project.repository.Data.Entities.Base.BaseEntity;
import com.example.ead2project.repository.Helper.DynamicQueryEnabled;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "products")
public class Product extends BaseEntity {
    
    public Product() {
        this.variants = new ArrayList<>();
        this.options = new ArrayList<>();
    }
    
    @Field("Title")
    @DynamicQueryEnabled
    private String title;
    
    @Field("Description")
    @DynamicQueryEnabled
    private String description;
    
    @Field("Amount")
    @DynamicQueryEnabled
    private BigDecimal amount;
    
    @DynamicQueryEnabled
    private List<String> images;
    
    private List<String> imgUrls;
    
    private List<ProductVariant> variants;
    
    private List<VariantOption> options;
    
    @Field("Tags")
    private List<String> tags;
    
    @Override
    public String toString() {
        return String.format("%s: %s (%d variants)", title, description, variants.size());
    }
} 