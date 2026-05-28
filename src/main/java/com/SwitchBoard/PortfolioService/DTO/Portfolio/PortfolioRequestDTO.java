package com.SwitchBoard.PortfolioService.DTO.Portfolio;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import com.SwitchBoard.PortfolioService.config.ValidImage;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Portfolio creation/update request payload")
public class PortfolioRequestDTO {

    @NotBlank(message = "Full name is required")
    @Schema(description = "Full name of the portfolio owner", example = "John Doe")
    private String fullName;

    @Email(message = "Please provide a valid email address")
    @Schema(description = "Email address", example = "john.doe@example.com")
    private String emailId;
    
    @Schema(description = "Contact number", example = "+1234567890")
    private String contactNumber;

    @Schema(description = "Short bio or about section", example = "Passionate backend developer with 3+ years of experience...")
    private String bio;

    @ValidImage
    @Schema(description = "Profile picture file to upload")
    private MultipartFile profileImage;

    @Schema(description = "Social media or external links", example = "[\"https://twitter.com/johndoe\", \"https://medium.com/@johndoe\"]")
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

    @Schema(description = "Resume file to upload")
    private MultipartFile resume;
}
