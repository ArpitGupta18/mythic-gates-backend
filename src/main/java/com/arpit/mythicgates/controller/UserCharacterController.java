package com.arpit.mythicgates.controller;

import com.arpit.mythicgates.model.dto.character.CharacterResponse;
import com.arpit.mythicgates.response.ApiResponse;
import com.arpit.mythicgates.service.UserCharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserCharacterController {
    private final UserCharacterService userCharacterService;

    @GetMapping("/me/characters")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<List<CharacterResponse>>> getUserCharacters() {
        return userCharacterService.getMyCharacters();
    }
}
