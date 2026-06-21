package com.arpit.mythicgates.controller;

import com.arpit.mythicgates.model.dto.character.CharacterRequest;
import com.arpit.mythicgates.model.dto.character.CharacterResponse;
import com.arpit.mythicgates.response.ApiResponse;
import com.arpit.mythicgates.service.CharacterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
}
