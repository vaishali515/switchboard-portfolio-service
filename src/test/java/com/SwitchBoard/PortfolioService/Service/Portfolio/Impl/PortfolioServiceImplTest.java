package com.SwitchBoard.PortfolioService.Service.Portfolio.Impl;

import com.SwitchBoard.PortfolioService.DTO.Portfolio.PortfolioRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Portfolio.PortfolioResponseDTO;
import com.SwitchBoard.PortfolioService.Entity.Portfolio;
import com.SwitchBoard.PortfolioService.Repository.PortfolioRepository;
import com.SwitchBoard.PortfolioService.Service.Portfolio.FileService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PortfolioServiceImplTest {

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private FileService fileService;

    @InjectMocks
    private PortfolioServiceImpl portfolioService;

    private UUID portfolioId;
    private String emailId;
    private Portfolio portfolio;
    private PortfolioRequestDTO portfolioRequest;

    @BeforeEach
    void setUp() {
        // Arrange - Common test data
        portfolioId = UUID.randomUUID();
        emailId = "test@example.com";

        portfolio = new Portfolio();
        portfolio.setId(portfolioId);
        portfolio.setEmailId(emailId);
        portfolio.setFullName("John Doe");
        portfolio.setContactNumber("+1234567890");
        portfolio.setBio("Test bio");
        portfolio.setSkills(new ArrayList<>());
        portfolio.setProjects(new ArrayList<>());
        portfolio.setEducations(new ArrayList<>());
        portfolio.setExperiences(new ArrayList<>());
        portfolio.setCertificates(new ArrayList<>());
        portfolio.setAchievements(new ArrayList<>());

        portfolioRequest = new PortfolioRequestDTO();
        portfolioRequest.setFullName("John Doe");
        portfolioRequest.setEmailId(emailId);
        portfolioRequest.setContactNumber("+1234567890");
        portfolioRequest.setBio("Test bio");
        portfolioRequest.setSocialLinks(new ArrayList<>());
    }

    @Test
    void testGetPortfolioById_Success() {
        // Arrange
        when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.of(portfolio));

        // Act
        PortfolioResponseDTO result = portfolioService.getPortfolioById(portfolioId);

        // Assert
        assertNotNull(result);
        assertEquals(portfolioId, result.getId());
        assertEquals(emailId, result.getEmailId());
        assertEquals("John Doe", result.getFullName());
        verify(portfolioRepository, times(1)).findById(portfolioId);
    }

    @Test
    void testGetPortfolioById_NotFound() {
        // Arrange
        when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            portfolioService.getPortfolioById(portfolioId);
        });
        assertTrue(exception.getMessage().contains("Portfolio not found"));
        verify(portfolioRepository, times(1)).findById(portfolioId);
    }

    @Test
    void testGetPortfolioByEmailId_Success() {
        // Arrange
        when(portfolioRepository.findByEmailId(emailId)).thenReturn(Optional.of(portfolio));

        // Act
        PortfolioResponseDTO result = portfolioService.getPortfolioByEmailId(emailId);

        // Assert
        assertNotNull(result);
        assertEquals(emailId, result.getEmailId());
        assertEquals("John Doe", result.getFullName());
        verify(portfolioRepository, times(1)).findByEmailId(emailId);
    }

    @Test
    void testGetPortfolioByEmailId_NotFound() {
        // Arrange
        when(portfolioRepository.findByEmailId(emailId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            portfolioService.getPortfolioByEmailId(emailId);
        });
        assertTrue(exception.getMessage().contains("Portfolio not found"));
        verify(portfolioRepository, times(1)).findByEmailId(emailId);
    }

    @Test
    void testCreatePortfolio_Success() throws IOException {
        // Arrange
        when(portfolioRepository.findByEmailId(emailId)).thenReturn(Optional.empty());
        when(portfolioRepository.save(any(Portfolio.class))).thenReturn(portfolio);

        // Act
        PortfolioResponseDTO result = portfolioService.createPortfolio(portfolioRequest);

        // Assert
        assertNotNull(result);
        assertEquals(emailId, result.getEmailId());
        assertEquals("John Doe", result.getFullName());
        verify(portfolioRepository, times(1)).findByEmailId(emailId);
        verify(portfolioRepository, times(1)).save(any(Portfolio.class));
    }

    @Test
    void testCreatePortfolio_WithProfileImage() throws IOException {
        // Arrange
        MockMultipartFile profileImage = new MockMultipartFile(
                "profileImage", "test.jpg", "image/jpeg", "test image content".getBytes());
        portfolioRequest.setProfileImage(profileImage);

        when(portfolioRepository.findByEmailId(emailId)).thenReturn(Optional.empty());
        when(fileService.uploadImage(anyString(), any())).thenReturn("https://s3.amazonaws.com/bucket/image.jpg");
        when(portfolioRepository.save(any(Portfolio.class))).thenReturn(portfolio);

        // Act
        PortfolioResponseDTO result = portfolioService.createPortfolio(portfolioRequest);

        // Assert
        assertNotNull(result);
        verify(fileService, times(1)).uploadImage(anyString(), any());
        verify(portfolioRepository, times(1)).save(any(Portfolio.class));
    }

    @Test
    void testCreatePortfolio_WithResume() throws IOException {
        // Arrange
        MockMultipartFile resume = new MockMultipartFile(
                "resume", "resume.pdf", "application/pdf", "test resume content".getBytes());
        portfolioRequest.setResume(resume);

        when(portfolioRepository.findByEmailId(emailId)).thenReturn(Optional.empty());
        when(fileService.uploadImage(anyString(), any())).thenReturn("https://s3.amazonaws.com/bucket/resume.pdf");
        when(portfolioRepository.save(any(Portfolio.class))).thenReturn(portfolio);

        // Act
        PortfolioResponseDTO result = portfolioService.createPortfolio(portfolioRequest);

        // Assert
        assertNotNull(result);
        verify(fileService, times(1)).uploadImage(anyString(), any());
        verify(portfolioRepository, times(1)).save(any(Portfolio.class));
    }

    @Test
    void testCreatePortfolio_AlreadyExists() {
        // Arrange
        when(portfolioRepository.findByEmailId(emailId)).thenReturn(Optional.of(portfolio));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            portfolioService.createPortfolio(portfolioRequest);
        });
        assertTrue(exception.getMessage().contains("already exists"));
        verify(portfolioRepository, times(1)).findByEmailId(emailId);
        verify(portfolioRepository, never()).save(any(Portfolio.class));
    }

    @Test
    void testUpdatePortfolio_Success() throws IOException {
        // Arrange
        when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.of(portfolio));
        when(portfolioRepository.save(any(Portfolio.class))).thenReturn(portfolio);

        portfolioRequest.setFullName("Jane Doe Updated");

        // Act
        PortfolioResponseDTO result = portfolioService.updatePortfolio(portfolioId, portfolioRequest);

        // Assert
        assertNotNull(result);
        verify(portfolioRepository, times(1)).findById(portfolioId);
        verify(portfolioRepository, times(1)).save(any(Portfolio.class));
    }

    @Test
    void testUpdatePortfolio_WithNewImage() throws IOException {
        // Arrange
        portfolio.setProfileImageUrl("https://s3.amazonaws.com/bucket/old-image.jpg");
        
        MockMultipartFile newImage = new MockMultipartFile(
                "profileImage", "new.jpg", "image/jpeg", "new image content".getBytes());
        portfolioRequest.setProfileImage(newImage);

        when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.of(portfolio));
        when(fileService.uploadImage(anyString(), any())).thenReturn("https://s3.amazonaws.com/bucket/new-image.jpg");
        when(portfolioRepository.save(any(Portfolio.class))).thenReturn(portfolio);

        // Act
        PortfolioResponseDTO result = portfolioService.updatePortfolio(portfolioId, portfolioRequest);

        // Assert
        assertNotNull(result);
        verify(fileService, times(1)).deleteImage(anyString());
        verify(fileService, times(1)).uploadImage(anyString(), any());
        verify(portfolioRepository, times(1)).save(any(Portfolio.class));
    }

    @Test
    void testUpdatePortfolio_NotFound() {
        // Arrange
        when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            portfolioService.updatePortfolio(portfolioId, portfolioRequest);
        });
        assertTrue(exception.getMessage().contains("Portfolio not found"));
        verify(portfolioRepository, times(1)).findById(portfolioId);
        verify(portfolioRepository, never()).save(any(Portfolio.class));
    }

    @Test
    void testDeletePortfolio_Success() {
        // Arrange
        when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.of(portfolio));
        doNothing().when(portfolioRepository).delete(any(Portfolio.class));

        // Act
        portfolioService.deletePortfolio(portfolioId);

        // Assert
        verify(portfolioRepository, times(1)).findById(portfolioId);
        verify(portfolioRepository, times(1)).delete(portfolio);
    }

    @Test
    void testDeletePortfolio_WithImage() {
        // Arrange
        portfolio.setProfileImageUrl("https://s3.amazonaws.com/bucket/image.jpg");
        when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.of(portfolio));
        doNothing().when(fileService).deleteImage(anyString());
        doNothing().when(portfolioRepository).delete(any(Portfolio.class));

        // Act
        portfolioService.deletePortfolio(portfolioId);

        // Assert
        verify(fileService, times(1)).deleteImage(anyString());
        verify(portfolioRepository, times(1)).delete(portfolio);
    }

    @Test
    void testDeletePortfolio_NotFound() {
        // Arrange
        when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            portfolioService.deletePortfolio(portfolioId);
        });
        assertTrue(exception.getMessage().contains("Portfolio not found"));
        verify(portfolioRepository, times(1)).findById(portfolioId);
        verify(portfolioRepository, never()).delete(any(Portfolio.class));
    }
}