package com.SwitchBoard.PortfolioService.Controller;

import com.SwitchBoard.PortfolioService.DTO.Project.ProjectRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Project.ProjectResponseDTO;
import com.SwitchBoard.PortfolioService.DTO.ApiResponse;
import com.SwitchBoard.PortfolioService.Service.Portfolio.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    private UUID portfolioId;
    private UUID projectId;
    private ProjectRequestDTO projectRequest;
    private ProjectResponseDTO projectResponse;

    @BeforeEach
    void setUp() {
        portfolioId = UUID.randomUUID();
        projectId = UUID.randomUUID();

        projectRequest = new ProjectRequestDTO();
        projectRequest.setTitle("E-Commerce Platform");
        projectRequest.setDescription("Full-stack e-commerce application");
        projectRequest.setRole("Lead Developer");

        projectResponse = new ProjectResponseDTO();
        projectResponse.setId(projectId);
        projectResponse.setTitle("E-Commerce Platform");
        projectResponse.setDescription("Full-stack e-commerce application");
    }

    @Test
    void testGetAllProjects_Success() {
        when(projectService.getAllProjectsByPortfolioId(portfolioId)).thenReturn(Arrays.asList(projectResponse));
        
        ResponseEntity<List<ProjectResponseDTO>> response = projectController.getAllProjects(portfolioId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(projectService, times(1)).getAllProjectsByPortfolioId(portfolioId);
    }

    @Test
    void testGetProjectById_Success() {
        when(projectService.getProjectById(projectId)).thenReturn(projectResponse);
        
        ResponseEntity<ProjectResponseDTO> response = projectController.getProjectById(projectId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("E-Commerce Platform", response.getBody().getTitle());
        verify(projectService, times(1)).getProjectById(projectId);
    }

    @Test
    void testCreateProject_Success() throws IOException {
        when(projectService.createProject(eq(portfolioId), any(ProjectRequestDTO.class)))
                .thenReturn(projectResponse);
        
        ResponseEntity<ApiResponse> response = projectController.createProject(portfolioId, projectRequest);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        verify(projectService, times(1)).createProject(eq(portfolioId), any(ProjectRequestDTO.class));
    }

    @Test
    void testUpdateProject_Success() throws IOException {
        when(projectService.updateProject(eq(projectId), any(ProjectRequestDTO.class)))
                .thenReturn(projectResponse);
        
        ResponseEntity<ApiResponse> response = projectController.updateProject(projectId, projectRequest);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        verify(projectService, times(1)).updateProject(eq(projectId), any(ProjectRequestDTO.class));
    }

    @Test
    void testDeleteProject_Success() {
        doNothing().when(projectService).deleteProject(projectId);
        
        ResponseEntity<ApiResponse> response = projectController.deleteProject(projectId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        verify(projectService, times(1)).deleteProject(projectId);
    }
}
