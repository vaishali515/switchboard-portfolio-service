package com.SwitchBoard.PortfolioService.Controller;

import com.SwitchBoard.PortfolioService.DTO.Skill.SkillRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Skill.SkillResponseDTO;
import com.SwitchBoard.PortfolioService.DTO.ApiResponse;
import com.SwitchBoard.PortfolioService.Service.Portfolio.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/v1/portfolio/{portfolioId}/skills")
@Tag(name = "Skill Management", description = "APIs for managing skill entries in portfolios")
public class SkillController {

    private final SkillService skillService;

    @Operation(summary = "Get all skills for a portfolio")
    @GetMapping
    public ResponseEntity<List<SkillResponseDTO>> getAllSkills(
            @Parameter(description = "ID of the portfolio to retrieve skills from") 
            @PathVariable UUID portfolioId) {
        log.info("SkillController :: getAllSkills :: fetching all skills for portfolio: {}", portfolioId);
        List<SkillResponseDTO> skills = skillService.getAllSkillsByPortfolioId(portfolioId);
        return ResponseEntity.ok(skills);
    }

    @Operation(summary = "Get a skill by ID")
    @GetMapping("/{skillId}")
    public ResponseEntity<SkillResponseDTO> getSkillById(
            @Parameter(description = "ID of the skill to retrieve") 
            @PathVariable UUID skillId) {
        log.info("SkillController :: getSkillById :: fetching skill: {}", skillId);
        SkillResponseDTO skill = skillService.getSkillById(skillId);
        return ResponseEntity.ok(skill);
    }

    @Operation(summary = "Create a new skill")
    @PostMapping
    public ResponseEntity<ApiResponse> createSkill(
            @Parameter(description = "ID of the portfolio to add skill to") 
            @PathVariable UUID portfolioId,
            @Parameter(description = "Skill details") 
            @Valid @RequestBody SkillRequestDTO skillRequest) {
        log.info("SkillController :: createSkill :: creating skill for portfolio: {}, skill: {}", portfolioId, skillRequest);
        SkillResponseDTO created = skillService.createSkill(portfolioId, skillRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body( ApiResponse.success("Skill created successfully", created, "201"));
    }

    @Operation(summary = "Update an existing skill")
    @PutMapping("/{skillId}")
    public ResponseEntity<ApiResponse> updateSkill(
            @Parameter(description = "ID of the portfolio") 
            @PathVariable UUID portfolioId,
            @Parameter(description = "ID of the skill to update") 
            @PathVariable UUID skillId,
            @Parameter(description = "Updated skill details") 
            @Valid @RequestBody SkillRequestDTO skillRequest) {
        log.info("SkillController :: updateSkill :: updating skill: {} for portfolio: {}, data: {}", skillId, portfolioId, skillRequest);
        SkillResponseDTO updated = skillService.updateSkill(skillId, skillRequest);
        return ResponseEntity.ok( ApiResponse.success("Skill updated successfully", updated, "200"));
    }

    @Operation(summary = "Delete a skill")
    @DeleteMapping("/{skillId}")
    public ResponseEntity<ApiResponse> deleteSkill(
            @Parameter(description = "ID of the portfolio") 
            @PathVariable UUID portfolioId,
            @Parameter(description = "ID of the skill to delete") 
            @PathVariable UUID skillId) {
        log.info("SkillController :: deleteSkill :: deleting skill: {} from portfolio: {}", skillId, portfolioId);
        skillService.deleteSkill(skillId);
        return ResponseEntity.ok( ApiResponse.success("Skill deleted successfully", true, "200"));
    }
}
