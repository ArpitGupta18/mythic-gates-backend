package com.arpit.mythicgates.repository;

import com.arpit.mythicgates.model.entity.GoldPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GoldPackageRepository extends JpaRepository<GoldPackage, Long> {
    List<GoldPackage> findAllByActiveTrueOrderByPriceAmountAsc();

    Optional<GoldPackage> findByPublicIdAndActiveTrue(UUID publicId);
}
