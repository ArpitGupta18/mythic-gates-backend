package com.arpit.mythicgates.service.impl;

import com.arpit.mythicgates.exception.custom.BadRequestException;
import com.arpit.mythicgates.exception.custom.CharacterAlreadyExistsException;
import com.arpit.mythicgates.exception.custom.ResourceNotFoundException;
import com.arpit.mythicgates.mapper.CharacterMapper;
import com.arpit.mythicgates.model.dto.character.CharacterRequest;
import com.arpit.mythicgates.model.dto.character.CharacterResponse;
import com.arpit.mythicgates.model.dto.character.UpdateCharacterRequest;
import com.arpit.mythicgates.model.entity.Character;
import com.arpit.mythicgates.repository.CharacterRepository;
import com.arpit.mythicgates.repository.UserRepository;
import com.arpit.mythicgates.response.ApiResponse;
import com.arpit.mythicgates.response.ApiResponseUtil;
import com.arpit.mythicgates.service.CharacterService;
import com.arpit.mythicgates.service.CloudinaryService;
import com.arpit.mythicgates.utils.UuidGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CharacterServiceImpl implements CharacterService {
    private final CharacterRepository characterRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public ResponseEntity<ApiResponse<CharacterResponse>> addCharacter(CharacterRequest request, MultipartFile image) {
        String name = request.name().trim();
        if (characterRepository.existsByNameIgnoreCase(name)) {
            throw new CharacterAlreadyExistsException("Character with this name already exists");
        }

        String imageUrl = null;

        if (image != null && !image.isEmpty()) {
            if (!image.getContentType().startsWith("image/")) {
                throw new BadRequestException("Only image files are allowed");
            }

            if (image.getSize() > 10 * 1024 * 1024) {
                throw new BadRequestException("Image size cannot exceed 10MB");
            }

            imageUrl = cloudinaryService.uploadImage(image);
        }

        Character character = Character.builder()
                .publicId(UuidGenerator.generate())
                .name(request.name())
                .rarity(request.rarity())
                .baseHealth(request.baseHealth())
                .baseMana(request.baseMana())
                .attack(request.attack())
                .defense(request.defense())
                .healPower(request.healPower())
                .critChance(request.critChance())
                .dodgeChance(request.dodgeChance())
                .price(request.price())
                .imageUrl(imageUrl)
                .build();

        Character createdCharacter = characterRepository.save(character);

        return ApiResponseUtil.created("Character created successfully", CharacterMapper.toCharacterResponseDto(createdCharacter));
    }

    @Override
    public ResponseEntity<ApiResponse<CharacterResponse>> updateCharacter(UUID characterId, UpdateCharacterRequest request, MultipartFile image) {
        Character characterToUpdate = characterRepository.findByPublicId(characterId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Character doesn't exists"));

        String name = request.name().trim();
        if (!characterToUpdate.getName().equalsIgnoreCase(name) && characterRepository.existsByNameIgnoreCase(name)) {
            throw new CharacterAlreadyExistsException("Character with this name already exists");
        }

        if (image != null && !image.isEmpty()) {
            if (!image.getContentType().startsWith("image/")) {
                throw new BadRequestException("Only image files are allowed");
            }

            if (image.getSize() > 10 * 1024 * 1024) {
                throw new BadRequestException("Image size cannot exceed 10MB");
            }

            characterToUpdate.setImageUrl(cloudinaryService.uploadImage(image));
        }

        characterToUpdate.setName(request.name());
        characterToUpdate.setRarity(request.rarity());
        characterToUpdate.setBaseHealth(request.baseHealth());
        characterToUpdate.setBaseMana(request.baseMana());
        characterToUpdate.setAttack(request.attack());
        characterToUpdate.setDefense(request.defense());
        characterToUpdate.setHealPower(request.healPower());
        characterToUpdate.setCritChance(request.critChance());
        characterToUpdate.setDodgeChance(request.dodgeChance());
        characterToUpdate.setPrice(request.price());
        characterToUpdate.setUpdatedAt(LocalDateTime.now());

        Character updatedCharacter = characterRepository.save(characterToUpdate);

        return ApiResponseUtil.success("Character updated successfully", CharacterMapper.toCharacterResponseDto(updatedCharacter));
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> deleteCharacter(UUID characterId) {
        Character character = characterRepository.findByPublicId(characterId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Character doesn't exist"));

        characterRepository.delete(character);
        return ApiResponseUtil.success("Character deleted successfully", null);
    }

    @Override
    public ResponseEntity<ApiResponse<List<CharacterResponse>>> getAllCharacters() {
        List<Character> characters = characterRepository.findAll();

        List<CharacterResponse> response = characters.stream().map(
                CharacterMapper::toCharacterResponseDto
        ).toList();

        return ApiResponseUtil.success("All characters fetched successfully", response);
    }

    @Override
    public ResponseEntity<ApiResponse<CharacterResponse>> getCharacter(UUID characterId) {
        Character character = characterRepository.findByPublicId(characterId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Character doesn't exist"));

        return ApiResponseUtil.success("Character fetched successfully", CharacterMapper.toCharacterResponseDto(character));
    }
}
