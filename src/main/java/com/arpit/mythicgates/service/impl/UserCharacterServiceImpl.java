package com.arpit.mythicgates.service.impl;

import com.arpit.mythicgates.helper.UserHelper;
import com.arpit.mythicgates.mapper.CharacterMapper;
import com.arpit.mythicgates.model.dto.character.CharacterResponse;
import com.arpit.mythicgates.model.entity.User;
import com.arpit.mythicgates.model.entity.UserCharacter;
import com.arpit.mythicgates.repository.UserCharacterRepository;
import com.arpit.mythicgates.response.ApiResponse;
import com.arpit.mythicgates.response.ApiResponseUtil;
import com.arpit.mythicgates.service.UserCharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCharacterServiceImpl implements UserCharacterService {
    private final UserHelper userHelper;
    private final UserCharacterRepository userCharacterRepository;

    @Override
    public ResponseEntity<ApiResponse<List<CharacterResponse>>> getMyCharacters() {
        User user = userHelper.getCurrentUser();

        List<UserCharacter> characters = userCharacterRepository.findByUserId(user.getId());

        List<CharacterResponse> response = characters.stream()
                .map(userCharacter -> CharacterMapper.toCharacterResponseDto(userCharacter.getCharacter()))
                .toList();

        return ApiResponseUtil.success("User character fetched", response);
    }
}
