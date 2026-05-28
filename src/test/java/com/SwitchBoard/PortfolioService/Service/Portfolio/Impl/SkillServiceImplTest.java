package com.SwitchBoard.PortfolioService.Service.Portfolio.Impl;

import com.SwitchBoard.PortfolioService.DTO.Skill.SkillRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Skill.SkillResponseDTO;
import com.SwitchBoard.PortfolioService.Entity.Portfolio;
import com.SwitchBoard.PortfolioService.Entity.Skill;
import com.SwitchBoard.PortfolioService.Repository.PortfolioRepository;
import com.SwitchBoard.PortfolioService.Repository.SkillRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkillServiceImplTest {

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private PortfolioRepository portfolioRepository;

    @InjectMocks
    private SkillServiceImpl skillService;

    private UUID portfolioId;
    private UUID skillId;
    private Portfolio portfolio;
    private Skill skill;
    private SkillRequestDTO skillRequest;

    @BeforeEach
    void setUp() {
        portfolioId = UUID.randomUUID();
        skillId = UUID.randomUUID();

        portfolio = new Portfolio();
        portfolio.setId(portfolioId);
        portfolio.setEmailId("test@example.com");
        portfolio.setFullName("John Doe");

        skill = new Skill();
        skill.setId(skillId);
        skill.setName("Java");
        skill.setCategory("Programming Language");
        skill.setProficiencyLevel(4);
        skill.setYearsOfExperience(3);
        skill.setPortfolio(portfolio);

        skillRequest = new SkillRequestDTO();
        skillRequest.setName("Java");
        skillRequest.setCategory("Programming Language");
        skillRequest.setProficiencyLevel(4);
        skillRequest.setYearsOfExperience(3);
    }

    @Test
    void testGetAllSkillsByPortfolioId_Success() {
        // Arrange
        when(portfolioRepository.existsById(portfolioId)).thenReturn(true);
        when(skillRepository.findByPortfolioId(portfolioId)).thenReturn(Arrays.asList(skill));

        // Act
        List<SkillResponseDTO> result = skillService.getAllSkillsByPortfolioId(portfolioId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Java", result.get(0).getName());
        verify(portfolioRepository, times(1)).existsById(portfolioId);
        verify(skillRepository, times(1)).findByPortfolioId(portfolioId);
    }

    @Test
    void testGetAllSkillsByPortfolioId_PortfolioNotFound() {
        // Arrange
        when(portfolioRepository.existsById(portfolioId)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> 
            skillService.getAllSkillsByPortfolioId(portfolioId));
        verify(portfolioRepository, times(1)).existsById(portfolioId);
        verify(skillRepository, never()).findByPortfolioId(any());
    }

    @Test
    void testGetSkillById_Success() {
        // Arrange
        when(skillRepository.findById(skillId)).thenReturn(Optional.of(skill));

        // Act
        SkillResponseDTO result = skillService.getSkillById(skillId);

        // Assert
        assertNotNull(result);
        assertEquals("Java", result.getName());
        assertEquals(4, result.getProficiencyLevel());
        verify(skillRepository, times(1)).findById(skillId);
    }

    @Test
    void testGetSkillById_NotFound() {
        // Arrange
        when(skillRepository.findById(skillId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> 
            skillService.getSkillById(skillId));
        verify(skillRepository, times(1)).findById(skillId);
    }

    @Test
    void testCreateSkill_Success() {
        // Arrange
        when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.of(portfolio));
        when(skillRepository.save(any(Skill.class))).thenReturn(skill);

        // Act
        SkillResponseDTO result = skillService.createSkill(portfolioId, skillRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Java", result.getName());
        verify(portfolioRepository, times(1)).findById(portfolioId);
        verify(skillRepository, times(1)).save(any(Skill.class));
    }

    @Test
    void testCreateSkill_PortfolioNotFound() {
        // Arrange
        when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> 
            skillService.createSkill(portfolioId, skillRequest));
        verify(portfolioRepository, times(1)).findById(portfolioId);
        verify(skillRepository, never()).save(any());
    }

    @Test
    void testUpdateSkill_Success() {
        // Arrange
        when(skillRepository.findById(skillId)).thenReturn(Optional.of(skill));
        when(skillRepository.save(any(Skill.class))).thenReturn(skill);

        // Act
        SkillResponseDTO result = skillService.updateSkill(skillId, skillRequest);

        // Assert
        assertNotNull(result);
        verify(skillRepository, times(1)).findById(skillId);
        verify(skillRepository, times(1)).save(any(Skill.class));
    }

    @Test
    void testUpdateSkill_NotFound() {
        // Arrange
        when(skillRepository.findById(skillId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> 
            skillService.updateSkill(skillId, skillRequest));
        verify(skillRepository, times(1)).findById(skillId);
        verify(skillRepository, never()).save(any());
    }

    @Test
    void testDeleteSkill_Success() {
        // Arrange
        when(skillRepository.existsById(skillId)).thenReturn(true);
        doNothing().when(skillRepository).deleteById(skillId);

        // Act
        skillService.deleteSkill(skillId);

        // Assert
        verify(skillRepository, times(1)).existsById(skillId);
        verify(skillRepository, times(1)).deleteById(skillId);
    }

    @Test
    void testDeleteSkill_NotFound() {
        // Arrange
        when(skillRepository.existsById(skillId)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> 
            skillService.deleteSkill(skillId));
        verify(skillRepository, times(1)).existsById(skillId);
        verify(skillRepository, never()).deleteById(any());
    }
}
