package com.arpit.mythicgates.service.impl;

import com.arpit.mythicgates.exception.custom.BadRequestException;
import com.arpit.mythicgates.exception.custom.ImageUploadFailedException;
import com.arpit.mythicgates.service.ImageStorageService;
import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Qualifier("cloudinaryService")
public class CloudinaryServiceImpl implements ImageStorageService {
    private final Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile file) {
        try {

            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    Map.of("folder", "mythic-gates/characters")
            );

            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new ImageUploadFailedException("Failed to upload image");
        }
    }

}
