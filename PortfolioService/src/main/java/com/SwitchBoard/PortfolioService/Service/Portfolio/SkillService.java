package com.SwitchBoard.PortfolioService.Service.Portfolio;

import com.SwitchBoard.PortfolioService.DTO.SkillDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface SkillService {
    
   
    List<SkillDTO> getAllSkillsByPortfolioId(UUID portfolioId);

    SkillDTO getSkillById(UUID skillId);

    SkillDTO createSkill(UUID portfolioId, SkillDTO skillDTO);
    

    SkillDTO updateSkill(UUID skillId, SkillDTO skillDTO);
    

    void deleteSkill(UUID skillId);
    

}
