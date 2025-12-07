package com.nutmeg.clientPortfolio.api;

import com.nutmeg.clientPortfolio.dto.ClientRequestDTO;
import com.nutmeg.clientPortfolio.dto.ClientResponseDTO;
import com.nutmeg.clientPortfolio.dto.DepositRequestDTO;
import com.nutmeg.clientPortfolio.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/clients")
public class ClientApi {
    private final ClientService clientService;

    @GetMapping("/{name}")
    public ResponseEntity<List<ClientResponseDTO>> getClient(@PathVariable String name) {
        return ResponseEntity.ok(
          clientService.get(name)
        );
    }

    @PostMapping("/create")
    public ResponseEntity<ClientResponseDTO> createClient(@RequestBody ClientRequestDTO client) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                clientService.save(client)
        );
    }

    @PostMapping("/{clientId}/deposit")
    public ResponseEntity<ClientResponseDTO> deposit(@PathVariable UUID clientId, @RequestBody DepositRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                clientService.deposit(clientId, request)
        );
    }
}
