package com.SwitchBoard.PortfolioService.Service.Portfolio;


import com.SwitchBoard.PortfolioService.DTO.Achievement.AchievementRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Achievement.AchievementResponseDTO;

import java.util.List;
import java.util.UUID;

public interface AchievementService {
    
    /**
     * Get all achievements for a portfolio with pagination
     */
    
    /**
     * Get all achievements for a portfolio
     */
    List<AchievementResponseDTO> getAllAchievementsByPortfolioId(UUID portfolioId);
    
    /**
     * Get achievement by ID
     */
    AchievementResponseDTO getAchievementById(UUID achievementId);
    
    /**
     * Create a new achievement for a portfolio
     */
    AchievementResponseDTO createAchievement(UUID portfolioId, AchievementRequestDTO achievementRequestDTO);
    
    /**
     * Update an existing achievement
     */
    AchievementResponseDTO updateAchievement(UUID achievementId, AchievementRequestDTO achievementRequestDTO);
    
    /**
     * Delete an achievement by ID
     */
    void deleteAchievement(UUID achievementId);
}
