package com.arpit.mythicgates.helper;

import com.arpit.mythicgates.exception.custom.BadRequestException;
import com.arpit.mythicgates.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class ImageUploadHelper {
    private final CloudinaryService cloudinaryService;

    public String uploadImageToCloudinary(MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            if (!image.getContentType().startsWith("image/")) {
                throw new BadRequestException("Only image files are allowed");
            }

            if (image.getSize() > 10 * 1024 * 1024) {
                throw new BadRequestException("Image size cannot exceed 10MB");
            }

            return cloudinaryService.uploadImage(image);
        }

        return null;
    }
}
