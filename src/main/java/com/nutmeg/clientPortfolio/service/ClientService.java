package com.nutmeg.clientPortfolio.service;

import com.nutmeg.clientPortfolio.dto.ClientRequestDTO;
import com.nutmeg.clientPortfolio.dto.ClientResponseDTO;
import com.nutmeg.clientPortfolio.dto.DepositRequestDTO;
import com.nutmeg.clientPortfolio.expection.ClientNotFound;
import com.nutmeg.clientPortfolio.expection.PortfolioModelNotFound;
import com.nutmeg.clientPortfolio.mapper.ClientMapper;
import com.nutmeg.clientPortfolio.model.Client;
import com.nutmeg.clientPortfolio.model.Goal;
import com.nutmeg.clientPortfolio.model.Holding;
import com.nutmeg.clientPortfolio.model.PortfolioModel;
import com.nutmeg.clientPortfolio.repository.ClientRepo;
import com.nutmeg.clientPortfolio.repository.PortfolioModelRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepo clientRepo;
    private final PortfolioModelRepo portfolioModelRepo;
    private final ClientMapper clientMapper;

    @Transactional(readOnly = true)
    public List<ClientResponseDTO> get(String name) {
        List<Client> clients = clientRepo.findAllByName(name);

        if(clients.isEmpty()) {
            log.debug("Throw ClientNotFound.");
            throw new ClientNotFound("Client does not exist.");
        }

        return clients.stream().map(clientMapper::toResponseDTO).toList();
    }

    @Transactional
    public ClientResponseDTO save(ClientRequestDTO clientDto) {
        // Validate if model by risk exists
        PortfolioModel portfolioModel = portfolioModelRepo.findById(clientDto.riskModel())
                .orElseThrow(() -> new PortfolioModelNotFound("Invalid risk level."));

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
    public ClientResponseDTO deposit(UUID clientId, DepositRequestDTO requestDTO) {
        if(requestDTO.amount() == null || requestDTO.amount().compareTo(BigDecimal.ZERO) <=  0){
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }

        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new ClientNotFound("Client does not exist."));

        BigDecimal bondAmount = requestDTO.amount().multiply(client.getPortfolioModel().getBondWeight());
        BigDecimal equityAmount = requestDTO.amount().multiply(client.getPortfolioModel().getEquityWeight());

        Holding clientHolding = client.getHolding();
        clientHolding.setBondAmount(clientHolding.getBondAmount().add(bondAmount));
        clientHolding.setEquityAmount(clientHolding.getEquityAmount().add(equityAmount));

        clientRepo.save(client);

        return clientMapper.toResponseDTO(client);
    }
}
