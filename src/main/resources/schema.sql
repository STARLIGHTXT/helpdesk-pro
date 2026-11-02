-- Esquema idempotente: se puede ejecutar en cada arranque sin borrar datos.
CREATE TABLE IF NOT EXISTS usuario(
                        id SERIAL PRIMARY KEY,
                        nombre VARCHAR(100) not null,
                        email VARCHAR(100) UNIQUE NOT NULL,
                        rol VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS ticket(
                       id SERIAL PRIMARY KEY,
                       titulo VARCHAR(150) NOT NULL,
                       descripcion TEXT,
                       fecha_creacion TIMESTAMP NOT NULL,
                       estado VARCHAR(20) NOT NULL,
                       prioridad VARCHAR(20) NOT NULL,
                       tipo VARCHAR(20) NOT NULL,
                       id_cliente INT NOT NULL REFERENCES usuario(id),
                       id_tecnico INT REFERENCES usuario(id)
);

-- Migración opcional para bases creadas antes, cuando la columna era DATE.
-- NO se ejecuta automáticamente a propósito: un "ALTER TABLE ... TYPE" en cada
-- arranque invalida los planes preparados de PostgreSQL y provoca el error
-- "cached plan must not change result type".
-- Si tu base es antigua, ejecuta esto UNA sola vez a mano:
--   ALTER TABLE ticket ALTER COLUMN fecha_creacion TYPE TIMESTAMP;
