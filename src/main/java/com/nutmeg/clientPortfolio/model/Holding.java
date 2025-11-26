package com.nutmeg.clientPortfolio.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Holding {
    @Id
    @GeneratedValue
    private UUID id;

    private BigDecimal bondAmount;
    private BigDecimal equityAmount;
}

