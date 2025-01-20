package com.example.ead2project.repository.Data.Entities.Base;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.Data;
import java.time.LocalDateTime;
import org.bson.types.ObjectId;

@Data
public abstract class BaseEntity implements IUserOwnedEntity {
    
    public static class BaseAttributes {
        public static final String ID = "_id";
        public static final String CREATED_AT = "CreatedAt";
        public static final String UPDATED_AT = "UpdatedAt";
        public static final String DELETED_AT = "DeletedAt";
        public static final String IS_DELETED = "isDeleted";
        public static final String CLIENT_ID = "ClientId";
    }

    @Id
    @Field(BaseAttributes.ID)
    private String id;

    @Field(BaseAttributes.CREATED_AT)
    private LocalDateTime createdAt;

    @Field(BaseAttributes.UPDATED_AT)
    private LocalDateTime updatedAt;

    @Field(BaseAttributes.DELETED_AT)
    private LocalDateTime deletedAt;

    @Field(BaseAttributes.IS_DELETED)
    private boolean isDeleted;

    @Field(BaseAttributes.CLIENT_ID)
    private String clientId;

    public BaseEntity() {
        this.id = new ObjectId().toString();
        this.createdAt = LocalDateTime.now();
        this.isDeleted = false;
    }
} 