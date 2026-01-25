package com.SwitchBoard.PortfolioService.Service.Portfolio.Impl;

import com.SwitchBoard.PortfolioService.DTO.Achievement.AchievementRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Achievement.AchievementResponseDTO;
import com.SwitchBoard.PortfolioService.Entity.Achievement;
import com.SwitchBoard.PortfolioService.Entity.Portfolio;
import com.SwitchBoard.PortfolioService.Repository.AchievementRepository;
import com.SwitchBoard.PortfolioService.Repository.PortfolioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AchievementServiceImplTest {

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private PortfolioRepository portfolioRepository;

    @InjectMocks
    private AchievementServiceImpl achievementService;

    private UUID portfolioId;
    private UUID achievementId;
    private Portfolio portfolio;
    private Achievement achievement;
    private AchievementRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        portfolioId = UUID.randomUUID();
        achievementId = UUID.randomUUID();
        
        portfolio = new Portfolio();
        portfolio.setId(portfolioId);
        
        achievement = new Achievement();
        achievement.setId(achievementId);
        achievement.setTitle("Best Developer Award");
        achievement.setDate(LocalDate.of(2024, 1, 1));
        achievement.setIssuer("Tech Company");
        achievement.setDescription("Outstanding contribution");
        achievement.setUrl("https://example.com/award");
        achievement.setPortfolio(portfolio);
        
        requestDTO = new AchievementRequestDTO();
        requestDTO.setTitle("Best Developer Award");
        requestDTO.setDate(LocalDate.of(2024, 1, 1));
        requestDTO.setIssuer("Tech Company");
        requestDTO.setDescription("Outstanding contribution");
        requestDTO.setUrl("https://example.com/award");
    }

    @Test
    void testGetAllAchievementsByPortfolioId_Success() {
        // Arrange
        when(portfolioRepository.existsById(portfolioId)).thenReturn(true);
        when(achievementRepository.findByPortfolioId(portfolioId)).thenReturn(Arrays.asList(achievement));

        // Act
        List<AchievementResponseDTO> result = achievementService.getAllAchievementsByPortfolioId(portfolioId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(achievement.getTitle(), result.get(0).getTitle());
        verify(portfolioRepository, times(1)).existsById(portfolioId);
        verify(achievementRepository, times(1)).findByPortfolioId(portfolioId);
    }

    @Test
    void testGetAllAchievementsByPortfolioId_PortfolioNotFound() {
        // Arrange
        when(portfolioRepository.existsById(portfolioId)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> 
            achievementService.getAllAchievementsByPortfolioId(portfolioId));
        verify(portfolioRepository, times(1)).existsById(portfolioId);
        verify(achievementRepository, never()).findByPortfolioId(any());
    }

    @Test
    void testGetAchievementById_Success() {
        // Arrange
        when(achievementRepository.findById(achievementId)).thenReturn(Optional.of(achievement));

        // Act
        AchievementResponseDTO result = achievementService.getAchievementById(achievementId);

        // Assert
        assertNotNull(result);
        assertEquals(achievement.getTitle(), result.getTitle());
        assertEquals(achievement.getIssuer(), result.getIssuer());
        verify(achievementRepository, times(1)).findById(achievementId);
    }

    @Test
    void testGetAchievementById_NotFound() {
        // Arrange
        when(achievementRepository.findById(achievementId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> 
            achievementService.getAchievementById(achievementId));
        verify(achievementRepository, times(1)).findById(achievementId);
    }

    @Test
    void testCreateAchievement_Success() {
        // Arrange
        when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.of(portfolio));
        when(achievementRepository.save(any(Achievement.class))).thenReturn(achievement);

        // Act
        AchievementResponseDTO result = achievementService.createAchievement(portfolioId, requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(requestDTO.getTitle(), result.getTitle());
        verify(portfolioRepository, times(1)).findById(portfolioId);
        verify(achievementRepository, times(1)).save(any(Achievement.class));
    }

    @Test
    void testCreateAchievement_PortfolioNotFound() {
        // Arrange
        when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> 
            achievementService.createAchievement(portfolioId, requestDTO));
        verify(portfolioRepository, times(1)).findById(portfolioId);
        verify(achievementRepository, never()).save(any());
    }

    @Test
    void testUpdateAchievement_Success() {
        // Arrange
        when(achievementRepository.findById(achievementId)).thenReturn(Optional.of(achievement));
        when(achievementRepository.save(any(Achievement.class))).thenReturn(achievement);

        // Act
        AchievementResponseDTO result = achievementService.updateAchievement(achievementId, requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(requestDTO.getTitle(), result.getTitle());
        verify(achievementRepository, times(1)).findById(achievementId);
        verify(achievementRepository, times(1)).save(any(Achievement.class));
    }

    @Test
    void testUpdateAchievement_NotFound() {
        // Arrange
        when(achievementRepository.findById(achievementId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> 
            achievementService.updateAchievement(achievementId, requestDTO));
        verify(achievementRepository, times(1)).findById(achievementId);
        verify(achievementRepository, never()).save(any());
    }

    @Test
    void testDeleteAchievement_Success() {
        // Arrange
        when(achievementRepository.existsById(achievementId)).thenReturn(true);
        doNothing().when(achievementRepository).deleteById(achievementId);

        // Act
        achievementService.deleteAchievement(achievementId);

        // Assert
        verify(achievementRepository, times(1)).existsById(achievementId);
        verify(achievementRepository, times(1)).deleteById(achievementId);
    }

    @Test
    void testDeleteAchievement_NotFound() {
        // Arrange
        when(achievementRepository.existsById(achievementId)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> 
            achievementService.deleteAchievement(achievementId));
        verify(achievementRepository, times(1)).existsById(achievementId);
        verify(achievementRepository, never()).deleteById(any());
    }
}
