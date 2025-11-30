CREATE TABLE portfolio_model (
    risk_level INT PRIMARY KEY,
    equity_weight DECIMAL(5,2) NOT NULL,
    bond_weight DECIMAL(5,2) NOT NULL
);

CREATE TABLE goal (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid ( ),
    name VARCHAR(100) NOT NULL,
    date DATE NOT NULL,
    risk_level INT NOT NULL REFERENCES portfolio_model(risk_level)
);

CREATE TABLE holding (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid ( ),
    equity_amount DECIMAL(15,2) NOT NULL,
    bond_amount DECIMAL(15,2) NOT NULL
);

CREATE TABLE client (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid ( ),
    name VARCHAR(50) NOT NULL,
    goal_id UUID REFERENCES goal(id),
    holding_id UUID REFERENCES holding(id),
    portfolio_model_risk_level INT REFERENCES portfolio_model(risk_level)
);
