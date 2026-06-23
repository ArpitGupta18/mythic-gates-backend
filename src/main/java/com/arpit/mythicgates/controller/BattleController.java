package com.arpit.mythicgates.controller;

import com.arpit.mythicgates.model.dto.battle.BattleResponse;
import com.arpit.mythicgates.model.dto.battle.StartBattleRequest;
import com.arpit.mythicgates.response.ApiResponse;
import com.arpit.mythicgates.service.BattleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
