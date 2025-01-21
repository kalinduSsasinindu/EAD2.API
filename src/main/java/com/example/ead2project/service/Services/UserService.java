package com.example.ead2project.service.Services;

import com.example.ead2project.repository.Data.DataService.MongoDBContext;
import com.example.ead2project.service.Helper.Utility;
import com.example.ead2project.serviceInterface.interfaces.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.ead2project.serviceInterface.Dto.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.StoreWebConfiguration;
import com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.body.DynamicPage;
import com.mongodb.client.model.Filters;
import com.example.ead2project.repository.Data.Entities.user.PaymentSettings;
import com.example.ead2project.repository.Data.Entities.user.ShippingRate;
import com.example.ead2project.repository.Data.Entities.user.StoreSettings;
import com.example.ead2project.repository.Data.Entities.user.User;
import com.example.ead2project.mapper.UserMapper;
import org.bson.conversions.Bson;
import java.util.function.Supplier;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final MongoDBContext mongoDBContext;
    private final HttpServletRequest request;
    private final UserMapper userMapper;
    
    @Value("${spring.application.name}")
    private String applicationName;

    private String getClientId() {
        return Utility.getUserIdFromRequest(request);
    }

    

    @Override
    public CompletableFuture<UserServiceDto> getUserByEmail() {
        return CompletableFuture.<UserServiceDto>supplyAsync((Supplier<UserServiceDto>) () -> {
            String userEmail = Utility.getUserEmailFromContext();

            if (userEmail == null || userEmail.isEmpty()) {
                throw new IllegalStateException("User email claim not found.");
            }

            Bson filter = Filters.eq("Email", userEmail);
            return mongoDBContext.getUsers()
                .findAsync(filter)
                .thenCompose(users -> {
                    if (!users.isEmpty()) {
                        User existingUser = users.get(0);
                        return CompletableFuture.completedFuture(userMapper.toDto(existingUser));
                    }
                    return createNewUserAsync(userEmail)
                        .thenApply(user -> userMapper.toDto(user));
                }).join();
        });
    }

    private CompletableFuture<User> createNewUserAsync(String userEmail) {
        return CompletableFuture.supplyAsync(() -> {
            Long newUserId = Utility.getNextSequenceValue("userid", null, mongoDBContext)
                .join();

            User newUser = new User();
            newUser.setClientId(String.valueOf(newUserId));
            newUser.setEmail(userEmail);

            mongoDBContext.getUsers().insertOneAsync(newUser).join();
            return newUser;
        });
    }

    @Override
    public CompletableFuture<UserServiceDto> getUserByUserId(HttpServletRequest request) {
        return CompletableFuture.<UserServiceDto>supplyAsync((Supplier<UserServiceDto>) () -> {
            String clientId = request.getAttribute("ClientId") != null ? 
                request.getAttribute("ClientId").toString() : null;

            if (clientId == null || clientId.isEmpty()) {
                throw new IllegalStateException("User client id not found.");
            }

            Bson filter = Filters.eq("ClientId", clientId);
            return mongoDBContext.getUsers()
                .findAsync(filter)
                .thenCompose(users -> {
                    if (!users.isEmpty()) {
                        User existingUser = users.get(0);
                        return CompletableFuture.completedFuture(userMapper.toDto(existingUser));
                    }
                    throw new RuntimeException("user record not found");
                }).join();
        });
    }

    @Override
    public CompletableFuture<Boolean> setDefaultCourier(UserServiceDto user, int courierId) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> setDefaultECommerce(UserServiceDto user, int ecommId) {
        return null;
    }

    @Override
    public CompletableFuture<ECommerceSettingsServiceDto> getEcommerceSettingByType(UserServiceDto user, int type) {
        return null;
    }

    @Override
    public CompletableFuture<CourierSettingsServiceDto> getCourierSettingByType(UserServiceDto user, int type) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> upsertECommerceSettings(UserServiceDto user, ECommerceSettingsServiceDto eComSetting) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> upsertCourierIntegration(UserServiceDto user, CourierSettingsServiceDto courierSetting) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> upsertStoreInformation(UserServiceDto user, StoreSettingsServiceDto storeSettings) {
        return CompletableFuture.supplyAsync(() -> {
            if (user == null) {
                throw new RuntimeException("user record not found");
            }

            StoreSettings storeDetails = userMapper.toStoreSettings(storeSettings);
            Bson filter = Filters.eq("_id", user.getId());
            Bson update = Updates.set("StoreSettings", storeDetails);
            
            UpdateResult result = mongoDBContext.getUsers()
                .updateOneAsync(filter, update)
                .join();
            
            return result.wasAcknowledged() && result.getModifiedCount() > 0;
        });
    }

    @Override
    public CompletableFuture<Boolean> upsertPaymentOptions(UserServiceDto user, PaymentSettingsServiceDto paymentOptions) {
        return CompletableFuture.supplyAsync(() -> {
            if (user == null) {
                throw new RuntimeException("User record not found");
            }

            PaymentSettings paymentSettings = userMapper.toPaymentSettings(paymentOptions);
            Bson filter = Filters.eq("_id", user.getId());
            Bson update = Updates.set("PaymentSettings", paymentSettings);
            
            UpdateResult result = mongoDBContext.getUsers()
                .updateOneAsync(filter, update)
                .join();
            
            return result.wasAcknowledged() && result.getModifiedCount() > 0;
        });
    }

    @Override
    public CompletableFuture<Boolean> upsertShippingRates(UserServiceDto user, List<ShippingRateServiceDto> shippingRates) {
        return CompletableFuture.supplyAsync(() -> {
            if (user == null) {
                throw new RuntimeException("User record not found");
            }

            List<ShippingRate> rates = userMapper.toShippingRates(shippingRates);
            Bson filter = Filters.eq("_id", user.getId());
            Bson update = Updates.set("StoreSettings.ShippingRates", rates);
            
            UpdateResult result = mongoDBContext.getUsers()
                .updateOneAsync(filter, update)
                .join();
            
            return result.wasAcknowledged() && result.getModifiedCount() > 0;
        });
    }

    @Override
    public CompletableFuture<Boolean> deleteCourierIntegration(UserServiceDto user, int courierType) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> deleteECommerceIntegration(UserServiceDto user, int ecommType) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> upsertStoreConfigSettings(UserServiceDto user, StoreWebConfiguration storeConfig) {
        return CompletableFuture.supplyAsync(() -> {
            if (user == null) {
                throw new RuntimeException("User record not found");
            }

            Bson filter = Filters.eq("_id", user.getId());
            Bson update = Updates.set("StoreWebConfiguration", storeConfig);
            
            UpdateResult result = mongoDBContext.getUsers()
                .updateOneAsync(filter, update)
                .join();
            
            return result.wasAcknowledged() && result.getModifiedCount() > 0;
        });
    }

    @Override
    public CompletableFuture<StoreWebConfiguration> getStoreConfiguration(HttpServletRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            String clientId = request.getAttribute("ClientId") != null ? 
                request.getAttribute("ClientId").toString() : null;

            if (clientId == null || clientId.isEmpty()) {
                throw new IllegalStateException("User client id not found.");
            }

            Bson filter = Filters.eq("ClientId", clientId);
            User existingUser = mongoDBContext.getUsers()
                .findAsync(filter)
                .thenApply(users -> !users.isEmpty() ? users.get(0) : null)
                .join();

            if (existingUser != null && existingUser.getStoreWebConfiguration() != null) {
                return existingUser.getStoreWebConfiguration();
            }
            throw new RuntimeException("store configuration is not available");
        });
    }

    @Override
    public CompletableFuture<StoreWebConfiguration> getStoreConfigurationByClientId(String clientId) {
        return CompletableFuture.supplyAsync(() -> {
            if (clientId == null || clientId.isEmpty()) {
                throw new IllegalStateException("User client id not found.");
            }

            Bson filter = Filters.eq("ClientId", clientId);
            User existingUser = mongoDBContext.getUsers()
                .findAsync(filter)
                .thenApply(users -> !users.isEmpty() ? users.get(0) : null)
                .join();

            if (existingUser != null && existingUser.getStoreWebConfiguration() != null) {
                return existingUser.getStoreWebConfiguration();
            }
            throw new RuntimeException("store configuration is not available");
        });
    }

    @Override
    public CompletableFuture<Boolean> createDynamicPage(UserServiceDto user, DynamicPage dynamicPage) {
        return CompletableFuture.supplyAsync(() -> {
            if (user == null) {
                throw new RuntimeException("User record not found");
            }

            Bson filter = Filters.eq("_id", user.getId());
            Bson update = Updates.push("StoreWebConfiguration.body.pages", dynamicPage);
            
            UpdateResult result = mongoDBContext.getUsers()
                .updateOneAsync(filter, update)
                .join();
            
            return result.wasAcknowledged() && result.getModifiedCount() > 0;
        });
    }

    @Override
    public CompletableFuture<Boolean> updateDynamicPage(UserServiceDto user, DynamicPage updatedPage) {
        return CompletableFuture.supplyAsync(() -> {
            if (user == null) {
                throw new RuntimeException("User record not found");
            }

            Bson filter = Filters.and(
                Filters.eq("_id", user.getId()),
                Filters.elemMatch("StoreWebConfiguration.body.pages", 
                    Filters.eq("id", updatedPage.getId()))
            );
            
            Bson update = Updates.set("StoreWebConfiguration.body.pages.$", updatedPage);
            
            UpdateResult result = mongoDBContext.getUsers()
                .updateOneAsync(filter, update)
                .join();
            
            return result.wasAcknowledged() && result.getModifiedCount() > 0;
        });
    }
} 