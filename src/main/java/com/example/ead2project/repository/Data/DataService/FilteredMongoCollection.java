package com.example.ead2project.repository.Data.DataService;

import com.example.ead2project.repository.Data.Entities.Base.IUserOwnedEntity;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class FilteredMongoCollection<T> {

    private final MongoTemplate mongoTemplate;
    private final Class<T> entityClass;
    
    public FilteredMongoCollection(MongoTemplate mongoTemplate, Class<T> entityClass) {
        this.mongoTemplate = mongoTemplate;
        this.entityClass = entityClass;
    }

    private String getClientId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            // Implement your logic to get user ID from JWT or session
            return attributes.getRequest().getHeader("X-User-Id");
        }
        return null;
    }

    private Query applyUserFilter(Query query) {
        String clientId = getClientId();
        if (clientId != null && IUserOwnedEntity.class.isAssignableFrom(entityClass)) {
            query.addCriteria(Criteria.where("clientId").is(clientId));
        }
        return query;
    }

    public Optional<T> findOne(Query query) {
        Query filteredQuery = applyUserFilter(query);
        return Optional.ofNullable(mongoTemplate.findOne(filteredQuery, entityClass));
    }

    public long count(Query query) {
        Query filteredQuery = applyUserFilter(query);
        return mongoTemplate.count(filteredQuery, entityClass);
    }

    public List<T> find(Query query) {
        Query filteredQuery = applyUserFilter(query);
        filteredQuery.addCriteria(Criteria.where("isDeleted").is(false));
        return mongoTemplate.find(filteredQuery, entityClass);
    }

    public void insertOne(T document) {
        String clientId = getClientId();
        if (clientId != null && document instanceof IUserOwnedEntity) {
            ((IUserOwnedEntity) document).setClientId(clientId);
        }
        mongoTemplate.insert(document);
    }

    public UpdateResult updateOne(Query query, Update update) {
        Query filteredQuery = applyUserFilter(query);
        return mongoTemplate.updateFirst(filteredQuery, update, entityClass);
    }

    public T findOneAndUpdate(Query query, Update update) {
        Query filteredQuery = applyUserFilter(query);
        return mongoTemplate.findAndModify(filteredQuery, update, entityClass);
    }

    public DeleteResult deleteOne(Query query) {
        Query filteredQuery = applyUserFilter(query);
        return mongoTemplate.remove(filteredQuery, entityClass);
    }

    public UpdateResult softDeleteOne(Query query) {
        Query filteredQuery = applyUserFilter(query);
        Update update = new Update()
                .set("isDeleted", true)
                .set("deletedAt", LocalDateTime.now());
        return mongoTemplate.updateFirst(filteredQuery, update, entityClass);
    }

    public void insertMany(List<T> documents) {
        String clientId = getClientId();
        if (clientId != null) {
            documents.forEach(document -> {
                if (document instanceof IUserOwnedEntity) {
                    ((IUserOwnedEntity) document).setClientId(clientId);
                }
            });
        }
        mongoTemplate.insertAll(documents);
    }

    public T replaceOne(Query query, T replacement) {
        Query filteredQuery = applyUserFilter(query);
        return mongoTemplate.findAndReplace(filteredQuery, replacement);
    }
}
