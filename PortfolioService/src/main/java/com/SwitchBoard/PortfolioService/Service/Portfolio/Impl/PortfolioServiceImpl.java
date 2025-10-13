package com.SwitchBoard.PortfolioService.Service.Portfolio.Impl;

import com.SwitchBoard.PortfolioService.DTO.PortfolioDTO;
import com.SwitchBoard.PortfolioService.DTO.PortfolioRequest;
import com.SwitchBoard.PortfolioService.Entity.Portfolio;
import com.SwitchBoard.PortfolioService.Entity.Project;
import com.SwitchBoard.PortfolioService.Repository.PortfolioRepository;
import com.SwitchBoard.PortfolioService.Service.Portfolio.FileService;
import com.SwitchBoard.PortfolioService.Service.Portfolio.PortfolioService;
import com.SwitchBoard.PortfolioService.Util.FileUploadUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final FileService fileService;


    @Override
    public PortfolioDTO getPortfolioByEmailId(String emailId) {
        return portfolioRepository.findByEmailId(emailId)
                .map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Portfolio not found with id: " + emailId));
    }

    @Override
    public PortfolioDTO getPortfolioById(UUID portfolioId) {
        return portfolioRepository.findById(portfolioId)
                .map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Portfolio not found for user with id: " + portfolioId));
    }

    @Override
    public PortfolioDTO createPortfolio(PortfolioRequest portfolioRequest) {
        // Check if a portfolio already exists for this user
        if (portfolioRepository.findByEmailId(portfolioRequest.getEmailId()).isPresent()) {
            throw new IllegalStateException("A portfolio already exists for this user");
        }
        
        Portfolio portfolio = new Portfolio();
        portfolio.setEmailId(portfolioRequest.getEmailId());
        portfolio.setFullName(portfolioRequest.getFullName());
        portfolio.setBio(portfolioRequest.getBio());
        portfolio.setProfileImageUrl(portfolioRequest.getProfileImageUrl());
        portfolio.setSocialLinks(portfolioRequest.getSocialLinks());
        portfolio.setOverview(portfolioRequest.getOverview());
        
        Portfolio savedPortfolio = portfolioRepository.save(portfolio);
        return convertToDTO(savedPortfolio);
    }



    @Override
    public PortfolioDTO updatePortfolio(UUID id, PortfolioRequest portfolioRequest, MultipartFile newImage) throws IOException {
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
            portfolioRequest.setProfileImageUrl(newImageUrl);
        } else {
            // Keep old image if no new image uploaded
            portfolioRequest.setProfileImageUrl(portfolio.getProfileImageUrl());
        }
        
        // Update only non-null fields
        if (portfolioRequest.getFullName() != null) {
            portfolio.setFullName(portfolioRequest.getFullName());
        }
        if (portfolioRequest.getBio() != null) {
            portfolio.setBio(portfolioRequest.getBio());
        }
        if (portfolioRequest.getProfileImageUrl() != null) {
            portfolio.setProfileImageUrl(portfolioRequest.getProfileImageUrl());
        }
        if (portfolioRequest.getSocialLinks() != null) {
            portfolio.setSocialLinks(portfolioRequest.getSocialLinks());
        }
        if (portfolioRequest.getOverview() != null) {
            portfolio.setOverview(portfolioRequest.getOverview());
        }

        portfolio.setProfileImageUrl(portfolioRequest.getProfileImageUrl());

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
    public PortfolioDTO updateOverview(UUID portfolioId, String overview) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new EntityNotFoundException("Portfolio not found with id: " + portfolioId));
        
        portfolio.setOverview(overview);
        Portfolio updatedPortfolio = portfolioRepository.save(portfolio);
        return convertToDTO(updatedPortfolio);
    }

    private PortfolioDTO convertToDTO(Portfolio portfolio) {
        PortfolioDTO portfolioDTO = new PortfolioDTO();
        BeanUtils.copyProperties(portfolio, portfolioDTO);
        // Note: We're not including nested collections here to avoid circular references
        // These should be loaded separately via their respective services
        return portfolioDTO;
    }
}
