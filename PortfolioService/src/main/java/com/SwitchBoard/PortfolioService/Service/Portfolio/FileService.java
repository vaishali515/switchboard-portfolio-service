package com.SwitchBoard.PortfolioService.Service.Portfolio;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileService {

    String uploadImage(String path , MultipartFile file) throws IOException;


    void deleteImage(String fileUrl);
}
