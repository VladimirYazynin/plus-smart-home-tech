CREATE TABLE IF NOT EXISTS orders (
    order_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    shopping_cart_id UUID NOT NULL,
    payment_id UUID,
    delivery_id UUID,
    state VARCHAR(50),
    delivery_weight NUMERIC(10, 3),
    delivery_volume NUMERIC(10, 3),
    fragile BOOLEAN,
    total_price NUMERIC(10, 2),
    delivery_price NUMERIC(10, 2),
    product_price NUMERIC(10, 2),
    username VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS order_products (
    order_id UUID NOT NULL REFERENCES orders(order_id),
    product_id UUID NOT NULL,
    product_quantity BIGINT NOT NULL
);
