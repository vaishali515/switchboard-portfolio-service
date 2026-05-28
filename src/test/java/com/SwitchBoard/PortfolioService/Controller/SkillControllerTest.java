package com.SwitchBoard.PortfolioService.Controller;

import com.SwitchBoard.PortfolioService.DTO.ApiResponse;
import com.SwitchBoard.PortfolioService.DTO.Skill.SkillRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Skill.SkillResponseDTO;
import com.SwitchBoard.PortfolioService.Service.Portfolio.SkillService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkillControllerTest {

    @Mock
    private SkillService skillService;

    @InjectMocks
    private SkillController skillController;

    private UUID portfolioId;
    private UUID skillId;
    private SkillRequestDTO skillRequest;
    private SkillResponseDTO skillResponse;

    @BeforeEach
    void setUp() {
        // Arrange - Common test data
        portfolioId = UUID.randomUUID();
        skillId = UUID.randomUUID();

        skillRequest = new SkillRequestDTO();
        skillRequest.setName("Java");
        skillRequest.setCategory("Programming Language");
        skillRequest.setProficiencyLevel(4);
        skillRequest.setYearsOfExperience(3);

        skillResponse = new SkillResponseDTO();
        skillResponse.setId(skillId);
        skillResponse.setName("Java");
        skillResponse.setCategory("Programming Language");
        skillResponse.setProficiencyLevel(4);
        skillResponse.setYearsOfExperience(3);
    }

    @Test
    void testGetAllSkills_Success() {
        // Arrange
        List<SkillResponseDTO> skills = Arrays.asList(skillResponse);
        when(skillService.getAllSkillsByPortfolioId(portfolioId)).thenReturn(skills);

        // Act
        ResponseEntity<List<SkillResponseDTO>> response = skillController.getAllSkills(portfolioId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(skillResponse, response.getBody().get(0));
        verify(skillService, times(1)).getAllSkillsByPortfolioId(portfolioId);
    }

    @Test
    void testGetAllSkills_EmptyList() {
        // Arrange
        when(skillService.getAllSkillsByPortfolioId(portfolioId)).thenReturn(Arrays.asList());

        // Act
        ResponseEntity<List<SkillResponseDTO>> response = skillController.getAllSkills(portfolioId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(skillService, times(1)).getAllSkillsByPortfolioId(portfolioId);
    }

    @Test
    void testGetSkillById_Success() {
        // Arrange
        when(skillService.getSkillById(skillId)).thenReturn(skillResponse);

        // Act
        ResponseEntity<SkillResponseDTO> response = skillController.getSkillById(skillId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(skillResponse, response.getBody());
        verify(skillService, times(1)).getSkillById(skillId);
    }

    @Test
    void testGetSkillById_NotFound() {
        // Arrange
        when(skillService.getSkillById(skillId))
                .thenThrow(new EntityNotFoundException("Skill not found"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            skillController.getSkillById(skillId);
        });
        verify(skillService, times(1)).getSkillById(skillId);
    }

    @Test
    void testCreateSkill_Success() {
        // Arrange
        when(skillService.createSkill(eq(portfolioId), any(SkillRequestDTO.class)))
                .thenReturn(skillResponse);

        // Act
        ResponseEntity<ApiResponse> response = skillController.createSkill(portfolioId, skillRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Skill created successfully", response.getBody().getMessage());
        verify(skillService, times(1)).createSkill(eq(portfolioId), any(SkillRequestDTO.class));
    }

    @Test
    void testUpdateSkill_Success() {
        // Arrange
        when(skillService.updateSkill(eq(skillId), any(SkillRequestDTO.class)))
                .thenReturn(skillResponse);

        // Act
        ResponseEntity<ApiResponse> response = skillController.updateSkill(portfolioId, skillId, skillRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Skill updated successfully", response.getBody().getMessage());
        verify(skillService, times(1)).updateSkill(eq(skillId), any(SkillRequestDTO.class));
    }

    @Test
    void testUpdateSkill_NotFound() {
        // Arrange
        when(skillService.updateSkill(eq(skillId), any(SkillRequestDTO.class)))
                .thenThrow(new EntityNotFoundException("Skill not found"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            skillController.updateSkill(portfolioId, skillId, skillRequest);
        });
        verify(skillService, times(1)).updateSkill(eq(skillId), any(SkillRequestDTO.class));
    }

    @Test
    void testDeleteSkill_Success() {
        // Arrange
        doNothing().when(skillService).deleteSkill(skillId);

        // Act
        ResponseEntity<ApiResponse> response = skillController.deleteSkill(portfolioId, skillId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        verify(skillService, times(1)).deleteSkill(skillId);
    }

    @Test
    void testDeleteSkill_NotFound() {
        // Arrange
        doThrow(new EntityNotFoundException("Skill not found"))
                .when(skillService).deleteSkill(skillId);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            skillController.deleteSkill(portfolioId, skillId);
        });
        verify(skillService, times(1)).deleteSkill(skillId);
    }
}
