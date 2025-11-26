package com.nutmeg.clientPortfolio.repository;

import com.nutmeg.clientPortfolio.model.Holding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HoldingRepo extends JpaRepository<Holding, UUID> {
}
