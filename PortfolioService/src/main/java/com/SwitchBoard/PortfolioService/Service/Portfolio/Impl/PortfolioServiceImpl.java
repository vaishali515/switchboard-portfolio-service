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
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final FileService fileService;

    // ---------------------- GET BY EMAIL ----------------------
    @Override
    public PortfolioResponseDTO getPortfolioByEmailId(String emailId) {
        Portfolio portfolio = portfolioRepository.findByEmailId(emailId)
                .orElseThrow(() -> new EntityNotFoundException("Portfolio not found with email: " + emailId));
        return convertToDTO(portfolio);
    }

    // ---------------------- GET BY ID ----------------------
    @Override
    public PortfolioResponseDTO getPortfolioById(UUID portfolioId) {
        log.info("Inside getPortfolio by id method");
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new EntityNotFoundException("Portfolio not found with ID: " + portfolioId));
        log.info("After Fetching portfolio");
        return convertToDTO(portfolio);
    }

    // ---------------------- CREATE ----------------------
    @Override
    public PortfolioResponseDTO createPortfolio(PortfolioRequestDTO portfolioRequest) throws IOException {
        log.info("Creating new portfolio for email: {}", portfolioRequest.getEmailId());

        Optional<Portfolio> existing = portfolioRepository.findByEmailId(portfolioRequest.getEmailId());
        if (existing.isPresent()) {
            throw new IllegalStateException("Portfolio already exists for this email");
        }

        Portfolio portfolio = new Portfolio();
        BeanUtils.copyProperties(portfolioRequest, portfolio, "profileImage", "resume");

        // Upload and set files
        uploadFilesAndSetUrls(portfolioRequest, portfolio);

        Portfolio saved = portfolioRepository.save(portfolio);
        log.info("Portfolio created successfully for: {}", portfolioRequest.getEmailId());
        return convertToDTO(saved);
    }

    // ---------------------- UPDATE ----------------------
    @Override
    public PortfolioResponseDTO updatePortfolio(UUID id, PortfolioRequestDTO portfolioRequest) throws IOException {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Portfolio not found with ID: " + id));

        // Update only changed fields
        if (portfolioRequest.getFullName() != null) portfolio.setFullName(portfolioRequest.getFullName());
        if (portfolioRequest.getBio() != null) portfolio.setBio(portfolioRequest.getBio());
        if (portfolioRequest.getSocialLinks() != null) portfolio.setSocialLinks(portfolioRequest.getSocialLinks());
        if (portfolioRequest.getOverview() != null) portfolio.setOverview(portfolioRequest.getOverview());

        uploadFilesAndSetUrls(portfolioRequest, portfolio);

        Portfolio updated = portfolioRepository.save(portfolio);
        return convertToDTO(updated);
    }

    // ---------------------- DELETE ----------------------
    @Override
    public void deletePortfolio(UUID id) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Portfolio not found with ID: " + id));

        if (portfolio.getProfileImageUrl() != null)
            fileService.deleteImage(portfolio.getProfileImageUrl());

        if (portfolio.getResumeLink() != null)
            fileService.deleteImage(portfolio.getResumeLink());

        portfolioRepository.delete(portfolio);
        log.info("Portfolio deleted with ID: {}", id);
    }

    // ---------------------- HELPER ----------------------
    private void uploadFilesAndSetUrls(PortfolioRequestDTO request, Portfolio portfolio) throws IOException {
        MultipartFile image = request.getProfileImage();
        MultipartFile resume = request.getResume();

        if (image != null && !image.isEmpty()) {
            log.info("Uploading profile image for {}", request.getEmailId());
            String imageUrl = fileService.uploadImage("portfolio-service", image);
            portfolio.setProfileImageUrl(imageUrl);
        }

        if (resume != null && !resume.isEmpty()) {
            log.info("Uploading resume for {}", request.getEmailId());
            String resumeUrl = fileService.uploadDocument("portfolio-service", resume);
            portfolio.setResumeLink(resumeUrl);
        }
    }

    private PortfolioResponseDTO convertToDTO(Portfolio portfolio) {
        PortfolioResponseDTO dto = new PortfolioResponseDTO();
        BeanUtils.copyProperties(portfolio, dto);
        return dto;
    }


}
