package com.nutmeg.clientPortfolio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "goal_id")
    private Goal goal;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "holding_id")
    private Holding holding;

    @ManyToOne
    @JoinColumn(name = "portfolio_model_risk_level")
    private PortfolioModel portfolioModel;
}
