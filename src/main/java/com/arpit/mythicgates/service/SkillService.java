package com.arpit.mythicgates.service;

import com.arpit.mythicgates.model.dto.skill.BulkSkillRequest;
import com.arpit.mythicgates.model.dto.skill.SkillRequest;
import com.arpit.mythicgates.model.dto.skill.SkillResponse;
import com.arpit.mythicgates.model.dto.skill.UpdateSkillRequest;
import com.arpit.mythicgates.response.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface SkillService {
    ResponseEntity<ApiResponse<SkillResponse>> createSkill(UUID characterId, SkillRequest request);
    ResponseEntity<ApiResponse<List<SkillResponse>>> createSkillsBulk(UUID characterId, BulkSkillRequest request);
    ResponseEntity<ApiResponse<List<SkillResponse>>> getSkillsByCharacter(UUID characterId);
    ResponseEntity<ApiResponse<SkillResponse>> getSkill(UUID skillId);
    ResponseEntity<ApiResponse<SkillResponse>> updateSkill(UUID skillId, UpdateSkillRequest request);
}
