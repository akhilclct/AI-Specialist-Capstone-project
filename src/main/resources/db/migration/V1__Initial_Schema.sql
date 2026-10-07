-- Create Users table
CREATE TABLE users (
    user_id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);

-- Create Categories table
CREATE TABLE categories (
    category_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(500)
);

-- Create Brands table
CREATE TABLE brands (
    brand_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL
);

-- Create Products table
CREATE TABLE products (
    product_id BIGSERIAL PRIMARY KEY,
    category_id BIGINT NOT NULL,
    brand_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    unit_price DECIMAL(10, 2) NOT NULL,
    currency_code VARCHAR(3) NOT NULL DEFAULT 'USD',
    stock_quantity INTEGER NOT NULL DEFAULT 0,
    thumbnail_url VARCHAR(1000),
    FOREIGN KEY (category_id) REFERENCES categories(category_id),
    FOREIGN KEY (brand_id) REFERENCES brands(brand_id)
);

CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_products_brand ON products(brand_id);
CREATE INDEX idx_products_name ON products(name);

-- Create Product Images table
CREATE TABLE product_images (
    product_image_id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    image_url VARCHAR(1000) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

CREATE INDEX idx_product_images_product ON product_images(product_id);

-- Create Carts table
CREATE TABLE carts (
    cart_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    CHECK (status IN ('ACTIVE', 'CHECKED_OUT', 'ABANDONED'))
);

CREATE INDEX idx_carts_user ON carts(user_id);
CREATE INDEX idx_carts_status ON carts(status);

-- Create Cart Items table
CREATE TABLE cart_items (
    cart_item_id BIGSERIAL PRIMARY KEY,
    cart_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    unit_price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (cart_id) REFERENCES carts(cart_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

CREATE INDEX idx_cart_items_cart ON cart_items(cart_id);
CREATE INDEX idx_cart_items_product ON cart_items(product_id);

-- Create Addresses table
CREATE TABLE addresses (
    address_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    address_line1 VARCHAR(255) NOT NULL,
    address_line2 VARCHAR(255),
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    country_code VARCHAR(2) NOT NULL,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE INDEX idx_addresses_user ON addresses(user_id);

-- Create Orders table
CREATE TABLE orders (
    order_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    delivery_address_id BIGINT NOT NULL,
    order_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    subtotal DECIMAL(10, 2) NOT NULL,
    gift_points_redeemed DECIMAL(10, 2) DEFAULT 0.00,
    total_amount DECIMAL(10, 2) NOT NULL,
    currency_code VARCHAR(3) NOT NULL DEFAULT 'USD',
    ordered_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (delivery_address_id) REFERENCES addresses(address_id),
    CHECK (order_status IN ('PENDING', 'CONFIRMED', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED'))
);

CREATE INDEX idx_orders_user ON orders(user_id);
CREATE INDEX idx_orders_status ON orders(order_status);
CREATE INDEX idx_orders_date ON orders(ordered_at);

-- Create Order Items table
CREATE TABLE order_items (
    order_item_id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    total_price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);

CREATE INDEX idx_order_items_order ON order_items(order_id);

-- Create Payments table
CREATE TABLE payments (
    payment_id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    currency_code VARCHAR(3) NOT NULL DEFAULT 'USD',
    payment_status VARCHAR(20) NOT NULL DEFAULT 'INITIATED',
    transaction_reference VARCHAR(255),
    initiated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    CHECK (payment_method IN ('CREDIT_CARD', 'DEBIT_CARD', 'OTHER')),
    CHECK (payment_status IN ('INITIATED', 'AUTHORIZED', 'COMPLETED', 'FAILED'))
);

CREATE INDEX idx_payments_order ON payments(order_id);
CREATE INDEX idx_payments_status ON payments(payment_status);

-- Create Gift Point Accounts table
CREATE TABLE gift_point_accounts (
    gift_point_account_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    balance DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE INDEX idx_gift_point_accounts_user ON gift_point_accounts(user_id);

-- Create Gift Point Transactions table
CREATE TABLE gift_point_transactions (
    gift_point_transaction_id BIGSERIAL PRIMARY KEY,
    gift_point_account_id BIGINT NOT NULL,
    order_id BIGINT,
    transaction_type VARCHAR(20) NOT NULL,
    points DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (gift_point_account_id) REFERENCES gift_point_accounts(gift_point_account_id),
    CHECK (transaction_type IN ('EARN', 'REDEEM', 'ADJUST'))
);

CREATE INDEX idx_gift_point_transactions_account ON gift_point_transactions(gift_point_account_id);
CREATE INDEX idx_gift_point_transactions_type ON gift_point_transactions(transaction_type);
CREATE INDEX idx_gift_point_transactions_date ON gift_point_transactions(created_at);

-- Made with Bob
