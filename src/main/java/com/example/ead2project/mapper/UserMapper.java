package com.example.ead2project.mapper;

import com.example.ead2project.repository.Data.Entities.user.PaymentSettings;
import com.example.ead2project.repository.Data.Entities.user.ShippingRate;
import com.example.ead2project.repository.Data.Entities.user.StoreSettings;
import com.example.ead2project.repository.Data.Entities.user.User;
import com.example.ead2project.serviceInterface.Dto.PaymentSettingsServiceDto;
import com.example.ead2project.serviceInterface.Dto.ShippingRateServiceDto;
import com.example.ead2project.serviceInterface.Dto.StoreSettingsServiceDto;
import com.example.ead2project.serviceInterface.Dto.UserServiceDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
   // @Mapping(target = "isDeleted", expression = "java(user.isDeleted())")
    UserServiceDto toDto(User user);

    //@Mapping(target = "isDeleted", expression = "java(dto.isDeleted())")
    User toEntity(UserServiceDto dto);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "companyName", target = "companyName")
    @Mapping(source = "companyEmail", target = "companyEmail")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "shippingRates", target = "shippingRates")
    StoreSettings toStoreSettings(StoreSettingsServiceDto dto);
    
    @Mapping(source = "paymentOptions", target = "paymentOptions")
    PaymentSettings toPaymentSettings(PaymentSettingsServiceDto dto);
    
    List<ShippingRate> toShippingRates(List<ShippingRateServiceDto> dto);
    
    @Mapping(source = "name", target = "name")
    @Mapping(source = "rate", target = "rate")
    //@Mapping(source = "isDefault", target = "isDefault")
    ShippingRate toShippingRate(ShippingRateServiceDto dto);
} 