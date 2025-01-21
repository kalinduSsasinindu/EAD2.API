package com.example.ead2project.serviceInterface.interfaces;

import jakarta.servlet.http.HttpServletRequest;
import com.example.ead2project.serviceInterface.Dto.UserServiceDto;
import com.example.ead2project.serviceInterface.Dto.ECommerceSettingsServiceDto;
import com.example.ead2project.serviceInterface.Dto.CourierSettingsServiceDto;
import com.example.ead2project.serviceInterface.Dto.StoreSettingsServiceDto;
import com.example.ead2project.serviceInterface.Dto.PaymentSettingsServiceDto;
import com.example.ead2project.serviceInterface.Dto.ShippingRateServiceDto;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.body.DynamicPage;
import com.example.ead2project.repository.Data.Entities.user.StoreConfiguration.StoreWebConfiguration;

public interface IUserService {
    CompletableFuture<UserServiceDto> getUserByEmail();
    CompletableFuture<UserServiceDto> getUserByUserId(HttpServletRequest request);
    CompletableFuture<Boolean> setDefaultCourier(UserServiceDto user, int courierId);
    CompletableFuture<Boolean> setDefaultECommerce(UserServiceDto user, int ecommId);
    CompletableFuture<ECommerceSettingsServiceDto> getEcommerceSettingByType(UserServiceDto user, int type);
    CompletableFuture<CourierSettingsServiceDto> getCourierSettingByType(UserServiceDto user, int type);
    CompletableFuture<Boolean> upsertECommerceSettings(UserServiceDto user, ECommerceSettingsServiceDto eComSetting);
    CompletableFuture<Boolean> upsertCourierIntegration(UserServiceDto user, CourierSettingsServiceDto courierSetting);
    CompletableFuture<Boolean> upsertStoreInformation(UserServiceDto user, StoreSettingsServiceDto storeSettings);
    CompletableFuture<Boolean> upsertPaymentOptions(UserServiceDto user, PaymentSettingsServiceDto paymentOptions);
    CompletableFuture<Boolean> upsertShippingRates(UserServiceDto user, List<ShippingRateServiceDto> shippingRates);
    CompletableFuture<Boolean> deleteCourierIntegration(UserServiceDto user, int courierType);
    CompletableFuture<Boolean> deleteECommerceIntegration(UserServiceDto user, int ecommType);
    CompletableFuture<Boolean> upsertStoreConfigSettings(UserServiceDto user, StoreWebConfiguration storeConfig);
    CompletableFuture<StoreWebConfiguration> getStoreConfiguration(HttpServletRequest request);
    CompletableFuture<StoreWebConfiguration> getStoreConfigurationByClientId(String clientId);
    CompletableFuture<Boolean> createDynamicPage(UserServiceDto user, DynamicPage dynamicPage);
    CompletableFuture<Boolean> updateDynamicPage(UserServiceDto user, DynamicPage dynamicPage);
}
