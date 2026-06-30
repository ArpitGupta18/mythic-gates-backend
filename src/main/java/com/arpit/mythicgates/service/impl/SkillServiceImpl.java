package com.arpit.mythicgates.service.impl;

import com.arpit.mythicgates.exception.custom.BadRequestException;
import com.arpit.mythicgates.exception.custom.ResourceNotFoundException;
import com.arpit.mythicgates.helper.ValidationHelper;
import com.arpit.mythicgates.mapper.SkillMapper;
import com.arpit.mythicgates.model.dto.skill.BulkSkillRequest;
import com.arpit.mythicgates.model.dto.skill.SkillRequest;
import com.arpit.mythicgates.model.dto.skill.SkillResponse;
import com.arpit.mythicgates.model.dto.skill.UpdateSkillRequest;
import com.arpit.mythicgates.model.entity.Character;
import com.arpit.mythicgates.model.entity.Skill;
import com.arpit.mythicgates.repository.CharacterRepository;
import com.arpit.mythicgates.repository.SkillRepository;
import com.arpit.mythicgates.response.ApiResponse;
import com.arpit.mythicgates.response.ApiResponseUtil;
import com.arpit.mythicgates.service.SkillService;
import com.arpit.mythicgates.utils.UuidGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {
    private final SkillRepository skillRepository;
    private final CharacterRepository characterRepository;
    private final ValidationHelper validationHelper;

    @Override
    public ResponseEntity<ApiResponse<SkillResponse>> createSkill(UUID characterId, SkillRequest request) {
        Character character = characterRepository.findByPublicId(characterId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Character doesn't exist"));

        if (skillRepository.existsByCharacterPublicIdAndTypeAndSlot(
                characterId,
                request.type(),
                request.slot()
        )) {
            throw new BadRequestException("This character already has this skill slot");
        }

        Skill skill = Skill.builder()
                .publicId(UuidGenerator.generate())
                .name(request.name())
                .type(request.type())
                .slot(request.slot())
                .damageMultiplier(request.damageMultiplier())
                .manaCost(request.manaCost())
                .cooldown(request.cooldown())
                .character(character)
                .build();

        Skill createdSkill = skillRepository.save(skill);

        return ApiResponseUtil.created("Skill added successfully", SkillMapper.toSkillResponseDto(createdSkill));
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<List<SkillResponse>>> createSkillsBulk(UUID characterId, BulkSkillRequest request) {
        Character character = characterRepository.findByPublicId(characterId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Character doesn't exist"));

        validationHelper.validateRequiredSkillSlots(request.skills());

        if (skillRepository.existsByCharacterPublicId(characterId))
            throw new BadRequestException("This character already has skills");

        List<Skill> skills = request.skills().stream()
                .map(skillRequest -> Skill.builder()
                        .publicId(UuidGenerator.generate())
                        .name(skillRequest.name().trim())
                        .type(skillRequest.type())
                        .slot(skillRequest.slot())
                        .damageMultiplier(skillRequest.damageMultiplier())
                        .manaCost(skillRequest.manaCost())
                        .cooldown(skillRequest.cooldown())
                        .character(character)
                        .build())
                .toList();

        List<Skill> savedSkills = skillRepository.saveAll(skills);

        List<SkillResponse> response = savedSkills.stream()
                .map(SkillMapper::toSkillResponseDto)
                .toList();

        return ApiResponseUtil.created("Skills added successfully", response);
    }

    @Override
    public ResponseEntity<ApiResponse<List<SkillResponse>>> getSkillsByCharacter(UUID characterId) {
        characterRepository.findByPublicId(characterId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Character doesn't exist"));

        List<SkillResponse> skills = skillRepository
                .findByCharacterPublicIdOrderByTypeAscSlotAsc(characterId)
                .stream()
                .map(SkillMapper::toSkillResponseDto)
                .toList();

        return ApiResponseUtil.success("Character skill fetched successfully", skills);
    }

    @Override
    public ResponseEntity<ApiResponse<SkillResponse>> getSkill(UUID skillId) {
        Skill skill = skillRepository.findByPublicId(skillId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Skill doesn't exist"));

        return ApiResponseUtil.success("Skill fetched successfully", SkillMapper.toSkillResponseDto(skill));
    }

    @Override
    public ResponseEntity<ApiResponse<SkillResponse>> updateSkill(UUID skillId, UpdateSkillRequest request) {
        Skill skill = skillRepository.findByPublicId(skillId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Skill doesn't exist"));

        skill.setName(request.name().trim());
        skill.setDamageMultiplier(request.damageMultiplier());
        skill.setManaCost(request.manaCost());

        Skill updatedSkill = skillRepository.save(skill);

        return ApiResponseUtil.success("Skill updated successfully", SkillMapper.toSkillResponseDto(updatedSkill));
    }
}
