package com.SwitchBoard.PortfolioService.Service.Portfolio.Impl;

import com.SwitchBoard.PortfolioService.DTO.Skill.SkillRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Skill.SkillResponseDTO;
import com.SwitchBoard.PortfolioService.Entity.Portfolio;
import com.SwitchBoard.PortfolioService.Entity.Skill;
import com.SwitchBoard.PortfolioService.Repository.PortfolioRepository;
import com.SwitchBoard.PortfolioService.Repository.SkillRepository;
import com.SwitchBoard.PortfolioService.Service.Portfolio.SkillService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;
    private final PortfolioRepository portfolioRepository;

    
    @Override
    public List<SkillResponseDTO> getAllSkillsByPortfolioId(UUID portfolioId) {
        log.info("SkillServiceImpl :: getAllSkillsByPortfolioId :: fetching skills for portfolio: {}", portfolioId);
        // Verify portfolio exists
        if (!portfolioRepository.existsById(portfolioId)) {
            log.error("SkillServiceImpl :: getAllSkillsByPortfolioId :: portfolio not found: {}", portfolioId);
            throw new EntityNotFoundException("Portfolio not found with id: " + portfolioId);
        }
        
        return skillRepository.findByPortfolioId(portfolioId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SkillResponseDTO getSkillById(UUID id) {
        log.info("SkillServiceImpl :: getSkillById :: fetching skill: {}", id);
        return skillRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> {
                    log.error("SkillServiceImpl :: getSkillById :: skill not found: {}", id);
                    return new EntityNotFoundException("Skill not found with id: " + id);
                });
    }

    @Override
    public SkillResponseDTO createSkill(UUID portfolioId, SkillRequestDTO skillDTO) {
        log.info("SkillServiceImpl :: createSkill :: creating skill for portfolio: {}, skill data: {}", portfolioId, skillDTO);
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> {
                    log.error("SkillServiceImpl :: createSkill :: portfolio not found: {}", portfolioId);
                    return new EntityNotFoundException("Portfolio not found with id: " + portfolioId);
                });
        
        Skill skill = new Skill();
        BeanUtils.copyProperties(skillDTO, skill, "id", "createdAt", "updatedAt");
        skill.setPortfolio(portfolio);
        
        Skill savedSkill = skillRepository.save(skill);
        return convertToDTO(savedSkill);
    }

    @Override
    public SkillResponseDTO updateSkill(UUID skillId, SkillRequestDTO skillDTO) {
        log.info("SkillServiceImpl :: updateSkill :: updating skill: {}, with data: {}", skillId, skillDTO);
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> {
                    log.error("SkillServiceImpl :: updateSkill :: skill not found: {}", skillId);
                    return new EntityNotFoundException("Skill not found with id: " + skillId);
                });
        
        // Update only non-null fields
        if (skillDTO.getName() != null) {
            skill.setName(skillDTO.getName());
        }
        if (skillDTO.getCategory() != null) {
            skill.setCategory(skillDTO.getCategory());
        }
//        if (skillDTO.getProficiencyLevel() != null) {
//            skill.setProficiencyLevel(skillDTO.getProficiencyLevel());
//        }
        if (skillDTO.getDescription() != null) {
            skill.setDescription(skillDTO.getDescription());
        }
        
        Skill updatedSkill = skillRepository.save(skill);
        return convertToDTO(updatedSkill);
    }

    @Override
    public void deleteSkill(UUID skillId) {
        log.info("SkillServiceImpl :: deleteSkill :: deleting skill: {}", skillId);
        if (!skillRepository.existsById(skillId)) {
            log.error("SkillServiceImpl :: deleteSkill :: skill not found: {}", skillId);
            throw new EntityNotFoundException("Skill not found with id: " + skillId);
        }
        skillRepository.deleteById(skillId);
    }

//    @Override
//    public List<SkillDTO> getSkillsByCategory(UUID portfolioId, String category) {
//        // Verify portfolio exists
//        if (!portfolioRepository.existsById(portfolioId)) {
//            throw new EntityNotFoundException("Portfolio not found with id: " + portfolioId);
//        }
//
//        return skillRepository.findByPortfolioIdAndCategory(portfolioId, category).stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * Convert Skill entity to DTO
//     */
    private SkillResponseDTO convertToDTO(Skill skill) {
        SkillResponseDTO skillDTO = new SkillResponseDTO();
        BeanUtils.copyProperties(skill, skillDTO);
        return skillDTO;
    }
}
