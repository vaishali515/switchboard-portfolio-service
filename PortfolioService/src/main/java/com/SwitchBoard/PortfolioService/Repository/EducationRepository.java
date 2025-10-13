package com.SwitchBoard.PortfolioService.Repository;

import com.SwitchBoard.PortfolioService.Entity.Education;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EducationRepository extends JpaRepository<Education, UUID> {
    List<Education> findByPortfolioId(UUID portfolioId);

}

