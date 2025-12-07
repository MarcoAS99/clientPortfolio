package com.nutmeg.clientPortfolio.service;

import com.nutmeg.clientPortfolio.dto.ClientRequestDTO;
import com.nutmeg.clientPortfolio.dto.ClientResponseDTO;
import com.nutmeg.clientPortfolio.dto.DepositRequestDTO;
import com.nutmeg.clientPortfolio.dto.HoldingDTO;
import com.nutmeg.clientPortfolio.expection.ClientNotFound;
import com.nutmeg.clientPortfolio.expection.PortfolioModelNotFound;
import com.nutmeg.clientPortfolio.mapper.ClientMapper;
import com.nutmeg.clientPortfolio.model.Client;
import com.nutmeg.clientPortfolio.model.Goal;
import com.nutmeg.clientPortfolio.model.Holding;
import com.nutmeg.clientPortfolio.model.PortfolioModel;
import com.nutmeg.clientPortfolio.repository.ClientRepo;
import com.nutmeg.clientPortfolio.repository.PortfolioModelRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepo clientRepo;

    @Mock
    private PortfolioModelRepo portfolioModelRepo;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientService clientService;

    @Test
    void getClient_returnClientResponseDTO() {
        // Arrange
        String clientName = "John Doe";
        UUID id = UUID.randomUUID();
        Client mockClient = Client.builder().id(id).name(clientName).build();

        ClientResponseDTO mockResponseDTO = new ClientResponseDTO(
                id,
                clientName,
                null,
                null,
                null,
                null
        );

        Mockito.when(clientRepo.findAllByName(clientName)).thenReturn(List.of(mockClient));
        Mockito.when(clientMapper.toResponseDTO(mockClient)).thenReturn(mockResponseDTO);

        // Act
        List<ClientResponseDTO> result = clientService.get(clientName);

        // Assert
        assertNotNull(result);
        assertEquals(List.of(mockResponseDTO), result);
        assertEquals(clientName, result.getFirst().name());
        assertEquals(id, result.getFirst().id());

        // Verify
        Mockito.verify(clientRepo).findAllByName(clientName);
        Mockito.verify(clientMapper).toResponseDTO(mockClient);
    }

    @Test
    void getClientByName_clientNotFoundException() {
        // Arrange
        String clientName = "John Doe";
        Mockito.when(clientRepo.findAllByName(clientName)).thenReturn(Collections.emptyList());

        // Act & Assertion
        ClientNotFound exception = assertThrows(ClientNotFound.class,
                () -> clientService.get(clientName));

        assertEquals("Client does not exist.", exception.getMessage());

        // Verify
        Mockito.verify(clientRepo).findAllByName(clientName);
        Mockito.verifyNoInteractions(clientMapper);
    }

    @Test
    void saveClient_returnClientResponseDTO() {
        // Arrange
        Goal mockGoal = Goal.builder()
                .name("test")
                .date(LocalDate.EPOCH)
                .build();
        Holding mockHolding = Holding.builder()
                .bondAmount(BigDecimal.ZERO)
                .equityAmount(BigDecimal.ZERO)
                .build();
        PortfolioModel mockPortfolioModel = PortfolioModel.builder()
                .riskLevel(5).build();
        Client mockClient = Client.builder()
                .id(UUID.randomUUID())
                .name("test")
                .goal(mockGoal)
                .holding(mockHolding)
                .portfolioModel(mockPortfolioModel)
                .build();

        ClientRequestDTO mockClientRequestDTO = new ClientRequestDTO(
                mockClient.getName(),
                mockGoal.getName(),
                mockGoal.getDate(),
                mockPortfolioModel.getRiskLevel()
        );

        ClientResponseDTO mockClientResponseDTO = new ClientResponseDTO(
                mockClient.getId(),
                mockClient.getName(),
                mockClient.getGoal().getName(),
                mockClient.getGoal().getDate(),
                mockClient.getPortfolioModel().getRiskLevel(),
                new HoldingDTO(
                        mockClient.getHolding().getBondAmount(),
                        mockClient.getHolding().getEquityAmount()
                )
        );

        Mockito.when(portfolioModelRepo.findById(mockClientRequestDTO.riskModel()))
                .thenReturn(Optional.of(mockPortfolioModel));
        Mockito.when(clientMapper.toClient(mockClientRequestDTO))
                .thenReturn(mockClient);
        Mockito.when(clientRepo.save(mockClient))
                .thenReturn(mockClient);
        Mockito.when(clientMapper.toResponseDTO(mockClient))
                .thenReturn(mockClientResponseDTO);

        ClientResponseDTO result = clientService.save(mockClientRequestDTO);

        assertNotNull(result);
        assertEquals(mockClientResponseDTO, result);

        Mockito.verify(portfolioModelRepo).findById(mockClientRequestDTO.riskModel());
        Mockito.verify(clientMapper).toClient(mockClientRequestDTO);
        Mockito.verify(clientRepo).save(mockClient);
        Mockito.verify(clientMapper).toResponseDTO(mockClient);
    }

    @Test
    void saveClient_portfolioNotFound_throwsException() {
        ClientRequestDTO clientRequestDTO = new ClientRequestDTO(
                "",
                "",
                LocalDate.now(),
                0
        );

        Mockito.when(portfolioModelRepo.findById(clientRequestDTO.riskModel())).thenReturn(Optional.empty());

        PortfolioModelNotFound exception = assertThrows(PortfolioModelNotFound.class,
                () -> clientService.save(clientRequestDTO));

        assertEquals("Invalid risk level.", exception.getMessage());

        Mockito.verify(portfolioModelRepo).findById(clientRequestDTO.riskModel());
        Mockito.verifyNoInteractions(clientMapper);
        Mockito.verifyNoInteractions(clientRepo);
    }

    @Test
    void deposit_returnClientResponse() {
        UUID clientId = UUID.randomUUID();
        DepositRequestDTO requestDTO = new DepositRequestDTO(BigDecimal.ONE);

        PortfolioModel portfolioModel = PortfolioModel.builder()
                .riskLevel(1)
                .bondWeight(BigDecimal.valueOf(0.20))
                .equityWeight(BigDecimal.valueOf(0.80))
                .build();

        Holding holding = Holding.builder()
                .equityAmount(BigDecimal.ZERO)
                .bondAmount(BigDecimal.ZERO)
                .build();

        Goal goal = Goal.builder()
                .date(LocalDate.now())
                .name("Goal")
                .build();

        Client client = Client.builder()
                .id(clientId)
                .name("John")
                .portfolioModel(portfolioModel)
                .holding(holding)
                .goal(goal)
                .build();

        Mockito.when(clientRepo.findById(clientId)).thenReturn(Optional.of(client));

        holding.setEquityAmount(
                holding.getEquityAmount()
                        .add(portfolioModel.getEquityWeight().multiply(requestDTO.amount()))
        );
        holding.setBondAmount(
                holding.getBondAmount()
                        .add(portfolioModel.getBondWeight().multiply(requestDTO.amount()))
        );

        client.setHolding(holding);

        HoldingDTO holdingDTO = new HoldingDTO(
                client.getHolding().getEquityAmount(),
                client.getHolding().getBondAmount()
                );

        ClientResponseDTO clientResponseDTO = new ClientResponseDTO(
                clientId,
                client.getName(),
                client.getGoal().getName(),
                client.getGoal().getDate(),
                portfolioModel.getRiskLevel(),
                holdingDTO
        );

        Mockito.when(clientMapper.toResponseDTO(client)).thenReturn(clientResponseDTO);

        ClientResponseDTO result = clientService.deposit(clientId,requestDTO);

        assertNotNull(result);
        assertEquals(result,clientResponseDTO);

        Mockito.verify(clientRepo).findById(clientId);
        Mockito.verify(clientMapper).toResponseDTO(client);
        Mockito.verify(clientRepo).save(client);
    }

    @Test
    void deposit_amountNotValid() {
        DepositRequestDTO requestDTO = new DepositRequestDTO(BigDecimal.valueOf(-1));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> clientService.deposit(UUID.randomUUID(),requestDTO));

        assertEquals("Deposit amount must be positive.", exception.getMessage());
        Mockito.verifyNoInteractions(clientRepo);
        Mockito.verifyNoInteractions(clientMapper);
    }

}