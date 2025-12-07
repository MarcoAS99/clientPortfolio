package com.nutmeg.clientPortfolio.repository;

import com.nutmeg.clientPortfolio.model.Client;
import com.nutmeg.clientPortfolio.model.PortfolioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepo extends JpaRepository<Client, UUID> {
    List<Client> findAllByName(String name);
}
