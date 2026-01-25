package com.SwitchBoard.PortfolioService.DTO.Skill;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SkillRequestDTOTest {

    private Validator validator;
    private SkillRequestDTO dto;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        dto = new SkillRequestDTO();
        dto.setName("Java");
        dto.setCategory("Programming Language");
        dto.setProficiencyLevel(4);
        dto.setYearsOfExperience(3);
        dto.setDescription("Expert in Java");
    }

    @Test
    void testSkillRequestDTO_Valid() {
        // Act
        Set<ConstraintViolation<SkillRequestDTO>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty());
    }

    @Test
    void testSkillRequestDTO_BlankName() {
        // Arrange
        dto.setName("");

        // Act
        Set<ConstraintViolation<SkillRequestDTO>> violations = validator.validate(dto);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Skill name is required")));
    }

    @Test
    void testSkillRequestDTO_ProficiencyLevelTooHigh() {
        // Arrange
        dto.setProficiencyLevel(6);

        // Act
        Set<ConstraintViolation<SkillRequestDTO>> violations = validator.validate(dto);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Proficiency level must be between 1 and 5")));
    }

    @Test
    void testSkillRequestDTO_ProficiencyLevelTooLow() {
        // Arrange
        dto.setProficiencyLevel(0);

        // Act
        Set<ConstraintViolation<SkillRequestDTO>> violations = validator.validate(dto);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Proficiency level must be between 1 and 5")));
    }

    @Test
    void testSkillRequestDTO_SettersAndGetters() {
        // Arrange
        String name = "Python";
        String category = "Programming Language";
        Integer proficiencyLevel = 5;
        Integer yearsOfExperience = 4;
        String description = "Advanced Python developer";

        // Act
        dto.setName(name);
        dto.setCategory(category);
        dto.setProficiencyLevel(proficiencyLevel);
        dto.setYearsOfExperience(yearsOfExperience);
        dto.setDescription(description);

        // Assert
        assertEquals(name, dto.getName());
        assertEquals(category, dto.getCategory());
        assertEquals(proficiencyLevel, dto.getProficiencyLevel());
        assertEquals(yearsOfExperience, dto.getYearsOfExperience());
        assertEquals(description, dto.getDescription());
    }

    @Test
    void testSkillRequestDTO_NoArgsConstructor() {
        // Act
        SkillRequestDTO newDto = new SkillRequestDTO();

        // Assert
        assertNotNull(newDto);
    }

    @Test
    void testSkillRequestDTO_AllArgsConstructor() {
        // Act
        SkillRequestDTO newDto = new SkillRequestDTO(
                "JavaScript", "Programming Language", 4, 2, "Frontend development"
        );

        // Assert
        assertNotNull(newDto);
        assertEquals("JavaScript", newDto.getName());
        assertEquals(4, newDto.getProficiencyLevel());
    }

    @Test
    void testSkillRequestDTO_WithNullOptionalFields() {
        // Arrange
        dto.setCategory(null);
        dto.setProficiencyLevel(null);
        dto.setYearsOfExperience(null);
        dto.setDescription(null);

        // Act
        Set<ConstraintViolation<SkillRequestDTO>> violations = validator.validate(dto);

        // Assert
        // Only name is required, so no violations for null optional fields
        assertTrue(violations.isEmpty());
    }

    @Test
    void testSkillRequestDTO_MinimumProficiencyLevel() {
        // Arrange
        dto.setProficiencyLevel(1);

        // Act
        Set<ConstraintViolation<SkillRequestDTO>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty());
    }

    @Test
    void testSkillRequestDTO_MaximumProficiencyLevel() {
        // Arrange
        dto.setProficiencyLevel(5);

        // Act
        Set<ConstraintViolation<SkillRequestDTO>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty());
    }
}
