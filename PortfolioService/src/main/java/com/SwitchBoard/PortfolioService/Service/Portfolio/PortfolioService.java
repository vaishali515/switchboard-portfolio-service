package com.SwitchBoard.PortfolioService.Service.Portfolio;

import com.SwitchBoard.PortfolioService.DTO.PortfolioDTO;
import com.SwitchBoard.PortfolioService.DTO.PortfolioRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface PortfolioService {
    
   
    PortfolioDTO getPortfolioByEmailId(String emailId);
    
   
     PortfolioDTO getPortfolioById(UUID portfolioId);
    
    PortfolioDTO createPortfolio(PortfolioRequest portfolioRequest);
    
    
    PortfolioDTO updatePortfolio(UUID portfolioId, PortfolioRequest portfolioRequest, MultipartFile newProfileImage) throws IOException;
    
    void deletePortfolio(UUID portfolioId);
    
    
//    String uploadProfileImage(UUID portfolioId, MultipartFile file) throws IOException;
//
    
    PortfolioDTO updateOverview(UUID portfolioId, String overview);
}
