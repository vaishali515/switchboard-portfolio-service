package com.SwitchBoard.PortfolioService.Repository;

import com.SwitchBoard.PortfolioService.Entity.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SkillRepository extends JpaRepository<Skill, UUID> {
    List<Skill> findByPortfolioId(UUID portfolioId);
    List<Skill> findByPortfolioIdAndCategory(UUID portfolioId, String category);
}
