package com.example.ead2project.service.Services;

import com.example.ead2project.repository.Data.DataService.MongoDBContext;
import com.example.ead2project.repository.Data.Entities.tags.Tag;
import com.example.ead2project.serviceInterface.interfaces.ITagsService;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class TagsService implements ITagsService {
    
    private final MongoDBContext mongoContext;

    @Override
    public Tag addOrUpdateTag(String name, String kind) {
        Document filter = new Document()
            .append("name", name)
            .append("kind", kind);

        Tag existingTag = mongoContext.getTags().find(filter).first();

        if (existingTag != null) {
            return existingTag;
        }

        Tag newTag = new Tag();
        newTag.setName(name);
        newTag.setKind(kind);

        mongoContext.getTags().insertOneAsync(newTag).join();

        return newTag;
    }

    @Override
    public Tag getTag(String name, String kind) {
        Document filter = new Document()
            .append("name", name)
            .append("kind", kind);

        return mongoContext.getTags().find(filter).first();
    }

    @Override
    public List<Tag> getTagsByClient(String clientId) {
        Document filter = new Document("clientId", clientId);
        return mongoContext.getTags().find(filter).into(new ArrayList<>());
    }

    @Override
    public void deleteTag(String tagId) {
        Document filter = new Document("_id", tagId);
        mongoContext.getTags().deleteOneAsync(filter).join();
    }

    @Override
    public List<Tag> getTagsByKind(String kind) {
        Document filter = new Document("kind", kind);
        return mongoContext.getTags().find(filter).into(new ArrayList<>());
    }
} 