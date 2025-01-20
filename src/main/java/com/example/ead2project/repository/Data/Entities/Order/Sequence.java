package com.example.ead2project.repository.Data.Entities.Order;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "sequences")
public class Sequence {
    
    @Id
    private String id;
    
    @Field("sequence_value")
    private Long sequenceValue;
} 