package com.arpit.mythicgates.service.impl;

import com.arpit.mythicgates.exception.custom.BadRequestException;
import com.arpit.mythicgates.exception.custom.BossAlreadyExistsException;
import com.arpit.mythicgates.exception.custom.ResourceNotFoundException;
import com.arpit.mythicgates.helper.ImageValidator;
import com.arpit.mythicgates.mapper.BossMapper;
import com.arpit.mythicgates.model.dto.boss.BossRequest;
import com.arpit.mythicgates.model.dto.boss.BossResponse;
import com.arpit.mythicgates.model.dto.boss.UpdateBossRequest;
import com.arpit.mythicgates.model.dto.pagination.PageResponse;
import com.arpit.mythicgates.model.entity.Boss;
import com.arpit.mythicgates.repository.BossRepository;
import com.arpit.mythicgates.response.ApiResponse;
import com.arpit.mythicgates.response.ApiResponseUtil;
import com.arpit.mythicgates.service.BossService;
import com.arpit.mythicgates.service.ImageStorageService;
import com.arpit.mythicgates.utils.UuidGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BossServiceImpl implements BossService {
    private final BossRepository bossRepository;
    private final ImageValidator imageValidator;
    private final @Qualifier("cloudinaryService") ImageStorageService imageStorageService;

    @Override
    public ResponseEntity<ApiResponse<BossResponse>> createBoss(BossRequest request, MultipartFile image) {
        if (image == null || image.isEmpty())
            throw new BadRequestException("Boss image is required");

        String name = request.name().trim();

        if (bossRepository.existsByNameIgnoreCase(name))
            throw new BossAlreadyExistsException("Boss with this name already exists");

        imageValidator.validateImage(image);

        String imageUrl = imageStorageService.uploadImage(image);

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

    @Override
    public ResponseEntity<ApiResponse<BossResponse>> updateBoss(UUID bossId, UpdateBossRequest request, MultipartFile image) {
        Boss bossToUpdate = bossRepository.findByPublicId(bossId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Boss doesn't exist"));

        String name = request.name().trim();

        if (!bossToUpdate.getName().equalsIgnoreCase(name) && bossRepository.existsByNameIgnoreCase(name)) {
            throw new BossAlreadyExistsException("Boss with this name already exists");
        }

        imageValidator.validateImage(image);

        String imageUrl = null;
        if (image == null || image.isEmpty()) {
            imageUrl = bossToUpdate.getImageUrl();
        } else {
            imageUrl = imageStorageService.uploadImage(image);
        }

        bossToUpdate.setName(request.name());
        bossToUpdate.setRarity(request.rarity());
        bossToUpdate.setBaseHealth(request.baseHealth());
        bossToUpdate.setAttack(request.attack());
        bossToUpdate.setDefense(request.defense());
        bossToUpdate.setCritChance(request.critChance());
        bossToUpdate.setDodgeChance(request.dodgeChance());
        bossToUpdate.setRageThreshold(request.rageThreshold());
        bossToUpdate.setRewardMin(request.rewardMin());
        bossToUpdate.setRewardMax(request.rewardMax());
        bossToUpdate.setHealInterval(request.healInterval());
        bossToUpdate.setHealPercentage(request.healPercentage());
        bossToUpdate.setImageUrl(imageUrl);
        bossToUpdate.setUpdatedAt(LocalDateTime.now());

        Boss updatedBoss = bossRepository.save(bossToUpdate);

        return ApiResponseUtil.success("Boss updated successfully", BossMapper.toBossResponseDto(updatedBoss));
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> deleteBoss(UUID bossId) {
        Boss boss = bossRepository.findByPublicId(bossId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Boss doesn't exist"));

        bossRepository.delete(boss);

        return ApiResponseUtil.success("Boss deleted successfully", null);
    }

    @Override
    public ResponseEntity<ApiResponse<PageResponse<BossResponse>>> getAllBosses(int page, int size) {
        Sort sort = Sort.by("rarity").ascending()
                .and(Sort.by("id").ascending());

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Boss> bossPage = bossRepository.findAll(pageable);

        List<BossResponse> bosses = bossPage
                .getContent()
                .stream()
                .map(
                        BossMapper::toBossResponseDto
                )
                .toList();

        PageResponse<BossResponse> pageResponse = new PageResponse<>(
                bosses,
                bossPage.getNumber(),
                bossPage.getSize(),
                bossPage.getTotalElements(),
                bossPage.getTotalPages(),
                bossPage.isLast()
        );

        return ApiResponseUtil.success("All bosses fetched successfully", pageResponse);
    }

    @Override
    public ResponseEntity<ApiResponse<BossResponse>> getBoss(UUID bossId) {
        Boss boss = bossRepository.findByPublicId(bossId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Boss doesn't exist"));

        return ApiResponseUtil.success("Boss fetched successfully", BossMapper.toBossResponseDto(boss));
    }
}
