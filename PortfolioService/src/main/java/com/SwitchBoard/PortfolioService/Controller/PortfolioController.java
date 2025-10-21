package com.SwitchBoard.PortfolioService.Controller;

import com.SwitchBoard.PortfolioService.DTO.Portfolio.PortfolioRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Portfolio.PortfolioResponseDTO;
import com.SwitchBoard.PortfolioService.DTO.ApiResponse;
import com.SwitchBoard.PortfolioService.Service.Portfolio.FileService;
import com.SwitchBoard.PortfolioService.Service.Portfolio.PortfolioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/portfolio")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Portfolio Management", description = "Portfolio management APIs")
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final FileService fileService;

    @Operation(
            summary = "Get portfolio by ID",
            description = "Get a portfolio by its unique identifier",
            tags = { "Portfolio Management" }
    )
    @GetMapping("/{portfolioId}")
    public ResponseEntity<PortfolioResponseDTO> getPortfolioById(
            @Parameter(description = "Portfolio ID", required = true)
            @PathVariable UUID portfolioId) {
        PortfolioResponseDTO portfolio = portfolioService.getPortfolioById(portfolioId);
        return ResponseEntity.ok(portfolio);
    }

    @Operation(
            summary = "Get portfolio by email ID",
            description = "Get a portfolio by user's email identifier",
            tags = { "Portfolio Management" }
    )
    @GetMapping("/user/{emailId}")
    public ResponseEntity<PortfolioResponseDTO> getPortfolioByUserId(
            @Parameter(description = "User's email ID", required = true)
            @PathVariable String emailId) {
        PortfolioResponseDTO portfolio = portfolioService.getPortfolioByEmailId(emailId);
        return ResponseEntity.ok(portfolio);
    }

    @Operation(
            summary = "Create a new portfolio",
            description = "Create a new portfolio with optional profile image upload",
            tags = { "Portfolio Management" }
    )
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse> createPortfolio(
            @Parameter(description = "Portfolio data", required = true)
            @ModelAttribute @Valid PortfolioRequestDTO portfolioRequest
    ) throws IOException {
        log.info("PortfolioController :: createPortfolio :: received request to create portfolio for email: {}", portfolioRequest.getEmail());
        try {
            MultipartFile profilePicture = portfolioRequest.getProfilePicture();
            if (profilePicture != null && !profilePicture.isEmpty()) {
                String contentType = profilePicture.getContentType();
                log.info("PortfolioController :: createPortfolio :: image content type: {}", contentType);
                if (contentType == null || !contentType.startsWith("image/")) {
                    log.error("PortfolioController :: createPortfolio :: invalid image content type: {}", contentType);
                    return ResponseEntity.badRequest()
                            .body(ApiResponse.error("Invalid image type. Only image files are allowed.", null));
                }

                String imageUrl = fileService.uploadImage("portfolio-service", profilePicture);
                log.info("PortfolioController :: createPortfolio :: uploaded image URL: {}", imageUrl);
            }

            PortfolioResponseDTO created = portfolioService.createPortfolio(portfolioRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Portfolio created successfully", created, null));

        } catch (MultipartException e) {
            log.error("PortfolioController :: createPortfolio :: multipart error: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error processing profile picture: " + e.getMessage(), null));
        }
    }

    @Operation(
            summary = "Update an existing portfolio",
            description = "Update a portfolio's details with optional profile image update",
            tags = { "Portfolio Management" }
    )
    @PutMapping(value = "/{portfolioId}", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse> updatePortfolio(
            @Parameter(description = "Portfolio ID", required = true)
            @PathVariable UUID portfolioId,
            @Parameter(description = "Updated portfolio data", required = true)
            @ModelAttribute @Valid PortfolioRequestDTO portfolioRequest
    ) throws IOException {
        try {
            MultipartFile profilePicture = portfolioRequest.getProfilePicture();
            if (profilePicture != null && !profilePicture.isEmpty()) {
                String contentType = profilePicture.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.badRequest()
                            .body(ApiResponse.error("Invalid image type. Only image files are allowed.", null));
                }

                String imageUrl = fileService.uploadImage("portfolio-service", profilePicture);
                log.info("PortfolioController :: updatePortfolio :: uploaded new image URL: {}", imageUrl);
            }

            PortfolioResponseDTO updated = portfolioService.updatePortfolio(portfolioId, portfolioRequest);
            return ResponseEntity.ok(ApiResponse.success("Portfolio updated successfully", updated, null));

        } catch (MultipartException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error processing profile picture: " + e.getMessage(), null));
        }
    }

    @Operation(
            summary = "Delete a portfolio",
            description = "Delete a portfolio and all associated data",
            tags = { "Portfolio Management" }
    )
    @DeleteMapping("/{portfolioId}")
    public ResponseEntity<ApiResponse> deletePortfolio(
            @Parameter(description = "Portfolio ID", required = true)
            @PathVariable UUID portfolioId) {
        portfolioService.deletePortfolio(portfolioId);
        return ResponseEntity.ok(ApiResponse.success("Portfolio deleted successfully", true, null));
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ApiResponse> handleMultipartException(MultipartException e) {
        log.error("PortfolioController :: handleMultipartException :: error handling multipart request: {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Error processing multipart request: Please ensure the request is properly formatted", null));
    }
}

