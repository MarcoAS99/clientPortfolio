--changeset you:002-insert-portfolio-models

INSERT INTO portfolio_model (risk_level, bond_weight, equity_weight) VALUES
(1, 0.20, 0.80),
(3, 0.40, 0.60),
(5, 0.50, 0.50),
(6, 0.60, 0.40),
(8, 0.75, 0.25),
(10, 0.90, 0.10);
