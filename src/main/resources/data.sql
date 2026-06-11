INSERT INTO customers (id, name) VALUES (1, 'Alice');
INSERT INTO customers (id, name) VALUES (2, 'Bob');
INSERT INTO customers (id, name) VALUES (3, 'Carol');

-- Transactions for Alice (id=1)
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (1, 1, 120.00, '2026-03-15');
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (2, 1, 75.50, '2026-04-10');
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (3, 1, 10.00, '2026-05-05');

-- Transactions for Bob (id=2)
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (4, 2, 200.00, '2026-03-20');
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (5, 2, 99.99, '2026-04-12');

-- Transactions for Carol (id=3)
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (6, 3, 50.00, '2026-05-01');
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (7, 3, 51.25, '2026-05-15');
