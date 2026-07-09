package com.arpit.mythicgates.service;

import com.arpit.mythicgates.model.dto.gold.GoldPackageResponse;

import java.util.List;

public interface GoldPackageService {
    List<GoldPackageResponse> getGoldPackages();
}
