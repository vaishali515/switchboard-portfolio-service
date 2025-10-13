package com.SwitchBoard.PortfolioService.Repository;

import com.SwitchBoard.PortfolioService.Entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, UUID> {
    Optional<Portfolio> findByEmailId(String emailId);
}