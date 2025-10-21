package com.SwitchBoard.PortfolioService.DTO.Portfolio;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import com.SwitchBoard.PortfolioService.config.ValidImage;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Portfolio creation/update request payload")
public class PortfolioRequestDTO {

    @NotBlank(message = "Full name is required")
    @Schema(description = "Full name of the portfolio owner", example = "John Doe")
    private String fullName;

    @NotBlank(message = "Tagline is required")
    @Schema(description = "Professional tagline or headline", example = "Senior Full Stack Developer")
    private String tagline;

    @Email(message = "Please provide a valid email address")
    @Schema(description = "Email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Phone number", example = "+1-234-567-8900")
    private String phone;

    @Schema(description = "Location", example = "New York, USA")
    private String location;

    @Schema(description = "Bio or about me section", example = "Passionate developer with 5+ years of experience...")
    private String bio;

    @ValidImage
    @Schema(description = "Profile picture")
    private MultipartFile profilePicture;

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
