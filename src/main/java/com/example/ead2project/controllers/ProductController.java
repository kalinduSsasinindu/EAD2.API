package com.example.ead2project.controllers;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.ead2project.serviceInterface.Dto.MediaServiceDto;
import com.example.ead2project.controllers.Dtos.MediaWebDto;
import com.example.ead2project.repository.Data.Entities.Search.ProductSearchResponse;
import com.example.ead2project.repository.Data.Entities.product.Product;
import com.example.ead2project.repository.Data.Entities.product.ProductVariant;
import com.example.ead2project.repository.Data.Entities.product.VariantOption;
import com.example.ead2project.service.Services.ProductVariantGenerator;
import com.example.ead2project.serviceInterface.interfaces.IProductService;
import java.math.BigDecimal;
import java.util.List;
import org.modelmapper.ModelMapper;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    
    private final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final IProductService productService;
    private final ModelMapper mapper;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<ProductSearchResponse> getAll() {
        return productService.getAllProducts();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable String id) {
        Product product = productService.getProductById(id);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        String id = productService.createProduct(product);
        product.setId(id);
        return ResponseEntity.created(null).body(product);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping
    public ResponseEntity<?> update(@RequestBody Product product) {
        return productService.updateProduct(product) 
            ? ResponseEntity.ok().build() 
            : ResponseEntity.notFound().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}/variants")
    public ResponseEntity<?> updateVariants(@PathVariable String id, @RequestBody List<ProductVariant> variants) {
        return productService.updateVariants(id, variants)
            ? ResponseEntity.ok().build()
            : ResponseEntity.notFound().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}/titledescription")
    public ResponseEntity<?> updateTitleDescription(@PathVariable String id, 
            @RequestParam String title, @RequestParam String description) {
        return productService.updateTitleAndDescription(id, title, description)
            ? ResponseEntity.ok().build()
            : ResponseEntity.notFound().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}/options")
    public ResponseEntity<?> updateOptions(@PathVariable String id, @RequestBody List<VariantOption> options) {
        if (productService.updateOptions(id, options)) {
            List<ProductVariant> variants = ProductVariantGenerator.generateProductVariants(
                options, null, null, null);
            return productService.updateVariants(id, variants)
                ? ResponseEntity.ok(variants)
                : ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return productService.deleteProduct(id)
            ? ResponseEntity.ok().build()
            : ResponseEntity.notFound().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/generate-variants")
    public ResponseEntity<List<ProductVariant>> generateVariants(@RequestBody List<VariantOption> options) {
        String baseSku = "BASE-SKU";
        BigDecimal basePrice = BigDecimal.ZERO;
        Integer baseAvailableQuantity = 100;

        List<ProductVariant> variants = ProductVariantGenerator.generateProductVariants(
            options, baseSku, basePrice, baseAvailableQuantity);

        return ResponseEntity.ok(variants);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/search")
    public ResponseEntity<List<ProductSearchResponse>> searchProduct(@RequestParam(required = false) String query) {
        List<ProductSearchResponse> result = productService.searchProducts(query);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/media")
    public ResponseEntity<?> updateMedia(@RequestBody MediaWebDto mediaWebDto) {
        MediaServiceDto media = mapper.map(mediaWebDto, MediaServiceDto.class);
        return productService.updateProductMedia(media)
            ? ResponseEntity.ok().build()
            : ResponseEntity.notFound().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{productId}/add-tag")
    public ResponseEntity<?> addTagToProduct(@PathVariable String productId, @RequestBody List<String> tagNames) {
        productService.addTagToProduct(productId, tagNames);
        return ResponseEntity.ok().build();
    }
}
