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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
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

        Mockito.when(clientRepo.findByName(clientName)).thenReturn(Optional.of(mockClient));
        Mockito.when(clientMapper.toResponseDTO(mockClient)).thenReturn(mockResponseDTO);

        // Act
        ClientResponseDTO result = clientService.get(clientName);

        // Assert
        assertNotNull(result);
        assertEquals(mockResponseDTO, result);
        assertEquals(clientName, result.name());
        assertEquals(id, result.id());

        // Verify
        Mockito.verify(clientRepo).findByName(clientName);
        Mockito.verify(clientMapper).toResponseDTO(mockClient);
    }

    @Test
    void getClientByName_clientNotFound_throwsException() {
        // Arrange
        String clientName = "John Doe";
        Mockito.when(clientRepo.findByName(clientName)).thenReturn(Optional.empty());

        // Act & Assertion
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> clientService.get(clientName));

        assertEquals("Client does not exist.", exception.getMessage());

        // Verify
        Mockito.verify(clientRepo).findByName(clientName);
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

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> clientService.save(clientRequestDTO));

        assertEquals("Invalid risk level.", exception.getMessage());

        Mockito.verify(portfolioModelRepo).findById(clientRequestDTO.riskModel());
        Mockito.verifyNoInteractions(clientMapper);
        Mockito.verifyNoInteractions(clientRepo);
    }

}