package com.arpit.mythicgates.controller;

import com.arpit.mythicgates.model.dto.skill.BulkSkillRequest;
import com.arpit.mythicgates.model.dto.skill.SkillRequest;
import com.arpit.mythicgates.model.dto.skill.SkillResponse;
import com.arpit.mythicgates.response.ApiResponse;
import com.arpit.mythicgates.service.SkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SkillController {
    private final SkillService skillService;

    @PostMapping("/admin/characters/{id}/skills")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SkillResponse>> createSkill(
            @PathVariable UUID id,
            @Valid @RequestBody SkillRequest request
    ) {
        return skillService.createSkill(id, request);
    }

    @PostMapping("/admin/characters/{id}/skills/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<SkillResponse>>> createSkillBulk(
            @PathVariable UUID id,
            @Valid @RequestBody BulkSkillRequest request
    ) {
        return skillService.createSkillsBulk(id, request);
    }
}
