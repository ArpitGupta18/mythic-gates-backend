package com.arpit.mythicgates.controller;

import com.arpit.mythicgates.model.dto.gold.GoldPackageResponse;
import com.arpit.mythicgates.response.ApiResponse;
import com.arpit.mythicgates.response.ApiResponseUtil;
import com.arpit.mythicgates.service.GoldPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/golds")
@RequiredArgsConstructor
public class GoldPackageController {
    private final GoldPackageService goldPackageService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<List<GoldPackageResponse>>> getGoldPackages() {
        List<GoldPackageResponse> packageResponses = goldPackageService.getGoldPackages();

        return ApiResponseUtil.success("Gold packages fetched success", packageResponses);
    }
}
