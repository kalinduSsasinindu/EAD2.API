package com.example.ead2project.service.Services;


import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.example.ead2project.repository.Data.Entities.product.ProductVariant;
import com.example.ead2project.repository.Data.Entities.product.VariantOption;

public class ProductVariantGenerator {
    private static int variantCounter = 0;

    public static List<ProductVariant> generateProductVariants(
            List<VariantOption> options, 
            String baseSku, 
            BigDecimal basePrice, 
            Integer baseAvailableQuantity) {
        
        List<ProductVariant> variants = new ArrayList<>();
        List<Map<String, String>> combinations = generateCombinations(options);

        for (Map<String, String> combination : combinations) {
            String variantName = combination.values().stream()
                    .collect(Collectors.joining(", "));
                    
            String sku = baseSku + "-" + combination.values().stream()
                    .collect(Collectors.joining("-"));

            ProductVariant variant = new ProductVariant();
            variant.setVariantId(++variantCounter);
            variant.setSku(sku);
            variant.setName(variantName);
            variant.setPrice(basePrice);
            variant.setAvailableQuantity(baseAvailableQuantity);
            variant.setCommittedQuantity(0);
            variant.setActive(true);

            variants.add(variant);
        }

        return variants;
    }

    private static List<Map<String, String>> generateCombinations(List<VariantOption> options) {
        List<Map<String, String>> combinations = new ArrayList<>();
        recurse(options, 0, new HashMap<>(), combinations);
        return combinations;
    }

    private static void recurse(
            List<VariantOption> options, 
            int index, 
            Map<String, String> current, 
            List<Map<String, String>> combinations) {
            
        if (index == options.size()) {
            combinations.add(new HashMap<>(current));
            return;
        }

        VariantOption option = options.get(index);
        for (String value : option.getValues()) {
            current.put(option.getName(), value);
            recurse(options, index + 1, current, combinations);
            current.remove(option.getName());
        }
    }
} 