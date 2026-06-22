package com.arpit.mythicgates.service;

import com.arpit.mythicgates.model.dto.character.CharacterResponse;
import com.arpit.mythicgates.response.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserCharacterService {
    ResponseEntity<ApiResponse<List<CharacterResponse>>> getMyCharacters();
}
