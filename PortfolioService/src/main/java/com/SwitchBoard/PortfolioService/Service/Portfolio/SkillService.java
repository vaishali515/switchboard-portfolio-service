package com.SwitchBoard.PortfolioService.Service.Portfolio;

import java.util.List;
import java.util.UUID;

public interface SkillService {
    
   
    List<SkillDTO> getAllSkillsByPortfolioId(UUID portfolioId);

    SkillDTO getSkillById(UUID skillId);

    SkillDTO createSkill(UUID portfolioId, SkillDTO skillDTO);
    

    SkillDTO updateSkill(UUID skillId, SkillDTO skillDTO);
    

    void deleteSkill(UUID skillId);
    

}
