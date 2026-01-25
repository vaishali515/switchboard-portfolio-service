package com.SwitchBoard.PortfolioService.Controller;

import com.SwitchBoard.PortfolioService.DTO.ApiResponse;
import com.SwitchBoard.PortfolioService.DTO.Portfolio.PortfolioRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Portfolio.PortfolioResponseDTO;
import com.SwitchBoard.PortfolioService.Service.Portfolio.PortfolioService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PortfolioControllerTest {

    @Mock
    private PortfolioService portfolioService;

    @InjectMocks
    private PortfolioController portfolioController;

    private UUID portfolioId;
    private String emailId;
    private PortfolioRequestDTO portfolioRequest;
    private PortfolioResponseDTO portfolioResponse;

    @BeforeEach
    void setUp() {
        // Arrange - Common test data
        portfolioId = UUID.randomUUID();
        emailId = "test@example.com";

        portfolioRequest = new PortfolioRequestDTO();
        portfolioRequest.setFullName("John Doe");
        portfolioRequest.setEmailId(emailId);
        portfolioRequest.setContactNumber("+1234567890");
        portfolioRequest.setBio("Test bio");
        portfolioRequest.setSocialLinks(new ArrayList<>());

        portfolioResponse = new PortfolioResponseDTO();
        portfolioResponse.setId(portfolioId);
        portfolioResponse.setFullName("John Doe");
        portfolioResponse.setEmailId(emailId);
    }

    @Test
    void testGetPortfolioById_Success() {
        // Arrange
        when(portfolioService.getPortfolioById(portfolioId)).thenReturn(portfolioResponse);

        // Act
        ResponseEntity<PortfolioResponseDTO> response = portfolioController.getPortfolioById(portfolioId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(portfolioResponse, response.getBody());
        verify(portfolioService, times(1)).getPortfolioById(portfolioId);
    }

    @Test
    void testGetPortfolioById_NotFound() {
        // Arrange
        when(portfolioService.getPortfolioById(portfolioId))
                .thenThrow(new EntityNotFoundException("Portfolio not found"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            portfolioController.getPortfolioById(portfolioId);
        });
        verify(portfolioService, times(1)).getPortfolioById(portfolioId);
    }

    @Test
    void testGetPortfolioByEmail_Success() {
        // Arrange
        when(portfolioService.getPortfolioByEmailId(emailId)).thenReturn(portfolioResponse);

        // Act
        ResponseEntity<PortfolioResponseDTO> response = portfolioController.getPortfolioByEmail(emailId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(portfolioResponse, response.getBody());
        verify(portfolioService, times(1)).getPortfolioByEmailId(emailId);
    }

    @Test
    void testGetPortfolioByHeaderEmail_Success() {
        // Arrange
        when(portfolioService.getPortfolioByEmailId(emailId)).thenReturn(portfolioResponse);

        // Act
        ResponseEntity<PortfolioResponseDTO> response = portfolioController.getPortfolioByHeaderEmail(emailId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(portfolioResponse, response.getBody());
        verify(portfolioService, times(1)).getPortfolioByEmailId(emailId);
    }

    @Test
    void testCreatePortfolio_Success() throws IOException {
        // Arrange
        MockMultipartFile profileImage = new MockMultipartFile(
                "profileImage", "test.jpg", "image/jpeg", "test image content".getBytes());
        portfolioRequest.setProfileImage(profileImage);

        when(portfolioService.createPortfolio(any(PortfolioRequestDTO.class)))
                .thenReturn(portfolioResponse);

        // Act
        ResponseEntity<ApiResponse> response = portfolioController.createPortfolio(portfolioRequest, emailId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Portfolio created successfully", response.getBody().getMessage());
        verify(portfolioService, times(1)).createPortfolio(any(PortfolioRequestDTO.class));
    }

    @Test
    void testCreatePortfolio_WithMultipartException() throws IOException {
        // Arrange
        when(portfolioService.createPortfolio(any(PortfolioRequestDTO.class)))
                .thenThrow(new MultipartException("Invalid file"));

        // Act
        ResponseEntity<ApiResponse> response = portfolioController.createPortfolio(portfolioRequest, emailId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Invalid file upload"));
    }

    @Test
    void testUpdatePortfolio_Success() throws IOException {
        // Arrange
        when(portfolioService.updatePortfolio(eq(portfolioId), any(PortfolioRequestDTO.class)))
                .thenReturn(portfolioResponse);

        // Act
        ResponseEntity<ApiResponse> response = portfolioController.updatePortfolio(portfolioId, portfolioRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Portfolio updated successfully", response.getBody().getMessage());
        verify(portfolioService, times(1)).updatePortfolio(eq(portfolioId), any(PortfolioRequestDTO.class));
    }

    @Test
    void testUpdatePortfolio_NotFound() throws IOException {
        // Arrange
        when(portfolioService.updatePortfolio(eq(portfolioId), any(PortfolioRequestDTO.class)))
                .thenThrow(new EntityNotFoundException("Portfolio not found"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            portfolioController.updatePortfolio(portfolioId, portfolioRequest);
        });
        verify(portfolioService, times(1)).updatePortfolio(eq(portfolioId), any(PortfolioRequestDTO.class));
    }

    @Test
    void testDeletePortfolio_Success() {
        // Arrange
        doNothing().when(portfolioService).deletePortfolio(portfolioId);

        // Act
        ResponseEntity<ApiResponse> response = portfolioController.deletePortfolio(portfolioId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Portfolio deleted successfully", response.getBody().getMessage());
        verify(portfolioService, times(1)).deletePortfolio(portfolioId);
    }

    @Test
    void testDeletePortfolio_NotFound() {
        // Arrange
        doThrow(new EntityNotFoundException("Portfolio not found"))
                .when(portfolioService).deletePortfolio(portfolioId);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            portfolioController.deletePortfolio(portfolioId);
        });
        verify(portfolioService, times(1)).deletePortfolio(portfolioId);
    }
}