package com.arpit.mythicgates.service.impl;

import com.arpit.mythicgates.exception.custom.BadRequestException;
import com.arpit.mythicgates.exception.custom.CharacterAlreadyExistsException;
import com.arpit.mythicgates.mapper.CharacterMapper;
import com.arpit.mythicgates.model.dto.character.CharacterRequest;
import com.arpit.mythicgates.model.dto.character.CharacterResponse;
import com.arpit.mythicgates.model.entity.Character;
import com.arpit.mythicgates.repository.CharacterRepository;
import com.arpit.mythicgates.response.ApiResponse;
import com.arpit.mythicgates.response.ApiResponseUtil;
import com.arpit.mythicgates.service.CharacterService;
import com.arpit.mythicgates.service.CloudinaryService;
import com.arpit.mythicgates.utils.UuidGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
}
