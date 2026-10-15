CREATE TABLE IF NOT EXISTS cierres_caja (
                                            id BINARY(16) NOT NULL,
    empresa_id BINARY(16) NOT NULL,
    sucursal_id BINARY(16) NOT NULL,
    usuario_id BINARY(16) NOT NULL,
    saldo_inicial DECIMAL(12,2) NOT NULL DEFAULT 0,
    total_ingresos DECIMAL(12,2) DEFAULT 0,
    total_egresos DECIMAL(12,2) DEFAULT 0,
    saldo_final DECIMAL(12,2) DEFAULT 0,
    estado VARCHAR(20) NOT NULL,
    fecha_apertura DATETIME,
    fecha_cierre DATETIME,
    fecha DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT fk_cierrecaja_empresa FOREIGN KEY (empresa_id) REFERENCES empresas (id),
    CONSTRAINT fk_cierrecaja_sucursal FOREIGN KEY (sucursal_id) REFERENCES sucursales (id),
    CONSTRAINT fk_cierrecaja_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS movimientos_caja (
                                                id BINARY(16) NOT NULL,
    empresa_id BINARY(16) NOT NULL,
    sucursal_id BINARY(16) NOT NULL,
    usuario_id BINARY(16) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    monto DECIMAL(12,2) NOT NULL,
    descripcion VARCHAR(255),
    fecha DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT fk_movcaja_empresa FOREIGN KEY (empresa_id) REFERENCES empresas (id),
    CONSTRAINT fk_movcaja_sucursal FOREIGN KEY (sucursal_id) REFERENCES sucursales (id),
    CONSTRAINT fk_movcaja_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;