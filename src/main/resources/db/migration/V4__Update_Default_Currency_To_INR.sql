-- Update column defaults from USD to INR
ALTER TABLE products  ALTER COLUMN currency_code SET DEFAULT 'INR';
ALTER TABLE orders    ALTER COLUMN currency_code SET DEFAULT 'INR';
ALTER TABLE payments  ALTER COLUMN currency_code SET DEFAULT 'INR';

-- Update existing seed data products
UPDATE products SET currency_code = 'INR' WHERE currency_code = 'USD';

-- Made with Bob
