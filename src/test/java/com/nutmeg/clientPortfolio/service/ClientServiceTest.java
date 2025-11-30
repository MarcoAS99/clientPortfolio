package com.nutmeg.clientPortfolio.service;

import com.nutmeg.clientPortfolio.dto.ClientResponseDTO;
import com.nutmeg.clientPortfolio.dto.HoldingDTO;
import com.nutmeg.clientPortfolio.mapper.ClientMapper;
import com.nutmeg.clientPortfolio.model.Client;
import com.nutmeg.clientPortfolio.repository.ClientRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepo clientRepo;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientService clientService;

    @Test
    void getClient_returnClientResponseDTO() {
        // Arrange
        String clientName = "John Doe";
        Client mockClient = Client.builder()
                .name(clientName)
                .build();

        HoldingDTO mockHoldingDTO = new HoldingDTO(BigDecimal.ZERO, BigDecimal.ZERO);
        ClientResponseDTO mockResponseDTO = new ClientResponseDTO(
                UUID.randomUUID(),
                clientName,
                "Retirement",
                null,
                3,
                mockHoldingDTO
        );

        Mockito.when(clientRepo.findByName(clientName)).thenReturn(Optional.of(mockClient));
        Mockito.when(clientMapper.toResponseDTO(mockClient)).thenReturn(mockResponseDTO);

        // Act
        ClientResponseDTO result = clientService.get(clientName);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.name());
        assertEquals("Retirement", result.goalName());
        assertEquals(3, result.riskLevel());
        assertEquals(mockHoldingDTO, result.holding());

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

}