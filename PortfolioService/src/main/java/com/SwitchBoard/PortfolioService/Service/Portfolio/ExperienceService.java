package com.SwitchBoard.PortfolioService.Service.Portfolio;

import java.util.List;
import java.util.UUID;

public interface ExperienceService {
    


    List<ExperienceDTO> getAllExperiencesByPortfolioId(UUID portfolioId);
    
    ExperienceDTO getExperienceById(UUID experienceId);
    
    
    ExperienceDTO createExperience(UUID portfolioId, ExperienceDTO experienceDTO);
    
    
    ExperienceDTO updateExperience(UUID experienceId, ExperienceDTO experienceDTO);
    
    void deleteExperience(UUID experienceId);
}
