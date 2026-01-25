package com.SwitchBoard.PortfolioService.Controller;

import com.SwitchBoard.PortfolioService.DTO.Experience.ExperienceRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Experience.ExperienceResponseDTO;
import com.SwitchBoard.PortfolioService.DTO.ApiResponse;
import com.SwitchBoard.PortfolioService.Service.Portfolio.ExperienceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExperienceControllerTest {

    @Mock
    private ExperienceService experienceService;

    @InjectMocks
    private ExperienceController experienceController;

    private UUID portfolioId;
    private UUID experienceId;
    private ExperienceRequestDTO experienceRequest;
    private ExperienceResponseDTO experienceResponse;

    @BeforeEach
    void setUp() {
        portfolioId = UUID.randomUUID();
        experienceId = UUID.randomUUID();

        experienceRequest = new ExperienceRequestDTO();
        experienceRequest.setCompany("Google");
        experienceRequest.setRole("Software Engineer");
        experienceRequest.setStartDate(YearMonth.of(2022, 1));
        experienceRequest.setEndDate(YearMonth.of(2024, 1));
        experienceRequest.setCurrent(false);

        experienceResponse = new ExperienceResponseDTO();
        experienceResponse.setId(experienceId);
        experienceResponse.setCompany("Google");
        experienceResponse.setRole("Software Engineer");
    }

    @Test
    void testGetAllExperiences_Success() {
        when(experienceService.getAllExperiencesByPortfolioId(portfolioId)).thenReturn(Arrays.asList(experienceResponse));
        
        ResponseEntity<List<ExperienceResponseDTO>> response = experienceController.getExperiencesByPortfolio(portfolioId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(experienceService, times(1)).getAllExperiencesByPortfolioId(portfolioId);
    }

    @Test
    void testGetExperienceById_Success() {
        when(experienceService.getExperienceById(experienceId)).thenReturn(experienceResponse);
        
        ResponseEntity<ExperienceResponseDTO> response = experienceController.getExperienceById(portfolioId, experienceId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Google", response.getBody().getCompany());
        verify(experienceService, times(1)).getExperienceById(experienceId);
    }

    @Test
    void testCreateExperience_Success() {
        when(experienceService.createExperience(eq(portfolioId), any(ExperienceRequestDTO.class)))
                .thenReturn(experienceResponse);
        
        ResponseEntity<ApiResponse> response = experienceController.createExperience(portfolioId, experienceRequest);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        verify(experienceService, times(1)).createExperience(eq(portfolioId), any(ExperienceRequestDTO.class));
    }

    @Test
    void testUpdateExperience_Success() {
        when(experienceService.updateExperience(eq(experienceId), any(ExperienceRequestDTO.class)))
                .thenReturn(experienceResponse);
        
        ResponseEntity<ApiResponse> response = experienceController.updateExperience(portfolioId, experienceId, experienceRequest);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        verify(experienceService, times(1)).updateExperience(eq(experienceId), any(ExperienceRequestDTO.class));
    }

    @Test
    void testDeleteExperience_Success() {
        doNothing().when(experienceService).deleteExperience(experienceId);
        
        ResponseEntity<ApiResponse> response = experienceController.deleteExperience(portfolioId, experienceId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        verify(experienceService, times(1)).deleteExperience(experienceId);
    }
}
