--changeset you:005-insert-sample-clients

INSERT INTO client (name, goal_id, holding_id, portfolio_model_risk_level) VALUES
('test0', '11111111-1111-1111-1111-111111111111', 'aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1', 6),
('test1', '22222222-2222-2222-2222-222222222222', 'bbbbbbb2-bbbb-bbbb-bbbb-bbbbbbbbbbb2', 3),
('test2', '33333333-3333-3333-3333-333333333333', 'ccccccc3-cccc-cccc-cccc-ccccccccccc3', 5);
