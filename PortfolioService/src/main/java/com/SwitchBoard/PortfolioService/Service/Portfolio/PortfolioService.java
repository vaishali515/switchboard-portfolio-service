package com.SwitchBoard.PortfolioService.Service.Portfolio;

import com.SwitchBoard.PortfolioService.DTO.Portfolio.PortfolioRequestDTO;
import com.SwitchBoard.PortfolioService.DTO.Portfolio.PortfolioResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface PortfolioService {

    PortfolioResponseDTO getPortfolioById(UUID portfolioId);

    PortfolioResponseDTO getPortfolioByEmailId(String emailId);

    PortfolioResponseDTO createPortfolio(PortfolioRequestDTO request) throws IOException;

    PortfolioResponseDTO updatePortfolio(UUID portfolioId, PortfolioRequestDTO request) throws IOException;

    void deletePortfolio(UUID portfolioId);

}
