package com.arpit.mythicgates.service;

import com.arpit.mythicgates.model.dto.character.CharacterRequest;
import com.arpit.mythicgates.model.dto.character.CharacterResponse;
import com.arpit.mythicgates.model.dto.character.UpdateCharacterRequest;
import com.arpit.mythicgates.response.ApiResponse;
import com.cloudinary.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface CharacterService {
    ResponseEntity<ApiResponse<CharacterResponse>> addCharacter(CharacterRequest request, MultipartFile image);

    ResponseEntity<ApiResponse<CharacterResponse>> updateCharacter(UUID characterId, UpdateCharacterRequest request, MultipartFile image);

    ResponseEntity<ApiResponse<Void>> deleteCharacter(UUID characterId);
}
