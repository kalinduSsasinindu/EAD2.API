package com.example.ead2project.service;

;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.ead2project.repository.Data.DataService.MongoDBContext;
import com.example.ead2project.repository.Data.Entities.Order.Order;
import com.example.ead2project.repository.Helper.Utility;
import com.example.ead2project.serviceInterface.interfaces.ITagsService;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final MongoDBContext mongoContext;
    private final ModelMapper mapper;
    private final HttpServletRequest request;
    private final BlobService blobService;
    private final ITagsService tagsService;
    private final String requestFilter;

    private String getClientId() {
        return Utility.getUserIdFromClaims(request);
    }

    @Override
    public List<OrderSearchResponse> getOrders() {
        var orders = mongoContext.getOrders().find(new Document())
                .sort(new Document("createdAt", -1))
                .limit(50)
                .into(new ArrayList<>());
        return mapper.map(orders, new TypeToken<List<OrderSearchResponse>>(){}.getType());
    }

    @Override
    public List<OrderCountItemServiceDto> getOrdersUpUntil(LocalDateTime upUntil) {
        var filter = new Document("createdAt", new Document("$gt", upUntil));
        
        var orders = mongoContext.getOrders().find(filter)
                .sort(new Document("createdAt", 1))
                .limit(50)
                .into(new ArrayList<>());

        return orders.stream()
                .collect(Collectors.groupingBy(
                    order -> order.getCreatedAt().toLocalDate(),
                    Collectors.collectingAndThen(
                        Collectors.counting(),
                        count -> new OrderCountItemServiceDto(
                            order.getCreatedAt().format(DateTimeFormatter.ISO_DATE),
                            count.intValue()
                        )
                    )
                ))
                .values()
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderSearchResponse> getOrdersByStatus(String fulfillmentStatus) {
        var filter = new Document("fulfillmentStatus", fulfillmentStatus);
        var orders = mongoContext.getOrders().find(filter)
                .sort(new Document("createdAt", -1))
                .limit(50)
                .into(new ArrayList<>());
        return mapper.map(orders, new TypeToken<List<OrderSearchResponse>>(){}.getType());
    }

    @Override
    public PagedOrdersResultServiceDto getOrdersWithTracking(int start, int end, String queryText) {
        int pageSize = end - start;
        var result = fetchOrdersAndCount(start, pageSize, queryText);
        
        if (result.getOrders().isEmpty()) {
            return null;
        }

        var orderDtos = mapper.map(result.getOrders(), new TypeToken<List<OrderServiceDto>>(){}.getType());
        enrichOrdersWithTrackingInfo(orderDtos);

        return PagedOrdersResultServiceDto.builder()
                .orders(orderDtos)
                .totalRecords(result.getTotalCount())
                .build();
    }

    @Override
    public void createOrder(Order order) {
        order.setName(generateOrderName(order));
        order.setCreatedAt(LocalDateTime.now());
        order.setTags(new ArrayList<>());

        var productIds = order.getLineItems().stream()
                .map(LineItem::getProductId)
                .distinct()
                .collect(Collectors.toList());

        var products = getProductsByIds(productIds);

        for (LineItem lineItem : order.getLineItems()) {
            var product = products.stream()
                    .filter(p -> p.getId().equals(lineItem.getProductId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            var productVariant = getProductVariant(product, lineItem.getVariantId());
            validateStockAvailability(productVariant, lineItem.getQuantity());
            updateStockQuantities(productVariant, lineItem.getQuantity());
            updateProductStock(product);
        }

        mongoContext.getOrders().insertOneAsync(order);
    }

    @Override
    public void updateOrder(String id, List<LineItem> lineItems) {
        var order = getOrderById(id);

        var fulfilledLineItems = lineItems.stream()
                .filter(x -> "tofulfill".equals(x.getFulfillmentStatus()))
                .collect(Collectors.toList());
                
        var productIds = fulfilledLineItems.stream()
                .map(LineItem::getProductId)
                .distinct()
                .collect(Collectors.toList());
                
        var products = getProductsByIds(productIds);

        for (LineItem lineItem : fulfilledLineItems) {
            var product = products.stream()
                    .filter(p -> p.getId().equals(lineItem.getProductId()))
                    .findFirst()
                    .orElseThrow();

            var productVariant = getProductVariant(product, lineItem.getVariantId());
            adjustCommittedQuantity(productVariant, lineItem.getQuantity());
            updateProductStock(product);
        }

        lineItems.stream()
                .filter(x -> "tofulfill".equals(x.getFulfillmentStatus()))
                .forEach(item -> item.setFulfillmentStatus("fulfilled"));

        var filter = new Document("_id", id);
        var update = new Document("$set", new Document("lineItems", lineItems));
        mongoContext.getOrders().updateOneAsync(filter, update).join();

        updateOrderFulfilledStatus(id);
        updateStock(order, lineItems);
    }

    private void updateStock(Order order, List<LineItem> lineItems) {
        updateInventoryForDeletedItems(order, lineItems);
        updateInventoryForAddedItems(order, lineItems);
        updateInventoryForUpdatedQuantities(order, lineItems);
    }

    private void updateInventoryForDeletedItems(Order order, List<LineItem> lineItems) {
        var excludedLineItems = order.getLineItems().stream()
                .filter(li -> lineItems.stream()
                        .noneMatch(oli -> oli.getProductId().equals(li.getProductId()) 
                                && oli.getVariantId() == li.getVariantId()))
                .collect(Collectors.toList());

        for (LineItem lineItem : excludedLineItems) {
            var productIds = order.getLineItems().stream()
                    .map(LineItem::getProductId)
                    .distinct()
                    .collect(Collectors.toList());

            if (productIds.isEmpty()) {
                throw new RuntimeException("No product IDs were found in the provided line items.");
            }

            var products = getProductsByIds(productIds);
            var product = products.stream()
                    .filter(p -> p.getId().equals(lineItem.getProductId()))
                    .findFirst()
                    .orElseThrow();

            var productVariant = getProductVariant(product, lineItem.getVariantId());
            updateDeletedStockQuantities(productVariant, lineItem.getQuantity());
            updateProductStock(product);
        }

        order.getLineItems().removeAll(excludedLineItems);
    }

    private void updateInventoryForAddedItems(Order order, List<LineItem> lineItems) {
        var newLineItems = lineItems.stream()
                .filter(li -> order.getLineItems().stream()
                        .noneMatch(oli -> oli.getProductId().equals(li.getProductId()) 
                                && oli.getVariantId() == li.getVariantId()))
                .collect(Collectors.toList());

        for (LineItem lineItem : newLineItems) {
            var productIds = lineItems.stream()
                    .map(LineItem::getProductId)
                    .distinct()
                    .collect(Collectors.toList());

            if (productIds.isEmpty()) {
                throw new RuntimeException("No product IDs were found in the provided line items.");
            }

            var products = getProductsByIds(productIds);
            var product = products.stream()
                    .filter(p -> p.getId().equals(lineItem.getProductId()))
                    .findFirst()
                    .orElseThrow();

            var productVariant = getProductVariant(product, lineItem.getVariantId());
            validateStockAvailability(productVariant, lineItem.getQuantity());
            updateStockQuantities(productVariant, lineItem.getQuantity());
            updateProductStock(product);
        }

        order.getLineItems().addAll(newLineItems);
    }

    private void updateInventoryForUpdatedQuantities(Order order, List<LineItem> lineItems) {
        for (LineItem lineItem : lineItems) {
            var products = getProductsByIds(lineItems.stream()
                    .map(LineItem::getProductId)
                    .distinct()
                    .collect(Collectors.toList()));

            var product = products.stream()
                    .filter(p -> p.getId().equals(lineItem.getProductId()))
                    .findFirst()
                    .orElseThrow();

            var productVariant = getProductVariant(product, lineItem.getVariantId());
            var orderLineItem = order.getLineItems().stream()
                    .filter(li -> li.getProductId().equals(lineItem.getProductId()) 
                            && li.getVariantId() == lineItem.getVariantId())
                    .findFirst()
                    .orElseThrow();

            if (orderLineItem.getQuantity() < lineItem.getQuantity()) {
                validateStockAvailability(productVariant, lineItem.getQuantity() - orderLineItem.getQuantity());
                updateStockQuantities(productVariant, lineItem.getQuantity() - orderLineItem.getQuantity());
                updateProductStock(product);
            }

            if (orderLineItem.getQuantity() > lineItem.getQuantity()) {
                updateDeletedStockQuantities(productVariant, orderLineItem.getQuantity() - lineItem.getQuantity());
                updateProductStock(product);
            }
        }
    }

    private void updateProductStock(Product product) {
        var updateFilter = new Document("_id", product.getId());
        var updateDoc = new Document("$set", new Document("variants", product.getVariants()));
        
        var result = mongoContext.getProducts().updateOneAsync(updateFilter, updateDoc).join();
        
        if (result.getModifiedCount() == 0) {
            throw new RuntimeException("Failed to update stock for product " + product.getTitle());
        }
    }

    @Override
    public void updateShippingAddress(String id, ShippingAddress shippingAddress) {
        var filter = new Document("_id", id);
        var update = new Document("$set", new Document("shippingAddress", shippingAddress));
        mongoContext.getOrders().updateOneAsync(filter, update).join();
    }

    @Override
    public void updatePaymentInfo(String id, PaymentInfo paymentInfo) {
        var filter = new Document("_id", id);
        var update = new Document("$set", new Document("paymentInfo", paymentInfo));
        mongoContext.getOrders().updateOneAsync(filter, update).join();
        updateOrderPaymentStatus(id);
    }

    @Override
    public void updateFulfillStatus(String id, String fulfillStatus) {
        var filter = new Document("_id", id);
        var update = new Document("$set", new Document("fulfillmentStatus", fulfillStatus));
        mongoContext.getOrders().updateOneAsync(filter, update).join();
        
        addTimelineAsync(id, TimeLineDetails.builder()
                .createdAt(LocalDateTime.now())
                .comment("fulfillment status updated to " + fulfillStatus)
                .build());
    }

    @Override
    public void updatePaymentStatus(String id, String paymentStatus, BigDecimal amount, String paymentMethod) {
        var filter = new Document("_id", id);
        var update = new Document("$set", new Document("financialStatus", paymentStatus));
        mongoContext.getOrders().updateOneAsync(filter, update).join();
        
        addTimelineAsync(id, TimeLineDetails.builder()
                .createdAt(LocalDateTime.now())
                .comment(String.format("payment status updated %s amount paid %s LKR via %s", 
                        paymentStatus, amount, paymentMethod))
                .build());
    }

    private void updateOrderPaymentStatus(String id) {
        var order = getOrderById(id);
        if (order == null) return;

        var totalPaidAmount = order.getPaymentInfo().getTotalPaidAmount();
        var totalPrice = order.getTotalPrice();

        String orderFulfillStatus;
        if (totalPaidAmount.compareTo(totalPrice) == 0) {
            orderFulfillStatus = "paid";
        } else if (totalPaidAmount.compareTo(BigDecimal.ZERO) == 0) {
            orderFulfillStatus = "pending";
        } else if (totalPaidAmount.compareTo(totalPrice) < 0) {
            orderFulfillStatus = "partially_paid";
        } else {
            orderFulfillStatus = "pending";
        }

        var latestPayment = order.getPaymentInfo().getPayments().stream()
                .reduce((first, second) -> second)
                .orElse(null);
                
        if (latestPayment == null) return;

        String paymentMethod = EnumHelper.getEnumDescription(
                PaymentOptions.class, 
                Integer.parseInt(latestPayment.getPaymentMethod()));
                
        updatePaymentStatus(id, orderFulfillStatus, latestPayment.getAmount(), 
                paymentMethod != null ? paymentMethod : "Unknown");
    }

    private void updateOrderFulfilledStatus(String id) {
        var order = getOrderById(id);
        if (order == null) return;

        int totalLineItemCount = order.getLineItems().size();
        long fulfilledCount = order.getLineItems().stream()
                .filter(item -> "fulfilled".equals(item.getFulfillmentStatus()))
                .count();

        String orderFulfillStatus;
        if (fulfilledCount == totalLineItemCount) {
            orderFulfillStatus = "fulfilled";
        } else if (fulfilledCount > 0) {
            orderFulfillStatus = "partially_fulfilled";
        } else {
            orderFulfillStatus = null;
        }

        updateFulfillStatus(id, orderFulfillStatus);
    }

    @Override
    public void addTimelineAsync(String orderId, TimeLineDetails details) {
        var imgUrls = blobService.uploadMedia(details.getImages()).join();
        details.setImgUrls(details.getImgUrls() != null ? details.getImgUrls() : new ArrayList<>());
        
        if (!imgUrls.isEmpty()) {
            details.getImgUrls().addAll(imgUrls);
        }

        details.setImages(null);
        details.setCreatedAt(LocalDateTime.now());

        var filter = new Document("_id", orderId);
        var update = new Document("$push", new Document("timeLineDetails", details));
        mongoContext.getOrders().updateOneAsync(filter, update).join();
    }

    // ... Rest of the implementation ...
} 