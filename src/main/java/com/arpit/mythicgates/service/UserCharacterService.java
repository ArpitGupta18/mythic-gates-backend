package com.arpit.mythicgates.service;

import com.arpit.mythicgates.model.dto.character.CharacterResponse;
import com.arpit.mythicgates.model.dto.pagination.PageResponse;
import com.arpit.mythicgates.response.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface UserCharacterService {
    ResponseEntity<ApiResponse<PageResponse<CharacterResponse>>> getMyCharacters(int page, int size);

    ResponseEntity<ApiResponse<Void>> unlockCharacter(UUID characterId);
}
