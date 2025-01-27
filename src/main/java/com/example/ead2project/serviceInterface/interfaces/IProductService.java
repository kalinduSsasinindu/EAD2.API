package com.example.ead2project.serviceInterface.interfaces;


import java.util.List;

import com.example.ead2project.repository.Data.Entities.Search.ProductSearchResponse;
import com.example.ead2project.repository.Data.Entities.product.Product;
import com.example.ead2project.repository.Data.Entities.product.ProductVariant;
import com.example.ead2project.repository.Data.Entities.product.VariantOption;
import com.example.ead2project.serviceInterface.Dto.MediaServiceDto;

public interface IProductService {
    
    List<ProductSearchResponse> getAllProducts();
    
    Product getProductById(String id);
    
    String createProduct(Product product);
    
    boolean updateProduct(Product product);
    
    boolean deleteProduct(String id);
    
    boolean updateVariants(String id, List<ProductVariant> variants);
    
    boolean updateTitleAndDescription(String id, String title, String description);
    
    boolean updateOptions(String id, List<VariantOption> options);
    
    List<ProductSearchResponse> searchProducts(String query);
    
    void addTagToProduct(String productId, List<String> tagNames);
    
    boolean updateProductMedia(MediaServiceDto mediaServiceDto);
} 