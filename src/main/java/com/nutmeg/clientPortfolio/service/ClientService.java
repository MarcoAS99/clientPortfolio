package com.nutmeg.clientPortfolio.service;

import com.nutmeg.clientPortfolio.dto.ClientRequestDTO;
import com.nutmeg.clientPortfolio.dto.ClientResponseDTO;
import com.nutmeg.clientPortfolio.repository.ClientRepo;
import com.nutmeg.clientPortfolio.repository.PortfolioModelRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepo clientRepo;
    private final PortfolioModelRepo portfolioModelRepo;

    @Transactional
    public ClientResponseDTO save(ClientRequestDTO client) {

        return null;
    }
}
