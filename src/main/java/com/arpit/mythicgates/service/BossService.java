package com.arpit.mythicgates.service;

import com.arpit.mythicgates.model.dto.boss.BossRequest;
import com.arpit.mythicgates.model.dto.boss.BossResponse;
import com.arpit.mythicgates.model.dto.boss.UpdateBossRequest;
import com.arpit.mythicgates.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface BossService {
    ResponseEntity<ApiResponse<BossResponse>> createBoss(BossRequest request, MultipartFile image);

    ResponseEntity<ApiResponse<BossResponse>> updateBoss(UUID bossId, UpdateBossRequest request, MultipartFile image);
}
