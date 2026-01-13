package com.SwitchBoard.PortfolioService.Service.Portfolio;

import com.SwitchBoard.PortfolioService.DTO.Education.EducationRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Education.EducationResponseDTO;

import java.util.List;
import java.util.UUID;

public interface EducationService {
   
    List<EducationResponseDTO> getAllEducationsByPortfolioId(UUID portfolioId);


    EducationResponseDTO getEducationById(UUID educationId);

    EducationResponseDTO createEducation(UUID portfolioId, EducationRequestDTO educationDTO);


    EducationResponseDTO updateEducation(UUID educationId, EducationRequestDTO educationDTO);
    
    
    void deleteEducation(UUID educationIdUUID);
}
