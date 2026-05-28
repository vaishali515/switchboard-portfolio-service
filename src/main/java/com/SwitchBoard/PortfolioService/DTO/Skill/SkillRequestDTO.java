package com.SwitchBoard.PortfolioService.DTO.Skill;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Skill creation/update request payload")
public class SkillRequestDTO {

    @NotBlank(message = "Skill name is required")
    @Schema(description = "Name of the skill", example = "Java")
    private String name;

    @Schema(description = "Category of the skill (e.g., Programming Language, Framework, Tool)", example = "Programming Language")
    private String category;

    @Min(value = 1, message = "Proficiency level must be between 1 and 5")
    @Max(value = 5, message = "Proficiency level must be between 1 and 5")
    @Schema(description = "Proficiency level (1-5)", example = "4")
    private Integer proficiencyLevel;

    @Schema(description = "Years of experience with the skill", example = "3")
    private Integer yearsOfExperience;

    @Schema(description = "Additional details about the skill", example = "Expert in Java 8+ features and Spring Framework")
    private String description;
}
