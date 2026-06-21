package com.arpit.mythicgates.service.impl;

import com.arpit.mythicgates.exception.custom.BadRequestException;
import com.arpit.mythicgates.exception.custom.ResourceNotFoundException;
import com.arpit.mythicgates.mapper.SkillMapper;
import com.arpit.mythicgates.model.dto.skill.SkillRequest;
import com.arpit.mythicgates.model.dto.skill.SkillResponse;
import com.arpit.mythicgates.model.entity.Character;
import com.arpit.mythicgates.model.entity.Skill;
import com.arpit.mythicgates.repository.CharacterRepository;
import com.arpit.mythicgates.repository.SkillRepository;
import com.arpit.mythicgates.response.ApiResponse;
import com.arpit.mythicgates.response.ApiResponseUtil;
import com.arpit.mythicgates.service.SkillService;
import com.arpit.mythicgates.utils.UuidGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {
    private final SkillRepository skillRepository;
    private final CharacterRepository characterRepository;

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
                .character(character)
                .build();

        Skill createdSkill = skillRepository.save(skill);

        return ApiResponseUtil.created("Skill added successfully", SkillMapper.toSkillResponseDto(createdSkill));
    }
}
