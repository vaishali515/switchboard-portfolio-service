package com.SwitchBoard.PortfolioService.Controller;

import com.SwitchBoard.PortfolioService.DTO.*;
import com.SwitchBoard.PortfolioService.Service.Portfolio.FileService;
import com.SwitchBoard.PortfolioService.Service.Portfolio.PortfolioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/portfolio")
@RequiredArgsConstructor
@Slf4j
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final FileService fileService;

    @GetMapping("/{portfolioId}")
    public ResponseEntity<PortfolioDTO> getPortfolioById(@PathVariable UUID portfolioId) {
        PortfolioDTO portfolio = portfolioService.getPortfolioById(portfolioId);
        return ResponseEntity.ok(portfolio);
    }

    @GetMapping("/user/{emailId}")
    public ResponseEntity<PortfolioDTO> getPortfolioByUserId(@PathVariable String emailId) {
        PortfolioDTO portfolio = portfolioService.getPortfolioByEmailId(emailId);
        return ResponseEntity.ok(portfolio);
    }


    @PostMapping
    public ResponseEntity<PortfolioDTO> createPortfolio(
            @Valid @RequestBody PortfolioRequest portfolioRequest, @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

            try {
                if (image != null && !image.isEmpty()) {

                    String contentType = image.getContentType();
                    if (contentType == null || !contentType.startsWith("image/")) {
                        return ResponseEntity.badRequest().body(null);
                    }

                    String imageUrl = fileService.uploadImage("portfolio-service", image);
                    portfolioRequest.setProfileImageUrl(imageUrl);
                }


                PortfolioDTO createdPortfolio = portfolioService.createPortfolio(portfolioRequest);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdPortfolio);

            } catch (MultipartException e) {
                log.error("InterviewExperienceController :: createInterviewExperience :: multipart error: {}", e.getMessage());
                throw new RuntimeException("Error processing multipart request: " + e.getMessage());
            } catch (Exception e) {
                log.error("InterviewExperienceController :: createInterviewExperience :: error: {}", e.getMessage());
                throw e;
            }
        }

        @ExceptionHandler(MultipartException.class)
        public ResponseEntity<String> handleMultipartException(MultipartException e) {
            log.error("InterviewExperienceController :: handleMultipartException :: error handling multipart request: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error processing multipart request: Please ensure the request is properly formatted");
        }


    @PutMapping("/{portfolioId}")
    public ResponseEntity<PortfolioDTO> updatePortfolio(
                        @PathVariable UUID portfolioId,
                        @Valid @RequestBody PortfolioRequest portfolioRequest, @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        try {
            PortfolioDTO updatedPortfolio = portfolioService.updatePortfolio(portfolioId, portfolioRequest, image);
            return ResponseEntity.ok(updatedPortfolio);
        }
        catch (Exception e) {
            log.error("CertificateController :: updateCertificate :: error :: {}", e.getMessage());
            throw e;
        }
    }


    @DeleteMapping("/{portfolioId}")
    public ResponseEntity<ApiResponse> deletePortfolio(@PathVariable UUID portfolioId) {
        
        // Get the portfolio to check if the authenticated user is the owner
        PortfolioDTO existingPortfolio = portfolioService.getPortfolioById(portfolioId);

        portfolioService.deletePortfolio(portfolioId);
        return ResponseEntity.ok( ApiResponse.success( "Portfolio deleted successfully",true));
    }

    @GetMapping("/{portfolioId}/overview")
    public ResponseEntity<String> getOverview(@PathVariable UUID portfolioId) {
        PortfolioDTO portfolio = portfolioService.getPortfolioById(portfolioId);
        return ResponseEntity.ok(portfolio.getOverview());
    }

    @PutMapping("/{id}/overview")
    public ResponseEntity<PortfolioDTO> updateOverview(
            @PathVariable UUID portfolioId,
            @Valid @RequestBody OverviewRequest overviewRequest) {

        PortfolioDTO updatedPortfolio = portfolioService.updateOverview(portfolioId, overviewRequest.getOverview());
        return ResponseEntity.ok(updatedPortfolio);
    }
}
