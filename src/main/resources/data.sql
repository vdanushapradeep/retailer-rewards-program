INSERT INTO customers (id, name) VALUES (1, 'Alice');
INSERT INTO customers (id, name) VALUES (2, 'Bob');
INSERT INTO customers (id, name) VALUES (3, 'Carol');
INSERT INTO customers (id, name) VALUES (4, 'Dave');
INSERT INTO customers (id, name) VALUES (5, 'Erin');
INSERT INTO customers (id, name) VALUES (6, 'Frank');
INSERT INTO customers (id, name) VALUES (7, 'Grace');
INSERT INTO customers (id, name) VALUES (8, 'Hannah');
INSERT INTO customers (id, name) VALUES (9, 'Ian');
INSERT INTO customers (id, name) VALUES (10, 'Judy');
INSERT INTO customers (id, name) VALUES (11, 'Kevin');
INSERT INTO customers (id, name) VALUES (12, 'Laura');
INSERT INTO customers (id, name) VALUES (13, 'Mike');
INSERT INTO customers (id, name) VALUES (14, 'Nina');
INSERT INTO customers (id, name) VALUES (15, 'Oscar');
INSERT INTO customers (id, name) VALUES (16, 'Paula');
INSERT INTO customers (id, name) VALUES (17, 'Quentin');
INSERT INTO customers (id, name) VALUES (18, 'Rita');
INSERT INTO customers (id, name) VALUES (19, 'Steve');
INSERT INTO customers (id, name) VALUES (20, 'Tina');

-- Transaction dates are relative to CURRENT_DATE so the sample remains valid over time.
-- The service includes only transactions from the last 90 days (inclusive).

-- Alice (id=1): mixed spending across multiple recent months
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (1, 1, 120.00, DATEADD('DAY', -10, CURRENT_DATE));
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (2, 1, 75.50, DATEADD('DAY', -35, CURRENT_DATE));
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (3, 1, 10.00, DATEADD('DAY', -70, CURRENT_DATE));

-- Bob (id=2): threshold and high-value examples
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (4, 2, 200.00, DATEADD('DAY', -5, CURRENT_DATE));
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (5, 2, 99.99, DATEADD('DAY', -40, CURRENT_DATE));
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (6, 2, 100.00, DATEADD('DAY', -60, CURRENT_DATE));

-- Carol (id=3): exact threshold and just-above threshold values
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (7, 3, 50.00, DATEADD('DAY', -15, CURRENT_DATE));
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (8, 3, 51.25, DATEADD('DAY', -45, CURRENT_DATE));

-- Dave (id=4): cutoff boundary coverage
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (9, 4, 80.00, DATEADD('DAY', -90, CURRENT_DATE));
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (10, 4, 110.00, DATEADD('DAY', -91, CURRENT_DATE));

-- Erin (id=5): fractional and threshold edge coverage
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (11, 5, 49.49, DATEADD('DAY', -12, CURRENT_DATE));
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (12, 5, 50.50, DATEADD('DAY', -22, CURRENT_DATE));
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (13, 5, 100.49, DATEADD('DAY', -32, CURRENT_DATE));
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (14, 5, 100.50, DATEADD('DAY', -42, CURRENT_DATE));
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (15, 5, 150.75, DATEADD('DAY', -52, CURRENT_DATE));
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (16, 5, 300.00, DATEADD('DAY', -120, CURRENT_DATE));

-- Frank (id=6): single recent reward transaction
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (17, 6, 65.00, DATEADD('DAY', -8, CURRENT_DATE));

-- Grace (id=7): two recent transactions across different months
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (18, 7, 130.00, DATEADD('DAY', -18, CURRENT_DATE));
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (19, 7, 55.00, DATEADD('DAY', -48, CURRENT_DATE));

-- Hannah (id=8): mixed values to exercise pagination content ordering
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (20, 8, 101.00, DATEADD('DAY', -6, CURRENT_DATE));
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (21, 8, 49.00, DATEADD('DAY', -26, CURRENT_DATE));

-- Ian (id=9): low and medium values
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (22, 9, 60.00, DATEADD('DAY', -14, CURRENT_DATE));
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (23, 9, 75.00, DATEADD('DAY', -44, CURRENT_DATE));

-- Judy (id=10): one transaction exactly at the threshold and one above it
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (24, 10, 50.00, DATEADD('DAY', -20, CURRENT_DATE));
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (25, 10, 150.00, DATEADD('DAY', -50, CURRENT_DATE));

-- Kevin (id=11): recent small and medium spends
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (26, 11, 62.00, DATEADD('DAY', -9, CURRENT_DATE));
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (27, 11, 140.00, DATEADD('DAY', -39, CURRENT_DATE));

-- Laura (id=12): threshold and above-threshold mix
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (28, 12, 50.00, DATEADD('DAY', -11, CURRENT_DATE));
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (29, 12, 115.00, DATEADD('DAY', -41, CURRENT_DATE));

-- Mike (id=13): one low and one high transaction
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (30, 13, 45.00, DATEADD('DAY', -13, CURRENT_DATE));
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (31, 13, 175.25, DATEADD('DAY', -43, CURRENT_DATE));

-- Nina (id=14): fractional amounts to keep decimal coverage broad
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (32, 14, 88.88, DATEADD('DAY', -16, CURRENT_DATE));
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (33, 14, 101.10, DATEADD('DAY', -46, CURRENT_DATE));

-- Oscar (id=15): high-value recent spend
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (34, 15, 250.00, DATEADD('DAY', -7, CURRENT_DATE));

-- Paula (id=16): simple two-transaction history
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (35, 16, 70.00, DATEADD('DAY', -19, CURRENT_DATE));
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (36, 16, 95.00, DATEADD('DAY', -49, CURRENT_DATE));

-- Quentin (id=17): exact threshold and above-threshold pair
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (37, 17, 50.00, DATEADD('DAY', -21, CURRENT_DATE));
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (38, 17, 150.50, DATEADD('DAY', -51, CURRENT_DATE));

-- Rita (id=18): low recent activity
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (39, 18, 58.00, DATEADD('DAY', -23, CURRENT_DATE));

-- Steve (id=19): mixed low and medium spends
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (40, 19, 72.75, DATEADD('DAY', -27, CURRENT_DATE));
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (41, 19, 125.00, DATEADD('DAY', -57, CURRENT_DATE));

-- Tina (id=20): includes a boundary-like recent purchase and a mid-range purchase
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (42, 20, 50.00, DATEADD('DAY', -29, CURRENT_DATE));
INSERT INTO transactions (id, customer_id, amount, transaction_date) VALUES (43, 20, 180.00, DATEADD('DAY', -59, CURRENT_DATE));
