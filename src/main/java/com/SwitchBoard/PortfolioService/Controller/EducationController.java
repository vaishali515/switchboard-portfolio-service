package com.SwitchBoard.PortfolioService.Controller;

import com.SwitchBoard.PortfolioService.DTO.Education.EducationRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Education.EducationResponseDTO;
import com.SwitchBoard.PortfolioService.DTO.ApiResponse;
import com.SwitchBoard.PortfolioService.Service.Portfolio.EducationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/portfolios/{portfolioId}/education")
@Tag(name = "Education Management", description = "APIs for managing education entries in portfolios")
public class EducationController {

    private final EducationService educationService;

    @Operation(summary = "Get all education entries for a portfolio")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", 
            description = "Successfully retrieved education list",
            content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = EducationResponseDTO.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", 
            description = "Portfolio not found")
    })
    @GetMapping
    public ResponseEntity<List<EducationResponseDTO>> getAllEducations(
            @Parameter(description = "ID of the portfolio to retrieve education entries from") 
            @PathVariable UUID portfolioId) {
        log.info("EducationController :: getAllEducations :: fetching all education records for portfolio: {}", portfolioId);
        List<EducationResponseDTO> educations = educationService.getAllEducationsByPortfolioId(portfolioId);
        return ResponseEntity.ok(educations);
    }

    @Operation(summary = "Get an education entry by ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", 
            description = "Successfully retrieved education entry",
            content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = EducationResponseDTO.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", 
            description = "Education entry not found")
    })
    @GetMapping("/{educationId}")
    public ResponseEntity<EducationResponseDTO> getEducationById(
            @Parameter(description = "ID of the education entry to retrieve") 
            @PathVariable UUID educationId) {
        log.info("EducationController :: getEducationById :: fetching education: {}", educationId);
        EducationResponseDTO education = educationService.getEducationById(educationId);
        return ResponseEntity.ok(education);
    }

    @Operation(summary = "Create a new education entry")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", 
            description = "Education entry created successfully",
            content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = EducationResponseDTO.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", 
            description = "Invalid input data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", 
            description = "Portfolio not found")
    })
    @PostMapping
    public ResponseEntity<ApiResponse> createEducation(
            @Parameter(description = "ID of the portfolio to add education to") 
            @PathVariable UUID portfolioId,
            @Parameter(description = "Education details") 
            @Valid @RequestBody EducationRequestDTO educationRequest) {
        log.info("EducationController :: createEducation :: creating education for portfolio: {}, data: {}", portfolioId, educationRequest);
        EducationResponseDTO created = educationService.createEducation(portfolioId, educationRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body( ApiResponse.success("Education entry created successfully", created, "201"));
    }

    @Operation(summary = "Update an existing education entry")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", 
            description = "Education entry updated successfully",
            content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = EducationResponseDTO.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", 
            description = "Invalid input data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", 
            description = "Education entry not found")
    })
    @PutMapping("/{educationId}")
    public ResponseEntity<ApiResponse> updateEducation(
            @Parameter(description = "ID of the education entry to update")
            @PathVariable UUID educationId,
            @Parameter(description = "Updated education details")
            @Valid @RequestBody EducationRequestDTO educationRequest) {
        log.info("EducationController :: updateEducation :: updating education: {}, data: {}", educationId, educationRequest);
        EducationResponseDTO updated = educationService.updateEducation(educationId, educationRequest);
        return ResponseEntity.ok( ApiResponse.success("Education entry updated successfully", updated, "200"));
    }

    @Operation(summary = "Delete an education entry")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
            description = "Education entry deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",
            description = "Education entry not found")
    })
    @DeleteMapping("/{educationId}")
    public ResponseEntity<ApiResponse> deleteEducation(
            @Parameter(description = "ID of the education entry to delete")
            @PathVariable UUID educationId) {
        log.info("EducationController :: deleteEducation :: deleting education: {}", educationId);
        educationService.deleteEducation(educationId);
        return ResponseEntity.ok( ApiResponse.success("Education entry deleted successfully", true, "200"));
    }
}
