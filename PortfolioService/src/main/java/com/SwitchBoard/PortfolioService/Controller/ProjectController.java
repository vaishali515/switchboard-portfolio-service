package com.SwitchBoard.PortfolioService.Controller;

import com.SwitchBoard.PortfolioService.DTO.Project.ProjectRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Project.ProjectResponseDTO;
import com.SwitchBoard.PortfolioService.DTO.ApiResponse;
import com.SwitchBoard.PortfolioService.Service.Portfolio.FileService;
import com.SwitchBoard.PortfolioService.Service.Portfolio.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/portfolios/{portfolioId}/projects")
@Tag(name = "Project Management", description = "APIs for managing project entries in portfolios")
public class ProjectController {

    private final ProjectService projectService;
    private final FileService fileService;

    @Operation(summary = "Get all projects for a portfolio")
    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects(
            @Parameter(description = "ID of the portfolio to retrieve projects from")
            @PathVariable UUID portfolioId) {
        List<ProjectResponseDTO> projects = projectService.getAllProjectsByPortfolioId(portfolioId);
        return ResponseEntity.ok(projects);
    }

    @Operation(summary = "Get a project by ID")
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(
            @Parameter(description = "ID of the project to retrieve")
            @PathVariable UUID projectId) {
        ProjectResponseDTO project = projectService.getProjectById(projectId);
        return ResponseEntity.ok(project);
    }

    @Operation(summary = "Create a new project")
    @PostMapping
    public ResponseEntity<ApiResponse> createProject(
            @Parameter(description = "ID of the portfolio to add project to")
            @PathVariable UUID portfolioId,
            @Parameter(description = "Project details")
            @Valid @ModelAttribute ProjectRequestDTO projectRequest,
            @Parameter(description = "Project image (optional, must be an image file)")
            @RequestPart(value = "projectImage", required = false) MultipartFile projectImage) throws IOException {
        try {
            String imageUrl = null;
            if (projectImage != null && !projectImage.isEmpty()) {
                String contentType = projectImage.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse("Invalid image type. Only image files are allowed.", null, "400"));
                }
                imageUrl = fileService.uploadImage("portfolio-service", projectImage);
                projectRequest.setProjectImage(projectImage);
            }

            ProjectResponseDTO created = projectService.createProject(portfolioId, projectRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Project created successfully", created, "201"));
        } catch (MultipartException e) {
            log.error("ProjectController :: createProject :: multipart error: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("Error processing project image: " + e.getMessage(), null, "400"));
        }
    }

    @Operation(summary = "Update an existing project")
    @PutMapping("/{projectId}")
    public ResponseEntity<ApiResponse> updateProject(
            @Parameter(description = "ID of the project to update")
            @PathVariable UUID projectId,
            @Parameter(description = "Updated project details")
            @Valid @ModelAttribute ProjectRequestDTO projectRequest,
            @Parameter(description = "Updated project image (optional, must be an image file)")
            @RequestPart(value = "projectImage", required = false) MultipartFile projectImage) throws IOException {
        try {
            String imageUrl = null;
            if (projectImage != null && !projectImage.isEmpty()) {
                String contentType = projectImage.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse("Invalid image type. Only image files are allowed.", null, "400"));
                }
                imageUrl = fileService.uploadImage("portfolio-service", projectImage);
                projectRequest.setProjectImage(projectImage);
            }

            ProjectResponseDTO updated = projectService.updateProject(projectId, projectRequest);
            return ResponseEntity.ok(new ApiResponse("Project updated successfully", updated, "200"));
        } catch (MultipartException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("Error processing project image: " + e.getMessage(), null, "400"));
        }
    }

    @Operation(summary = "Delete a project")
    @DeleteMapping("/{projectId}")
    public ResponseEntity<ApiResponse> deleteProject(
            @Parameter(description = "ID of the project to delete")
            @PathVariable UUID projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.ok(new ApiResponse("Project deleted successfully", true, "200"));
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ApiResponse> handleMultipartException(MultipartException e) {
        log.error("ProjectController :: handleMultipartException :: error handling multipart request: {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(new ApiResponse("Error processing project image: Please ensure the request is properly formatted", null, "400"));
    }
}
