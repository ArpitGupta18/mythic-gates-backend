package com.arpit.mythicgates.controller;

import com.arpit.mythicgates.model.dto.character.CharacterRequest;
import com.arpit.mythicgates.model.dto.character.CharacterResponse;
import com.arpit.mythicgates.model.dto.character.UpdateCharacterRequest;
import com.arpit.mythicgates.response.ApiResponse;
import com.arpit.mythicgates.service.CharacterService;
import com.cloudinary.Api;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CharacterController {
    private final CharacterService characterService;

    @PostMapping(value = "/admin/characters", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CharacterResponse>> createCharacter(
            @Valid @ModelAttribute CharacterRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return characterService.addCharacter(request, image);
    }

    @PutMapping(value = "/admin/characters/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CharacterResponse>> updateCharacter(
            @Valid @ModelAttribute UpdateCharacterRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @PathVariable UUID id
    ) {
        return characterService.updateCharacter(id, request, image);
    }

    @DeleteMapping("/admin/characters/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCharacter(
            @PathVariable UUID id
    ) {
        return characterService.deleteCharacter(id);
    }

    @GetMapping("/characters")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<List<CharacterResponse>>> getAllCharacters() {
        return characterService.getAllCharacters();
    }

    @GetMapping("/characters/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<CharacterResponse>> getCharacter(
            @PathVariable("id") UUID characterId
    ) {
        return characterService.getCharacter(characterId);
    }
}
