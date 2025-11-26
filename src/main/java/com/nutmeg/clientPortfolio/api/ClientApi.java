package com.nutmeg.clientPortfolio.api;

import com.nutmeg.clientPortfolio.dto.ClientRequestDTO;
import com.nutmeg.clientPortfolio.dto.ClientResponseDTO;
import com.nutmeg.clientPortfolio.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@ControllerAdvice("api/client")
public class ClientApi {
    private final ClientService clientService;

    @PostMapping(path = "create")
    public ResponseEntity<ClientResponseDTO> createClient(@RequestBody ClientRequestDTO client) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                clientService.save(client)
        );
    }
}
