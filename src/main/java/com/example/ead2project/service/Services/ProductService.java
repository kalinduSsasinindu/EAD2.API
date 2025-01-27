package com.example.ead2project.service.Services;


import lombok.RequiredArgsConstructor;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.example.ead2project.repository.Data.DataService.MongoDBContext;
import com.example.ead2project.repository.Data.Entities.Search.ProductSearchResponse;
import com.example.ead2project.repository.Data.Entities.product.Product;
import com.example.ead2project.repository.Data.Entities.product.ProductVariant;
import com.example.ead2project.repository.Data.Entities.product.VariantOption;
import com.example.ead2project.repository.Data.Entities.tags.Tag;
import com.example.ead2project.repository.Helper.QueryBuilder;
import com.example.ead2project.repository.Helper.Utility;
import com.example.ead2project.repository.blob.CloudinaryService;
import com.example.ead2project.serviceInterface.Dto.MediaServiceDto;
import com.example.ead2project.serviceInterface.Dto.MediaUpdate;
import com.example.ead2project.serviceInterface.interfaces.IProductService;
import com.example.ead2project.serviceInterface.interfaces.ITagsService;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.mongodb.client.result.UpdateResult;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final CloudinaryService cloudinaryService;
    private final MongoDBContext mongoContext;
    private final ITagsService tagsService;
    private final HttpServletRequest httpRequest;

    private String getClientId() {
        return Utility.getUserIdFromClaims(httpRequest);
    }

    @Override
    public List<ProductSearchResponse> getAllProducts() {
        return mongoContext.getProducts().find(new Document())
                .sort(new Document("createdAt", -1))  // -1 for DESC
                .limit(100)
                .into(new ArrayList<>())
                .stream()
                .map(this::mapToSearchResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Product getProductById(String id) {
        return mongoContext.getProducts().find(new Document("_id", id)).first();
    }

    @Override
    public String createProduct(Product product) {
        CompletableFuture<List<String>> uploadFuture = cloudinaryService.uploadMultipleFiles(product.getImages());
        
        List<String> imgUrls = uploadFuture.join(); // Wait for upload to complete
        if (!imgUrls.isEmpty()) {
            product.getImgUrls().addAll(imgUrls);
        }
        
        product.setImages(null);
        product.setCreatedAt(LocalDateTime.now());
        
        mongoContext.getProducts().insertOneAsync(product);
        return product.getId();
    }

    @Override
    public boolean updateProduct(Product product) {
        CompletableFuture<List<String>> uploadFuture = cloudinaryService.uploadMultipleFiles(product.getImages());
        
        List<String> imgUrls = uploadFuture.join(); // Wait for upload to complete
        if (!imgUrls.isEmpty()) {
            product.getImgUrls().addAll(imgUrls);
        }
        
        product.setImages(null);
        product.setUpdatedAt(LocalDateTime.now());
        
        var result = (UpdateResult) mongoContext.getProducts().replaceOneAsync(
            new Document("_id", product.getId()),
            product
        ).join();
        return result.wasAcknowledged() && result.getModifiedCount() > 0;
    }

    @Override
    public boolean deleteProduct(String id) {
        var result = (UpdateResult) mongoContext.getProducts().updateOneAsync(
            new Document("_id", id),
            new Document("$set", new Document("isDeleted", true))
        ).join();
        return result.wasAcknowledged() && result.getModifiedCount() > 0;
    }

    @Override
    public boolean updateVariants(String id, List<ProductVariant> variants) {
        var result = (UpdateResult) mongoContext.getProducts().updateOneAsync(
            new Document("_id", id),
            new Document("$set", new Document("variants", variants))
        ).join();
        return result.wasAcknowledged() && result.getModifiedCount() > 0;
    }

    @Override
    public boolean updateTitleAndDescription(String id, String title, String description) {
        var result = (UpdateResult) mongoContext.getProducts().updateOneAsync(
            new Document("_id", id),
            new Document("$set", new Document()
                .append("title", title)
                .append("description", description))
        ).join();
        return result.wasAcknowledged() && result.getModifiedCount() > 0;
    }

    @Override
    public boolean updateOptions(String id, List<VariantOption> options) {
        var result = (UpdateResult) mongoContext.getProducts().updateOneAsync(
            new Document("_id", id),
            new Document("$set", new Document("options", options))
        ).join();
        return result.wasAcknowledged() && result.getModifiedCount() > 0;
    }

    @Override
    public List<ProductSearchResponse> searchProducts(String query) {
        List<Product> result;
        
        if (query == null || query.isEmpty()) {
            Document filter = new Document();
            result = mongoContext.getProducts().find(filter)
                    .sort(new Document("createdAt", -1))
                    .limit(20)
                    .into(new ArrayList<>());
        } else {
            List<String> searchFields = Arrays.asList("Title", "Variants.Barcode");
            var pipeline = QueryBuilder.buildSearchFilter(query, "product_search_index", getClientId(), searchFields);
            List<Bson> pipelineStages = pipeline.stream()
                    .map(doc -> (Bson) doc)
                    .collect(Collectors.toList());
            result = mongoContext.getProducts().aggregate(pipelineStages)
                    .into(new ArrayList<>());
        }

        logger.info("Search Results Count: {}", result.size());
        return result.stream()
                    .map(this::mapToSearchResponse)
                    .collect(Collectors.toList());
    }

    @Override
    public void addTagToProduct(String productId, List<String> tagNames) {
        List<Tag> tags = new ArrayList<>();
        
        for (String tagName : tagNames) {
            Tag tag = tagsService.addOrUpdateTag(tagName, "product");
            tags.add(tag);
        }
        
        List<String> tagNamesToAdd = tags.stream()
                                        .map(Tag::getName)
                                        .collect(Collectors.toList());

        Document filter = new Document("_id", productId);
        Document update = new Document("$set", new Document("tags", tagNamesToAdd));
        mongoContext.getProducts().updateOneAsync(filter, update);
    }

    @Override
    public boolean updateProductMedia(MediaServiceDto mediaServiceDto) {
        Product product = getProductById(mediaServiceDto.getProductId());
        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        // Upload new images
        CompletableFuture<List<String>> uploadFuture = cloudinaryService.uploadMultipleFiles(mediaServiceDto.getNewMediaBase64());
        List<String> newImageUrls = uploadFuture.join();

        if (product.getImgUrls() == null) {
            product.setImgUrls(new ArrayList<>());
        }
        product.getImgUrls().addAll(newImageUrls);

        // Handle deletions
        Set<String> urlsToDelete = mediaServiceDto.getMediaUpdates().stream()
                .filter(MediaUpdate::isDeleted)
                .map(MediaUpdate::getUrl)
                .collect(Collectors.toSet());

        if (!urlsToDelete.isEmpty()) {
            // Delete from Cloudinary
            urlsToDelete.forEach(url -> {
                String publicId = cloudinaryService.getPublicIdFromUrl(url);
                cloudinaryService.deleteFile(publicId);
            });

            // Update product URLs
            List<String> remainingUrls = product.getImgUrls().stream()
                    .filter(url -> !urlsToDelete.contains(url))
                    .collect(Collectors.toList());
            product.setImgUrls(remainingUrls);
        }

        var result = (UpdateResult) mongoContext.getProducts().replaceOneAsync(
            new Document("_id", product.getId()),
            product
        ).join();
        return result.wasAcknowledged() && result.getModifiedCount() > 0;
    }

    private ProductSearchResponse mapToSearchResponse(Product product) {
        // Implement your mapping logic here
        ProductSearchResponse response = new ProductSearchResponse();
        // ... map fields
        return response;
    }
} 