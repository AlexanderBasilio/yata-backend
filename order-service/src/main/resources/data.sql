-- -------------------------------------------------------------
-- 1. INSERCIÓN EN LA TABLA 'orders' (Una Orden Completa)
-- -------------------------------------------------------------

-- Insertamos la Orden 1
-- NOTA: Estamos asumiendo que las columnas de los @Embedded están fusionadas.
-- Ajusta los nombres de las columnas (location_address, payment_method, etc.) según tu DDL real si es necesario.
INSERT INTO orders (
    id,
    order_id,
    created_at,
    status,
    delivery_note,
    customer_name,
    customer_phone,
    -- Campos asumiendo que son de las clases Location, Payment, Summary
    location_address,
    location_latitude,
    location_longitude,
    payment_method,
    payment_amount,
    summary_subtotal,
    summary_delivery_fee,
    summary_total
)
VALUES (
    1, -- ID de la Orden
    'ORD-20251031-001', -- orderId (Debe ser UNICO)
    NOW(),             -- created_at (Hora actual)
    'CONFIRMED',       -- status
    'Llamar al llegar al portal', -- deliveryNote
    'Ana García',      -- customerName
    '555123456',       -- customerPhone
    'Calle Falsa 123', -- location_address
    19.4326,           -- location_latitude
    -99.1332,          -- location_longitude
    'CREDIT_CARD',     -- payment_method
    35.50,             -- payment_amount (Este valor debe coincidir con summary_total)
    30.00,             -- summary_subtotal
    5.50,              -- summary_delivery_fee
    35.50              -- summary_total
);


-- -------------------------------------------------------------
-- 2. INSERCIÓN EN LA TABLA 'order_items' (Ítems para la Orden 1)
-- -------------------------------------------------------------

-- Ítem 1 de la Orden 1
INSERT INTO order_items (
    id,
    order_id,          -- CLAVE FORÁNEA que apunta a orders.id
    product_id,
    name,
    price,
    quantity,
    unit
)
VALUES (
    1,
    1,                 -- Orden a la que pertenece
    'PROD-456',
    'Café Americano Grande',
    3.50,
    2,
    'unidad'
);

-- Ítem 2 de la Orden 1
INSERT INTO order_items (
    id,
    order_id,          -- CLAVE FORÁNEA que apunta a orders.id
    product_id,
    name,
    price,
    quantity,
    unit
)
VALUES (
    2,
    1,                 -- Orden a la que pertenece
    'PROD-789',
    'Pastel de Chocolate',
    5.00,
    5,
    'unidad'
);