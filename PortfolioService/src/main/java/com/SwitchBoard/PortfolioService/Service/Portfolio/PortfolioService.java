package com.SwitchBoard.PortfolioService.Service.Portfolio;

import com.SwitchBoard.PortfolioService.DTO.Portfolio.PortfolioRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Portfolio.PortfolioResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface PortfolioService {
    
   
    PortfolioResponseDTO getPortfolioByEmailId(String emailId);


    PortfolioResponseDTO getPortfolioById(UUID portfolioId);

    PortfolioResponseDTO createPortfolio(PortfolioRequestDTO portfolioRequest);


    PortfolioResponseDTO updatePortfolio(UUID portfolioId, PortfolioRequestDTO portfolioRequest, MultipartFile newProfileImage) throws IOException;
    
    void deletePortfolio(UUID portfolioId);
    
    
//    String uploadProfileImage(UUID portfolioId, MultipartFile file) throws IOException;
//

    PortfolioResponseDTO updateOverview(UUID portfolioId, String overview);
}
