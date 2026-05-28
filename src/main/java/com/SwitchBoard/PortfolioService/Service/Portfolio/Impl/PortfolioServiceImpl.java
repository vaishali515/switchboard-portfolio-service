package com.SwitchBoard.PortfolioService.Service.Portfolio.Impl;

import com.SwitchBoard.PortfolioService.DTO.Achievement.AchievementResponseDTO;
import com.SwitchBoard.PortfolioService.DTO.Certificate.CertificateResponseDTO;
import com.SwitchBoard.PortfolioService.DTO.Education.EducationResponseDTO;
import com.SwitchBoard.PortfolioService.DTO.Experience.ExperienceResponseDTO;
import com.SwitchBoard.PortfolioService.DTO.Portfolio.PortfolioRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Portfolio.PortfolioResponseDTO;
import com.SwitchBoard.PortfolioService.DTO.Project.ProjectResponseDTO;
import com.SwitchBoard.PortfolioService.DTO.Skill.SkillResponseDTO;
import com.SwitchBoard.PortfolioService.Entity.*;
import com.SwitchBoard.PortfolioService.Repository.PortfolioRepository;
import com.SwitchBoard.PortfolioService.Service.Portfolio.FileService;
import com.SwitchBoard.PortfolioService.Service.Portfolio.PortfolioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final FileService fileService;


    @Override
    public PortfolioResponseDTO getPortfolioByEmailId(String emailId) {
        return portfolioRepository.findByEmailId(emailId)
                .map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Portfolio not found with id: " + emailId));
    }

    @Override
    public PortfolioResponseDTO getPortfolioById(UUID portfolioId) {
        return portfolioRepository.findById(portfolioId)
                .map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Portfolio not found for user with id: " + portfolioId));
    }

    @Override
    public PortfolioResponseDTO createPortfolio(PortfolioRequestDTO portfolioRequest) throws IOException {
        log.info( "PortfolioServiceImpl :: createPortfolio :: creating portfolio for user: {}", portfolioRequest.getEmailId());
        // Check if a portfolio already exists for this user
        if (portfolioRepository.findByEmailId(portfolioRequest.getEmailId()).isPresent()) {
            log.info( "PortfolioServiceImpl :: createPortfolio :: portfolio already exists for user: {}", portfolioRequest.getEmailId());
            throw new IllegalStateException("A portfolio already exists for this user");
        }
        log.info("PortfolioServiceImpl :: createPortfolio :: no existing portfolio found, proceeding to create new one");
        Portfolio portfolio = new Portfolio();
        portfolio.setEmailId(portfolioRequest.getEmailId());
        portfolio.setFullName(portfolioRequest.getFullName());
        portfolio.setContactNumber(portfolioRequest.getContactNumber());
        portfolio.setBio(portfolioRequest.getBio());
        portfolio.setSocialLinks(portfolioRequest.getSocialLinks());
        portfolio.setOverview(portfolioRequest.getOverview());
        portfolio.setLeetcodeLink(portfolioRequest.getLeetcodeLink());
        portfolio.setGithubLink(portfolioRequest.getGithubLink());
        portfolio.setLinkedInLink(portfolioRequest.getLinkedInLink());
        portfolio.setTwitterLink(portfolioRequest.getTwitterLink());
        portfolio.setPersonalWebsiteLink(portfolioRequest.getPersonalWebsiteLink());
        
        // Handle profile image upload
        if (portfolioRequest.getProfileImage() != null && !portfolioRequest.getProfileImage().isEmpty()) {
            String profileImageUrl = fileService.uploadImage("portfolio-service", portfolioRequest.getProfileImage());
            portfolio.setProfileImageUrl(profileImageUrl);
            log.info("PortfolioServiceImpl :: createPortfolio :: profile image uploaded successfully");
        }
        
        // Handle resume upload
        if (portfolioRequest.getResume() != null && !portfolioRequest.getResume().isEmpty()) {
            String resumeUrl = fileService.uploadImage("portfolio-service", portfolioRequest.getResume());
            portfolio.setResumeLink(resumeUrl);
            log.info("PortfolioServiceImpl :: createPortfolio :: resume uploaded successfully");
        }
        
        log.info( "PortfolioServiceImpl :: createPortfolio :: saving new portfolio for user: {}", portfolioRequest.getEmailId());
        Portfolio savedPortfolio = portfolioRepository.save(portfolio);
        log.info( "PortfolioServiceImpl :: createPortfolio :: successfully created portfolio for user: {}", portfolioRequest.getEmailId());
        return convertToDTO(savedPortfolio);
    }



    @Override
    public PortfolioResponseDTO updatePortfolio(UUID id, PortfolioRequestDTO portfolioRequest) throws IOException {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Portfolio not found with id: " + id));

        // Handle new image upload
        if (portfolioRequest.getProfileImage() != null && !portfolioRequest.getProfileImage().isEmpty()) {
            // Delete old image from S3 if exists
            if (portfolio.getProfileImageUrl() != null && !portfolio.getProfileImageUrl().isEmpty()) {
                fileService.deleteImage(portfolio.getProfileImageUrl());
            }
            // Upload new image
            String newImageUrl = fileService.uploadImage("portfolio-service", portfolioRequest.getProfileImage());
            portfolio.setProfileImageUrl(newImageUrl);
        }
        
        // Handle resume upload
        if (portfolioRequest.getResume() != null && !portfolioRequest.getResume().isEmpty()) {
            // Delete old resume from S3 if exists
            if (portfolio.getResumeLink() != null && !portfolio.getResumeLink().isEmpty()) {
                fileService.deleteImage(portfolio.getResumeLink());
            }
            // Upload new resume
            String newResumeUrl = fileService.uploadImage("portfolio-service", portfolioRequest.getResume());
            portfolio.setResumeLink(newResumeUrl);
        }
        
        // Update only non-null fields
        if (portfolioRequest.getFullName() != null) {
            portfolio.setFullName(portfolioRequest.getFullName());
        }
        if (portfolioRequest.getContactNumber() != null) {
            portfolio.setContactNumber(portfolioRequest.getContactNumber());
        }
        if (portfolioRequest.getBio() != null) {
            portfolio.setBio(portfolioRequest.getBio());
        }
        if (portfolioRequest.getSocialLinks() != null) {
            portfolio.setSocialLinks(portfolioRequest.getSocialLinks());
        }
        if (portfolioRequest.getOverview() != null) {
            portfolio.setOverview(portfolioRequest.getOverview());
        }
        if (portfolioRequest.getLeetcodeLink() != null) {
            portfolio.setLeetcodeLink(portfolioRequest.getLeetcodeLink());
        }
        if (portfolioRequest.getGithubLink() != null) {
            portfolio.setGithubLink(portfolioRequest.getGithubLink());
        }
        if (portfolioRequest.getLinkedInLink() != null) {
            portfolio.setLinkedInLink(portfolioRequest.getLinkedInLink());
        }
        if (portfolioRequest.getTwitterLink() != null) {
            portfolio.setTwitterLink(portfolioRequest.getTwitterLink());
        }
        if (portfolioRequest.getPersonalWebsiteLink() != null) {
            portfolio.setPersonalWebsiteLink(portfolioRequest.getPersonalWebsiteLink());
        }

        Portfolio updatedPortfolio = portfolioRepository.save(portfolio);
        return convertToDTO(updatedPortfolio);
    }

    @Override
    public void deletePortfolio(UUID id) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> {
                    return new RuntimeException("Portfolio not found");
                });

        // Delete image from S3 if exists
        if (portfolio.getProfileImageUrl() != null && !portfolio.getProfileImageUrl().isEmpty()) {
            try {
                fileService.deleteImage(portfolio.getProfileImageUrl());
            } catch (Exception e) {
                // Optional: you can throw exception if you want to fail delete if image deletion fails
            }
        }
        
        // Delete resume from S3 if exists
        if (portfolio.getResumeLink() != null && !portfolio.getResumeLink().isEmpty()) {
            try {
                fileService.deleteImage(portfolio.getResumeLink());
            } catch (Exception e) {
                // Optional: you can throw exception if you want to fail delete if resume deletion fails
            }
        }

        // Delete DB record
        portfolioRepository.delete(portfolio);

    }

    private PortfolioResponseDTO convertToDTO(Portfolio portfolio) {
        PortfolioResponseDTO portfolioDTO = new PortfolioResponseDTO();
        BeanUtils.copyProperties(portfolio, portfolioDTO);
        
        // Map nested collections to their full DTO representations
        // Note: We exclude the 'portfolio' field from nested entities to avoid circular references
        portfolioDTO.setSkills(portfolio.getSkills() != null ? 
            portfolio.getSkills().stream().map(this::convertSkillToDTO).toList() : 
            Collections.emptyList());
            
        portfolioDTO.setProjects(portfolio.getProjects() != null ? 
            portfolio.getProjects().stream().map(this::convertProjectToDTO).toList() : 
            Collections.emptyList());
            
        portfolioDTO.setEducations(portfolio.getEducations() != null ? 
            portfolio.getEducations().stream().map(this::convertEducationToDTO).toList() : 
            Collections.emptyList());
            
        portfolioDTO.setExperiences(portfolio.getExperiences() != null ? 
            portfolio.getExperiences().stream().map(this::convertExperienceToDTO).toList() : 
            Collections.emptyList());
            
        portfolioDTO.setCertificates(portfolio.getCertificates() != null ? 
            portfolio.getCertificates().stream().map(this::convertCertificateToDTO).toList() : 
            Collections.emptyList());
            
        portfolioDTO.setAchievements(portfolio.getAchievements() != null ? 
            portfolio.getAchievements().stream().map(this::convertAchievementToDTO).toList() : 
            Collections.emptyList());
            
        return portfolioDTO;
    }
    
    // Helper methods to convert nested entities to DTOs
    private SkillResponseDTO convertSkillToDTO(Skill skill) {
        SkillResponseDTO dto = new SkillResponseDTO();
        BeanUtils.copyProperties(skill, dto);
        return dto;
    }
    
    private ProjectResponseDTO convertProjectToDTO(Project project) {
        ProjectResponseDTO dto = new ProjectResponseDTO();
        BeanUtils.copyProperties(project, dto);
        // Handle null collections
        if (dto.getTechnologies() == null) dto.setTechnologies(Collections.emptyList());
        if (dto.getFeatures() == null) dto.setFeatures(Collections.emptyList());
        return dto;
    }
    
    private EducationResponseDTO convertEducationToDTO(Education education) {
        EducationResponseDTO dto = new EducationResponseDTO();
        BeanUtils.copyProperties(education, dto);
        return dto;
    }
    
    private ExperienceResponseDTO convertExperienceToDTO(Experience experience) {
        ExperienceResponseDTO dto = new ExperienceResponseDTO();
        BeanUtils.copyProperties(experience, dto);
        return dto;
    }
    
    private CertificateResponseDTO convertCertificateToDTO(Certificate certificate) {
        CertificateResponseDTO dto = new CertificateResponseDTO();
        BeanUtils.copyProperties(certificate, dto);
        return dto;
    }
    
    private AchievementResponseDTO convertAchievementToDTO(Achievement achievement) {
        AchievementResponseDTO dto = new AchievementResponseDTO();
        BeanUtils.copyProperties(achievement, dto);
        return dto;
    }
}
