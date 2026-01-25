package com.SwitchBoard.PortfolioService.Controller;

import com.SwitchBoard.PortfolioService.DTO.Certificate.CertificateRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Certificate.CertificateResponseDTO;
import com.SwitchBoard.PortfolioService.DTO.ApiResponse;
import com.SwitchBoard.PortfolioService.Service.Portfolio.CertificateService;
import com.SwitchBoard.PortfolioService.Service.Portfolio.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CertificateControllerTest {

    @Mock
    private CertificateService certificateService;

    @Mock
    private FileService fileService;

    @InjectMocks
    private CertificateController certificateController;

    private UUID portfolioId;
    private UUID certificateId;
    private CertificateRequestDTO certificateRequest;
    private CertificateResponseDTO certificateResponse;

    @BeforeEach
    void setUp() {
        // Arrange - Common test data
        portfolioId = UUID.randomUUID();
        certificateId = UUID.randomUUID();

        certificateRequest = new CertificateRequestDTO();
        certificateRequest.setTitle("AWS Certified Solutions Architect");
        certificateRequest.setIssuer("Amazon Web Services");
        certificateRequest.setIssueDate(YearMonth.of(2024, 1));
        certificateRequest.setCredentialId("AWS-123456");

        certificateResponse = new CertificateResponseDTO();
        certificateResponse.setId(certificateId);
        certificateResponse.setTitle("AWS Certified Solutions Architect");
        certificateResponse.setIssuer("Amazon Web Services");
        certificateResponse.setIssueDate(YearMonth.of(2024, 1));
        certificateResponse.setCredentialId("AWS-123456");
    }

    @Test
    void testGetAllCertificates_WithResults() {
        // Arrange
        List<CertificateResponseDTO> certificates = Arrays.asList(certificateResponse);
        when(certificateService.getAllCertificatesByPortfolioId(portfolioId)).thenReturn(certificates);

        // Act
        ResponseEntity<List<CertificateResponseDTO>> response = certificateController.getAllCertificates(portfolioId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(certificateService, times(1)).getAllCertificatesByPortfolioId(portfolioId);
    }

    @Test
    void testGetAllCertificates_EmptyList() {
        // Arrange
        when(certificateService.getAllCertificatesByPortfolioId(portfolioId)).thenReturn(List.of());

        // Act
        ResponseEntity<List<CertificateResponseDTO>> response = certificateController.getAllCertificates(portfolioId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(certificateService, times(1)).getAllCertificatesByPortfolioId(portfolioId);
    }

    @Test
    void testGetCertificateById_Success() {
        // Arrange
        when(certificateService.getCertificateById(certificateId)).thenReturn(certificateResponse);

        // Act
        ResponseEntity<CertificateResponseDTO> response = certificateController.getCertificateById(certificateId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("AWS Certified Solutions Architect", response.getBody().getTitle());
        verify(certificateService, times(1)).getCertificateById(certificateId);
    }

    @Test
    void testCreateCertificate_Success() throws IOException {
        // Arrange
        when(certificateService.createCertificate(eq(portfolioId), any(CertificateRequestDTO.class)))
                .thenReturn(certificateResponse);

        // Act
        ResponseEntity<ApiResponse> response = certificateController.createCertificate(portfolioId, certificateRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Certificate created successfully", response.getBody().getMessage());
        verify(certificateService, times(1)).createCertificate(eq(portfolioId), any(CertificateRequestDTO.class));
    }

    @Test
    void testCreateCertificate_WithImage() throws IOException {
        // Arrange
        MockMultipartFile imageFile = new MockMultipartFile(
                "certificateImage", "cert.jpg", "image/jpeg", "certificate content".getBytes());
        certificateRequest.setCertificateImage(imageFile);
        when(certificateService.createCertificate(eq(portfolioId), any(CertificateRequestDTO.class)))
                .thenReturn(certificateResponse);

        // Act
        ResponseEntity<ApiResponse> response = certificateController.createCertificate(portfolioId, certificateRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(certificateService, times(1)).createCertificate(eq(portfolioId), any(CertificateRequestDTO.class));
    }

    @Test
    void testUpdateCertificate_Success() throws IOException {
        // Arrange
        when(certificateService.updateCertificate(eq(certificateId), any(CertificateRequestDTO.class)))
                .thenReturn(certificateResponse);

        // Act
        ResponseEntity<ApiResponse> response = certificateController.updateCertificate(certificateId, certificateRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Certificate updated successfully", response.getBody().getMessage());
        verify(certificateService, times(1)).updateCertificate(eq(certificateId), any(CertificateRequestDTO.class));
    }

    @Test
    void testDeleteCertificate_Success() {
        // Arrange
        doNothing().when(certificateService).deleteCertificate(certificateId);

        // Act
        ResponseEntity<ApiResponse> response = certificateController.deleteCertificate(certificateId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Certificate deleted successfully", response.getBody().getMessage());
        verify(certificateService, times(1)).deleteCertificate(certificateId);
    }
}
