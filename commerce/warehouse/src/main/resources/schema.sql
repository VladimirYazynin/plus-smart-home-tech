CREATE TABLE IF NOT EXISTS product_storage (
    product_id UUID NOT NULL UNIQUE PRIMARY KEY,
    fragile BOOLEAN,
    width DOUBLE PRECISION NOT NULL,
    height DOUBLE PRECISION NOT NULL,
    depth DOUBLE PRECISION NOT NULL,
    weight DOUBLE PRECISION NOT NULL,
    quantity BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS booking (
    order_id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    delivery_id uuid
);

CREATE TABLE IF NOT EXISTS booking_product (
     order_id uuid NOT NULL,
     product_id uuid NOT NULL,
     quantity bigint NOT NULL,
     CONSTRAINT pk_booking_product PRIMARY KEY (order_id, product_id),
     CONSTRAINT fk_booking_product_order
        FOREIGN KEY (order_id)
            REFERENCES booking (order_id)
            ON DELETE CASCADE,
     CONSTRAINT chk_booking_product_quantity
        CHECK (quantity > 0)
);
