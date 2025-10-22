package com.SwitchBoard.PortfolioService.Service.Portfolio.Impl;

import com.SwitchBoard.PortfolioService.DTO.Education.EducationRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Education.EducationResponseDTO;
import com.SwitchBoard.PortfolioService.Entity.Education;
import com.SwitchBoard.PortfolioService.Entity.Portfolio;
import com.SwitchBoard.PortfolioService.Repository.EducationRepository;
import com.SwitchBoard.PortfolioService.Repository.PortfolioRepository;
import com.SwitchBoard.PortfolioService.Service.Portfolio.EducationService;
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
public class EducationServiceImpl implements EducationService {

    private final EducationRepository educationRepository;
    private final PortfolioRepository portfolioRepository;


    @Override
    public List<EducationResponseDTO> getAllEducationsByPortfolioId(UUID portfolioId) {
        log.info("EducationServiceImpl :: getAllEducationsByPortfolioId :: fetching educations for portfolio: {}", portfolioId);
        // Verify portfolio exists
        if (!portfolioRepository.existsById(portfolioId)) {
            log.error("EducationServiceImpl :: getAllEducationsByPortfolioId :: portfolio not found: {}", portfolioId);
            throw new EntityNotFoundException("Portfolio not found with id: " + portfolioId);
        }
        
        return educationRepository.findByPortfolioId(portfolioId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EducationResponseDTO getEducationById(UUID id) {
        log.info("EducationServiceImpl :: getEducationById :: fetching education: {}", id);
        return educationRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> {
                    log.error("EducationServiceImpl :: getEducationById :: education not found: {}", id);
                    return new EntityNotFoundException("Education not found with id: " + id);
                });
    }

    @Override
    public EducationResponseDTO createEducation(UUID portfolioId, EducationRequestDTO educationDTO) {
        log.info("EducationServiceImpl :: createEducation :: creating education for portfolio: {}, data: {}", portfolioId, educationDTO);
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> {
                    log.error("EducationServiceImpl :: createEducation :: portfolio not found: {}", portfolioId);
                    return new EntityNotFoundException("Portfolio not found with id: " + portfolioId);
                });
        
        Education education = new Education();
        BeanUtils.copyProperties(educationDTO, education, "id", "createdAt", "updatedAt");
        education.setPortfolio(portfolio);
        
        Education savedEducation = educationRepository.save(education);
        return convertToDTO(savedEducation);
    }

    @Override
    public EducationResponseDTO updateEducation(UUID id, EducationRequestDTO educationDTO) {
        log.info("EducationServiceImpl :: updateEducation :: updating education: {}, data: {}", id, educationDTO);
        Education education = educationRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("EducationServiceImpl :: updateEducation :: education not found: {}", id);
                    return new EntityNotFoundException("Education not found with id: " + id);
                });
        
        // Update only non-null fields
        if (educationDTO.getInstitution() != null) {
            education.setInstitution(educationDTO.getInstitution());
        }
        if (educationDTO.getDegree() != null) {
            education.setDegree(educationDTO.getDegree());
        }
        if (educationDTO.getFieldOfStudy() != null) {
            education.setFieldOfStudy(educationDTO.getFieldOfStudy());
        }
        if (educationDTO.getGrade() != null) {
            education.setGrade(educationDTO.getGrade());
        }
        if (educationDTO.getStartDate() != null) {
            education.setStartDate(educationDTO.getStartDate());
        }
        if (educationDTO.getEndDate() != null) {
            education.setEndDate(educationDTO.getEndDate());
        }
        if (educationDTO.getOngoing() != null) {
            education.setOngoing(educationDTO.getOngoing());
        }
        if (educationDTO.getDescription() != null) {
            education.setDescription(educationDTO.getDescription());
        }
        
        Education updatedEducation = educationRepository.save(education);
        return convertToDTO(updatedEducation);
    }

    @Override
    public void deleteEducation(UUID id) {
        log.info("EducationServiceImpl :: deleteEducation :: deleting education: {}", id);
        if (!educationRepository.existsById(id)) {
            log.error("EducationServiceImpl :: deleteEducation :: education not found: {}", id);
            throw new EntityNotFoundException("Education not found with id: " + id);
        }
        educationRepository.deleteById(id);
        log.info("EducationServiceImpl :: deleteEducation :: successfully deleted education: {}", id);
    }
    

    private EducationResponseDTO convertToDTO(Education education) {
        EducationResponseDTO educationDTO = new EducationResponseDTO();
        BeanUtils.copyProperties(education, educationDTO);
        return educationDTO;
    }
}
