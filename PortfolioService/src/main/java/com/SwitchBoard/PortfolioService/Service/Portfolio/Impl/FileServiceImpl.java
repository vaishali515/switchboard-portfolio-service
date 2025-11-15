package com.SwitchBoard.PortfolioService.Service.Portfolio.Impl;

import com.SwitchBoard.PortfolioService.Service.Portfolio.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);
    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.region}")
    private String region;

    public FileServiceImpl(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        log.info("Uploading file: {} to S3 path: {}", file.getOriginalFilename(), path);

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String key = path + "/" + fileName;

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .acl("public-read")
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));

        String publicUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;
        log.info("File uploaded successfully. Public URL: {}", publicUrl);

        return publicUrl;
    }

    @Override
    public void deleteImage(String fileUrl) {
        if (fileUrl == null || !fileUrl.contains(".com/")) {
            log.warn("Invalid file URL, skipping delete: {}", fileUrl);
            return;
        }

        String key = fileUrl.substring(fileUrl.indexOf(".com/") + 5);
        log.info("Deleting file from S3 with key: {}", key);
        s3Client.deleteObject(builder -> builder.bucket(bucket).key(key));
    }
}
