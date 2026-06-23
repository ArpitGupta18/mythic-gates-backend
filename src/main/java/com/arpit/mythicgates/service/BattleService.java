package com.arpit.mythicgates.service;

import com.arpit.mythicgates.model.dto.battle.BattleResponse;
import com.arpit.mythicgates.model.dto.battle.StartBattleRequest;
import com.arpit.mythicgates.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface BattleService {
    ResponseEntity<ApiResponse<BattleResponse>> startBattle(StartBattleRequest request);
}
