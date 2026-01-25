package com.SwitchBoard.PortfolioService.Controller;

import com.SwitchBoard.PortfolioService.DTO.Education.EducationRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Education.EducationResponseDTO;
import com.SwitchBoard.PortfolioService.DTO.ApiResponse;
import com.SwitchBoard.PortfolioService.Service.Portfolio.EducationService;
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
class EducationControllerTest {

    @Mock
    private EducationService educationService;

    @InjectMocks
    private EducationController educationController;

    private UUID portfolioId;
    private UUID educationId;
    private EducationRequestDTO educationRequest;
    private EducationResponseDTO educationResponse;

    @BeforeEach
    void setUp() {
        portfolioId = UUID.randomUUID();
        educationId = UUID.randomUUID();

        educationRequest = new EducationRequestDTO();
        educationRequest.setInstitution("MIT");
        educationRequest.setDegree("Bachelor of Science");
        educationRequest.setFieldOfStudy("Computer Science");
        educationRequest.setStartDate(YearMonth.of(2020, 9));
        educationRequest.setEndDate(YearMonth.of(2024, 5));
        educationRequest.setGrade(3.8);

        educationResponse = new EducationResponseDTO();
        educationResponse.setId(educationId);
        educationResponse.setInstitution("MIT");
        educationResponse.setDegree("Bachelor of Science");
        educationResponse.setFieldOfStudy("Computer Science");
    }

    @Test
    void testGetAllEducations_Success() {
        when(educationService.getAllEducationsByPortfolioId(portfolioId)).thenReturn(Arrays.asList(educationResponse));
        
        ResponseEntity<List<EducationResponseDTO>> response = educationController.getAllEducations(portfolioId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(educationService, times(1)).getAllEducationsByPortfolioId(portfolioId);
    }

    @Test
    void testGetEducationById_Success() {
        when(educationService.getEducationById(educationId)).thenReturn(educationResponse);
        
        ResponseEntity<EducationResponseDTO> response = educationController.getEducationById(educationId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("MIT", response.getBody().getInstitution());
        verify(educationService, times(1)).getEducationById(educationId);
    }

    @Test
    void testCreateEducation_Success() {
        when(educationService.createEducation(eq(portfolioId), any(EducationRequestDTO.class)))
                .thenReturn(educationResponse);
        
        ResponseEntity<ApiResponse> response = educationController.createEducation(portfolioId, educationRequest);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        verify(educationService, times(1)).createEducation(eq(portfolioId), any(EducationRequestDTO.class));
    }

    @Test
    void testUpdateEducation_Success() {
        when(educationService.updateEducation(eq(educationId), any(EducationRequestDTO.class)))
                .thenReturn(educationResponse);
        
        ResponseEntity<ApiResponse> response = educationController.updateEducation(educationId, educationRequest);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        verify(educationService, times(1)).updateEducation(eq(educationId), any(EducationRequestDTO.class));
    }

    @Test
    void testDeleteEducation_Success() {
        doNothing().when(educationService).deleteEducation(educationId);
        
        ResponseEntity<ApiResponse> response = educationController.deleteEducation(educationId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        verify(educationService, times(1)).deleteEducation(educationId);
    }
}
