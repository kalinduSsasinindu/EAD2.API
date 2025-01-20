package com.example.ead2project.controllers;

import com.example.ead2project.services.CloudinaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/files")
@Tag(name = "File Upload", description = "File Upload API using Cloudinary")
public class FileUploadController {

    private final CloudinaryService cloudinaryService;

    public FileUploadController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @Operation(
        summary = "Upload multiple files",
        description = "Upload multiple files to Cloudinary"
    )
    @PostMapping("/upload")
    public CompletableFuture<ResponseEntity<List<String>>> uploadFiles(
            @RequestBody List<String> base64Files) {
        return cloudinaryService.uploadMultipleFiles(base64Files)
                .thenApply(ResponseEntity::ok);
    }

    @Operation(
        summary = "Delete file",
        description = "Delete a file from Cloudinary using its URL"
    )
    @DeleteMapping
    public CompletableFuture<ResponseEntity<Boolean>> deleteFile(
            @RequestParam String imageUrl) {
        String publicId = cloudinaryService.getPublicIdFromUrl(imageUrl);
        return cloudinaryService.deleteFile(publicId)
                .thenApply(ResponseEntity::ok);
    }
} 