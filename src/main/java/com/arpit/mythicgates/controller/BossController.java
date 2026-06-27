package com.arpit.mythicgates.controller;

import com.arpit.mythicgates.model.dto.boss.BossRequest;
import com.arpit.mythicgates.model.dto.boss.BossResponse;
import com.arpit.mythicgates.model.dto.boss.UpdateBossRequest;
import com.arpit.mythicgates.model.dto.character.CharacterResponse;
import com.arpit.mythicgates.model.dto.character.UpdateCharacterRequest;
import com.arpit.mythicgates.model.dto.pagination.PageResponse;
import com.arpit.mythicgates.response.ApiResponse;
import com.arpit.mythicgates.service.BossService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BossController {
    private final BossService bossService;

    @PostMapping(value = "/admin/bosses", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BossResponse>> createBoss(
            @Valid @ModelAttribute BossRequest request,
            @RequestPart(value = "image") MultipartFile image
    ) {
        return bossService.createBoss(request, image);
    }

    @PutMapping(value = "/admin/bosses/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BossResponse>> updateBoss(
            @Valid @ModelAttribute UpdateBossRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @PathVariable UUID id
    ) {
        return bossService.updateBoss(id, request, image);
    }

    @DeleteMapping("/admin/bosses/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteBoss(
            @PathVariable UUID id
    ) {
        return bossService.deleteBoss(id);
    }

    @GetMapping("/bosses")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<PageResponse<BossResponse>>> getAllBosses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        return bossService.getAllBosses(page, size);
    }

    @GetMapping("/bosses/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<BossResponse>> getBoss(
            @PathVariable("id") UUID bossId
    ) {
        return bossService.getBoss(bossId);
    }
}
