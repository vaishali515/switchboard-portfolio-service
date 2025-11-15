package com.SwitchBoard.PortfolioService.DTO.Education;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Education creation/update request payload")
public class EducationRequestDTO {

    @NotBlank(message = "Institution name is required")
    @Schema(description = "Name of the educational institution", example = "Stanford University")
    private String institution;

    @NotBlank(message = "Degree is required")
    @Schema(description = "Degree or certification obtained", example = "Bachelor of Science")
    private String degree;

    @Schema(description = "Field of study or major", example = "Computer Science")
    private String fieldOfStudy;

    @Schema(description = "Grade or GPA achieved", example = "9.2")
    private Double grade;

    @Schema(description = "Start date of the education", example = "2020-08-01")
    private LocalDate startDate;

    @Schema(description = "End date of the education", example = "2024-05-31")
    private LocalDate endDate;

    @Schema(description = "Indicates if the education is ongoing", example = "false")
    private Boolean ongoing;

    @Schema(description = "Additional description or notes", example = "Focused on software engineering and algorithms")
    private String description;
}

