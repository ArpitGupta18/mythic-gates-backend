package com.arpit.mythicgates.mapper;

import com.arpit.mythicgates.model.dto.character.CharacterResponse;
import com.arpit.mythicgates.model.entity.Character;

public class CharacterMapper {
    public static CharacterResponse toCharacterResponseDto(Character character) {
        if (character == null) {
            return null;
        }

        return new CharacterResponse(
                character.getPublicId(),
                character.getName(),
                character.getRarity(),
                character.getBaseHealth(),
                character.getBaseMana(),
                character.getAttack(),
                character.getDefense(),
                character.getHealPower(),
                character.getCritChance(),
                character.getDodgeChance(),
                character.getPrice(),
                character.getImageUrl(),
                character.isStarter(),
                character.getCreatedAt(),
                character.getUpdatedAt()
        );
    }
}
