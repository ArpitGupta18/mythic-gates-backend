package com.arpit.mythicgates.model.dto.skill;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record BulkSkillRequest(
        @NotEmpty(message = "Skills are required")
        @Size(min=4, max=4, message = "Exactly 4 skills are required")
        List<@Valid SkillRequest> skills
) {
}
