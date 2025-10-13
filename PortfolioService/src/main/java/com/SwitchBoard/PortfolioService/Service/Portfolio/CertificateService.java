package com.SwitchBoard.PortfolioService.Service.Portfolio;

import com.SwitchBoard.PortfolioService.DTO.CertificateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface CertificateService {
    

    /**
     * Get all certificates for a portfolio
     */
    List<CertificateDTO> getAllCertificatesByPortfolioId(UUID portfolioId);
    
    /**
     * Get certificate by ID
     */
    CertificateDTO getCertificateById(UUID id);
    
    /**
     * Create a new certificate for a portfolio
     */
    CertificateDTO createCertificate(UUID portfolioId, CertificateDTO certificateDTO);
    
    /**
     * Update an existing certificate
     */
    CertificateDTO updateCertificate(UUID id, CertificateDTO certificateDTO, MultipartFile newImage) throws IOException;
    
    /**
     * Delete a certificate by ID
     */
    void deleteCertificate(UUID id);
    
    /**
     * Upload a certificate image
     */
//    String uploadCertificateImage(UUID certificateId, MultipartFile file) throws IOException;
}
