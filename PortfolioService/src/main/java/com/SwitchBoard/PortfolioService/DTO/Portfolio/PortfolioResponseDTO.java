package com.SwitchBoard.PortfolioService.DTO.Portfolio;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Portfolio response payload")
public class PortfolioResponseDTO {

    @Schema(description = "Unique identifier of the portfolio", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Full name of the portfolio owner", example = "John Doe")
    private String fullName;

    @Schema(description = "Email address", example = "john.doe@example.com")
    private String emailId;

    @Schema(description = "Short bio or about section", example = "Passionate backend developer with 3+ years of experience...")
    private String bio;

    @Schema(description = "URL of the profile image", example = "https://example.com/images/profile.jpg")
    private String profileImageUrl;

    @Schema(description = "List of social media links", example = "[\"https://twitter.com/johndoe\", \"https://medium.com/@johndoe\"]")
    private List<String> socialLinks;

    @Schema(description = "LeetCode profile URL", example = "https://leetcode.com/johndoe")
    private String leetcodeLink;

    @Schema(description = "GitHub profile URL", example = "https://github.com/johndoe")
    private String githubLink;

    @Schema(description = "LinkedIn profile URL", example = "https://linkedin.com/in/johndoe")
    private String linkedInLink;

    @Schema(description = "Twitter profile URL", example = "https://twitter.com/johndoe")
    private String twitterLink;

    @Schema(description = "Personal website URL", example = "https://johndoe.dev")
    private String personalWebsiteLink;

    @Schema(description = "Professional overview section", example = "Experienced software engineer specializing in Java and Spring Boot...")
    private String overview;

    @Schema(description = "Resume download link", example = "https://example.com/resume.pdf")
    private String resumeLink;

    @Schema(description = "Skills associated with the portfolio")
    private List<String> skills;

    @Schema(description = "Projects associated with the portfolio")
    private List<String> projects;

    @Schema(description = "Education history associated with the portfolio")
    private List<String> educations;

    @Schema(description = "Work experience associated with the portfolio")
    private List<String> experiences;

    @Schema(description = "Certificates associated with the portfolio")
    private List<String> certificates;

    @Schema(description = "Achievements associated with the portfolio")
    private List<String> achievements;
}
