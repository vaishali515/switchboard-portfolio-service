package com.SwitchBoard.PortfolioService.DTO.Certificate;

import com.SwitchBoard.PortfolioService.config.ValidImage;
import com.SwitchBoard.PortfolioService.config.ValidImageDocument;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Certificate creation/update request payload")
public class CertificateRequestDTO {

    @NotBlank(message = "Certificate title is required")
    @Schema(description = "Title of the certificate", example = "Java Spring Boot Developer")
    private String title;

    @NotBlank(message = "Issuer is required")
    @Schema(description = "Organization or person issuing the certificate", example = "Oracle Academy")
    private String issuer;

    @Schema(description = "Date when the certificate was issued", example = "2024-06-15")
    private LocalDate issueDate;

    @Schema(description = "Expiry date of the certificate, if applicable", example = "2026-06-15")
    private LocalDate expiryDate;

    @Schema(description = "Credential ID of the certificate", example = "CERT123456")
    private String credentialId;

    @Schema(description = "URL to verify the certificate", example = "https://example.com/certificate/123456")
    private String credentialUrl;

    @ValidImageDocument
    @Schema(description = "Certificate image file (optional)")
    private MultipartFile certificateImage;

    @Schema(description = "Description or notes about the certificate", example = "Completed advanced Spring Boot course with distinction")
    private String description;
}
