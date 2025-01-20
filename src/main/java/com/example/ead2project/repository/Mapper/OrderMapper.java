package com.example.ead2project.repository.Mapper;
/*package com.example.ead2project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.ead2project.repository.Data.Entities.Order.LineItem;
import com.example.ead2project.repository.Data.Entities.Order.Order;
import com.example.ead2project.repository.Data.Entities.Order.ShippingAddress;
import com.example.ead2project.repository.Data.Entities.product.Product;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "address1", source = "address1")
    @Mapping(target = "address2", source = "address2")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "city", source = "city")
    ShippingAddress toShippingAddress(ShippingAddressDto shippingAddressDto);

    LineItem toLineItem(LineItemDto lineItemDto);

    OrderSearchResponse toOrderSearchResponse(Order order);

    ProductSearchResponse toProductSearchResponse(Product product);
} */