package com.example.ead2project.controllers;


import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.ead2project.repository.Data.Entities.Order.LineItem;
import com.example.ead2project.repository.Data.Entities.Order.Order;
import com.example.ead2project.repository.Data.Entities.Order.PaymentInfo;
import com.example.ead2project.repository.Data.Entities.Order.ShippingAddress;
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
    @GetMapping("/order")
    public ResponseEntity<List<OrderSearchResponse>> searchOrder(@RequestParam(required = false) String query) {
        var result = orderService.searchOrdersAsync(query);
        return ResponseEntity.ok(result);
    }

    

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{orderId}/add-tag")
    public ResponseEntity<?> addTagToOrder(@PathVariable String orderId, @RequestBody List<String> tagNames) {
        orderService.addTagToOrder(orderId, tagNames);
        return ResponseEntity.ok().build();
    }
} 