package com.example.ead2project.service.Services;

import com.example.ead2project.dto.*;
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
import com.example.ead2project.repository.Data.Entities.user.User;
import com.example.ead2project.mapper.UserMapper;
import org.bson.conversions.Bson;
import java.util.function.Supplier;
import java.util.Arrays;

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
        return null;
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
        return null;
    }

    @Override
    public CompletableFuture<Boolean> upsertPaymentOptions(UserServiceDto user, PaymentSettingsServiceDto paymentOptions) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> upsertShippingRates(UserServiceDto user, List<ShippingRateServiceDto> shippingRates) {
        return null;
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
        return null;
    }

    @Override
    public CompletableFuture<StoreWebConfiguration> getStoreConfiguration(HttpServletRequest request) {
        return null;
    }

    @Override
    public CompletableFuture<StoreWebConfiguration> getStoreConfigurationByClientId(String clientId) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> createDynamicPage(UserServiceDto user, DynamicPage dynamicPage) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> updateDynamicPage(UserServiceDto user, DynamicPage dynamicPage) {
        return null;
    }
} 