CREATE TABLE IF NOT EXISTS stock (
                                     id BINARY(16) NOT NULL,
    empresa_id BINARY(16) NOT NULL,
    sucursal_id BINARY(16) NOT NULL,
    producto_id BINARY(16) NOT NULL,
    cantidad INT NOT NULL DEFAULT 0,
    stock_minimo INT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT uk_stock_sucursal_producto UNIQUE (sucursal_id, producto_id),
    CONSTRAINT fk_stock_empresa FOREIGN KEY (empresa_id) REFERENCES empresas (id),
    CONSTRAINT fk_stock_sucursal FOREIGN KEY (sucursal_id) REFERENCES sucursales (id),
    CONSTRAINT fk_stock_producto FOREIGN KEY (producto_id) REFERENCES productos (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS movimientos_stock (
                                                 id BINARY(16) NOT NULL,
    empresa_id BINARY(16) NOT NULL,
    sucursal_id BINARY(16) NOT NULL,
    producto_id BINARY(16) NOT NULL,
    usuario_id BINARY(16) NOT NULL,
    tipo_movimiento VARCHAR(20) NOT NULL,
    cantidad INT NOT NULL,
    stock_anterior INT NOT NULL,
    stock_nuevo INT NOT NULL,
    observacion TEXT,
    created_at DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT fk_movstock_empresa FOREIGN KEY (empresa_id) REFERENCES empresas (id),
    CONSTRAINT fk_movstock_sucursal FOREIGN KEY (sucursal_id) REFERENCES sucursales (id),
    CONSTRAINT fk_movstock_producto FOREIGN KEY (producto_id) REFERENCES productos (id),
    CONSTRAINT fk_movstock_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;