package com.SwitchBoard.PortfolioService.DTO.Certificate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Certificate response payload")
public class CertificateResponseDTO {

    @Schema(description = "Unique identifier of the certificate", example = "1")
    private UUID id;

    @Schema(description = "Title of the certificate", example = "Java Spring Boot Developer")
    private String title;

    @Schema(description = "Organization or person issuing the certificate", example = "Oracle Academy")
    private String issuer;

    @Schema(description = "Date when the certificate was issued (YYYY-MM format)", example = "2024-06")
    private YearMonth issueDate;

    @Schema(description = "Expiry date of the certificate, if applicable (YYYY-MM format)", example = "2026-06")
    private YearMonth expiryDate;

    @Schema(description = "Credential ID of the certificate", example = "CERT123456")
    private String credentialId;

    @Schema(description = "URL to verify the certificate", example = "https://example.com/certificate/123456")
    private String credentialUrl;

    @Schema(description = "URL of the certificate image", example = "https://example.com/images/certificate123.jpg")
    private String certificateImageUrl;

    @Schema(description = "Description or notes about the certificate", example = "Completed advanced Spring Boot course with distinction")
    private String description;
}
