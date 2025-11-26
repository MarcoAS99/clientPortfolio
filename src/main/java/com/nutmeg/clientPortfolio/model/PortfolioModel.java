package com.nutmeg.clientPortfolio.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioModel {
    @Id
    private Integer riskLevel;

    private BigDecimal bondWeight;
    private BigDecimal equityWeight;
}
