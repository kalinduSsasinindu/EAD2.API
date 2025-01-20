package com.example.ead2project.repository.Data.DataService;

import com.example.ead2project.repository.Data.Entities.Base.IUserOwnedEntity;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.bson.conversions.Bson;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class FilteredMongoCollection<T> {
    private final MongoCollection<T> collection;
    private final HttpServletRequest request;
    
    private String getClientId() {
        return request.getHeader("X-User-Id");
    }
    
    private Bson applyUserFilter(Bson filter) {
        String clientId = getClientId();
        if (clientId != null && IUserOwnedEntity.class.isAssignableFrom(collection.getDocumentClass())) {
            Bson userFilter = Filters.eq("ClientId", clientId);
            return Filters.and(filter, userFilter);
        }
        return filter;
    }

    public Optional<T> findOne(Bson filter) {
        return Optional.ofNullable(collection.find(applyUserFilter(filter)).first());
    }

    public long count(Bson filter) {
        return collection.countDocuments(applyUserFilter(filter));
    }

    public List<T> find(Bson filter) {
        Bson combinedFilter = Filters.and(
            applyUserFilter(filter),
            Filters.eq("isDeleted", false)
        );
        return collection.find(combinedFilter).into(new ArrayList<>());
    }

    public void insertOne(T document) {
        String clientId = getClientId();
        if (clientId != null && document instanceof IUserOwnedEntity) {
            ((IUserOwnedEntity) document).setClientId(clientId);
        }
        collection.insertOne(document);
    }

    public UpdateResult updateOne(Bson filter, Bson update) {
        return collection.updateOne(applyUserFilter(filter), update);
    }

    public DeleteResult deleteOne(Bson filter) {
        return collection.deleteOne(applyUserFilter(filter));
    }

    public UpdateResult softDeleteOne(Bson filter) {
        Bson update = Updates.combine(
            Updates.set("IsDeleted", true),
            Updates.set("DeletedAt", LocalDateTime.now())
        );
        return collection.updateOne(applyUserFilter(filter), update);
    }

    public void insertMany(List<T> documents) {
        String clientId = getClientId();
        if (clientId != null) {
            documents.forEach(doc -> {
                if (doc instanceof IUserOwnedEntity) {
                    ((IUserOwnedEntity) doc).setClientId(clientId);
                }
            });
        }
        collection.insertMany(documents);
    }
}
