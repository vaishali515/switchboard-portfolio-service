package com.SwitchBoard.PortfolioService.Controller;

import com.SwitchBoard.PortfolioService.DTO.ApiResponse;
import com.SwitchBoard.PortfolioService.DTO.ExperienceDTO;
import com.SwitchBoard.PortfolioService.DTO.PortfolioDTO;
import com.SwitchBoard.PortfolioService.Service.Portfolio.ExperienceService;
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
@Slf4j

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/portfolios/{portfolioId}/experience")

public class ExperienceController {

    private final ExperienceService experienceService;


    @GetMapping
    public ResponseEntity<List<ExperienceDTO>> getExperiencesByPortfolio(
            @PathVariable UUID portfolioId) {

        log.info("Fetching experiences for portfolioId: {}", portfolioId);
        List<ExperienceDTO> experiences = experienceService.getAllExperiencesByPortfolioId(portfolioId);
        return ResponseEntity.ok(experiences);
    }


    @GetMapping("/{experienceId}")
    public ResponseEntity<ExperienceDTO> getExperienceById(
            @PathVariable UUID portfolioId,
            @PathVariable UUID experienceId) {

        log.info("Fetching experienceId: {} for portfolioId: {}", experienceId, portfolioId);
        ExperienceDTO experience = experienceService.getExperienceById(experienceId);
        return ResponseEntity.ok(experience);
    }


    @PostMapping
    public ResponseEntity<ExperienceDTO> createExperience(
            @PathVariable UUID portfolioId,
            @Valid @RequestBody ExperienceDTO experienceDTO) {

        ExperienceDTO createdExperience = experienceService.createExperience(portfolioId, experienceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdExperience);
    }


    @PutMapping("/{experienceId}")
    public ResponseEntity<ExperienceDTO> updateExperience(
            @PathVariable UUID portfolioId,
            @PathVariable UUID experienceId,
            @Valid @RequestBody ExperienceDTO experienceDTO) {

        ExperienceDTO updatedExperience = experienceService.updateExperience(experienceId, experienceDTO);
        return ResponseEntity.ok(updatedExperience);
    }


    @DeleteMapping("/{experienceId}")
    public ResponseEntity<ApiResponse> deleteExperience(
            @PathVariable UUID portfolioId,
            @PathVariable UUID experienceId) {


        experienceService.deleteExperience(experienceId);
        return ResponseEntity.ok(ApiResponse.success("Experience deleted successfully", true));
    }

}
