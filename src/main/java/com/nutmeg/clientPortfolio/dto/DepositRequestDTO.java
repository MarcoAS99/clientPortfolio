package com.nutmeg.clientPortfolio.dto;

import java.math.BigDecimal;

public record DepositRequestDTO(
        BigDecimal amount
) {
}
