package com.SwitchBoard.PortfolioService.Service.Portfolio.Impl;

import com.SwitchBoard.PortfolioService.DTO.Project.ProjectRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Project.ProjectResponseDTO;
import com.SwitchBoard.PortfolioService.Entity.Portfolio;
import com.SwitchBoard.PortfolioService.Entity.Project;
import com.SwitchBoard.PortfolioService.Repository.PortfolioRepository;
import com.SwitchBoard.PortfolioService.Repository.ProjectRepository;
import com.SwitchBoard.PortfolioService.Service.Portfolio.FileService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private FileService fileService;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private UUID portfolioId;
    private UUID projectId;
    private Portfolio portfolio;
    private Project project;
    private ProjectRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        portfolioId = UUID.randomUUID();
        projectId = UUID.randomUUID();
        
        portfolio = new Portfolio();
        portfolio.setId(portfolioId);
        
        project = new Project();
        project.setId(projectId);
        project.setTitle("E-commerce Platform");
        project.setDescription("Online shopping system");
        project.setPortfolio(portfolio);
        
        requestDTO = new ProjectRequestDTO();
        requestDTO.setTitle("E-commerce Platform");
        requestDTO.setDescription("Online shopping system");
    }

    @Test
    void testGetAllProjectsByPortfolioId_Success() {
        when(portfolioRepository.existsById(portfolioId)).thenReturn(true);
        when(projectRepository.findByPortfolioId(portfolioId)).thenReturn(Arrays.asList(project));

        List<ProjectResponseDTO> result = projectService.getAllProjectsByPortfolioId(portfolioId);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllProjectsByPortfolioId_PortfolioNotFound() {
        when(portfolioRepository.existsById(portfolioId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> 
            projectService.getAllProjectsByPortfolioId(portfolioId));
    }

    @Test
    void testGetProjectById_Success() {
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        ProjectResponseDTO result = projectService.getProjectById(projectId);

        assertNotNull(result);
    }

    @Test
    void testGetProjectById_NotFound() {
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> 
            projectService.getProjectById(projectId));
    }

    @Test
    void testCreateProject_Success() throws IOException {
        when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.of(portfolio));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectResponseDTO result = projectService.createProject(portfolioId, requestDTO);

        assertNotNull(result);
    }

    @Test
    void testCreateProject_PortfolioNotFound() {
        when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> 
            projectService.createProject(portfolioId, requestDTO));
    }

    @Test
    void testUpdateProject_Success() throws IOException {
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectResponseDTO result = projectService.updateProject(projectId, requestDTO);

        assertNotNull(result);
    }

    @Test
    void testUpdateProject_NotFound() {
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> 
            projectService.updateProject(projectId, requestDTO));
    }

    @Test
    void testDeleteProject_Success() throws IOException {
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        doNothing().when(projectRepository).delete(project);

        projectService.deleteProject(projectId);

        verify(projectRepository).delete(project);
    }

    @Test
    void testDeleteProject_NotFound() {
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> 
            projectService.deleteProject(projectId));
    }
}
