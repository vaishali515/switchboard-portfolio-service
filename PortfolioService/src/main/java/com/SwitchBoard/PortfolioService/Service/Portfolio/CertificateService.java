package com.SwitchBoard.PortfolioService.Service.Portfolio;

import com.SwitchBoard.PortfolioService.DTO.Certificate.CertificateRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Certificate.CertificateResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface CertificateService {
    

    /**
     * Get all certificates for a portfolio
     */
    List<CertificateResponseDTO> getAllCertificatesByPortfolioId(UUID portfolioId);
    
    /**
     * Get certificate by ID
     */
    CertificateResponseDTO getCertificateById(UUID id);
    
    /**
     * Create a new certificate for a portfolio
     */
    CertificateResponseDTO createCertificate(UUID portfolioId, CertificateRequestDTO certificateDTO) throws IOException;
    
    /**
     * Update an existing certificate
     */
    CertificateResponseDTO updateCertificate(UUID id, CertificateRequestDTO certificateDTO) throws IOException;
    
    /**
     * Delete a certificate by ID
     */
    void deleteCertificate(UUID id);
    
    /**
     * Upload a certificate image
     */
//    String uploadCertificateImage(UUID certificateId, MultipartFile file) throws IOException;
}
