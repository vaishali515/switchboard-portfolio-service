package com.SwitchBoard.PortfolioService.Service.Portfolio.Impl;


import com.SwitchBoard.PortfolioService.Entity.Certificate;
import com.SwitchBoard.PortfolioService.Entity.Portfolio;
import com.SwitchBoard.PortfolioService.Repository.CertificateRepository;
import com.SwitchBoard.PortfolioService.Repository.PortfolioRepository;
import com.SwitchBoard.PortfolioService.Service.Portfolio.CertificateService;
import com.SwitchBoard.PortfolioService.Service.Portfolio.FileService;
import com.SwitchBoard.PortfolioService.Util.FileUploadUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final PortfolioRepository portfolioRepository;
    private final FileUploadUtil fileUploadUtil;
    private final FileService fileService;


    @Override
    public List<CertificateDTO> getAllCertificatesByPortfolioId(UUID portfolioId) {
        log.info("CertificateServiceImpl :: getAllCertificatesByPortfolioId :: fetching certificates for portfolio: {}", portfolioId);
        // Verify portfolio exists
        if (!portfolioRepository.existsById(portfolioId)) {
            log.error("CertificateServiceImpl :: getAllCertificatesByPortfolioId :: portfolio not found: {}", portfolioId);
            throw new EntityNotFoundException("Portfolio not found with id: " + portfolioId);
        }
        
        return certificateRepository.findByPortfolioId(portfolioId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CertificateDTO getCertificateById(UUID id) {
        log.info("CertificateServiceImpl :: getCertificateById :: fetching certificate: {}", id);
        return certificateRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> {
                    log.error("CertificateServiceImpl :: getCertificateById :: certificate not found: {}", id);
                    return new EntityNotFoundException("Certificate not found with id: " + id);
                });
    }

    @Override
    public CertificateDTO createCertificate(UUID portfolioId, CertificateDTO certificateDTO) {
        log.info("CertificateServiceImpl :: createCertificate :: creating certificate for portfolio: {}, data: {}", portfolioId, certificateDTO);
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> {
                    log.error("CertificateServiceImpl :: createCertificate :: portfolio not found: {}", portfolioId);
                    return new EntityNotFoundException("Portfolio not found with id: " + portfolioId);
                });
        
        Certificate certificate = new Certificate();
        BeanUtils.copyProperties(certificateDTO, certificate, "id", "createdAt", "updatedAt");
        certificate.setPortfolio(portfolio);
        
        Certificate savedCertificate = certificateRepository.save(certificate);
        return convertToDTO(savedCertificate);
    }

    @Override
    public CertificateDTO updateCertificate(UUID id, CertificateDTO certificateDTO, MultipartFile newImage) throws IOException {
        Certificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Certificate not found with id: " + id));


        // Handle new image upload
        if (newImage != null && !newImage.isEmpty()) {
            // Delete old image from S3 if exists
            if (certificate.getCertificateImageUrl() != null && !certificate.getCertificateImageUrl().isEmpty()) {
                log.info("CertificateServiceImpl :: updateCertificate :: deleting old image from S3: {}", certificate.getCertificateImageUrl());
                fileService.deleteImage(certificate.getCertificateImageUrl());
            }

            // Upload new image
            String newImageUrl = fileService.uploadImage("portfolio-service", newImage);
            log.info("CertificateServiceImpl :: updateCertificate :: uploaded new image to S3: {}", newImageUrl);
            certificateDTO.setCertificateImageUrl(newImageUrl);
        } else {
            // Keep old image if no new image uploaded
            certificateDTO.setCertificateImageUrl(certificate.getCertificateImageUrl());
        }

        // Update only non-null fields
        if (certificateDTO.getTitle() != null) {
            certificate.setTitle(certificateDTO.getTitle());
        }
        if (certificateDTO.getIssuer() != null) {
            certificate.setIssuer(certificateDTO.getIssuer());
        }
        if (certificateDTO.getIssueDate() != null) {
            certificate.setIssueDate(certificateDTO.getIssueDate());
        }
        if (certificateDTO.getExpiryDate() != null) {
            certificate.setExpiryDate(certificateDTO.getExpiryDate());
        }
        if (certificateDTO.getCredentialId() != null) {
            certificate.setCredentialId(certificateDTO.getCredentialId());
        }
        if (certificateDTO.getCredentialUrl() != null) {
            certificate.setCredentialUrl(certificateDTO.getCredentialUrl());
        }
        if (certificateDTO.getDescription() != null) {
            certificate.setDescription(certificateDTO.getDescription());
        }

        certificate.setCertificateImageUrl(certificateDTO.getCertificateImageUrl());

        
        Certificate updatedCertificate = certificateRepository.save(certificate);
        return convertToDTO(updatedCertificate);
    }



    @Override
    public void deleteCertificate(UUID id) {
        Certificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> {
                    return new RuntimeException("Certificate not found");
                });

        // Delete image from S3 if exists
        if (certificate.getCertificateImageUrl() != null && !certificate.getCertificateImageUrl().isEmpty()) {
            try {
                log.info("CertificateServiceImpl :: deleteCertificate :: deleting image from S3: {}", certificate.getCertificateImageUrl());
                fileService.deleteImage(certificate.getCertificateImageUrl());
                log.info("CertificateServiceImpl :: deleteCertificate :: deleted image from S3: {}", certificate.getCertificateImageUrl());
            } catch (Exception e) {
                log.error("CertificateServiceImpl :: deleteCertificate :: failed to delete image from S3: {}", e.getMessage());
                // Optional: you can throw exception if you want to fail delete if image deletion fails
            }
        }

        // Delete DB record
        certificateRepository.delete(certificate);
        log.info("CertificateServiceImpl :: deleteCertificate :: deleted certificate with id: {}", id);

    }

    /**
     * Convert Certificate entity to DTO
     */
    private CertificateDTO convertToDTO(Certificate certificate) {
        CertificateDTO certificateDTO = new CertificateDTO();
        BeanUtils.copyProperties(certificate, certificateDTO);
        return certificateDTO;
    }
}
