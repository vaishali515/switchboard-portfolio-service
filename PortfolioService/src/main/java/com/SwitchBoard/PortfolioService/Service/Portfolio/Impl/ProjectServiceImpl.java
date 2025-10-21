package com.SwitchBoard.PortfolioService.Service.Portfolio.Impl;

import com.SwitchBoard.PortfolioService.Entity.Portfolio;
import com.SwitchBoard.PortfolioService.Entity.Project;
import com.SwitchBoard.PortfolioService.Repository.PortfolioRepository;
import com.SwitchBoard.PortfolioService.Repository.ProjectRepository;
import com.SwitchBoard.PortfolioService.Service.Portfolio.FileService;
import com.SwitchBoard.PortfolioService.Service.Portfolio.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final PortfolioRepository portfolioRepository;
    private final FileService fileService;

    @Override
    public List<ProjectDTO> getAllProjectsByPortfolioId(UUID portfolioId) {
        // Verify portfolio exists
        if (!portfolioRepository.existsById(portfolioId)) {
            throw new EntityNotFoundException("Portfolio not found with id: " + portfolioId);
        }
        
        return projectRepository.findByPortfolioId(portfolioId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDTO getProjectById(UUID projectId) {
        return projectRepository.findById(projectId)
                .map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));
    }

    @Override
    public ProjectDTO createProject(UUID portfolioId, ProjectDTO projectDTO) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new EntityNotFoundException("Portfolio not found with id: " + portfolioId));
        
        Project project = new Project();
        BeanUtils.copyProperties(projectDTO, project, "id", "createdAt", "updatedAt");
        project.setPortfolio(portfolio);
        
        Project savedProject = projectRepository.save(project);
        return convertToDTO(savedProject);
    }

    @Override
    public ProjectDTO updateProject(UUID projectId, ProjectDTO projectDTO, MultipartFile newImage) throws IOException {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));

        // Handle new image upload
        if (newImage != null && !newImage.isEmpty()) {
            // Delete old image from S3 if exists
            if (project.getImageUrl() != null && !project.getImageUrl().isEmpty()) {
                fileService.deleteImage(project.getImageUrl());
            }

            // Upload new image
            String newImageUrl = fileService.uploadImage("portfolio-service", newImage);
            projectDTO.setImageUrl(newImageUrl);
        } else {
            // Keep old image if no new image uploaded
            projectDTO.setImageUrl(project.getImageUrl());
        }
        
        // Update only non-null fields
        if (projectDTO.getName() != null) {
            project.setName(projectDTO.getName());
        }
        if (projectDTO.getDescription() != null) {
            project.setDescription(projectDTO.getDescription());
        }
        if (projectDTO.getUrl() != null) {
            project.setUrl(projectDTO.getUrl());
        }
        if (projectDTO.getTechnologies() != null) {
            project.setTechnologies(projectDTO.getTechnologies());
        }
        if (projectDTO.getStartDate() != null) {
            project.setStartDate(projectDTO.getStartDate());
        }
        if (projectDTO.getEndDate() != null) {
            project.setEndDate(projectDTO.getEndDate());
        }
        if (projectDTO.getOngoing() != null) {
            project.setOngoing(projectDTO.getOngoing());
        }
        project.setImageUrl(projectDTO.getImageUrl());
        
        Project updatedProject = projectRepository.save(project);
        return convertToDTO(updatedProject);
    }

    @Override
    public void deleteProject(UUID projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> {
                    return new RuntimeException("Project not found");
                });

        // Delete image from S3 if exists
        if (project.getImageUrl() != null && !project.getImageUrl().isEmpty()) {
            try {
                fileService.deleteImage(project.getImageUrl());
            } catch (Exception e) {
                // Optional: you can throw exception if you want to fail delete if image deletion fails
            }
        }

        // Delete DB record
        projectRepository.delete(project);
    }


    /**
     * Convert Project entity to DTO
     */
    private ProjectDTO convertToDTO(Project project) {
        ProjectDTO projectDTO = new ProjectDTO();
        BeanUtils.copyProperties(project, projectDTO);
        return projectDTO;
    }
}
