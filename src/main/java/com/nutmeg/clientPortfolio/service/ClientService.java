package com.nutmeg.clientPortfolio.service;

import com.nutmeg.clientPortfolio.dto.ClientRequestDTO;
import com.nutmeg.clientPortfolio.dto.ClientResponseDTO;
import com.nutmeg.clientPortfolio.dto.HoldingDTO;
import com.nutmeg.clientPortfolio.mapper.ClientMapper;
import com.nutmeg.clientPortfolio.model.Client;
import com.nutmeg.clientPortfolio.model.Goal;
import com.nutmeg.clientPortfolio.model.Holding;
import com.nutmeg.clientPortfolio.model.PortfolioModel;
import com.nutmeg.clientPortfolio.repository.ClientRepo;
import com.nutmeg.clientPortfolio.repository.PortfolioModelRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepo clientRepo;
    private final PortfolioModelRepo portfolioModelRepo;
    private final ClientMapper clientMapper;

    @Transactional
    public ClientResponseDTO get(String name) {
        Client client = clientRepo.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Client does not exist."));

        return clientMapper.toResponseDTO(client);
    }

    @Transactional
    public ClientResponseDTO save(ClientRequestDTO clientDto) {
        // Validate if model by risk exists
        PortfolioModel portfolioModel = portfolioModelRepo.findById(clientDto.riskModel())
                .orElseThrow(() -> new IllegalArgumentException("Invalid risk level."));

        // Create base client
        Client client = clientMapper.toClient(clientDto);

        Goal goal = Goal.builder()
                .name(clientDto.goalName())
                .date(clientDto.goalDate())
                .riskLevel(clientDto.riskModel())
                .build();

        Holding holding = Holding.builder()
                .bondAmount(BigDecimal.ZERO)
                .equityAmount(BigDecimal.ZERO)
                .build();

        client.setGoal(goal);
        client.setHolding(holding);
        client.setPortfolioModel(portfolioModel);

        Client saved = clientRepo.save(client);

        return clientMapper.toResponseDTO(saved);
    }

    @Transactional
    public ClientResponseDTO deposit(UUID clientId, BigDecimal amount) {
        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client does not exist."));

        BigDecimal bondAmount = amount.multiply(client.getPortfolioModel().getBondWeight());
        BigDecimal equityAmount = amount.multiply(client.getPortfolioModel().getEquityWeight());

        Holding clientHolding = client.getHolding();
        clientHolding.setBondAmount(clientHolding.getBondAmount().add(bondAmount));
        clientHolding.setEquityAmount(clientHolding.getEquityAmount().add(equityAmount));

        return clientMapper.toResponseDTO(client);
    }
}
