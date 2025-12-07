package com.nutmeg.clientPortfolio.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutmeg.clientPortfolio.api.config.SecurityConfig;
import com.nutmeg.clientPortfolio.dto.ClientRequestDTO;
import com.nutmeg.clientPortfolio.dto.ClientResponseDTO;
import com.nutmeg.clientPortfolio.dto.HoldingDTO;
import com.nutmeg.clientPortfolio.service.ClientService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientApi.class)
@Import(SecurityConfig.class)
class ClientApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void saveClient_shouldReturnClientResponseDTO() throws Exception {
        ClientRequestDTO clientRequestDTO = new ClientRequestDTO(
                "John",
                "Housing",
                LocalDate.now(),
                5
        );

        ClientResponseDTO clientResponseDTO = new ClientResponseDTO(
                UUID.randomUUID(),
                "John",
                "Housing",
                LocalDate.now(),
                5,
                new HoldingDTO(BigDecimal.ZERO, BigDecimal.ZERO)
        );

        Mockito.when(clientService.save(clientRequestDTO)).thenReturn(clientResponseDTO);

        String requestJson = objectMapper.writeValueAsString(clientRequestDTO);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/clients/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.goalName").value("Housing"))
                .andExpect(jsonPath("$.goalDate").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.riskLevel").value(5));
    }
}