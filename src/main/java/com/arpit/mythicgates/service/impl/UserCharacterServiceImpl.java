package com.arpit.mythicgates.service.impl;

import com.arpit.mythicgates.exception.custom.BadRequestException;
import com.arpit.mythicgates.exception.custom.ResourceNotFoundException;
import com.arpit.mythicgates.helper.UserHelper;
import com.arpit.mythicgates.mapper.CharacterMapper;
import com.arpit.mythicgates.model.dto.character.CharacterResponse;
import com.arpit.mythicgates.model.entity.Character;
import com.arpit.mythicgates.model.entity.User;
import com.arpit.mythicgates.model.entity.UserCharacter;
import com.arpit.mythicgates.repository.CharacterRepository;
import com.arpit.mythicgates.repository.UserCharacterRepository;
import com.arpit.mythicgates.response.ApiResponse;
import com.arpit.mythicgates.response.ApiResponseUtil;
import com.arpit.mythicgates.service.UserCharacterService;
import com.arpit.mythicgates.utils.UuidGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserCharacterServiceImpl implements UserCharacterService {
    private final UserHelper userHelper;
    private final UserCharacterRepository userCharacterRepository;
    private final CharacterRepository characterRepository;

    @Override
    public ResponseEntity<ApiResponse<List<CharacterResponse>>> getMyCharacters() {
        User user = userHelper.getCurrentUser();

        List<UserCharacter> characters = userCharacterRepository.findByUserId(user.getId());

        List<CharacterResponse> response = characters.stream()
                .map(userCharacter -> CharacterMapper.toCharacterResponseDto(userCharacter.getCharacter()))
                .toList();

        return ApiResponseUtil.success("User character fetched", response);
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<Void>> unlockCharacter(UUID characterId) {
        User user = userHelper.getCurrentUser();

        Character character = characterRepository.findByPublicId(characterId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Character doesn't exist"));

        System.out.println("-------------------------------------------------------");
        System.out.println(character.getPublicId());
        System.out.println(character.getId());
        System.out.println(user.getId());
        if (userCharacterRepository.existsByUserIdAndCharacterId(user.getId(), character.getId())) {
            throw new BadRequestException("Character already unlocked");
        }

        int userGold = user.getGold();
        int characterPrice = character.getPrice();

        if (userGold < characterPrice) {
            throw new BadRequestException("Not enough gold");
        }

        user.setGold(userGold - characterPrice);

        userCharacterRepository.save(
                UserCharacter.builder()
                        .publicId(UuidGenerator.generate())
                        .user(user)
                        .character(character)
                        .obtainedAt(LocalDateTime.now())
                        .build()
        );

        return ApiResponseUtil.success("Character unlocked successfully", null);
    }
}
