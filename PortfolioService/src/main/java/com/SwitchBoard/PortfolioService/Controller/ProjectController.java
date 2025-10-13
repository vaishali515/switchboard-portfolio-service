package com.SwitchBoard.PortfolioService.Controller;

import com.SwitchBoard.PortfolioService.DTO.ApiResponse;
import com.SwitchBoard.PortfolioService.DTO.CertificateDTO;
import com.SwitchBoard.PortfolioService.DTO.PortfolioDTO;
import com.SwitchBoard.PortfolioService.DTO.ProjectDTO;
import com.SwitchBoard.PortfolioService.Service.Portfolio.FileService;
import com.SwitchBoard.PortfolioService.Service.Portfolio.PortfolioService;
import com.SwitchBoard.PortfolioService.Service.Portfolio.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/portfolios/{portfolioId}/projects")
@Slf4j
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final FileService fileService;


    @GetMapping("/all")
    public ResponseEntity<List<ProjectDTO>> getAllProjects(@PathVariable UUID portfolioId) {
        List<ProjectDTO> projects = projectService.getAllProjectsByPortfolioId(portfolioId);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable UUID projectId) {
        ProjectDTO project = projectService.getProjectById(projectId);
        return ResponseEntity.ok(project);
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(
            @PathVariable UUID portfolioId,
            @Valid @RequestBody ProjectDTO projectDTO,@RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        try {
            if (image != null && !image.isEmpty()) {

                String contentType = image.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.badRequest().body(null);
                }

                String imageUrl = fileService.uploadImage("portfolio-service", image);
                projectDTO.setImageUrl(imageUrl);
            }


            ProjectDTO project = projectService.createProject(portfolioId,projectDTO );
            return ResponseEntity.status(HttpStatus.CREATED).body(project);

        } catch (MultipartException e) {
            log.error("InterviewExperienceController :: createInterviewExperience :: multipart error: {}", e.getMessage());
            throw new RuntimeException("Error processing multipart request: " + e.getMessage());
        } catch (Exception e) {
            log.error("InterviewExperienceController :: createInterviewExperience :: error: {}", e.getMessage());
            throw e;
        }
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<String> handleMultipartException(MultipartException e) {
        log.error("InterviewExperienceController :: handleMultipartException :: error handling multipart request: {}", e.getMessage());
        return ResponseEntity.badRequest().body("Error processing multipart request: Please ensure the request is properly formatted");
    }


    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectDTO> updateProject(
            @PathVariable UUID portfolioId,
            @PathVariable UUID projectId,
            @Valid @RequestBody ProjectDTO projectDTO, @Valid @RequestBody CertificateDTO certificateDTO,@RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        try {
            ProjectDTO updatedProject = projectService.updateProject(projectId, projectDTO, image);
            return ResponseEntity.ok(updatedProject);
        }
        catch (Exception e) {
            log.error("CertificateController :: updateCertificate :: error :: {}", e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<ApiResponse> deleteProject(
            @PathVariable UUID portfolioId,
            @PathVariable UUID projectId) {
        
        projectService.deleteProject(projectId);
        return ResponseEntity.ok(ApiResponse.success("Project deleted successfully", true));
    }



}
