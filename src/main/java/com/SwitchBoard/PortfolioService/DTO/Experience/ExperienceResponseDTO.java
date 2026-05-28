package com.SwitchBoard.PortfolioService.DTO.Experience;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Experience response payload")
public class ExperienceResponseDTO {

    @Schema(description = "Unique identifier of the experience record", example = "1")
    private UUID id;

    @Schema(description = "Name of the company", example = "TCS")
    private String company;

    @Schema(description = "Role or designation held", example = "Java Spring Boot Developer")
    private String role;

    @Schema(description = "Location of the company or job", example = "Bangalore, India")
    private String location;

    @Schema(description = "Start date of the experience (YYYY-MM format)", example = "2024-06")
    private YearMonth startDate;

    @Schema(description = "End date of the experience (YYYY-MM format)", example = "2025-06")
    private YearMonth endDate;

    @Schema(description = "Indicates if the experience is current", example = "true")
    private Boolean current;

    @Schema(description = "Brief description of the experience", example = "Worked on backend development using Spring Boot")
    private String description;

    @Schema(description = "Key responsibilities handled", example = "Developed REST APIs, integrated AWS S3, wrote unit tests")
    private String responsibilities;
}
