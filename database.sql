-- 1. Tabla de Clientes
CREATE TABLE IF NOT EXISTS cliente (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(15),
    email VARCHAR(100) UNIQUE NOT NULL
    ) ENGINE=InnoDB;

-- 2. Tabla de Reservas (Relacionada con Cliente)
CREATE TABLE IF NOT EXISTS reserva (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       fecha_hora DATETIME NOT NULL,
                                       servicio VARCHAR(100) NOT NULL,
    cliente_id BIGINT,
    CONSTRAINT fk_reserva_cliente FOREIGN KEY (cliente_id)
    REFERENCES cliente(id) ON DELETE CASCADE
    ) ENGINE=InnoDB;

-- 3. Tabla de Pagos (Relacionada con Reserva)
CREATE TABLE IF NOT EXISTS pago (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    monto DECIMAL(10, 2) NOT NULL,
    metodo_pago ENUM('Yape', 'Plin', 'Efectivo', 'Transferencia') NOT NULL,
    reserva_id BIGINT UNIQUE,
    CONSTRAINT fk_pago_reserva FOREIGN KEY (reserva_id)
    REFERENCES reserva(id) ON DELETE CASCADE
    ) ENGINE=InnoDB;