package com.SwitchBoard.PortfolioService.Service.Portfolio;

import com.SwitchBoard.PortfolioService.DTO.Experience.ExperienceRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Experience.ExperienceResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ExperienceService {
    


    List<ExperienceResponseDTO> getAllExperiencesByPortfolioId(UUID portfolioId);

    ExperienceResponseDTO getExperienceById(UUID experienceId);


    ExperienceResponseDTO createExperience(UUID portfolioId, ExperienceRequestDTO experienceDTO);


    ExperienceResponseDTO updateExperience(UUID experienceId, ExperienceRequestDTO experienceDTO);
    
    void deleteExperience(UUID experienceId);
}
