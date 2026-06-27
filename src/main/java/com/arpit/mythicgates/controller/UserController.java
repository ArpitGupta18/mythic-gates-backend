package com.arpit.mythicgates.controller;

import com.arpit.mythicgates.model.dto.character.CharacterResponse;
import com.arpit.mythicgates.model.dto.pagination.PageResponse;
import com.arpit.mythicgates.model.dto.user.UserProfileResponse;
import com.arpit.mythicgates.response.ApiResponse;
import com.arpit.mythicgates.service.EconomyService;
import com.arpit.mythicgates.service.UserCharacterService;
import com.arpit.mythicgates.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final EconomyService economyService;
    private final UserCharacterService userCharacterService;
    private final UserService userService;

    @PostMapping("/add/gold")
    @PreAuthorize("hasRole('USER')")
    public String addGold(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody int amount
    ) {

        economyService.addGold(user.getUsername(), amount);
        return "Gold added";
    }

    @PostMapping("/deduct/gold")
    @PreAuthorize("hasRole('USER')")
    public String deductGold(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody int amount
    ) {
        economyService.deductGold(user.getUsername(), amount);
        return "Gold deducted";
    }

    @GetMapping("/balance")
    @PreAuthorize("hasRole('USER')")
    public int getUserBalance(
            @AuthenticationPrincipal UserDetails user
    ) {
        return economyService.getUserBalance(user.getUsername());
    }

    @GetMapping("/me/characters")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<PageResponse<CharacterResponse>>> getUserCharacters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size
    ) {
        return userCharacterService.getMyCharacters(page, size);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getMe() {
        return userService.getMe();
    }
}
