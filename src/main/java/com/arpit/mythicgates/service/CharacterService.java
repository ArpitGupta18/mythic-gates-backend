package com.arpit.mythicgates.service;

import com.arpit.mythicgates.model.dto.character.CharacterRequest;
import com.arpit.mythicgates.model.dto.character.CharacterResponse;
import com.arpit.mythicgates.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface CharacterService {
    ResponseEntity<ApiResponse<CharacterResponse>> addCharacter(CharacterRequest request, MultipartFile image);
}
