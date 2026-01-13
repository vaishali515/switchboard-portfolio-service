package com.SwitchBoard.PortfolioService.DTO.Skill;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Skill response payload")
public class SkillResponseDTO {

    @Schema(description = "Unique identifier of the skill", example = "1")
    private UUID id;

    @Schema(description = "Name of the skill", example = "Java")
    private String name;

    @Schema(description = "Category of the skill (e.g., Programming Language, Framework, Tool)", example = "Programming Language")
    private String category;

    @Schema(description = "Proficiency level (1-5)", example = "4")
    private Integer proficiencyLevel;

    @Schema(description = "Years of experience with the skill", example = "3")
    private Integer yearsOfExperience;

    @Schema(description = "Additional details about the skill", example = "Expert in Java 8+ features and Spring Framework")
    private String description;
}
