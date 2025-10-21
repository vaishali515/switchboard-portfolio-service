package com.SwitchBoard.PortfolioService.DTO.Achievement;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Achievement response payload")
public class AchievementResponseDTO {

    @Schema(description = "Unique identifier of the achievement", example = "1")
    private Long id;

    @Schema(description = "Title of the achievement", example = "Best Innovator Award 2024")
    private String title;

    @Schema(description = "Date of the achievement", example = "2024-05-20")
    private LocalDate date;

    @Schema(description = "Issuer of the achievement", example = "Tech Innovators Inc.")
    private String issuer;

    @Schema(description = "Detailed description of the achievement", example = "Awarded for outstanding innovation in software development.")
    private String description;

    @Schema(description = "URL related to the achievement", example = "https://example.com/award-details")
    private String url;
}
