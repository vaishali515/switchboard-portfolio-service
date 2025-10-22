package com.SwitchBoard.PortfolioService.Service.Portfolio.Impl;

import com.SwitchBoard.PortfolioService.DTO.Portfolio.PortfolioRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Portfolio.PortfolioResponseDTO;
import com.SwitchBoard.PortfolioService.Entity.Portfolio;
import com.SwitchBoard.PortfolioService.Repository.PortfolioRepository;
import com.SwitchBoard.PortfolioService.Service.Portfolio.FileService;
import com.SwitchBoard.PortfolioService.Service.Portfolio.PortfolioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public PortfolioResponseDTO createPortfolio(PortfolioRequestDTO portfolioRequest) throws IllegalStateException {
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
        portfolio.setBio(portfolioRequest.getBio());
//        portfolio.setProfileImageUrl(portfolioRequest.getProfileImageUrl());
        portfolio.setSocialLinks(portfolioRequest.getSocialLinks());
        portfolio.setOverview(portfolioRequest.getOverview());
        log.info( "PortfolioServiceImpl :: createPortfolio :: saving new portfolio for user: {}", portfolioRequest.getEmailId());
        Portfolio savedPortfolio = portfolioRepository.save(portfolio);
        log.info( "PortfolioServiceImpl :: createPortfolio :: successfully created portfolio for user: {}", portfolioRequest.getEmailId());
        return convertToDTO(savedPortfolio);
    }



    @Override
    public PortfolioResponseDTO updatePortfolio(UUID id, PortfolioRequestDTO portfolioRequest, MultipartFile newImage) throws IOException {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Portfolio not found with id: " + id));


        // Handle new image upload
        if (newImage != null && !newImage.isEmpty()) {
            // Delete old image from S3 if exists
            if (portfolio.getProfileImageUrl() != null && !portfolio.getProfileImageUrl().isEmpty()) {
                fileService.deleteImage(portfolio.getProfileImageUrl());
            }

            // Upload new image
            String newImageUrl = fileService.uploadImage("portfolio-service", newImage);
            portfolio.setProfileImageUrl(newImageUrl);
        } else {
            // Keep old image if no new image uploaded
            portfolio.setProfileImageUrl(portfolio.getProfileImageUrl());
        }
        
        // Update only non-null fields
        if (portfolioRequest.getFullName() != null) {
            portfolio.setFullName(portfolioRequest.getFullName());
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

        // Delete DB record
        portfolioRepository.delete(portfolio);

    }

    @Override
    public PortfolioResponseDTO updateOverview(UUID portfolioId, String overview) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new EntityNotFoundException("Portfolio not found with id: " + portfolioId));
        
        portfolio.setOverview(overview);
        Portfolio updatedPortfolio = portfolioRepository.save(portfolio);
        return convertToDTO(updatedPortfolio);
    }

    private PortfolioResponseDTO convertToDTO(Portfolio portfolio) {
        PortfolioResponseDTO portfolioDTO = new PortfolioResponseDTO();
        BeanUtils.copyProperties(portfolio, portfolioDTO);
        // Note: We're not including nested collections here to avoid circular references
        // These should be loaded separately via their respective services
        return portfolioDTO;
    }
}
