package com.SwitchBoard.PortfolioService.Controller;

import com.SwitchBoard.PortfolioService.DTO.ApiResponse;
import com.SwitchBoard.PortfolioService.DTO.PortfolioDTO;
import com.SwitchBoard.PortfolioService.DTO.SkillDTO;
import com.SwitchBoard.PortfolioService.Service.Portfolio.Impl.SkillServiceImpl;
import com.SwitchBoard.PortfolioService.Service.Portfolio.PortfolioService;
import com.SwitchBoard.PortfolioService.Service.Portfolio.SkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/portfolios/{portfolioId}/skills")
@RequiredArgsConstructor
public class SkillController {


    private final SkillService skillService;

    @GetMapping("/all")
    public ResponseEntity<List<SkillDTO>> getAllSkills(@PathVariable UUID portfolioId) {
        List<SkillDTO> skills = skillService.getAllSkillsByPortfolioId(portfolioId);
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/{skillId}")
    public ResponseEntity<SkillDTO> getSkillById(@PathVariable UUID skillId) {
        SkillDTO skill = skillService.getSkillById(skillId);
        return ResponseEntity.ok(skill);
    }
    


    @PostMapping
    public ResponseEntity<SkillDTO> createSkill(
            @PathVariable UUID portfolioId,
            @Valid @RequestBody SkillDTO skillDTO) {

        
        SkillDTO createdSkill = skillService.createSkill(portfolioId, skillDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSkill);
    }

    @PutMapping("/{skillId}")
    public ResponseEntity<SkillDTO> updateSkill(
            @PathVariable UUID portfolioId,
            @PathVariable UUID skillId,
            @Valid @RequestBody SkillDTO skillDTO) {
        

        SkillDTO updatedSkill = skillService.updateSkill(skillId, skillDTO);
        return ResponseEntity.ok(updatedSkill);
    }

    @DeleteMapping("/{skillId}")
    public ResponseEntity<ApiResponse> deleteSkill(
            @PathVariable UUID portfolioId,
            @PathVariable UUID skillId) {

        
        skillService.deleteSkill(skillId);
        return ResponseEntity.ok( ApiResponse.success("Skill deleted successfully", true));
    }
    

}
