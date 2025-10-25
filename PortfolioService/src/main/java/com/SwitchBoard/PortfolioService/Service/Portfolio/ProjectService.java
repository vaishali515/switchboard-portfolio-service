package com.SwitchBoard.PortfolioService.Service.Portfolio;

import com.SwitchBoard.PortfolioService.DTO.Project.ProjectRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Project.ProjectResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ProjectService {
    
    
    List<ProjectResponseDTO> getAllProjectsByPortfolioId(UUID portfolioId);

    ProjectResponseDTO getProjectById(UUID projectId);


    ProjectResponseDTO createProject(UUID portfolioId, ProjectRequestDTO projectDTO ) ;


    ProjectResponseDTO updateProject(UUID projectId, ProjectRequestDTO projectDTO) ;
    
   
    void deleteProject(UUID projectId);
    
//    String uploadProjectImage(UUID projectId, MultipartFile file) throws IOException;
}
