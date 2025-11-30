package com.nutmeg.clientPortfolio.dto;

import java.time.LocalDate;
import java.util.UUID;

public record ClientResponseDTO(
        UUID id,
        String name,
        String goalName,
        LocalDate goalDate,
        Integer riskLevel,
        HoldingDTO holding) {
}
