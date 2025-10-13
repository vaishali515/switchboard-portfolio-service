package com.SwitchBoard.PortfolioService.Controller;

import com.SwitchBoard.PortfolioService.DTO.ApiResponse;
import com.SwitchBoard.PortfolioService.DTO.EducationDTO;
import com.SwitchBoard.PortfolioService.DTO.PortfolioDTO;
import com.SwitchBoard.PortfolioService.Service.Portfolio.EducationService;
import com.SwitchBoard.PortfolioService.Service.Portfolio.PortfolioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/portfolios/{portfolioId}/education")
public class EducationController {

    private final EducationService educationService;



    @GetMapping
    public ResponseEntity<List<EducationDTO>> getAllEducations(@PathVariable UUID portfolioId) {
        log.info("Fetching all education records for portfolioId: {}", portfolioId);
        List<EducationDTO> educations = educationService.getAllEducationsByPortfolioId(portfolioId);
        return ResponseEntity.ok(educations);
    }

    @GetMapping("/{educationId}")
    public ResponseEntity<EducationDTO> getEducationById(@PathVariable UUID educationId) {
        log.info("Fetching education entry with ID: {}", educationId);
        EducationDTO education = educationService.getEducationById(educationId);
        return ResponseEntity.ok(education);
    }


    @PostMapping
    public ResponseEntity<EducationDTO> createEducation(
            @PathVariable UUID portfolioId,
            @Valid @RequestBody EducationDTO educationDTO) {


        EducationDTO createdEducation = educationService.createEducation(portfolioId, educationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEducation);
    }

    /**
     * Update an existing education entry
     */
    @PutMapping("/{educationId}")
    public ResponseEntity<EducationDTO> updateEducation(
            @PathVariable UUID portfolioId,
            @PathVariable UUID educationId,
            @Valid @RequestBody EducationDTO educationDTO) {


        EducationDTO updatedEducation = educationService.updateEducation(educationId, educationDTO);
        return ResponseEntity.ok(updatedEducation);
    }

    /**
     * Delete an education entry by ID
     */
    @DeleteMapping("/{educationId}")
    public ResponseEntity<ApiResponse> deleteEducation(
            @PathVariable UUID portfolioId,
            @PathVariable UUID educationId) {


        educationService.deleteEducation(educationId);
        return ResponseEntity.ok(ApiResponse.success("Education entry deleted successfully", true));
    }

}
