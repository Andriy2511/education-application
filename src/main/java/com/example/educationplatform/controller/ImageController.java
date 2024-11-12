package com.example.educationplatform.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class ImageController {
    @GetMapping("/tutorphotos/{photo}")
    public ResponseEntity<byte[]> getImage(@PathVariable String photo) {
        byte[] imageBytes = new byte[0];
        try {
            String imagePath = "src/main/resources/static/tutorphotos/" + photo;
            Path path = Paths.get(imagePath);
            imageBytes = Files.readAllBytes(path);
        } catch (NoSuchFileException e) {
            log.warn("Cannot find a photo with path {}", e.getMessage());
        } catch (IOException e) {
            log.error("Error occurred while reading the photo with path {}", e.getMessage());
        }
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noCache().cachePrivate())
                .body(imageBytes);
    }

    public static String addPhotoToFolderWithUniqueName(MultipartFile photo){
        String uniqueFileName = null;
        if (photo != null && !photo.isEmpty()) {
            String originalFileName = photo.getOriginalFilename();
            uniqueFileName = getUniqueFileName(originalFileName);
            addPhotoToFolder(photo, uniqueFileName);
        }

        return uniqueFileName;
    }

    private static void addPhotoToFolder(MultipartFile photoFile, String uniquePhotoName){
        try {
            Path filePath = Paths.get("src/main/resources/static/tutorphotos/" + uniquePhotoName);
            photoFile.transferTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getUniqueFileName(String fileName) {
        Path filePath = Paths.get("src/main/resources/static/tutorphotos/" + fileName);
        if (Files.exists(filePath)) {
            String uniqueFileName = fileName.substring(0, fileName.lastIndexOf('.'))
                    + "_" + System.currentTimeMillis() + fileName.substring(fileName.lastIndexOf('.'));
            return getUniqueFileName(uniqueFileName);
        }
        return fileName;
    }
}
