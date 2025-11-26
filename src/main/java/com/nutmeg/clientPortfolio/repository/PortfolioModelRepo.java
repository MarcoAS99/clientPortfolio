package com.nutmeg.clientPortfolio.repository;

import com.nutmeg.clientPortfolio.model.PortfolioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioModelRepo extends JpaRepository<PortfolioModel, Integer> {
}
