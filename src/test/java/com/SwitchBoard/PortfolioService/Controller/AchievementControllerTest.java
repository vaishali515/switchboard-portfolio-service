package com.SwitchBoard.PortfolioService.Controller;

import com.SwitchBoard.PortfolioService.DTO.Achievement.AchievementRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Achievement.AchievementResponseDTO;
import com.SwitchBoard.PortfolioService.DTO.ApiResponse;
import com.SwitchBoard.PortfolioService.Service.Portfolio.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AchievementControllerTest {

    @Mock
    private AchievementService achievementService;

    @InjectMocks
    private AchievementController achievementController;

    private UUID portfolioId;
    private UUID achievementId;
    private AchievementRequestDTO achievementRequest;
    private AchievementResponseDTO achievementResponse;

    @BeforeEach
    void setUp() {
        // Arrange - Common test data
        portfolioId = UUID.randomUUID();
        achievementId = UUID.randomUUID();

        achievementRequest = new AchievementRequestDTO();
        achievementRequest.setTitle("Best Developer Award");
        achievementRequest.setIssuer("Tech Company");
        achievementRequest.setDate(LocalDate.of(2024, 1, 15));
        achievementRequest.setDescription("Awarded for outstanding performance");

        achievementResponse = new AchievementResponseDTO();
        achievementResponse.setId(achievementId);
        achievementResponse.setTitle("Best Developer Award");
        achievementResponse.setIssuer("Tech Company");
        achievementResponse.setDate(LocalDate.of(2024, 1, 15));
        achievementResponse.setDescription("Awarded for outstanding performance");
    }

    @Test
    void testGetAllAchievements_WithResults() {
        // Arrange
        List<AchievementResponseDTO> achievements = Arrays.asList(achievementResponse);
        when(achievementService.getAllAchievementsByPortfolioId(portfolioId)).thenReturn(achievements);

        // Act
        ResponseEntity<List<AchievementResponseDTO>> response = achievementController.getAllAchievements(portfolioId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Best Developer Award", response.getBody().get(0).getTitle());
        verify(achievementService, times(1)).getAllAchievementsByPortfolioId(portfolioId);
    }

    @Test
    void testGetAllAchievements_EmptyList() {
        // Arrange
        when(achievementService.getAllAchievementsByPortfolioId(portfolioId)).thenReturn(List.of());

        // Act
        ResponseEntity<List<AchievementResponseDTO>> response = achievementController.getAllAchievements(portfolioId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(achievementService, times(1)).getAllAchievementsByPortfolioId(portfolioId);
    }

    @Test
    void testGetAchievementById_Success() {
        // Arrange
        when(achievementService.getAchievementById(achievementId)).thenReturn(achievementResponse);

        // Act
        ResponseEntity<AchievementResponseDTO> response = achievementController.getAchievementById(achievementId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Best Developer Award", response.getBody().getTitle());
        assertEquals("Tech Company", response.getBody().getIssuer());
        verify(achievementService, times(1)).getAchievementById(achievementId);
    }

    @Test
    void testCreateAchievement_Success() {
        // Arrange
        when(achievementService.createAchievement(eq(portfolioId), any(AchievementRequestDTO.class)))
                .thenReturn(achievementResponse);

        // Act
        ResponseEntity<ApiResponse> response = achievementController.createAchievement(portfolioId, achievementRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Achievement created successfully", response.getBody().getMessage());
        verify(achievementService, times(1)).createAchievement(eq(portfolioId), any(AchievementRequestDTO.class));
    }

    @Test
    void testUpdateAchievement_Success() {
        // Arrange
        when(achievementService.updateAchievement(eq(achievementId), any(AchievementRequestDTO.class)))
                .thenReturn(achievementResponse);

        // Act
        ResponseEntity<ApiResponse> response = achievementController.updateAchievement(achievementId, achievementRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Achievement updated successfully", response.getBody().getMessage());
        verify(achievementService, times(1)).updateAchievement(eq(achievementId), any(AchievementRequestDTO.class));
    }

    @Test
    void testDeleteAchievement_Success() {
        // Arrange
        doNothing().when(achievementService).deleteAchievement(achievementId);

        // Act
        ResponseEntity<ApiResponse> response = achievementController.deleteAchievement(achievementId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Achievement deleted successfully", response.getBody().getMessage());
        verify(achievementService, times(1)).deleteAchievement(achievementId);
    }
}
