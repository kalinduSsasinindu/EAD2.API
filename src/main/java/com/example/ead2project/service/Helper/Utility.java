package com.example.ead2project.service.Helper;
import java.util.function.Supplier;
import com.example.ead2project.repository.Data.DataService.MongoDBContext;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import jakarta.servlet.http.HttpServletRequest;
import org.bson.BsonDecimal128;
import org.bson.conversions.Bson;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.ead2project.repository.Data.Entities.Order.Sequence;
import org.bson.types.Decimal128;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

public class Utility {

    public static CompletableFuture<Long> getNextSequenceValue(String sequenceName, String clientId, MongoDBContext context) {
        String sequenceId = clientId == null || clientId.isEmpty() ? 
            sequenceName : sequenceName + "_" + clientId;

        Bson filter = Filters.eq("_id", sequenceId);
        Bson update = Updates.inc("sequence_value", 1);

        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions()
            .returnDocument(ReturnDocument.AFTER)
            .upsert(true);

        return CompletableFuture.<Long>supplyAsync((Supplier<Long>) () -> {
            Sequence result = context.getSequences()
                .findOneAndUpdateAsync(filter, update, options)
                .join();
            return result.getSequenceValue();
        });
    }

    public static String getUserEmailFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        return null;
    }

    public static String getUserIdFromRequest(HttpServletRequest request) {
        return (String) request.getAttribute("ClientId");
    }

    public static BigDecimal convertToDecimal(BsonDecimal128 number) {
        return number.decimal128Value().bigDecimalValue();
    }

    public static BsonDecimal128 convertToBsonDecimal(BigDecimal number) {
        return new BsonDecimal128(new Decimal128(number));
    }
} 