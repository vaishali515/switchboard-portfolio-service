package com.SwitchBoard.PortfolioService.Service.Portfolio.Impl;

import com.SwitchBoard.PortfolioService.DTO.Certificate.CertificateRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Certificate.CertificateResponseDTO;
import com.SwitchBoard.PortfolioService.Entity.Certificate;
import com.SwitchBoard.PortfolioService.Entity.Portfolio;
import com.SwitchBoard.PortfolioService.Repository.CertificateRepository;
import com.SwitchBoard.PortfolioService.Repository.PortfolioRepository;
import com.SwitchBoard.PortfolioService.Service.Portfolio.FileService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CertificateServiceImplTest {

    @Mock
    private CertificateRepository certificateRepository;

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private FileService fileService;

    @InjectMocks
    private CertificateServiceImpl certificateService;

    private UUID portfolioId;
    private UUID certificateId;
    private Portfolio portfolio;
    private Certificate certificate;
    private CertificateRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        portfolioId = UUID.randomUUID();
        certificateId = UUID.randomUUID();
        
        portfolio = new Portfolio();
        portfolio.setId(portfolioId);
        
        certificate = new Certificate();
        certificate.setId(certificateId);
        certificate.setTitle("AWS Certified");
        certificate.setIssuer("Amazon");
        certificate.setIssueDate(YearMonth.of(2024, 1));
        certificate.setPortfolio(portfolio);
        
        requestDTO = new CertificateRequestDTO();
        requestDTO.setTitle("AWS Certified");
        requestDTO.setIssuer("Amazon");
        requestDTO.setIssueDate(YearMonth.of(2024, 1));
    }

    @Test
    void testGetAllCertificatesByPortfolioId_Success() {
        when(portfolioRepository.existsById(portfolioId)).thenReturn(true);
        when(certificateRepository.findByPortfolioId(portfolioId)).thenReturn(Arrays.asList(certificate));

        List<CertificateResponseDTO> result = certificateService.getAllCertificatesByPortfolioId(portfolioId);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllCertificatesByPortfolioId_PortfolioNotFound() {
        when(portfolioRepository.existsById(portfolioId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> 
            certificateService.getAllCertificatesByPortfolioId(portfolioId));
    }

    @Test
    void testGetCertificateById_Success() {
        when(certificateRepository.findById(certificateId)).thenReturn(Optional.of(certificate));

        CertificateResponseDTO result = certificateService.getCertificateById(certificateId);

        assertNotNull(result);
    }

    @Test
    void testGetCertificateById_NotFound() {
        when(certificateRepository.findById(certificateId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> 
            certificateService.getCertificateById(certificateId));
    }

    @Test
    void testCreateCertificate_Success() throws IOException {
        when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.of(portfolio));
        when(certificateRepository.save(any(Certificate.class))).thenReturn(certificate);

        CertificateResponseDTO result = certificateService.createCertificate(portfolioId, requestDTO);

        assertNotNull(result);
    }

    @Test
    void testCreateCertificate_PortfolioNotFound() {
        when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> 
            certificateService.createCertificate(portfolioId, requestDTO));
    }

    @Test
    void testUpdateCertificate_Success() throws IOException {
        when(certificateRepository.findById(certificateId)).thenReturn(Optional.of(certificate));
        when(certificateRepository.save(any(Certificate.class))).thenReturn(certificate);

        CertificateResponseDTO result = certificateService.updateCertificate(certificateId, requestDTO);

        assertNotNull(result);
    }

    @Test
    void testUpdateCertificate_NotFound() {
        when(certificateRepository.findById(certificateId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> 
            certificateService.updateCertificate(certificateId, requestDTO));
    }

    @Test
    void testDeleteCertificate_Success() throws IOException {
        when(certificateRepository.findById(certificateId)).thenReturn(Optional.of(certificate));
        doNothing().when(certificateRepository).delete(certificate);

        certificateService.deleteCertificate(certificateId);

        verify(certificateRepository).delete(certificate);
    }

    @Test
    void testDeleteCertificate_NotFound() {
        when(certificateRepository.findById(certificateId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> 
            certificateService.deleteCertificate(certificateId));
    }
}
