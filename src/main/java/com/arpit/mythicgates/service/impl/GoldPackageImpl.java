package com.arpit.mythicgates.service.impl;

import com.arpit.mythicgates.mapper.GoldPackageMapper;
import com.arpit.mythicgates.model.dto.gold.GoldPackageResponse;
import com.arpit.mythicgates.model.entity.GoldPackage;
import com.arpit.mythicgates.repository.GoldPackageRepository;
import com.arpit.mythicgates.service.GoldPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoldPackageImpl implements GoldPackageService {
    private final GoldPackageRepository goldPackageRepository;

    @Override
    public List<GoldPackageResponse> getGoldPackages() {
        List<GoldPackage> packages = goldPackageRepository.findAllByActiveTrueOrderByPriceAmountAsc();

        return packages.stream().map(
                        GoldPackageMapper::toGoldPackageResponse
                )
                .toList();
    }
}
