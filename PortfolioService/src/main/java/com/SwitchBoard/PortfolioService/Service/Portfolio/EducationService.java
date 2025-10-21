package com.SwitchBoard.PortfolioService.Service.Portfolio;

import java.util.List;
import java.util.UUID;

public interface EducationService {
   
    List<EducationDTO> getAllEducationsByPortfolioId(UUID portfolioId);
    
    
    EducationDTO getEducationById(UUID educationId);
    
    EducationDTO createEducation(UUID portfolioId, EducationDTO educationDTO);
    
    
    EducationDTO updateEducation(UUID educationId, EducationDTO educationDTO);
    
    
    void deleteEducation(UUID educationIdUUID);
}
