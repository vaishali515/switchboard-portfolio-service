package com.SwitchBoard.PortfolioService.Controller;

import com.SwitchBoard.PortfolioService.DTO.ApiResponse;
import com.SwitchBoard.PortfolioService.DTO.Project.ProjectRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Project.ProjectResponseDTO;
import com.SwitchBoard.PortfolioService.Service.Portfolio.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/portfolios/{portfolioId}/projects")
@Tag(name = "Project Management", description = "APIs for managing projects in portfolios")
public class ProjectController {

    private final ProjectService projectService;

    @Operation(summary = "Get all projects for a portfolio")
    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects(
            @PathVariable UUID portfolioId) {
        log.info("ProjectController :: getAllProjects :: fetching projects for portfolio: {}", portfolioId);
        return ResponseEntity.ok(projectService.getAllProjectsByPortfolioId(portfolioId));
    }

    @Operation(summary = "Get project by ID")
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable UUID projectId) {
        log.info("ProjectController :: getProjectById :: fetching project: {}", projectId);
        return ResponseEntity.ok(projectService.getProjectById(projectId));
    }

    @Operation(summary = "Create new project")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> createProject(
            @PathVariable UUID portfolioId,
            @Valid @ModelAttribute ProjectRequestDTO projectRequest) throws IOException {

        ProjectResponseDTO created = projectService.createProject(portfolioId, projectRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Project created successfully", created, "201"));
    }

    @Operation(summary = "Update an existing project")
    @PutMapping(path = "/{projectId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> updateProject(
            @PathVariable UUID projectId,
            @Valid @ModelAttribute ProjectRequestDTO projectRequest) throws IOException {

        ProjectResponseDTO updated = projectService.updateProject(projectId, projectRequest);
        return ResponseEntity.ok(ApiResponse.success("Project updated successfully", updated, "200"));
    }

    @Operation(summary = "Delete a project")
    @DeleteMapping("/{projectId}")
    public ResponseEntity<ApiResponse> deleteProject(@PathVariable UUID projectId) {
        log.info("ProjectController :: deleteProject :: deleting project: {}", projectId);
        projectService.deleteProject(projectId);
        return ResponseEntity.ok(ApiResponse.success("Project deleted successfully", true, "200"));
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ApiResponse> handleMultipartException(MultipartException e) {
        log.error("ProjectController :: handleMultipartException :: error handling multipart request: {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Error processing project image: Please ensure the request is properly formatted", null, "400"));
    }
}
