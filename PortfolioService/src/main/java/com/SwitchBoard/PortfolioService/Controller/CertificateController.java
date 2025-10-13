package com.SwitchBoard.PortfolioService.Controller;


import com.SwitchBoard.PortfolioService.DTO.ApiResponse;
import com.SwitchBoard.PortfolioService.Service.Portfolio.CertificateService;
import com.SwitchBoard.PortfolioService.Service.Portfolio.FileService;
import com.SwitchBoard.PortfolioService.DTO.CertificateDTO;
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

public class CertificateController {

    private final CertificateService certificateService;
    private final FileService fileService;


    @GetMapping
    public ResponseEntity<List<CertificateDTO>> getAllCertificates(@PathVariable UUID portfolioId) {
        log.info("Fetching all certificates for portfolioId: {}", portfolioId);
        List<CertificateDTO> certificates = certificateService.getAllCertificatesByPortfolioId(portfolioId);
        return ResponseEntity.ok(certificates);
    }

    @GetMapping("/{certificateId}")
    public ResponseEntity<CertificateDTO> getCertificateById(@PathVariable UUID certificateId) {
        log.info("Fetching certificate with ID: {}", certificateId);
        CertificateDTO certificate = certificateService.getCertificateById(certificateId);
        return ResponseEntity.ok(certificate);
    }



    @PostMapping
    public ResponseEntity<CertificateDTO> createCertificate(
            @PathVariable UUID portfolioId,
            @Valid @RequestBody CertificateDTO certificateDTO, @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        try {
           if (image != null && !image.isEmpty()) {
               log.info("InterviewExperienceController :: createInterviewExperience :: processing image: {} of type: {}",
                        image.getOriginalFilename(), image.getContentType());

               String contentType = image.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    log.error("InterviewExperienceController :: createInterviewExperience :: invalid content type: {}", contentType);
                    return ResponseEntity.badRequest().body(null);
                }

                String imageUrl = fileService.uploadImage("portfolio-service", image);
               certificateDTO.setCertificateImageUrl(imageUrl);
            }


        CertificateDTO createdCertificate = certificateService.createCertificate(portfolioId, certificateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCertificate);

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


    @PutMapping("/{certificateId}")
    public ResponseEntity<CertificateDTO> updateCertificate(
            @PathVariable UUID portfolioId,
            @PathVariable UUID certificateId,
            @Valid @RequestBody CertificateDTO certificateDTO,@RequestPart(value = "image", required = false) MultipartFile image
   ) throws IOException {
        try {
            CertificateDTO updatedCertificate = certificateService.updateCertificate(certificateId, certificateDTO, image);
            return ResponseEntity.ok(updatedCertificate);
        }
        catch (Exception e) {
                log.error("CertificateController :: updateCertificate :: error :: {}", e.getMessage());
                throw e;
            }
    }


    @DeleteMapping("/{certificateId}")
    public ResponseEntity<ApiResponse> deleteCertificate(
            @PathVariable UUID portfolioId,
            @PathVariable UUID certificateId) {


        certificateService.deleteCertificate(certificateId);
        return ResponseEntity.ok(ApiResponse.success("Certificate deleted successfully", true));
    }
}
