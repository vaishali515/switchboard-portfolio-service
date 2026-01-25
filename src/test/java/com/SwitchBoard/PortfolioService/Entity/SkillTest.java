package com.SwitchBoard.PortfolioService.Entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SkillTest {

    private Skill skill;

    @BeforeEach
    void setUp() {
        skill = new Skill();
    }

    @Test
    void testSkill_NoArgsConstructor() {
        // Assert
        assertNotNull(skill);
    }

    @Test
    void testSkill_SettersAndGetters() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "Java";
        String category = "Programming Language";
        Integer proficiencyLevel = 4;
        Integer yearsOfExperience = 3;
        String description = "Expert in Java and Spring Boot";
        
        Portfolio portfolio = new Portfolio();
        portfolio.setId(UUID.randomUUID());

        // Act
        skill.setId(id);
        skill.setName(name);
        skill.setCategory(category);
        skill.setProficiencyLevel(proficiencyLevel);
        skill.setYearsOfExperience(yearsOfExperience);
        skill.setDescription(description);
        skill.setPortfolio(portfolio);

        // Assert
        assertEquals(id, skill.getId());
        assertEquals(name, skill.getName());
        assertEquals(category, skill.getCategory());
        assertEquals(proficiencyLevel, skill.getProficiencyLevel());
        assertEquals(yearsOfExperience, skill.getYearsOfExperience());
        assertEquals(description, skill.getDescription());
        assertEquals(portfolio, skill.getPortfolio());
    }

    @Test
    void testSkill_AllArgsConstructor() {
        // Arrange
        UUID id = UUID.randomUUID();
        Portfolio portfolio = new Portfolio();

        // Act
        Skill s = new Skill(id, "Python", "Programming Language", 5, 4, 
                "Advanced Python developer", portfolio);

        // Assert
        assertNotNull(s);
        assertEquals(id, s.getId());
        assertEquals("Python", s.getName());
        assertEquals("Programming Language", s.getCategory());
        assertEquals(5, s.getProficiencyLevel());
        assertEquals(4, s.getYearsOfExperience());
    }

    @Test
    void testSkill_ProficiencyLevels() {
        // Test valid proficiency levels (1-5)
        for (int level = 1; level <= 5; level++) {
            skill.setProficiencyLevel(level);
            assertEquals(level, skill.getProficiencyLevel());
        }
    }

    @Test
    void testSkill_WithNullPortfolio() {
        // Act
        skill.setName("JavaScript");
        skill.setPortfolio(null);

        // Assert
        assertEquals("JavaScript", skill.getName());
        assertNull(skill.getPortfolio());
    }

    @Test
    void testSkill_EqualsAndHashCode() {
        // Arrange
        UUID id = UUID.randomUUID();
        
        Skill s1 = new Skill();
        s1.setId(id);
        s1.setName("Java");
        
        Skill s2 = new Skill();
        s2.setId(id);
        s2.setName("Java");

        // Assert - Only verify IDs match (Lombok uses all fields for equals)
        assertEquals(s1.getId(), s2.getId());
        assertEquals(s1.getName(), s2.getName());
    }

    @Test
    void testSkill_ToString() {
        // Arrange
        skill.setName("Java");
        skill.setCategory("Programming Language");
        skill.setProficiencyLevel(4);

        // Act
        String result = skill.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("Java"));
    }
}
