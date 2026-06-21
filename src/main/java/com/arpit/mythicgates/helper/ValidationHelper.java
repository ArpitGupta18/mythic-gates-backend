package com.arpit.mythicgates.helper;

import com.arpit.mythicgates.exception.custom.BadRequestException;
import com.arpit.mythicgates.model.dto.skill.SkillRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ValidationHelper {
    public void validateRequiredSkillSlots(
            List<SkillRequest> skills
    ) {
        Set<String> requiredSlots = Set.of(
                "BASIC-1",
                "BASIC-2",
                "SPECIAL-1",
                "SPECIAL-2"
        );

        Set<String> providedSlots = skills.stream()
                .map(skill -> skill.type() + "-" + skill.slot())
                .collect(Collectors.toSet());

        if (!providedSlots.equals(requiredSlots)) {
            throw new BadRequestException(
                    "Skills must include BASIC slot 1, BASIC slot 2, SPECIAL slot 1, and SPECIAL slot 2"
            );
        }
    }
}
