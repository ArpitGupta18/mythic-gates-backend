package com.arpit.mythicgates.service.impl;

import com.arpit.mythicgates.exception.custom.BadRequestException;
import com.arpit.mythicgates.exception.custom.CharacterAlreadyExistsException;
import com.arpit.mythicgates.exception.custom.ResourceNotFoundException;
import com.arpit.mythicgates.helper.ImageValidator;
import com.arpit.mythicgates.mapper.CharacterMapper;
import com.arpit.mythicgates.model.dto.character.CharacterRequest;
import com.arpit.mythicgates.model.dto.character.CharacterResponse;
import com.arpit.mythicgates.model.dto.character.UpdateCharacterRequest;
import com.arpit.mythicgates.model.dto.pagination.PageResponse;
import com.arpit.mythicgates.model.entity.Character;
import com.arpit.mythicgates.repository.CharacterRepository;
import com.arpit.mythicgates.response.ApiResponse;
import com.arpit.mythicgates.response.ApiResponseUtil;
import com.arpit.mythicgates.service.CharacterService;
import com.arpit.mythicgates.service.ImageStorageService;
import com.arpit.mythicgates.utils.UuidGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final @Qualifier("cloudinaryService") ImageStorageService imageStorageService;
    private final ImageValidator imageValidator;

    @Override
    public ResponseEntity<ApiResponse<CharacterResponse>> addCharacter(CharacterRequest request, MultipartFile image) {
        if (image == null || image.isEmpty())
            throw new BadRequestException("Character image is required");

        String name = request.name().trim();
        if (characterRepository.existsByNameIgnoreCase(name)) {
            throw new CharacterAlreadyExistsException("Character with this name already exists");
        }

        imageValidator.validateImage(image);

        String imageUrl = imageStorageService.uploadImage(image);

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
                .isStarter(request.isStarter())
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

        imageValidator.validateImage(image);

        String imageUrl = null;
        if (image == null || image.isEmpty()) {
            imageUrl = characterToUpdate.getImageUrl();
        } else {
            imageUrl = imageStorageService.uploadImage(image);
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
        characterToUpdate.setImageUrl(imageUrl);
        characterToUpdate.setStarter(request.isStarter());
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
    public ResponseEntity<ApiResponse<PageResponse<CharacterResponse>>> getAllCharacters(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

//        List<Character> characters = characterRepository.findAll();

        Page<Character> characterPage = characterRepository.findAll(pageable);

        List<CharacterResponse> characters = characterPage
                .getContent()
                .stream()
                .map(CharacterMapper::toCharacterResponseDto)
                .toList();

        PageResponse<CharacterResponse> pageResponse = new PageResponse<>(
                characters,
                characterPage.getNumber(),
                characterPage.getSize(),
                characterPage.getTotalElements(),
                characterPage.getTotalPages(),
                characterPage.isLast()
        );

        return ApiResponseUtil.success("All characters fetched successfully", pageResponse);
    }

    @Override
    public ResponseEntity<ApiResponse<CharacterResponse>> getCharacter(UUID characterId) {
        Character character = characterRepository.findByPublicId(characterId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Character doesn't exist"));

        return ApiResponseUtil.success("Character fetched successfully", CharacterMapper.toCharacterResponseDto(character));
    }
}
