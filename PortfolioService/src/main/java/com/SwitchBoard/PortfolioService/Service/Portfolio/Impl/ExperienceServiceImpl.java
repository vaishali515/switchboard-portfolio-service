package com.SwitchBoard.PortfolioService.Service.Portfolio.Impl;

import com.SwitchBoard.PortfolioService.Entity.Experience;
import com.SwitchBoard.PortfolioService.Entity.Portfolio;
import com.SwitchBoard.PortfolioService.Repository.ExperienceRepository;
import com.SwitchBoard.PortfolioService.Repository.PortfolioRepository;
import com.SwitchBoard.PortfolioService.Service.Portfolio.ExperienceService;
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
public class ExperienceServiceImpl implements ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final PortfolioRepository portfolioRepository;


    @Override
    public List<ExperienceDTO> getAllExperiencesByPortfolioId(UUID portfolioId) {
        log.info("ExperienceServiceImpl :: getAllExperiencesByPortfolioId :: fetching experiences for portfolio: {}", portfolioId);
        // Verify portfolio exists
        if (!portfolioRepository.existsById(portfolioId)) {
            log.error("ExperienceServiceImpl :: getAllExperiencesByPortfolioId :: portfolio not found: {}", portfolioId);
            throw new EntityNotFoundException("Portfolio not found with id: " + portfolioId);
        }
        
        return experienceRepository.findByPortfolioId(portfolioId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ExperienceDTO getExperienceById(UUID experienceId) {
        log.info("ExperienceServiceImpl :: getExperienceById :: fetching experience: {}", experienceId);
        return experienceRepository.findById(experienceId)
                .map(this::convertToDTO)
                .orElseThrow(() -> {
                    log.error("ExperienceServiceImpl :: getExperienceById :: experience not found: {}", experienceId);
                    return new EntityNotFoundException("Experience not found with id: " + experienceId);
                });
    }

    @Override
    public ExperienceDTO createExperience(UUID portfolioId, ExperienceDTO experienceDTO) {
        log.info("ExperienceServiceImpl :: createExperience :: creating experience for portfolio: {}, data: {}", portfolioId, experienceDTO);
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> {
                    log.error("ExperienceServiceImpl :: createExperience :: portfolio not found: {}", portfolioId);
                    return new EntityNotFoundException("Portfolio not found with id: " + portfolioId);
                });
        
        Experience experience = new Experience();
        BeanUtils.copyProperties(experienceDTO, experience, "id", "createdAt", "updatedAt");
        experience.setPortfolio(portfolio);
        
        Experience savedExperience = experienceRepository.save(experience);
        return convertToDTO(savedExperience);
    }

    @Override
    public ExperienceDTO updateExperience(UUID experienceId, ExperienceDTO experienceDTO) {
        log.info("ExperienceServiceImpl :: updateExperience :: updating experience: {}, data: {}", experienceId, experienceDTO);
        Experience experience = experienceRepository.findById(experienceId)
                .orElseThrow(() -> {
                    log.error("ExperienceServiceImpl :: updateExperience :: experience not found: {}", experienceId);
                    return new EntityNotFoundException("Experience not found with id: " + experienceId);
                });
        
        // Update only non-null fields
        if (experienceDTO.getCompany() != null) {
            experience.setCompany(experienceDTO.getCompany());
        }
        if (experienceDTO.getRole() != null) {
            experience.setRole(experienceDTO.getRole());
        }
        if (experienceDTO.getLocation() != null) {
            experience.setLocation(experienceDTO.getLocation());
        }
        if (experienceDTO.getStartDate() != null) {
            experience.setStartDate(experienceDTO.getStartDate());
        }
        if (experienceDTO.getEndDate() != null) {
            experience.setEndDate(experienceDTO.getEndDate());
        }
        if (experienceDTO.getCurrent() != null) {
            experience.setCurrent(experienceDTO.getCurrent());
        }
        if (experienceDTO.getDescription() != null) {
            experience.setDescription(experienceDTO.getDescription());
        }
        if (experienceDTO.getResponsibilities() != null) {
            experience.setResponsibilities(experienceDTO.getResponsibilities());
        }
        
        Experience updatedExperience = experienceRepository.save(experience);
        return convertToDTO(updatedExperience);
    }

    @Override
    public void deleteExperience(UUID experienceId) {
        log.info("ExperienceServiceImpl :: deleteExperience :: deleting experience: {}", experienceId);
        if (!experienceRepository.existsById(experienceId)) {
            log.error("ExperienceServiceImpl :: deleteExperience :: experience not found: {}", experienceId);
            throw new EntityNotFoundException("Experience not found with id: " + experienceId);
        }
        experienceRepository.deleteById(experienceId);
        log.info("ExperienceServiceImpl :: deleteExperience :: successfully deleted experience: {}", experienceId);
    }

    private ExperienceDTO convertToDTO(Experience experience) {
        ExperienceDTO experienceDTO = new ExperienceDTO();
        BeanUtils.copyProperties(experience, experienceDTO);
        return experienceDTO;
    }
}
