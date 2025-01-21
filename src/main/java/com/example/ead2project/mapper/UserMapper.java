package com.example.ead2project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.example.ead2project.serviceInterface.Dto.UserServiceDto;
import com.example.ead2project.repository.Data.Entities.user.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "clientId", target = "clientId")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "userIntegration", target = "userIntegration")
    @Mapping(source = "isDeleted", target = "isDeleted")
    @Mapping(source = "storeSettings", target = "storeSettings")
    @Mapping(source = "paymentSettings", target = "paymentSettings")
    UserServiceDto toDto(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "clientId", target = "clientId")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "userIntegration", target = "userIntegration")
    @Mapping(source = "isDeleted", target = "isDeleted")
    @Mapping(source = "storeSettings", target = "storeSettings")
    @Mapping(source = "paymentSettings", target = "paymentSettings")
    User toEntity(UserServiceDto dto);
} 