CREATE TABLE IF NOT EXISTS address (
    address_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    country VARCHAR(20),
    city VARCHAR(30),
    street VARCHAR(50) NOT NULL,
    house VARCHAR(10),
    flat VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS delivery (
    delivery_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    from_address_id UUID NOT NULL,
    to_address_id UUID NOT NULL,
    order_id UUID NOT NULL,
    delivery_state VARCHAR(20) NOT NULL DEFAULT 'CREATED',
    CONSTRAINT fk_delivery_from_address
    FOREIGN KEY (from_address_id)
    REFERENCES address (address_id),
    CONSTRAINT fk_delivery_to_address
    FOREIGN KEY (to_address_id)
    REFERENCES address (address_id)
);