package com.SwitchBoard.PortfolioService.Controller;

import com.SwitchBoard.PortfolioService.DTO.ApiResponse;
import com.SwitchBoard.PortfolioService.DTO.AchievementDTO;
import com.SwitchBoard.PortfolioService.DTO.PortfolioDTO;
import com.SwitchBoard.PortfolioService.Service.Portfolio.AchievementService;
import com.SwitchBoard.PortfolioService.Service.Portfolio.PortfolioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/portfolios/{portfolioId}/achievements")
public class AchievementController {

    private final AchievementService achievementService;


    @GetMapping
    public ResponseEntity<List<AchievementDTO>> getAllAchievements(@PathVariable UUID portfolioId) {
        log.info("Fetching all achievements for portfolioId: {}", portfolioId);
        List<AchievementDTO> achievements = achievementService.getAllAchievementsByPortfolioId(portfolioId);
        return ResponseEntity.ok(achievements);
    }

    @GetMapping("/{achievementId}")
    public ResponseEntity<AchievementDTO> getAchievementById(@PathVariable UUID achievementId) {
        log.info("Fetching achievement with ID: {}", achievementId);
        AchievementDTO achievement = achievementService.getAchievementById(achievementId);
        return ResponseEntity.ok(achievement);
    }

    @PostMapping
    public ResponseEntity<AchievementDTO> createAchievement(
            @PathVariable UUID portfolioId,
            @Valid @RequestBody AchievementDTO achievementDTO) {

        AchievementDTO createdAchievement = achievementService.createAchievement(portfolioId, achievementDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAchievement);
    }


    @PutMapping("/{achievementId}")
    public ResponseEntity<AchievementDTO> updateAchievement(
            @PathVariable UUID portfolioId,
            @PathVariable UUID achievementId,
            @Valid @RequestBody AchievementDTO achievementDTO) {


        AchievementDTO updatedAchievement = achievementService.updateAchievement(achievementId, achievementDTO);
        return ResponseEntity.ok(updatedAchievement);
    }


    @DeleteMapping("/{achievementId}")
    public ResponseEntity<ApiResponse> deleteAchievement(
            @PathVariable UUID achievementId) {

        achievementService.deleteAchievement(achievementId);
        return ResponseEntity.ok(ApiResponse.success("Achievement deleted successfully", true));
    }

}
