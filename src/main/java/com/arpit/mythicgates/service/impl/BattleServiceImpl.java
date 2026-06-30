package com.arpit.mythicgates.service.impl;

import com.arpit.mythicgates.exception.custom.BadRequestException;
import com.arpit.mythicgates.exception.custom.ResourceNotFoundException;
import com.arpit.mythicgates.helper.UserHelper;
import com.arpit.mythicgates.mapper.BattleMapper;
import com.arpit.mythicgates.model.dto.battle.*;
import com.arpit.mythicgates.model.dto.pagination.PageResponse;
import com.arpit.mythicgates.model.entity.*;
import com.arpit.mythicgates.model.entity.Character;
import com.arpit.mythicgates.model.enums.BattleStatus;
import com.arpit.mythicgates.repository.*;
import com.arpit.mythicgates.response.ApiResponse;
import com.arpit.mythicgates.response.ApiResponseUtil;
import com.arpit.mythicgates.service.BattleService;
import com.arpit.mythicgates.service.calculator.BattleAttackCalculator;
import com.arpit.mythicgates.service.calculator.BattleResultCalculator;
import com.arpit.mythicgates.utils.UuidGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BattleServiceImpl implements BattleService {
    private final UserHelper userHelper;
    private final BattleRepository battleRepository;
    private final UserCharacterRepository userCharacterRepository;
    private final BossRepository bossRepository;
    private final SkillRepository skillRepository;
    private final BattleAttackCalculator battleAttackCalculator;
    private final BattleResultCalculator battleResultCalculator;
    private final BattleSkillCooldownRepository battleSkillCooldownRepository;

    @Override
    public ResponseEntity<ApiResponse<BattleResponse>> startBattle(StartBattleRequest request) {
        User user = userHelper.getCurrentUser();

        Optional<Battle> existingBattle =
                battleRepository.findByUserCharacterUserIdAndStatus(
                        user.getId(),
                        BattleStatus.ONGOING
                );

        if (existingBattle.isPresent()) {
            throw new BadRequestException(
                    "You already have an ongoing battle. Finish or forfeit it before starting a new one."
            );
        }

        return createNewBattle(user, request);
    }

    @Override
    public ResponseEntity<ApiResponse<AttackBattleResponse>> attack(UUID battleId, AttackBattleRequest request) {
        User user = userHelper.getCurrentUser();

        Battle battle = battleRepository.findByPublicId(battleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Battle not found"));

        validateBattleOwner(battle, user);
        validateBattleOngoing(battle);

        Character character = battle.getUserCharacter().getCharacter();
        Boss boss = battle.getBoss();

        Skill skill = skillRepository
                .findByPublicIdAndCharacterId(request.skillId(), character.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Skill not found"));

        BattleSkillCooldown skillCooldown = battleSkillCooldownRepository
                .findByBattleIdAndSkillId(
                        battle.getId(), skill.getId()
                ).orElseThrow(() -> new ResourceNotFoundException("Skill cooldown not found"));

        if (skillCooldown.getRemainingCooldown() > 0) {
            throw new BadRequestException(
                    skill.getName() + " is on cooldown for "
                            + skillCooldown.getRemainingCooldown()
                            + " more turn(s)."
            );
        }

        if (battle.getPlayerCurrentMana() < skill.getManaCost()) {
            throw new BadRequestException("Not enough mana");
        }

        battle.setPlayerCurrentMana(
                battle.getPlayerCurrentMana() - skill.getManaCost()
        );

        DamageResult playerDamageResult = battleAttackCalculator.calculatePlayerDamage(character, boss, skill);

        int playerDamage = playerDamageResult.damage();

        int newBossHealth = Math.max(
                0,
                battle.getBossCurrentHealth() - playerDamage
        );

        battle.setBossCurrentHealth(newBossHealth);
        battle.setDamageDealt(battle.getDamageDealt() + playerDamage);

        String playerMessage;
        if (playerDamageResult.dodged()) {
            playerMessage = boss.getName() + " dodged " + character.getName() + "'s attack.";
        } else {
            playerMessage = character.getName()
                    + " used "
                    + skill.getName()
                    + " and dealt "
                    + playerDamage
                    + " damage."
                    + (playerDamageResult.critical() ? " Critical hit!" : "");
        }

        skillCooldown.setRemainingCooldown(skill.getCooldown());
        reduceCooldowns(battle, skill);

        String bossMessage = "";

        if (battle.getBossCurrentHealth() <= 0) {
            battleResultCalculator.endBattleAsWon(battle, user);

            Battle savedBattle = battleRepository.save(battle);

            AttackBattleResponse response = new AttackBattleResponse(
                    BattleMapper.toBattleResponseDto(savedBattle),
                    playerMessage,
                    bossMessage,
                    true,
                    BattleStatus.WON
            );

            return ApiResponseUtil.success("Battle won", response);
        }

        String bossHealMessage = battleAttackCalculator.healBossIfNeeded(battle, boss);

        DamageResult bossDamageResult = battleAttackCalculator.calculateBossDamage(boss, character, battle);

        int bossDamage = bossDamageResult.damage();

        int newPlayerHealth = Math.max(
                0,
                battle.getPlayerCurrentHealth() - bossDamage
        );

        battle.setPlayerCurrentHealth(newPlayerHealth);

        if (bossHealMessage != "") {
            bossMessage = bossHealMessage + " ";
        }

        if (bossDamageResult.dodged()) {
            bossMessage += character.getName()
                    + " dodged "
                    + boss.getName()
                    + "'s attack.";
        } else {
            bossMessage += boss.getName()
                    + " attacked and dealt "
                    + bossDamage
                    + " damage."
                    + (bossDamageResult.critical() ? " Critical Hit!" : "");
        }

        if (battle.getPlayerCurrentHealth() <= 0) {
            battleResultCalculator.endBattleAsLost(battle, user);

            Battle savedBattle = battleRepository.save(battle);

            AttackBattleResponse response = new AttackBattleResponse(
                    BattleMapper.toBattleResponseDto(savedBattle),
                    playerMessage,
                    bossMessage,
                    true,
                    BattleStatus.LOST
            );

            return ApiResponseUtil.success("Battle lost", response);
        }

        battle.setTurnCount(battle.getTurnCount() + 1);
        Battle savedBattle = battleRepository.save(battle);

        AttackBattleResponse response = new AttackBattleResponse(
                BattleMapper.toBattleResponseDto(savedBattle),
                playerMessage,
                bossMessage,
                false,
                BattleStatus.ONGOING
        );
        return ApiResponseUtil.success("Attack completed", response);
    }

    @Override
    public ResponseEntity<ApiResponse<AttackBattleResponse>> heal(UUID battleId) {
        User user = userHelper.getCurrentUser();

        Battle battle = battleRepository.findByPublicId(battleId)
                .orElseThrow(() -> new ResourceNotFoundException("Battle not found"));

        validateBattleOwner(battle, user);
        validateBattleOngoing(battle);

        Character character = battle.getUserCharacter().getCharacter();
        Boss boss = battle.getBoss();

        int manaCost = (int) Math.max(20, character.getHealPower() / 3);

        if (battle.getPlayerCurrentMana() < manaCost) {
            throw new BadRequestException("Not enough mana to heal");
        }

        battle.setPlayerCurrentMana(
                battle.getPlayerCurrentMana() - manaCost
        );

        int oldHealth = battle.getPlayerCurrentHealth();

        int newHealth = Math.min(
                character.getBaseHealth(),
                oldHealth + character.getHealPower()
        );

        battle.setPlayerCurrentHealth(newHealth);

        int actualHealed = newHealth - oldHealth;

        String playerMessage = character.getName() + " healed for " + actualHealed + " HP";

        reduceCooldowns(battle, null);

        String bossHealMessage = battleAttackCalculator.healBossIfNeeded(battle, boss);

        DamageResult bossDamageResult = battleAttackCalculator.calculateBossDamage(boss, character, battle);

        int bossDamage = bossDamageResult.damage();

        int newPlayerHealth = Math.max(
                0,
                battle.getPlayerCurrentHealth() - bossDamage
        );

        battle.setPlayerCurrentHealth(newPlayerHealth);

        String bossMessage = "";

        if (bossHealMessage != null && !bossHealMessage.isBlank()) {
            bossMessage = bossHealMessage + " ";
        }

        if (bossDamageResult.dodged()) {
            bossMessage += character.getName()
                    + " dodged "
                    + boss.getName()
                    + "'s attack.";
        } else {
            bossMessage += boss.getName()
                    + " attacked and dealt "
                    + bossDamage
                    + " damage."
                    + (bossDamageResult.critical() ? " Critical Hit!" : "");
        }

        if (battle.getPlayerCurrentHealth() <= 0) {
            battleResultCalculator.endBattleAsLost(battle, user);

            Battle savedBattle = battleRepository.save(battle);

            AttackBattleResponse response = new AttackBattleResponse(
                    BattleMapper.toBattleResponseDto(savedBattle),
                    playerMessage,
                    bossMessage,
                    true,
                    BattleStatus.LOST
            );

            return ApiResponseUtil.success("Battle lost", response);
        }

        battle.setTurnCount(battle.getTurnCount() + 1);
        Battle savedBattle = battleRepository.save(battle);

        AttackBattleResponse response = new AttackBattleResponse(
                BattleMapper.toBattleResponseDto(savedBattle),
                playerMessage,
                bossMessage,
                false,
                BattleStatus.ONGOING
        );
        return ApiResponseUtil.success("Heal completed", response);

    }

    @Override
    public ResponseEntity<ApiResponse<AttackBattleResponse>> restoreMana(UUID battleId) {
        User user = userHelper.getCurrentUser();

        Battle battle = battleRepository.findByPublicId(battleId)
                .orElseThrow(() -> new ResourceNotFoundException("Battle not found"));

        validateBattleOwner(battle, user);
        validateBattleOngoing(battle);

        Character character = battle.getUserCharacter().getCharacter();
        Boss boss = battle.getBoss();

        battle.setPlayerCurrentMana(character.getBaseMana());

        String playerMessage = character.getName()
                + " restored mana to full.";

        reduceCooldowns(battle, null);

        String bossHealMessage = battleAttackCalculator.healBossIfNeeded(battle, boss);

        DamageResult bossDamageResult = battleAttackCalculator.calculateBossDamage(boss, character, battle);
        int bossDamage = bossDamageResult.damage();

        battle.setPlayerCurrentHealth(
                Math.max(0, battle.getPlayerCurrentHealth() - bossDamage)
        );

        String bossMessage = "";

        if (bossHealMessage != null && !bossHealMessage.isBlank()) {
            bossMessage += bossHealMessage + " ";
        }

        if (bossDamageResult.dodged()) {
            bossMessage += character.getName()
                    + " dodged "
                    + boss.getName()
                    + "'s attack.";
        } else {
            bossMessage += boss.getName()
                    + " attacked and dealt "
                    + bossDamage
                    + " damage."
                    + (bossDamageResult.critical() ? " Critical Hit!" : "");
        }

        if (battle.getPlayerCurrentHealth() <= 0) {
            battleResultCalculator.endBattleAsLost(battle, user);

            Battle savedBattle = battleRepository.save(battle);

            AttackBattleResponse response = new AttackBattleResponse(
                    BattleMapper.toBattleResponseDto(savedBattle),
                    playerMessage,
                    bossMessage,
                    true,
                    BattleStatus.LOST
            );

            return ApiResponseUtil.success("Battle lost", response);
        }

        battle.setTurnCount(battle.getTurnCount() + 1);

        Battle savedBattle = battleRepository.save(battle);

        AttackBattleResponse response = new AttackBattleResponse(
                BattleMapper.toBattleResponseDto(savedBattle),
                playerMessage,
                bossMessage,
                false,
                BattleStatus.ONGOING
        );

        return ApiResponseUtil.success("Mana restored", response);
    }

    @Override
    public ResponseEntity<ApiResponse<PageResponse<BattleResponse>>> getMyBattles(int page, int size, String sortBy, String sortDir) {
        User user = userHelper.getCurrentUser();

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Battle> battlePage = battleRepository.findByUserCharacterUserId(user.getId(), pageable);
        List<BattleResponse> battles = battlePage
                .getContent()
                .stream()
                .map(BattleMapper::toBattleResponseDto)
                .toList();

        PageResponse<BattleResponse> pageResponse = new PageResponse<>(
                battles,
                battlePage.getNumber(),
                battlePage.getSize(),
                battlePage.getTotalElements(),
                battlePage.getTotalPages(),
                battlePage.isLast()
        );

        return ApiResponseUtil.success(
                "Battles fetched successfully",
                pageResponse
        );
    }

    @Override
    public ResponseEntity<ApiResponse<BattleResponse>> getBattle(UUID battleId) {
        User user = userHelper.getCurrentUser();

        Battle battle = battleRepository
                .findByPublicIdAndUserCharacterUserId(battleId, user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Battle not found"));

        return ApiResponseUtil.success(
                "Battle fetched successfully",
                BattleMapper.toBattleResponseDto(battle)
        );
    }

    @Override
    public ResponseEntity<ApiResponse<AttackBattleResponse>> forfeitBattle(UUID battleId) {
        User user = userHelper.getCurrentUser();

        Battle battle = battleRepository
                .findByPublicIdAndUserCharacterUserId(battleId, user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Battle not found"));

        validateBattleOngoing(battle);

        battle.setStatus(BattleStatus.LOST);
        battle.setGoldEarned(0);

        Battle savedBattle = battleRepository.save(battle);

        AttackBattleResponse response = new AttackBattleResponse(
                BattleMapper.toBattleResponseDto(savedBattle),
                "Player forfeited the match",
                null,
                true,
                BattleStatus.LOST
        );
        return ApiResponseUtil.success(
                "Battle forfeited successfully",
                response
        );
    }

    @Override
    public ResponseEntity<ApiResponse<BattleResponse>> getOngoingBattle() {
        User user = userHelper.getCurrentUser();
        Battle battle = battleRepository
                .findByUserCharacterUserIdAndStatus(user.getId(), BattleStatus.ONGOING)
                .orElseThrow(() -> new ResourceNotFoundException("No ongoing battle found"));

        BattleResponse response = BattleMapper.toBattleResponseDto(battle);
        return ApiResponseUtil.success(
                "Ongoing battle found",
                response
        );
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

        List<Skill> skills = skillRepository.findByCharacterPublicIdOrderByTypeAscSlotAsc(
                character.getPublicId()
        );

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

        for (Skill skill : skills) {
            BattleSkillCooldown skillCooldown = BattleSkillCooldown.builder()
                    .battle(battle)
                    .skill(skill)
                    .remainingCooldown(0)
                    .build();

            battle.getSkillCooldowns().add(skillCooldown);
        }

        Battle savedBattle = battleRepository.save(battle);

        return ApiResponseUtil.created(
                "Battle started successfully",
                BattleMapper.toBattleResponseDto(savedBattle)
        );
    }

    private void validateBattleOwner(Battle battle, User user) {
        if (!battle.getUserCharacter().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Battle not found");
        }
    }

    private void validateBattleOngoing(Battle battle) {
        if (battle.getStatus() != BattleStatus.ONGOING) {
            throw new BadRequestException("Battle has already ended");
        }
    }
    private void reduceCooldowns(Battle battle, Skill usedSkill) {
        for (BattleSkillCooldown cooldown : battle.getSkillCooldowns()) {
            if (
                    usedSkill != null &&
                    cooldown.getSkill().getId().equals(usedSkill.getId())) {
                continue;
            }

            if (cooldown.getRemainingCooldown() > 0) {
                cooldown.setRemainingCooldown(
                        cooldown.getRemainingCooldown() - 1
                );
            }
        }
    }

}
