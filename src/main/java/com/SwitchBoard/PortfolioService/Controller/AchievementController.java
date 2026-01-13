package com.SwitchBoard.PortfolioService.Controller;

import com.SwitchBoard.PortfolioService.DTO.Achievement.AchievementRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Achievement.AchievementResponseDTO;
import com.SwitchBoard.PortfolioService.DTO.ApiResponse;
import com.SwitchBoard.PortfolioService.Service.Portfolio.AchievementService;
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
@RequestMapping("/api/v1/portfolio/{portfolioId}/achievements")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Achievement Management", description = "APIs for managing achievement entries in portfolios")
public class AchievementController {

    private final AchievementService achievementService;

    @Operation(summary = "Get all achievements for a portfolio")
    @GetMapping
    public ResponseEntity<List<AchievementResponseDTO>> getAllAchievements(
            @Parameter(description = "ID of the portfolio") @PathVariable UUID portfolioId) {

        log.info("Fetching all achievements for portfolioId: {}", portfolioId);
        List<AchievementResponseDTO> achievements = achievementService.getAllAchievementsByPortfolioId(portfolioId);
        return ResponseEntity.ok(achievements);
    }

    @Operation(summary = "Get a single achievement by ID")
    @GetMapping("/{achievementId}")
    public ResponseEntity<AchievementResponseDTO> getAchievementById(
            @Parameter(description = "ID of the achievement") @PathVariable UUID achievementId) {

        log.info("Fetching achievement with ID: {}", achievementId);
        AchievementResponseDTO achievement = achievementService.getAchievementById(achievementId);
        return ResponseEntity.ok(achievement);
    }

    @Operation(summary = "Create a new achievement")
    @PostMapping
    public ResponseEntity<ApiResponse> createAchievement(
            @Parameter(description = "ID of the portfolio") @PathVariable UUID portfolioId,
            @Parameter(description = "Achievement details") @Valid @RequestBody AchievementRequestDTO achievementRequest) {

        AchievementResponseDTO created = achievementService.createAchievement(portfolioId, achievementRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body( ApiResponse.success("Achievement created successfully", created, "201"));
    }

    @Operation(summary = "Update an existing achievement")
    @PutMapping("/{achievementId}")
    public ResponseEntity<ApiResponse> updateAchievement(
            @Parameter(description = "ID of the achievement") @PathVariable UUID achievementId,
            @Parameter(description = "Updated achievement details") @Valid @RequestBody AchievementRequestDTO achievementRequest) {

        AchievementResponseDTO updated = achievementService.updateAchievement(achievementId, achievementRequest);
        return ResponseEntity.ok( ApiResponse.success("Achievement updated successfully", updated, "200"));
    }

    @Operation(summary = "Delete an achievement")
    @DeleteMapping("/{achievementId}")
    public ResponseEntity<ApiResponse> deleteAchievement(
            @Parameter(description = "ID of the achievement") @PathVariable UUID achievementId) {

        log.info("Deleting achievement with ID: {}", achievementId);
        achievementService.deleteAchievement(achievementId);
        return ResponseEntity.ok( ApiResponse.success("Achievement deleted successfully", true, "200"));
    }
}
