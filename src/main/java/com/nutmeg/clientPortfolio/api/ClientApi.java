package com.nutmeg.clientPortfolio.api;

import com.nutmeg.clientPortfolio.dto.ClientRequestDTO;
import com.nutmeg.clientPortfolio.dto.ClientResponseDTO;
import com.nutmeg.clientPortfolio.dto.HoldingDTO;
import com.nutmeg.clientPortfolio.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@ControllerAdvice("api/clients")
public class ClientApi {
    private final ClientService clientService;

    @GetMapping("/{name}")
    public ResponseEntity<ClientResponseDTO> getClient(@RequestParam String name) {
        return ResponseEntity.status(HttpStatus.OK).body(
          clientService.get(name)
        );
    }

    @PostMapping("/create")
    public ResponseEntity<ClientResponseDTO> createClient(@RequestBody ClientRequestDTO client) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                clientService.save(client)
        );
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<ClientResponseDTO> deposit(@RequestParam UUID clientId, @RequestParam BigDecimal amount) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                clientService.deposit(clientId, amount)
        );
    }
}
