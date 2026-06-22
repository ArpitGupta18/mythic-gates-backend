package com.arpit.mythicgates.controller;

import com.arpit.mythicgates.model.dto.boss.BossRequest;
import com.arpit.mythicgates.model.dto.boss.BossResponse;
import com.arpit.mythicgates.response.ApiResponse;
import com.arpit.mythicgates.service.BossService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BossController {
    private final BossService bossService;

    @PostMapping(value = "/admin/bosses", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BossResponse>> createBoss(
            @Valid @ModelAttribute BossRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return bossService.createBoss(request, image);
    }
}
