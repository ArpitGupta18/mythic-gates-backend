package com.arpit.mythicgates.service;

import com.arpit.mythicgates.model.dto.battle.AttackBattleRequest;
import com.arpit.mythicgates.model.dto.battle.AttackBattleResponse;
import com.arpit.mythicgates.model.dto.battle.BattleResponse;
import com.arpit.mythicgates.model.dto.battle.StartBattleRequest;
import com.arpit.mythicgates.model.dto.pagination.PageResponse;
import com.arpit.mythicgates.response.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface BattleService {
    ResponseEntity<ApiResponse<BattleResponse>> startBattle(StartBattleRequest request);

    ResponseEntity<ApiResponse<AttackBattleResponse>> attack(UUID battleId, AttackBattleRequest request);

    ResponseEntity<ApiResponse<AttackBattleResponse>> heal(UUID battleId);

    ResponseEntity<ApiResponse<AttackBattleResponse>> restoreMana(UUID battleId);

    ResponseEntity<ApiResponse<PageResponse<BattleResponse>>> getMyBattles(int page, int size, String sortBy, String sortDir);

    ResponseEntity<ApiResponse<BattleResponse>> getBattle(UUID battleId);

    ResponseEntity<ApiResponse<BattleResponse>> forfeitBattle(UUID battleId);
}
