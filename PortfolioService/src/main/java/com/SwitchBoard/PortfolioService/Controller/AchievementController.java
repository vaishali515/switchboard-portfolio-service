package com.SwitchBoard.PortfolioService.Controller;

import com.SwitchBoard.PortfolioService.DTO.ApiResponse;
import com.SwitchBoard.PortfolioService.DTO.AchievementDTO;
import com.SwitchBoard.PortfolioService.DTO.PortfolioDTO;
import com.SwitchBoard.PortfolioService.Service.Portfolio.AchievementService;
import com.SwitchBoard.PortfolioService.Service.Portfolio.PortfolioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/portfolios/{portfolioId}/achievements")
public class AchievementController {

    private final AchievementService achievementService;
    private final PortfolioService portfolioService;

    
    @GetMapping("/all")
    public ResponseEntity<List<AchievementDTO>> getAllAchievements(@PathVariable Long portfolioId) {
        List<AchievementDTO> achievements = achievementService.getAllAchievementsByPortfolioId(portfolioId);
        return ResponseEntity.ok(achievements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AchievementDTO> getAchievementById(@PathVariable Long id) {
        AchievementDTO achievement = achievementService.getAchievementById(id);
        return ResponseEntity.ok(achievement);
    }

    @PostMapping
    public ResponseEntity<AchievementDTO> createAchievement(
            @PathVariable Long portfolioId,
            @Valid @RequestBody AchievementDTO achievementDTO,
            String emailId) {

        
        AchievementDTO createdAchievement = achievementService.createAchievement(portfolioId, achievementDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAchievement);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AchievementDTO> updateAchievement(
            @PathVariable Long portfolioId,
            @PathVariable Long id,
            @Valid @RequestBody AchievementDTO achievementDTO,
            Authentication authentication) {

        
        AchievementDTO updatedAchievement = achievementService.updateAchievement(id, achievementDTO);
        return ResponseEntity.ok(updatedAchievement);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteAchievement(
            @PathVariable Long portfolioId,
            @PathVariable Long id,
            Authentication authentication) {

        achievementService.deleteAchievement(id);
        return ResponseEntity.ok( ApiResponse.(true, "Achievement deleted successfully"));
    }

}
