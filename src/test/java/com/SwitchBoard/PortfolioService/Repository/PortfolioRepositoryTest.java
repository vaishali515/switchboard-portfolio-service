package com.SwitchBoard.PortfolioService.Repository;

import com.SwitchBoard.PortfolioService.Entity.Portfolio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PortfolioRepositoryTest {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindByEmailId_Success() {
        // Arrange
        Portfolio portfolio = new Portfolio();
        portfolio.setEmailId("test@example.com");
        portfolio.setFullName("John Doe");
        portfolio.setContactNumber("+1234567890");
        portfolio.setBio("Test bio");
        portfolio.setSkills(new ArrayList<>());
        portfolio.setProjects(new ArrayList<>());
        portfolio.setEducations(new ArrayList<>());
        portfolio.setExperiences(new ArrayList<>());
        portfolio.setCertificates(new ArrayList<>());
        portfolio.setAchievements(new ArrayList<>());
        
        entityManager.persist(portfolio);
        entityManager.flush();

        // Act
        Optional<Portfolio> result = portfolioRepository.findByEmailId("test@example.com");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getFullName());
        assertEquals("test@example.com", result.get().getEmailId());
    }

    @Test
    void testFindByEmailId_NotFound() {
        // Act
        Optional<Portfolio> result = portfolioRepository.findByEmailId("nonexistent@example.com");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testSavePortfolio() {
        // Arrange
        Portfolio portfolio = new Portfolio();
        portfolio.setEmailId("save@example.com");
        portfolio.setFullName("Jane Smith");
        portfolio.setContactNumber("+9876543210");
        portfolio.setBio("New bio");
        portfolio.setSkills(new ArrayList<>());
        portfolio.setProjects(new ArrayList<>());
        portfolio.setEducations(new ArrayList<>());
        portfolio.setExperiences(new ArrayList<>());
        portfolio.setCertificates(new ArrayList<>());
        portfolio.setAchievements(new ArrayList<>());

        // Act
        Portfolio saved = portfolioRepository.save(portfolio);

        // Assert
        assertNotNull(saved.getId());
        assertEquals("save@example.com", saved.getEmailId());
        assertEquals("Jane Smith", saved.getFullName());
    }

    @Test
    void testDeletePortfolio() {
        // Arrange
        Portfolio portfolio = new Portfolio();
        portfolio.setEmailId("delete@example.com");
        portfolio.setFullName("Delete User");
        portfolio.setContactNumber("+1111111111");
        portfolio.setBio("Delete test");
        portfolio.setSkills(new ArrayList<>());
        portfolio.setProjects(new ArrayList<>());
        portfolio.setEducations(new ArrayList<>());
        portfolio.setExperiences(new ArrayList<>());
        portfolio.setCertificates(new ArrayList<>());
        portfolio.setAchievements(new ArrayList<>());
        
        Portfolio saved = entityManager.persist(portfolio);
        entityManager.flush();

        // Act
        portfolioRepository.delete(saved);
        Optional<Portfolio> result = portfolioRepository.findById(saved.getId());

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testFindById_Success() {
        // Arrange
        Portfolio portfolio = new Portfolio();
        portfolio.setEmailId("findbyid@example.com");
        portfolio.setFullName("Find User");
        portfolio.setContactNumber("+2222222222");
        portfolio.setBio("Find test");
        portfolio.setSkills(new ArrayList<>());
        portfolio.setProjects(new ArrayList<>());
        portfolio.setEducations(new ArrayList<>());
        portfolio.setExperiences(new ArrayList<>());
        portfolio.setCertificates(new ArrayList<>());
        portfolio.setAchievements(new ArrayList<>());
        
        Portfolio saved = entityManager.persist(portfolio);
        entityManager.flush();

        // Act
        Optional<Portfolio> result = portfolioRepository.findById(saved.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Find User", result.get().getFullName());
    }
}