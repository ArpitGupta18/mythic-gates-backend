package com.arpit.mythicgates.service.impl;

import com.arpit.mythicgates.exception.custom.BadRequestException;
import com.arpit.mythicgates.exception.custom.BossAlreadyExistsException;
import com.arpit.mythicgates.helper.ImageUploadHelper;
import com.arpit.mythicgates.mapper.BossMapper;
import com.arpit.mythicgates.model.dto.boss.BossRequest;
import com.arpit.mythicgates.model.dto.boss.BossResponse;
import com.arpit.mythicgates.model.entity.Boss;
import com.arpit.mythicgates.repository.BossRepository;
import com.arpit.mythicgates.response.ApiResponse;
import com.arpit.mythicgates.response.ApiResponseUtil;
import com.arpit.mythicgates.service.BossService;
import com.arpit.mythicgates.service.CloudinaryService;
import com.arpit.mythicgates.utils.UuidGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BossServiceImpl implements BossService {
    private final BossRepository bossRepository;
    private final ImageUploadHelper imageUploadHelper;

    @Override
    public ResponseEntity<ApiResponse<BossResponse>> createBoss(BossRequest request, MultipartFile image) {
        String name = request.name().trim();

        if (bossRepository.existsByNameIgnoreCase(name))
            throw new BossAlreadyExistsException("Boss with this name already exists");

        String imageUrl = imageUploadHelper.uploadImageToCloudinary(image);

        if (imageUrl == null) {
            throw new BadRequestException("Failed to upload image");
        }

        Boss boss = Boss.builder()
                .publicId(UuidGenerator.generate())
                .name(request.name())
                .rarity(request.rarity())
                .baseHealth(request.baseHealth())
                .attack(request.attack())
                .defense(request.defense())
                .critChance(request.critChance())
                .dodgeChance(request.dodgeChance())
                .rageThreshold(request.rageThreshold())
                .rewardMin(request.rewardMin())
                .rewardMax(request.rewardMax())
                .healInterval(request.healInterval())
                .healPercentage(request.healPercentage())
                .imageUrl(imageUrl)
                .build();

        Boss createdBoss = bossRepository.save(boss);

        return ApiResponseUtil.created("Boss created successfully", BossMapper.toBossResponseDto(createdBoss));
    }
}
