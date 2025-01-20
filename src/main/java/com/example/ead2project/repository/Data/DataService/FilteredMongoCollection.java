package com.example.ead2project.repository.Data.DataService;

import com.example.ead2project.repository.Data.Entities.Base.IUserOwnedEntity;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
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
import java.util.concurrent.CompletableFuture;

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

    public CompletableFuture<T> findOneAsync(Bson filter) {
        return CompletableFuture.supplyAsync(() -> 
            collection.find(applyUserFilter(filter)).first()
        );
    }

    public CompletableFuture<Long> countDocumentsAsync(Bson filter) {
        return CompletableFuture.supplyAsync(() -> 
            collection.countDocuments(applyUserFilter(filter))
        );
    }

    public CompletableFuture<List<T>> findAsync(Bson filter) {
        return CompletableFuture.supplyAsync(() -> 
            collection.find(applyUserFilter(filter)).into(new ArrayList<>())
        );
    }

    public FindIterable<T> find(Bson filter) {
        Bson combinedFilter = Filters.and(
            applyUserFilter(filter),
            Filters.eq("isDeleted", false)
        );
        return collection.find(combinedFilter);
    }

    public AggregateIterable<T> aggregate() {
        return collection.aggregate(new ArrayList<>());
    }

    public CompletableFuture<Void> insertOneAsync(T document) {
        return CompletableFuture.runAsync(() -> {
            String clientId = getClientId();
            if (clientId != null && document instanceof IUserOwnedEntity) {
                ((IUserOwnedEntity) document).setClientId(clientId);
            }
            collection.insertOne(document);
        });
    }

    public CompletableFuture<UpdateResult> updateOneAsync(Bson filter, Bson update) {
        return CompletableFuture.supplyAsync(() -> 
            collection.updateOne(applyUserFilter(filter), update)
        );
    }

    public CompletableFuture<UpdateResult> replaceOneAsync(Bson filter, T replacement) {
        return CompletableFuture.supplyAsync(() -> 
            collection.replaceOne(applyUserFilter(filter), replacement)
        );
    }

    public CompletableFuture<UpdateResult> replaceOneAsync(Bson filter, T replacement, ReplaceOptions options) {
        return CompletableFuture.supplyAsync(() -> 
            collection.replaceOne(applyUserFilter(filter), replacement, options)
        );
    }

    public CompletableFuture<DeleteResult> deleteOneAsync(Bson filter) {
        return CompletableFuture.supplyAsync(() -> 
            collection.deleteOne(applyUserFilter(filter))
        );
    }

    public CompletableFuture<UpdateResult> softDeleteOneAsync(Bson filter) {
        return CompletableFuture.supplyAsync(() -> {
            Bson update = Updates.combine(
                Updates.set("IsDeleted", true),
                Updates.set("DeletedAt", LocalDateTime.now())
            );
            return collection.updateOne(applyUserFilter(filter), update);
        });
    }

    public CompletableFuture<T> findOneAndUpdateAsync(Bson filter, Bson update, FindOneAndUpdateOptions options) {
        return CompletableFuture.supplyAsync(() -> 
            collection.findOneAndUpdate(applyUserFilter(filter), update, options)
        );
    }

    public CompletableFuture<Void> insertManyAsync(List<T> documents) {
        return CompletableFuture.runAsync(() -> {
            String clientId = getClientId();
            if (clientId != null) {
                documents.forEach(doc -> {
                    if (doc instanceof IUserOwnedEntity) {
                        ((IUserOwnedEntity) doc).setClientId(clientId);
                    }
                });
            }
            collection.insertMany(documents);
        });
    }
}
