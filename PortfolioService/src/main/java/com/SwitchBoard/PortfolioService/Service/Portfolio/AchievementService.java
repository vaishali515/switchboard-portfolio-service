package com.SwitchBoard.PortfolioService.Service.Portfolio;


import com.SwitchBoard.PortfolioService.DTO.AchievementDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AchievementService {
    
    /**
     * Get all achievements for a portfolio with pagination
     */
    
    /**
     * Get all achievements for a portfolio
     */
    List<AchievementDTO> getAllAchievementsByPortfolioId(UUID portfolioId);
    
    /**
     * Get achievement by ID
     */
    AchievementDTO getAchievementById(UUID achievementId);
    
    /**
     * Create a new achievement for a portfolio
     */
    AchievementDTO createAchievement(UUID portfolioId, AchievementDTO achievementDTO);
    
    /**
     * Update an existing achievement
     */
    AchievementDTO updateAchievement(UUID achievementId, AchievementDTO achievementDTO);
    
    /**
     * Delete an achievement by ID
     */
    void deleteAchievement(UUID achievementId);
}
