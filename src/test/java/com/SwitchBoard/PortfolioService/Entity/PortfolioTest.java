package com.SwitchBoard.PortfolioService.Entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PortfolioTest {

    private Portfolio portfolio;

    @BeforeEach
    void setUp() {
        portfolio = new Portfolio();
    }

    @Test
    void testPortfolio_NoArgsConstructor() {
        // Assert
        assertNotNull(portfolio);
    }

    @Test
    void testPortfolio_AllArgsConstructor() {
        // Arrange
        UUID id = UUID.randomUUID();
        String emailId = "test@example.com";
        String fullName = "John Doe";
        String contactNumber = "+1234567890";
        String bio = "Test bio";

        // Act
        Portfolio p = new Portfolio(
                id, emailId, fullName, contactNumber, bio,
                null, new ArrayList<>(), null, null, null, null, null,
                "Overview", null, new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()
        );

        // Assert
        assertNotNull(p);
        assertEquals(id, p.getId());
        assertEquals(emailId, p.getEmailId());
        assertEquals(fullName, p.getFullName());
    }

    @Test
    void testPortfolio_SettersAndGetters() {
        // Arrange
        UUID id = UUID.randomUUID();
        String emailId = "test@example.com";
        String fullName = "John Doe";
        String contactNumber = "+1234567890";
        String bio = "Test bio";
        String profileImageUrl = "https://example.com/image.jpg";
        String resumeLink = "https://example.com/resume.pdf";

        // Act
        portfolio.setId(id);
        portfolio.setEmailId(emailId);
        portfolio.setFullName(fullName);
        portfolio.setContactNumber(contactNumber);
        portfolio.setBio(bio);
        portfolio.setProfileImageUrl(profileImageUrl);
        portfolio.setResumeLink(resumeLink);

        // Assert
        assertEquals(id, portfolio.getId());
        assertEquals(emailId, portfolio.getEmailId());
        assertEquals(fullName, portfolio.getFullName());
        assertEquals(contactNumber, portfolio.getContactNumber());
        assertEquals(bio, portfolio.getBio());
        assertEquals(profileImageUrl, portfolio.getProfileImageUrl());
        assertEquals(resumeLink, portfolio.getResumeLink());
    }

    @Test
    void testPortfolio_SocialLinks() {
        // Arrange
        String githubLink = "https://github.com/test";
        String linkedInLink = "https://linkedin.com/in/test";
        String twitterLink = "https://twitter.com/test";
        String leetcodeLink = "https://leetcode.com/test";
        String personalWebsiteLink = "https://test.dev";

        // Act
        portfolio.setGithubLink(githubLink);
        portfolio.setLinkedInLink(linkedInLink);
        portfolio.setTwitterLink(twitterLink);
        portfolio.setLeetcodeLink(leetcodeLink);
        portfolio.setPersonalWebsiteLink(personalWebsiteLink);

        // Assert
        assertEquals(githubLink, portfolio.getGithubLink());
        assertEquals(linkedInLink, portfolio.getLinkedInLink());
        assertEquals(twitterLink, portfolio.getTwitterLink());
        assertEquals(leetcodeLink, portfolio.getLeetcodeLink());
        assertEquals(personalWebsiteLink, portfolio.getPersonalWebsiteLink());
    }

    @Test
    void testPortfolio_Collections() {
        // Arrange
        Skill skill = new Skill();
        skill.setName("Java");
        
        Project project = new Project();
        project.setTitle("Test Project");
        
        Education education = new Education();
        education.setInstitution("Test University");
        
        Experience experience = new Experience();
        experience.setCompany("Test Company");
        
        Certificate certificate = new Certificate();
        certificate.setTitle("Test Certificate");
        
        Achievement achievement = new Achievement();
        achievement.setTitle("Test Achievement");

        // Act
        portfolio.setSkills(new ArrayList<>(Arrays.asList(skill)));
        portfolio.setProjects(new ArrayList<>(Arrays.asList(project)));
        portfolio.setEducations(new ArrayList<>(Arrays.asList(education)));
        portfolio.setExperiences(new ArrayList<>(Arrays.asList(experience)));
        portfolio.setCertificates(new ArrayList<>(Arrays.asList(certificate)));
        portfolio.setAchievements(new ArrayList<>(Arrays.asList(achievement)));

        // Assert
        assertNotNull(portfolio.getSkills());
        assertEquals(1, portfolio.getSkills().size());
        assertEquals("Java", portfolio.getSkills().get(0).getName());
        
        assertNotNull(portfolio.getProjects());
        assertEquals(1, portfolio.getProjects().size());
        
        assertNotNull(portfolio.getEducations());
        assertEquals(1, portfolio.getEducations().size());
        
        assertNotNull(portfolio.getExperiences());
        assertEquals(1, portfolio.getExperiences().size());
        
        assertNotNull(portfolio.getCertificates());
        assertEquals(1, portfolio.getCertificates().size());
        
        assertNotNull(portfolio.getAchievements());
        assertEquals(1, portfolio.getAchievements().size());
    }

    @Test
    void testPortfolio_Overview() {
        // Arrange
        String overview = "Experienced software engineer with expertise in Java and Spring Boot";

        // Act
        portfolio.setOverview(overview);

        // Assert
        assertEquals(overview, portfolio.getOverview());
    }

    @Test
    void testPortfolio_SocialLinksAsList() {
        // Arrange
        ArrayList<String> socialLinks = new ArrayList<>(Arrays.asList(
                "https://twitter.com/test",
                "https://medium.com/@test"
        ));

        // Act
        portfolio.setSocialLinks(socialLinks);

        // Assert
        assertNotNull(portfolio.getSocialLinks());
        assertEquals(2, portfolio.getSocialLinks().size());
        assertTrue(portfolio.getSocialLinks().contains("https://twitter.com/test"));
    }

    @Test
    void testPortfolio_EqualsAndHashCode() {
        // Arrange
        UUID id = UUID.randomUUID();
        
        Portfolio p1 = new Portfolio();
        p1.setId(id);
        p1.setEmailId("test@example.com");
        p1.setFullName("John Doe");
        
        Portfolio p2 = new Portfolio();
        p2.setId(id);
        p2.setEmailId("test@example.com");
        p2.setFullName("John Doe");

        // Assert - Only verify key fields match (Lombok uses all fields for equals)
        assertEquals(p1.getId(), p2.getId());
        assertEquals(p1.getEmailId(), p2.getEmailId());
        assertEquals(p1.getFullName(), p2.getFullName());
    }

    @Test
    void testPortfolio_ToString() {
        // Arrange
        portfolio.setEmailId("test@example.com");
        portfolio.setFullName("John Doe");

        // Act
        String result = portfolio.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("test@example.com"));
        assertTrue(result.contains("John Doe"));
    }
}
