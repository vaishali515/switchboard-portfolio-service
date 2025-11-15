package com.SwitchBoard.PortfolioService.Controller;

import com.SwitchBoard.PortfolioService.DTO.ApiResponse;
import com.SwitchBoard.PortfolioService.DTO.Portfolio.PortfolioRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Portfolio.PortfolioResponseDTO;
import com.SwitchBoard.PortfolioService.Service.Portfolio.PortfolioService;
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

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/portfolio")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Portfolio Management", description = "Portfolio management APIs")
public class PortfolioController {

    private final PortfolioService portfolioService;

    // ------------------ GET BY ID ------------------
    @GetMapping("/{portfolioId}")
    @Operation(summary = "Get portfolio by ID")
    public ResponseEntity<PortfolioResponseDTO> getPortfolioById(
            @Parameter(description = "Portfolio ID", required = true)
            @PathVariable UUID portfolioId) {
        return ResponseEntity.ok(portfolioService.getPortfolioById(portfolioId));
    }

    // ------------------ GET BY EMAIL ------------------
    @GetMapping("/user/{emailId}")
    @Operation(summary = "Get portfolio by user email ID")
    public ResponseEntity<PortfolioResponseDTO> getPortfolioByEmail(
            @Parameter(description = "User email ID", required = true)
            @PathVariable String emailId) {
        return ResponseEntity.ok(portfolioService.getPortfolioByEmailId(emailId));
    }

    //----------------GET BY HEADER EMAIL-----------------
    @GetMapping("/user")
    @Operation(summary = "Get portfolio by user email ID")
    public ResponseEntity<PortfolioResponseDTO> getPortfolioByHeaderEmail(
            @Parameter(description = "User email ID", required = true)
            @RequestHeader("X-User-Email") String userEmailIdHeader) {

        return ResponseEntity.ok(portfolioService.getPortfolioByEmailId(userEmailIdHeader));
    }

    // ------------------ CREATE ------------------
    @PostMapping(consumes = {"multipart/form-data"})
    @Operation(summary = "Create a new portfolio with image and resume upload")
    public ResponseEntity<ApiResponse> createPortfolio(
            @ModelAttribute @Valid PortfolioRequestDTO portfolioRequest,
            @RequestHeader("X-User-Email") String userEmailIdHeader) throws IOException {

        log.info("PortfolioController :: createPortfolio :: received request for {}", portfolioRequest.getEmailId());
        portfolioRequest.setEmailId(userEmailIdHeader);
        try {
            PortfolioResponseDTO created = portfolioService.createPortfolio(portfolioRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Portfolio created successfully", created, null));

        } catch (MultipartException e) {
            log.error("Error in multipart processing: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid file upload: " + e.getMessage(), null, "400" ));
        }
    }

    // ------------------ UPDATE ------------------
    @PutMapping(value = "/{portfolioId}", consumes = {"multipart/form-data"})
    @Operation(summary = "Update portfolio details or files")
    public ResponseEntity<ApiResponse> updatePortfolio(
            @PathVariable UUID portfolioId,
            @ModelAttribute @Valid PortfolioRequestDTO portfolioRequest) throws IOException {

        PortfolioResponseDTO updated = portfolioService.updatePortfolio(portfolioId, portfolioRequest);
        return ResponseEntity.ok(ApiResponse.success("Portfolio updated successfully", updated, null));
    }

    // ------------------ DELETE ------------------
    @DeleteMapping("/{portfolioId}")
    @Operation(summary = "Delete a portfolio")
    public ResponseEntity<ApiResponse> deletePortfolio(@PathVariable UUID portfolioId) {
        portfolioService.deletePortfolio(portfolioId);
        return ResponseEntity.ok(ApiResponse.success("Portfolio deleted successfully", true, null));
    }
}
