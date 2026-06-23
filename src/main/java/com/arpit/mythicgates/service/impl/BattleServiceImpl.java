package com.arpit.mythicgates.service.impl;

import com.arpit.mythicgates.exception.custom.ResourceNotFoundException;
import com.arpit.mythicgates.helper.UserHelper;
import com.arpit.mythicgates.mapper.BattleMapper;
import com.arpit.mythicgates.model.dto.battle.BattleResponse;
import com.arpit.mythicgates.model.dto.battle.StartBattleRequest;
import com.arpit.mythicgates.model.entity.*;
import com.arpit.mythicgates.model.entity.Character;
import com.arpit.mythicgates.model.enums.BattleStatus;
import com.arpit.mythicgates.repository.BattleRepository;
import com.arpit.mythicgates.repository.BossRepository;
import com.arpit.mythicgates.repository.UserCharacterRepository;
import com.arpit.mythicgates.response.ApiResponse;
import com.arpit.mythicgates.response.ApiResponseUtil;
import com.arpit.mythicgates.service.BattleService;
import com.arpit.mythicgates.utils.UuidGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BattleServiceImpl implements BattleService {
    private final UserHelper userHelper;
    private final BattleRepository battleRepository;
    private final UserCharacterRepository userCharacterRepository;
    private final BossRepository bossRepository;

    @Override
    public ResponseEntity<ApiResponse<BattleResponse>> startBattle(StartBattleRequest request) {
        User user = userHelper.getCurrentUser();

        return battleRepository.findByUserCharacterUserIdAndStatus(user.getId(), BattleStatus.ONGOING)
                .map(existingBattle ->
                        ApiResponseUtil.success(
                                "You already have an ongoing battle",
                                BattleMapper.toBattleResponseDto(existingBattle)
                        ))
                .orElseGet(() -> createNewBattle(user, request));
    }

    private ResponseEntity<ApiResponse<BattleResponse>> createNewBattle(
            User user,
            StartBattleRequest request
    ) {
        UserCharacter userCharacter = userCharacterRepository
                .findByUserIdAndCharacterPublicId(user.getId(), request.characterId())
                .orElseThrow(() -> new ResourceNotFoundException("You do not own this character"));

        Boss boss = bossRepository.findByPublicId(request.bossId())
                .orElseThrow(() -> new ResourceNotFoundException("Boss not found"));

        Character character = userCharacter.getCharacter();

        Battle battle = Battle.builder()
                .publicId(UuidGenerator.generate())
                .userCharacter(userCharacter)
                .boss(boss)
                .playerCurrentHealth(character.getBaseHealth())
                .playerCurrentMana(character.getBaseMana())
                .bossCurrentHealth(boss.getBaseHealth())
                .turnCount(1)
                .damageDealt(0)
                .goldEarned(0)
                .status(BattleStatus.ONGOING)
                .build();

        Battle savedBattle = battleRepository.save(battle);

        return ApiResponseUtil.created(
                "Battle started successfully",
                BattleMapper.toBattleResponseDto(savedBattle)
        );
    }
}
