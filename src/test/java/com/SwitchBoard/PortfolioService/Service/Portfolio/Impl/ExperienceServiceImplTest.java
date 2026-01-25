package com.SwitchBoard.PortfolioService.Service.Portfolio.Impl;

import com.SwitchBoard.PortfolioService.DTO.Experience.ExperienceRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Experience.ExperienceResponseDTO;
import com.SwitchBoard.PortfolioService.Entity.Experience;
import com.SwitchBoard.PortfolioService.Entity.Portfolio;
import com.SwitchBoard.PortfolioService.Repository.ExperienceRepository;
import com.SwitchBoard.PortfolioService.Repository.PortfolioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExperienceServiceImplTest {

    @Mock
    private ExperienceRepository experienceRepository;

    @Mock
    private PortfolioRepository portfolioRepository;

    @InjectMocks
    private ExperienceServiceImpl experienceService;

    private UUID portfolioId;
    private UUID experienceId;
    private Portfolio portfolio;
    private Experience experience;
    private ExperienceRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        portfolioId = UUID.randomUUID();
        experienceId = UUID.randomUUID();
        
        portfolio = new Portfolio();
        portfolio.setId(portfolioId);
        
        experience = new Experience();
        experience.setId(experienceId);
        experience.setRole("Software Engineer");
        experience.setCompany("Tech Corp");
        experience.setStartDate(YearMonth.of(2020, 1));
        experience.setEndDate(YearMonth.of(2024, 5));
        experience.setPortfolio(portfolio);
        
        requestDTO = new ExperienceRequestDTO();
        requestDTO.setRole("Software Engineer");
        requestDTO.setCompany("Tech Corp");
        requestDTO.setStartDate(YearMonth.of(2020, 1));
        requestDTO.setEndDate(YearMonth.of(2024, 5));
    }

    @Test
    void testGetAllExperiencesByPortfolioId_Success() {
        when(portfolioRepository.existsById(portfolioId)).thenReturn(true);
        when(experienceRepository.findByPortfolioId(portfolioId)).thenReturn(Arrays.asList(experience));

        List<ExperienceResponseDTO> result = experienceService.getAllExperiencesByPortfolioId(portfolioId);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllExperiencesByPortfolioId_PortfolioNotFound() {
        when(portfolioRepository.existsById(portfolioId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> 
            experienceService.getAllExperiencesByPortfolioId(portfolioId));
    }

    @Test
    void testGetExperienceById_Success() {
        when(experienceRepository.findById(experienceId)).thenReturn(Optional.of(experience));

        ExperienceResponseDTO result = experienceService.getExperienceById(experienceId);

        assertNotNull(result);
    }

    @Test
    void testGetExperienceById_NotFound() {
        when(experienceRepository.findById(experienceId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> 
            experienceService.getExperienceById(experienceId));
    }

    @Test
    void testCreateExperience_Success() {
        when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.of(portfolio));
        when(experienceRepository.save(any(Experience.class))).thenReturn(experience);

        ExperienceResponseDTO result = experienceService.createExperience(portfolioId, requestDTO);

        assertNotNull(result);
    }

    @Test
    void testCreateExperience_PortfolioNotFound() {
        when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> 
            experienceService.createExperience(portfolioId, requestDTO));
    }

    @Test
    void testUpdateExperience_Success() {
        when(experienceRepository.findById(experienceId)).thenReturn(Optional.of(experience));
        when(experienceRepository.save(any(Experience.class))).thenReturn(experience);

        ExperienceResponseDTO result = experienceService.updateExperience(experienceId, requestDTO);

        assertNotNull(result);
    }

    @Test
    void testUpdateExperience_NotFound() {
        when(experienceRepository.findById(experienceId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> 
            experienceService.updateExperience(experienceId, requestDTO));
    }

    @Test
    void testDeleteExperience_Success() {
        when(experienceRepository.existsById(experienceId)).thenReturn(true);

        experienceService.deleteExperience(experienceId);

        verify(experienceRepository).deleteById(experienceId);
    }

    @Test
    void testDeleteExperience_NotFound() {
        when(experienceRepository.existsById(experienceId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> 
            experienceService.deleteExperience(experienceId));
    }
}
