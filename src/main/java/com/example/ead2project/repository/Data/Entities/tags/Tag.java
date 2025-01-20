package com.example.ead2project.repository.Data.Entities.tags;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.example.ead2project.repository.Data.Entities.Base.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "tags")
public class Tag extends BaseEntity {
    
    @Field("name")
    private String name;
    
    @Field("kind")
    private String kind;
} 