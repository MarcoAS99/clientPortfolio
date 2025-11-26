package com.nutmeg.clientPortfolio.dto;

import java.math.BigDecimal;

public record HoldingDTO(
        BigDecimal equityAmount,
        BigDecimal bondAmount
) {
}
