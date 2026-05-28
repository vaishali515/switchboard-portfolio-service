package com.SwitchBoard.PortfolioService.DTO.Portfolio;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PortfolioRequestDTOTest {

    private Validator validator;
    private PortfolioRequestDTO dto;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        dto = new PortfolioRequestDTO();
        dto.setFullName("John Doe");
        dto.setEmailId("test@example.com");
        dto.setContactNumber("+1234567890");
        dto.setBio("Test bio");
        dto.setSocialLinks(new ArrayList<>());
    }

    @Test
    void testPortfolioRequestDTO_Valid() {
        // Act
        Set<ConstraintViolation<PortfolioRequestDTO>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty());
    }

    @Test
    void testPortfolioRequestDTO_BlankFullName() {
        // Arrange
        dto.setFullName("");

        // Act
        Set<ConstraintViolation<PortfolioRequestDTO>> violations = validator.validate(dto);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Full name is required")));
    }

    @Test
    void testPortfolioRequestDTO_InvalidEmail() {
        // Arrange
        dto.setEmailId("invalid-email");

        // Act
        Set<ConstraintViolation<PortfolioRequestDTO>> violations = validator.validate(dto);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("valid email")));
    }

    @Test
    void testPortfolioRequestDTO_SettersAndGetters() {
        // Arrange
        String fullName = "Jane Smith";
        String emailId = "jane@example.com";
        String contactNumber = "+9876543210";
        String bio = "Updated bio";
        String overview = "Software Engineer";
        String githubLink = "https://github.com/jane";
        String linkedInLink = "https://linkedin.com/in/jane";
        String twitterLink = "https://twitter.com/jane";
        String leetcodeLink = "https://leetcode.com/jane";
        String personalWebsiteLink = "https://jane.dev";
        List<String> socialLinks = List.of("https://medium.com/@jane");
        
        MockMultipartFile profileImage = new MockMultipartFile(
                "profileImage", "test.jpg", "image/jpeg", "test".getBytes());
        MockMultipartFile resume = new MockMultipartFile(
                "resume", "resume.pdf", "application/pdf", "test".getBytes());

        // Act
        dto.setFullName(fullName);
        dto.setEmailId(emailId);
        dto.setContactNumber(contactNumber);
        dto.setBio(bio);
        dto.setOverview(overview);
        dto.setGithubLink(githubLink);
        dto.setLinkedInLink(linkedInLink);
        dto.setTwitterLink(twitterLink);
        dto.setLeetcodeLink(leetcodeLink);
        dto.setPersonalWebsiteLink(personalWebsiteLink);
        dto.setSocialLinks(socialLinks);
        dto.setProfileImage(profileImage);
        dto.setResume(resume);

        // Assert
        assertEquals(fullName, dto.getFullName());
        assertEquals(emailId, dto.getEmailId());
        assertEquals(contactNumber, dto.getContactNumber());
        assertEquals(bio, dto.getBio());
        assertEquals(overview, dto.getOverview());
        assertEquals(githubLink, dto.getGithubLink());
        assertEquals(linkedInLink, dto.getLinkedInLink());
        assertEquals(twitterLink, dto.getTwitterLink());
        assertEquals(leetcodeLink, dto.getLeetcodeLink());
        assertEquals(personalWebsiteLink, dto.getPersonalWebsiteLink());
        assertEquals(socialLinks, dto.getSocialLinks());
        assertEquals(profileImage, dto.getProfileImage());
        assertEquals(resume, dto.getResume());
    }

    @Test
    void testPortfolioRequestDTO_NoArgsConstructor() {
        // Act
        PortfolioRequestDTO newDto = new PortfolioRequestDTO();

        // Assert
        assertNotNull(newDto);
    }

    @Test
    void testPortfolioRequestDTO_AllArgsConstructor() {
        // Act
        PortfolioRequestDTO newDto = new PortfolioRequestDTO(
                "John Doe", "test@example.com", "+1234567890",
                "Bio", null, new ArrayList<>(),
                "https://leetcode.com/test", "https://github.com/test",
                "https://linkedin.com/in/test", "https://twitter.com/test",
                "https://test.dev", "Overview", null
        );

        // Assert
        assertNotNull(newDto);
        assertEquals("John Doe", newDto.getFullName());
        assertEquals("test@example.com", newDto.getEmailId());
    }

    @Test
    void testPortfolioRequestDTO_WithNullOptionalFields() {
        // Arrange
        dto.setContactNumber(null);
        dto.setBio(null);
        dto.setOverview(null);
        dto.setSocialLinks(null);

        // Act
        Set<ConstraintViolation<PortfolioRequestDTO>> violations = validator.validate(dto);

        // Assert
        // Only fullName and emailId are required, so no violations
        assertTrue(violations.isEmpty());
    }
}
