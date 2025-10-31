-- auth_service_db ya está creado por POSTGRES_DB
-- Creamos las otras dos bases de datos

CREATE DATABASE order_service_db;

-- Opcional: crear usuarios específicos por servicio
-- CREATE USER user_service WITH PASSWORD 'user123';
-- CREATE USER order_service WITH PASSWORD 'order123';
-- GRANT ALL PRIVILEGES ON DATABASE user_service_db TO user_service;
-- GRANT ALL PRIVILEGES ON DATABASE order_service_db TO order_service;