package com.SwitchBoard.PortfolioService.Service.Portfolio;

import com.SwitchBoard.PortfolioService.DTO.ProjectDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ProjectService {
    
    
    List<ProjectDTO> getAllProjectsByPortfolioId(UUID portfolioId);
   
    ProjectDTO getProjectById(UUID projectId);
    
    
    ProjectDTO createProject(UUID portfolioId, ProjectDTO projectDTO);
    
    
    ProjectDTO updateProject(UUID projectId, ProjectDTO projectDTO, MultipartFile newImage) throws IOException;
    
   
    void deleteProject(UUID projectId);
    
//    String uploadProjectImage(UUID projectId, MultipartFile file) throws IOException;
}
