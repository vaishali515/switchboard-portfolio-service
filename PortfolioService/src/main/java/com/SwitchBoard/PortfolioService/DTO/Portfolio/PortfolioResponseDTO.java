package com.SwitchBoard.PortfolioService.DTO.Portfolio;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Portfolio response payload")
public class PortfolioResponseDTO {

    @Schema(description = "Unique identifier of the portfolio", example = "1")
    private Long id;

    @Schema(description = "Full name of the portfolio owner", example = "John Doe")
    private String fullName;

    @Schema(description = "Professional tagline or headline", example = "Senior Full Stack Developer")
    private String tagline;

    @Schema(description = "Email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Phone number", example = "+1-234-567-8900")
    private String phone;

    @Schema(description = "Location", example = "New York, USA")
    private String location;

    @Schema(description = "Bio or about me section", example = "Passionate developer with 5+ years of experience...")
    private String bio;

    @Schema(description = "URL of the profile picture", example = "https://example.com/images/profile.jpg")
    private String profilePictureUrl;

    @Schema(description = "LinkedIn profile URL", example = "https://linkedin.com/in/johndoe")
    private String linkedinUrl;

    @Schema(description = "GitHub profile URL", example = "https://github.com/johndoe")
    private String githubUrl;

    @Schema(description = "Personal website URL", example = "https://johndoe.dev")
    private String websiteUrl;

    @Schema(description = "Resume download URL", example = "https://example.com/resume.pdf")
    private String resumeUrl;

    @Schema(description = "Additional contact information or social links", example = "Twitter: @johndoe")
    private String additionalLinks;
}
