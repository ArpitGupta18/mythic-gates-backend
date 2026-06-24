package com.arpit.mythicgates.controller;

import com.arpit.mythicgates.model.dto.battle.AttackBattleRequest;
import com.arpit.mythicgates.model.dto.battle.AttackBattleResponse;
import com.arpit.mythicgates.model.dto.battle.BattleResponse;
import com.arpit.mythicgates.model.dto.battle.StartBattleRequest;
import com.arpit.mythicgates.response.ApiResponse;
import com.arpit.mythicgates.service.BattleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/battles")
@RequiredArgsConstructor
public class BattleController {
    private final BattleService battleService;

    @PostMapping("/start")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<BattleResponse>> startBattle(
            @Valid @RequestBody StartBattleRequest request
    ) {
        return battleService.startBattle(request);
    }

    @PostMapping("/{battleId}/attack")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<AttackBattleResponse>> attack(
            @PathVariable UUID battleId,
            @Valid @RequestBody AttackBattleRequest request
    ) {
        return battleService.attack(battleId, request);
    }

    @PostMapping("/{battleId}/heal")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<AttackBattleResponse>> heal(
            @PathVariable UUID battleId
    ) {
        return battleService.heal(battleId);
    }

    @PostMapping("/{battleId}/restore-mana")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<AttackBattleResponse>> restore(
            @PathVariable UUID battleId
    ) {
        return battleService.restoreMana(battleId);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<BattleResponse>>> getMyBattles() {
        return battleService.getMyBattles();
    }

    @GetMapping("/{battleId}")
    public ResponseEntity<ApiResponse<BattleResponse>> getBattle(
            @PathVariable UUID battleId
    ) {
        return battleService.getBattle(battleId);
    }

    @PostMapping("/{battleId}/forfeit")
    public ResponseEntity<ApiResponse<BattleResponse>> forfeitBattle(
            @PathVariable UUID battleId
    ) {
        return battleService.forfeitBattle(battleId);
    }
}
