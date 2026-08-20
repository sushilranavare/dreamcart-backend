package com.dreamcart.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

import java.util.UUID;

@Service
public class ImageStorageService {

    private final Path uploadDirectory =
            Paths.get("uploads/products");

    public ImageStorageService() throws IOException {

        Files.createDirectories(uploadDirectory);
    }

    public String saveImage(MultipartFile file)
            throws IOException {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException(
                    "Image file is required"
            );
        }

        String originalFilename =
                file.getOriginalFilename();

        String extension = "";

        if (originalFilename != null &&
                originalFilename.contains(".")) {

            extension =
                    originalFilename.substring(
                            originalFilename.lastIndexOf(".")
                    );
        }

        String filename =
                UUID.randomUUID() + extension;

        Path filePath =
                uploadDirectory.resolve(filename);

        Files.copy(
                file.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING
        );

        return "/uploads/products/" + filename;
    }
}