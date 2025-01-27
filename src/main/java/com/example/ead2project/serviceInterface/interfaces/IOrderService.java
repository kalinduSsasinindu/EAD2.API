package com.example.ead2project.serviceInterface.interfaces;

import com.example.ead2project.repository.Data.Entities.Order.*;
import com.example.ead2project.repository.Data.Entities.Search.OrderSearchResponse;
import com.example.ead2project.service.dto.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IOrderService {
    List<OrderSearchResponse> getOrders();
    CompletableFuture<Void> createOrder(Order order);
    CompletableFuture<Void> update(String id, List<LineItem> lineItems);
    CompletableFuture<Void> update(String id, ShippingAddress shippingAddress);
    CompletableFuture<Void> update(String id, PaymentInfo paymentInfo);
    CompletableFuture<Void> updateFulfillStatus(String id, String fulfillStatus);
    CompletableFuture<Void> updatePaymentStatus(String id, String paymentStatus, BigDecimal amount, String paymentMethod);
    Order getById(String id);
    CompletableFuture<List<Order>> getByIds(List<String> ids);
    List<OrderSearchResponse> getOrdersByStatus(String fulfillmentStatus);
    PagedOrdersResultServiceDto getOrdersWithTracking(int start, int end, String queryText);
    boolean deleteProductAsync(String id);
    boolean cancelOrderAsync(String id);
    boolean update(String id, BigDecimal subtotalPrice, BigDecimal totalLineItemsPrice, 
            BigDecimal totalPrice, BigDecimal totalShippingPrice, BigDecimal totalDiscountPrice);
    List<OrderSearchResponse> searchOrdersAsync(String query);
    List<OrderCountItemServiceDto> getOrdersUpUntil(LocalDateTime upUntil);
    CompletableFuture<Void> addTagToOrder(String productId, List<String> tagNames);
    CompletableFuture<Void> addTimelineAsync(String orderId, TimeLineDetails details);
    CompletableFuture<Void> updateStatus(String id, boolean isDrafted);
}
