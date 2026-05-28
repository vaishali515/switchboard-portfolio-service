package com.SwitchBoard.PortfolioService.DTO.Portfolio;

import com.SwitchBoard.PortfolioService.DTO.Achievement.AchievementResponseDTO;
import com.SwitchBoard.PortfolioService.DTO.Certificate.CertificateResponseDTO;
import com.SwitchBoard.PortfolioService.DTO.Education.EducationResponseDTO;
import com.SwitchBoard.PortfolioService.DTO.Experience.ExperienceResponseDTO;
import com.SwitchBoard.PortfolioService.DTO.Project.ProjectResponseDTO;
import com.SwitchBoard.PortfolioService.DTO.Skill.SkillResponseDTO;
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
    
    @Schema(description = "Contact number", example = "+1234567890")
    private String contactNumber;

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
    private List<SkillResponseDTO> skills;

    @Schema(description = "Projects associated with the portfolio")
    private List<ProjectResponseDTO> projects;

    @Schema(description = "Education history associated with the portfolio")
    private List<EducationResponseDTO> educations;

    @Schema(description = "Work experience associated with the portfolio")
    private List<ExperienceResponseDTO> experiences;

    @Schema(description = "Certificates associated with the portfolio")
    private List<CertificateResponseDTO> certificates;

    @Schema(description = "Achievements associated with the portfolio")
    private List<AchievementResponseDTO> achievements;
}
