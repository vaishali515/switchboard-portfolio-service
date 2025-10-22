package com.SwitchBoard.PortfolioService.Service.Portfolio;

import com.SwitchBoard.PortfolioService.DTO.Skill.SkillRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Skill.SkillResponseDTO;

import java.util.List;
import java.util.UUID;

public interface SkillService {
    
   
    List<SkillResponseDTO> getAllSkillsByPortfolioId(UUID portfolioId);

    SkillResponseDTO getSkillById(UUID skillId);

    SkillResponseDTO createSkill(UUID portfolioId, SkillRequestDTO skillDTO);


    SkillResponseDTO updateSkill(UUID skillId, SkillRequestDTO skillDTO);
    

    void deleteSkill(UUID skillId);
    

}
