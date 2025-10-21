package com.SwitchBoard.PortfolioService.Controller;

import com.SwitchBoard.PortfolioService.DTO.Certificate.CertificateRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Certificate.CertificateResponseDTO;
import com.SwitchBoard.PortfolioService.DTO.ApiResponse;
import com.SwitchBoard.PortfolioService.Service.Portfolio.CertificateService;
import com.SwitchBoard.PortfolioService.Service.Portfolio.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/portfolios/{portfolioId}/certificates")
@Tag(name = "Certificate Management", description = "APIs for managing certificates in portfolios")
public class CertificateController {

    private final CertificateService certificateService;
    private final FileService fileService;

    @Operation(summary = "Get all certificates for a portfolio")
    @GetMapping
    public ResponseEntity<List<CertificateResponseDTO>> getAllCertificates(
            @Parameter(description = "ID of the portfolio to retrieve certificates from")
            @PathVariable UUID portfolioId) {
        log.info("CertificateController :: getAllCertificates :: fetching all certificates for portfolio: {}", portfolioId);
        List<CertificateResponseDTO> certificates = certificateService.getAllCertificatesByPortfolioId(portfolioId);
        return ResponseEntity.ok(certificates);
    }

    @Operation(summary = "Get a certificate by ID")
    @GetMapping("/{certificateId}")
    public ResponseEntity<CertificateResponseDTO> getCertificateById(
            @Parameter(description = "ID of the certificate to retrieve")
            @PathVariable UUID certificateId) {
        log.info("CertificateController :: getCertificateById :: fetching certificate: {}", certificateId);
        CertificateResponseDTO certificate = certificateService.getCertificateById(certificateId);
        return ResponseEntity.ok(certificate);
    }

    @Operation(summary = "Create a new certificate")
    @PostMapping
    public ResponseEntity<ApiResponse> createCertificate(
            @Parameter(description = "ID of the portfolio to add certificate to")
            @PathVariable UUID portfolioId,
            @Parameter(description = "Certificate details")
            @Valid @ModelAttribute CertificateRequestDTO certificateRequest,
            @Parameter(description = "Certificate image (optional, must be an image file)")
            @RequestPart(value = "certificateImage", required = false) MultipartFile certificateImage) throws IOException {
        try {
            if (certificateImage != null && !certificateImage.isEmpty()) {
                String contentType = certificateImage.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse("Invalid image type. Only image files are allowed.", null, "400"));
                }

                String imageUrl = fileService.uploadImage("portfolio-service", certificateImage);
                certificateRequest.setCertificateImage(certificateImage);
            }

            CertificateResponseDTO created = certificateService.createCertificate(portfolioId, certificateRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Certificate created successfully", created, "201"));
        } catch (MultipartException e) {
            log.error("CertificateController :: createCertificate :: multipart error: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("Error processing certificate image: " + e.getMessage(), null, "400"));
        }
    }

    @Operation(summary = "Update an existing certificate")
    @PutMapping("/{certificateId}")
    public ResponseEntity<ApiResponse> updateCertificate(
            @Parameter(description = "ID of the certificate to update")
            @PathVariable UUID certificateId,
            @Parameter(description = "Updated certificate details")
            @Valid @ModelAttribute CertificateRequestDTO certificateRequest,
            @Parameter(description = "Updated certificate image (optional, must be an image file)")
            @RequestPart(value = "certificateImage", required = false) MultipartFile certificateImage) throws IOException {
        try {
            if (certificateImage != null && !certificateImage.isEmpty()) {
                String contentType = certificateImage.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse("Invalid image type. Only image files are allowed.", null, "400"));
                }

                String imageUrl = fileService.uploadImage("portfolio-service", certificateImage);
                certificateRequest.setCertificateImage(certificateImage);
            }

            CertificateResponseDTO updated = certificateService.updateCertificate(certificateId, certificateRequest);
            return ResponseEntity.ok(new ApiResponse("Certificate updated successfully", updated, "200"));
        } catch (MultipartException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("Error processing certificate image: " + e.getMessage(), null, "400"));
        }
    }

    @Operation(summary = "Delete a certificate")
    @DeleteMapping("/{certificateId}")
    public ResponseEntity<ApiResponse> deleteCertificate(
            @Parameter(description = "ID of the certificate to delete")
            @PathVariable UUID certificateId) {
        log.info("CertificateController :: deleteCertificate :: deleting certificate: {}", certificateId);
        certificateService.deleteCertificate(certificateId);
        return ResponseEntity.ok(new ApiResponse("Certificate deleted successfully", true, "200"));
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ApiResponse> handleMultipartException(MultipartException e) {
        log.error("CertificateController :: handleMultipartException :: error handling multipart request: {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(new ApiResponse("Error processing certificate image: Please ensure the request is properly formatted", null, "400"));
    }
}
