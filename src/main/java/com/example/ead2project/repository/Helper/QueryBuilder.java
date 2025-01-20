package com.example.ead2project.repository.Helper;

import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.BsonBoolean;
import org.bson.BsonInt32;
import java.util.List;
import java.util.Arrays;

public class QueryBuilder {
    
    public static List<BsonDocument> buildSearchFilter(String query, String indexName, 
                                                     String clientId, List<String> indexedColumns) {
        // Match stage for filtering by ClientId and isDeleted flag
        BsonDocument matchStage = new BsonDocument("$match", 
            new BsonDocument()
                .append("ClientId", new BsonString(clientId))
                .append("isDeleted", new BsonBoolean(false)));

        // Create a BsonArray to hold each autocomplete clause with fuzzy search
        BsonArray shouldClauses = new BsonArray();
        for (String field : indexedColumns) {
            BsonDocument autocompleteClause = new BsonDocument("autocomplete", 
                new BsonDocument()
                    .append("query", new BsonString(query))
                    .append("path", new BsonString(field))
                    .append("fuzzy", new BsonDocument()
                        .append("maxEdits", new BsonInt32(2))
                        .append("prefixLength", new BsonInt32(query.length()))
                        .append("maxExpansions", new BsonInt32(10))));
            shouldClauses.add(autocompleteClause);
        }

        BsonDocument searchStage = new BsonDocument("$search", 
            new BsonDocument()
                .append("index", new BsonString(indexName))
                .append("compound", new BsonDocument()
                    .append("should", shouldClauses)));

        return Arrays.asList(searchStage, matchStage);
    }
} 