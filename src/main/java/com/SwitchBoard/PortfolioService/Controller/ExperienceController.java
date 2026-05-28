package com.SwitchBoard.PortfolioService.Controller;

import com.SwitchBoard.PortfolioService.DTO.Experience.ExperienceRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Experience.ExperienceResponseDTO;
import com.SwitchBoard.PortfolioService.DTO.ApiResponse;
import com.SwitchBoard.PortfolioService.Service.Portfolio.ExperienceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/portfolio/{portfolioId}/experience")
@Tag(name = "Experience Management", description = "APIs for managing experience entries in portfolios")
public class ExperienceController {

    private final ExperienceService experienceService;

    @Operation(summary = "Get all experiences for a portfolio")
    @GetMapping
    public ResponseEntity<List<ExperienceResponseDTO>> getExperiencesByPortfolio(
            @Parameter(description = "ID of the portfolio to retrieve experiences from")
            @PathVariable UUID portfolioId) {
        log.info("ExperienceController :: getExperiencesByPortfolio :: fetching experiences for portfolio: {}", portfolioId);
        List<ExperienceResponseDTO> experiences = experienceService.getAllExperiencesByPortfolioId(portfolioId);
        return ResponseEntity.ok(experiences);
    }

    @Operation(summary = "Get an experience entry by ID")
    @GetMapping("/{experienceId}")
    public ResponseEntity<ExperienceResponseDTO> getExperienceById(
            @Parameter(description = "ID of the portfolio")
            @PathVariable UUID portfolioId,
            @Parameter(description = "ID of the experience to retrieve")
            @PathVariable UUID experienceId) {
        log.info("ExperienceController :: getExperienceById :: fetching experience: {} for portfolio: {}", experienceId, portfolioId);
        ExperienceResponseDTO experience = experienceService.getExperienceById(experienceId);
        return ResponseEntity.ok(experience);
    }

    @Operation(summary = "Create a new experience entry")
    @PostMapping
    public ResponseEntity<ApiResponse> createExperience(
            @Parameter(description = "ID of the portfolio to add experience to")
            @PathVariable UUID portfolioId,
            @Parameter(description = "Experience details")
            @Valid @RequestBody ExperienceRequestDTO experienceRequest) {
        log.info("ExperienceController :: createExperience :: creating experience for portfolio: {}, data: {}", portfolioId, experienceRequest);
        ExperienceResponseDTO created = experienceService.createExperience(portfolioId, experienceRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Experience created successfully", created, null));
    }

    @Operation(summary = "Update an existing experience entry")
    @PutMapping("/{experienceId}")
    public ResponseEntity<ApiResponse> updateExperience(
            @Parameter(description = "ID of the portfolio")
            @PathVariable UUID portfolioId,
            @Parameter(description = "ID of the experience to update")
            @PathVariable UUID experienceId,
            @Parameter(description = "Updated experience details")
            @Valid @RequestBody ExperienceRequestDTO experienceRequest) {
        log.info("ExperienceController :: updateExperience :: updating experience: {} for portfolio: {}, data: {}", experienceId, portfolioId, experienceRequest);
        ExperienceResponseDTO updated = experienceService.updateExperience(experienceId, experienceRequest);
        return ResponseEntity.ok(ApiResponse.success("Experience updated successfully", updated, null));
    }

    @Operation(summary = "Delete an experience entry")
    @DeleteMapping("/{experienceId}")
    public ResponseEntity<ApiResponse> deleteExperience(
            @Parameter(description = "ID of the portfolio")
            @PathVariable UUID portfolioId,
            @Parameter(description = "ID of the experience to delete")
            @PathVariable UUID experienceId) {
        log.info("ExperienceController :: deleteExperience :: deleting experience: {} from portfolio: {}", experienceId, portfolioId);
        experienceService.deleteExperience(experienceId);
        return ResponseEntity.ok(ApiResponse.success("Experience deleted successfully", true, null));
    }

}

