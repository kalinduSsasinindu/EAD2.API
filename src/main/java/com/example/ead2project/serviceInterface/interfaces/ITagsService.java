package com.example.ead2project.serviceInterface.interfaces;

import java.util.List;

import com.example.ead2project.repository.Data.Entities.tags.Tag;

public interface ITagsService {
    Tag addOrUpdateTag(String name, String kind);
    Tag getTag(String name, String kind);
    List<Tag> getTagsByClient(String clientId);
    void deleteTag(String tagId);
    List<Tag> getTagsByKind(String kind);
} 