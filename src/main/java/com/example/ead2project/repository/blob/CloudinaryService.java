package com.example.ead2project.repository.blob;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public CompletableFuture<String> uploadFile(String base64File) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Convert base64 to byte array
                byte[] imageBytes = Base64.getDecoder().decode(base64File);

                // Upload to cloudinary
                Map uploadResult = cloudinary.uploader().upload(imageBytes, ObjectUtils.asMap(
                    "folder", "products",
                    "resource_type", "auto"
                ));

                // Return the secure URL
                return uploadResult.get("secure_url").toString();

            } catch (IOException e) {
                throw new RuntimeException("Failed to upload file to Cloudinary", e);
            }
        });
    }

    public CompletableFuture<List<String>> uploadMultipleFiles(List<String> base64Files) {
        if (base64Files == null || base64Files.isEmpty()) {
            return CompletableFuture.completedFuture(new ArrayList<>());
        }

        List<CompletableFuture<String>> uploadTasks = base64Files.stream()
                .map(this::uploadFile)
                .toList();

        return CompletableFuture.allOf(uploadTasks.toArray(new CompletableFuture[0]))
                .thenApply(v -> uploadTasks.stream()
                        .map(CompletableFuture::join)
                        .toList());
    }

    public CompletableFuture<Boolean> deleteFile(String publicId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                return "ok".equals(result.get("result"));
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete file from Cloudinary", e);
            }
        });
    }

    // Helper method to extract public ID from URL
    public String getPublicIdFromUrl(String imageUrl) {
        // Example URL: https://res.cloudinary.com/your-cloud-name/image/upload/v1234567890/products/image123.jpg
        // We need to extract: products/image123
        String[] urlParts = imageUrl.split("/");
        int length = urlParts.length;
        if (length >= 2) {
            String fileName = urlParts[length - 1];
            String folder = urlParts[length - 2];
            // Remove file extension
            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
            return folder + "/" + fileName;
        }
        throw new IllegalArgumentException("Invalid Cloudinary URL");
    }
} 