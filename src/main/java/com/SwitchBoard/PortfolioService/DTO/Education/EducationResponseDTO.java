package com.SwitchBoard.PortfolioService.DTO.Education;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Education response payload")
public class EducationResponseDTO {

    @Schema(description = "Unique identifier of the education record", example = "1")
    private UUID id;

    @Schema(description = "Name of the educational institution", example = "Stanford University")
    private String institution;

    @Schema(description = "Degree or certification obtained", example = "Bachelor of Science")
    private String degree;

    @Schema(description = "Field of study or major", example = "Computer Science")
    private String fieldOfStudy;

    @Schema(description = "Grade or GPA achieved", example = "9.2")
    private Double grade;
    
    @Schema(description = "Percentage achieved", example = "85.5")
    private Double percentage;

    @Schema(description = "Start date of the education (YYYY-MM format)", example = "2020-08")
    private YearMonth startDate;

    @Schema(description = "End date of the education (YYYY-MM format)", example = "2024-05")
    private YearMonth endDate;

    @Schema(description = "Indicates if the education is ongoing", example = "false")
    private Boolean ongoing;

    @Schema(description = "Additional description or notes", example = "Focused on software engineering and algorithms")
    private String description;
}
