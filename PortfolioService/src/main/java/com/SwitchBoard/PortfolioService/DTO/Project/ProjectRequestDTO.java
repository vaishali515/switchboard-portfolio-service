package com.SwitchBoard.PortfolioService.DTO.Project;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Project creation/update request payload")
public class ProjectRequestDTO {

    @NotBlank(message = "Project title is required")
    @Schema(description = "Title of the project", example = "E-Commerce Platform")
    private String title;

    @Schema(description = "Brief description of the project", example = "A full-stack e-commerce platform with microservices architecture")
    private String description;

    @Schema(description = "Start date of the project", example = "2024-01-01")
    private LocalDate startDate;

    @Schema(description = "End date of the project", example = "2024-06-30")
    private LocalDate endDate;

    @Schema(description = "List of technologies used in the project", example = "[\"Java\", \"Spring Boot\", \"React\", \"AWS\"]")
    private List<String> technologies;

    @Schema(description = "URL of the live project", example = "https://example.com/project")
    private String liveUrl;

    @Schema(description = "URL of the project repository", example = "https://github.com/username/project")
    private String repoUrl;

    @Schema(description = "Key features of the project", example = "[\"User Authentication\", \"Payment Integration\", \"Real-time Chat\"]")
    private List<String> features;

    @Schema(description = "Role in the project", example = "Lead Developer")
    private String role;

    @Schema(description = "Project status (e.g., Completed, In Progress)", example = "Completed")
    private String status;
    @Schema(description = "Indicates if the project is ongoing", example = "false")
    private Boolean ongoing = false;
}
