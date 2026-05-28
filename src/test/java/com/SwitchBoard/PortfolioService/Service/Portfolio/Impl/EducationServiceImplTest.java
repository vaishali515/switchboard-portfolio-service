package com.SwitchBoard.PortfolioService.Service.Portfolio.Impl;

import com.SwitchBoard.PortfolioService.DTO.Education.EducationRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Education.EducationResponseDTO;
import com.SwitchBoard.PortfolioService.Entity.Education;
import com.SwitchBoard.PortfolioService.Entity.Portfolio;
import com.SwitchBoard.PortfolioService.Repository.EducationRepository;
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
class EducationServiceImplTest {

    @Mock
    private EducationRepository educationRepository;

    @Mock
    private PortfolioRepository portfolioRepository;

    @InjectMocks
    private EducationServiceImpl educationService;

    private UUID portfolioId;
    private UUID educationId;
    private Portfolio portfolio;
    private Education education;
    private EducationRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        portfolioId = UUID.randomUUID();
        educationId = UUID.randomUUID();
        
        portfolio = new Portfolio();
        portfolio.setId(portfolioId);
        
        education = new Education();
        education.setId(educationId);
        education.setInstitution("MIT");
        education.setDegree("Computer Science");
        education.setStartDate(YearMonth.of(2020, 1));
        education.setEndDate(YearMonth.of(2024, 5));
        education.setPortfolio(portfolio);
        
        requestDTO = new EducationRequestDTO();
        requestDTO.setInstitution("MIT");
        requestDTO.setDegree("Computer Science");
        requestDTO.setStartDate(YearMonth.of(2020, 1));
        requestDTO.setEndDate(YearMonth.of(2024, 5));
    }

    @Test
    void testGetAllEducationsByPortfolioId_Success() {
        when(portfolioRepository.existsById(portfolioId)).thenReturn(true);
        when(educationRepository.findByPortfolioId(portfolioId)).thenReturn(Arrays.asList(education));

        List<EducationResponseDTO> result = educationService.getAllEducationsByPortfolioId(portfolioId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(portfolioRepository).existsById(portfolioId);
        verify(educationRepository).findByPortfolioId(portfolioId);
    }

    @Test
    void testGetAllEducationsByPortfolioId_PortfolioNotFound() {
        when(portfolioRepository.existsById(portfolioId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> 
            educationService.getAllEducationsByPortfolioId(portfolioId));
        verify(portfolioRepository).existsById(portfolioId);
    }

    @Test
    void testGetEducationById_Success() {
        when(educationRepository.findById(educationId)).thenReturn(Optional.of(education));

        EducationResponseDTO result = educationService.getEducationById(educationId);

        assertNotNull(result);
        assertEquals(education.getInstitution(), result.getInstitution());
        verify(educationRepository).findById(educationId);
    }

    @Test
    void testGetEducationById_NotFound() {
        when(educationRepository.findById(educationId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> 
            educationService.getEducationById(educationId));
    }

    @Test
    void testCreateEducation_Success() {
        when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.of(portfolio));
        when(educationRepository.save(any(Education.class))).thenReturn(education);

        EducationResponseDTO result = educationService.createEducation(portfolioId, requestDTO);

        assertNotNull(result);
        verify(portfolioRepository).findById(portfolioId);
        verify(educationRepository).save(any(Education.class));
    }

    @Test
    void testCreateEducation_PortfolioNotFound() {
        when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> 
            educationService.createEducation(portfolioId, requestDTO));
    }

    @Test
    void testUpdateEducation_Success() {
        when(educationRepository.findById(educationId)).thenReturn(Optional.of(education));
        when(educationRepository.save(any(Education.class))).thenReturn(education);

        EducationResponseDTO result = educationService.updateEducation(educationId, requestDTO);

        assertNotNull(result);
        verify(educationRepository).findById(educationId);
        verify(educationRepository).save(any(Education.class));
    }

    @Test
    void testUpdateEducation_NotFound() {
        when(educationRepository.findById(educationId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> 
            educationService.updateEducation(educationId, requestDTO));
    }

    @Test
    void testDeleteEducation_Success() {
        when(educationRepository.existsById(educationId)).thenReturn(true);

        educationService.deleteEducation(educationId);

        verify(educationRepository).existsById(educationId);
        verify(educationRepository).deleteById(educationId);
    }

    @Test
    void testDeleteEducation_NotFound() {
        when(educationRepository.existsById(educationId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> 
            educationService.deleteEducation(educationId));
    }
}
