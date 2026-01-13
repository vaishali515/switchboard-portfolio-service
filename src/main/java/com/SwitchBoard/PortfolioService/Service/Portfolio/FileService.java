package com.SwitchBoard.PortfolioService.Service.Portfolio;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface FileService {

    String uploadImage(String path, MultipartFile file) throws IOException;

    void deleteImage(String fileUrl);

    // Added for better semantic clarity
    default String uploadDocument(String path, MultipartFile file) throws IOException {
        return uploadImage(path, file); // Reuse same logic for S3 uploads
    }
}
