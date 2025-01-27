package com.example.ead2project.controller;


import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.ead2project.repository.Data.Entities.Order.Order;
import com.example.ead2project.repository.Data.Entities.Order.TimeLineDetails;
import com.example.ead2project.repository.Data.Entities.Search.OrderSearchResponse;
import com.example.ead2project.serviceInterface.interfaces.IOrderService;
import com.example.ead2project.serviceInterface.interfaces.IUserService;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final IOrderService orderService;
    private final ModelMapper mapper;
    private final IUserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<OrderSearchResponse> getOrders() {
        return orderService.getOrders();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/GetOrdersByStatus/{fulfillmentStatus}")
    public List<OrderSearchResponse> getOrdersByStatus(@PathVariable String fulfillmentStatus) {
        return orderService.getOrdersByStatus(fulfillmentStatus);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/GetOrderCountsForGraph/{upUntil}")
    public ResponseEntity<?> getOrderCountsForGraph(@PathVariable String upUntil) {
        LocalDateTime result = LocalDateTime.parse(upUntil);
        var filteredRes = orderService.getOrdersUpUntil(result);
        var countList = mapper.map(filteredRes, List.class);
        return ResponseEntity.ok(countList);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/GetOrdersWithTracking")
    public ResponseEntity<?> getOrderPagesWithTracking(@RequestBody PortalSearchWebDto searchParams) {
        logger.info("get orders info");
        logger.debug("get orders debug");
        var filteredRes = orderService.getOrdersWithTracking(
                searchParams.getStart(), 
                searchParams.getEnd(), 
                searchParams.getQueryText());
        return ResponseEntity.ok(filteredRes);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Order order) {
        orderService.createOrder(order);
        orderService.addTimelineAsync(order.getId(), TimeLineDetails.builder()
                .createdAt(LocalDateTime.now())
                .comment("Order placed successfully")
                .build());
        return ResponseEntity.ok(Map.of("id", order.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}/timeline")
    public ResponseEntity<?> updateTimeline(@PathVariable String id, @RequestBody TimeLineDetails timeLineDetails) {
        orderService.addTimelineAsync(id, timeLineDetails);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}/lineItems")
    public ResponseEntity<?> updateLineItems(@PathVariable String id, @RequestBody List<LineItem> lineItems) {
        orderService.update(id, lineItems);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}/shippingAddress")
    public ResponseEntity<?> updateShippingAddress(@PathVariable String id, @RequestBody ShippingAddress shippingAddress) {
        orderService.update(id, shippingAddress);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}/paymentInfo")
    public ResponseEntity<?> updatePaymentInfo(@PathVariable String id, @RequestBody PaymentInfo paymentInfo) {
        orderService.update(id, paymentInfo);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable String id) {
        Order order = orderService.getById(id);
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        boolean success = orderService.deleteProductAsync(id);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable String id) {
        boolean success = orderService.cancelOrderAsync(id);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}/paymentAmounts")
    public ResponseEntity<?> updatePaymentAmounts(@PathVariable String id, @RequestBody OrderPaymentWebDto updateDto) {
        if (updateDto == null) {
            return ResponseEntity.badRequest().body("Update data is required.");
        }
        
        Order existingOrder = orderService.getById(id);
        if (existingOrder == null) {
            return ResponseEntity.notFound().build();
        }

        var subtotalPrice = updateDto.getSubtotalPrice() != null ? 
                updateDto.getSubtotalPrice() : existingOrder.getSubtotalPrice();
        var totalLineItemsPrice = updateDto.getTotalLineItemsPrice() != null ? 
                updateDto.getTotalLineItemsPrice() : existingOrder.getTotalLineItemsPrice();
        var totalPrice = updateDto.getTotalPrice() != null ? 
                updateDto.getTotalPrice() : existingOrder.getTotalPrice();
        var totalShippingPrice = updateDto.getTotalShippingPrice() != null ? 
                updateDto.getTotalShippingPrice() : existingOrder.getTotalShippingPrice();
        var totalDiscountPrice = updateDto.getTotalDiscountPrice() != null ? 
                updateDto.getTotalDiscountPrice() : existingOrder.getTotalDiscountPrice();

        orderService.update(id, subtotalPrice, totalLineItemsPrice, totalPrice, 
                totalShippingPrice, totalDiscountPrice);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/order")
    public ResponseEntity<List<OrderSearchResponse>> searchOrder(@RequestParam(required = false) String query) {
        var result = orderService.searchOrdersAsync(query);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/shopifysync")
    public ResponseEntity<?> syncShopify(HttpServletRequest request) {
        User user = userService.getUserByUserId(request);
        if (user != null) {
            var eCommerceSettings = userService.getEcommerceSettingByType(user, ECommTypes.SHOPIFY.getValue());
            orderService.syncOrdersAsync(eCommerceSettings);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("E commerce integration is not setup");
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{orderId}/add-tag")
    public ResponseEntity<?> addTagToOrder(@PathVariable String orderId, @RequestBody List<String> tagNames) {
        orderService.addTagToOrder(orderId, tagNames);
        return ResponseEntity.ok().build();
    }
} 