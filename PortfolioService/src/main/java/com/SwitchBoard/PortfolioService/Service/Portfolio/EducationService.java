package com.SwitchBoard.PortfolioService.Service.Portfolio;

import com.SwitchBoard.PortfolioService.DTO.EducationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface EducationService {
   
    List<EducationDTO> getAllEducationsByPortfolioId(UUID portfolioId);
    
    
    EducationDTO getEducationById(UUID educationId);
    
    EducationDTO createEducation(UUID portfolioId, EducationDTO educationDTO);
    
    
    EducationDTO updateEducation(UUID educationId, EducationDTO educationDTO);
    
    
    void deleteEducation(UUID educationIdUUID);
}
