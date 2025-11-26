package com.nutmeg.clientPortfolio.dto;

import java.time.LocalDate;

public record ClientRequestDTO(
        String name,
        String goalName,
        LocalDate goalDate,
        Integer riskModel) {
}
