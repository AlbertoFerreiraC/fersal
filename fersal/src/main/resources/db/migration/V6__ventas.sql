CREATE TABLE IF NOT EXISTS ventas (
                                      id BINARY(16) NOT NULL,
    empresa_id BINARY(16) NOT NULL,
    sucursal_id BINARY(16) NOT NULL,
    usuario_id BINARY(16) NOT NULL,
    cliente_id BINARY(16),
    numero VARCHAR(40) NOT NULL,
    tipo_pago VARCHAR(40) NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL DEFAULT 0,
    total DECIMAL(12,2) NOT NULL DEFAULT 0,
    estado VARCHAR(30) NOT NULL,
    observacion TEXT,
    fecha DATETIME,
    fecha_vencimiento DATETIME,
    motivo_anulacion TEXT,
    fecha_anulacion DATETIME,
    usuario_anulacion_id BINARY(16),
    PRIMARY KEY (id),
    CONSTRAINT fk_venta_empresa FOREIGN KEY (empresa_id) REFERENCES empresas (id),
    CONSTRAINT fk_venta_sucursal FOREIGN KEY (sucursal_id) REFERENCES sucursales (id),
    CONSTRAINT fk_venta_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id),
    CONSTRAINT fk_venta_cliente FOREIGN KEY (cliente_id) REFERENCES clientes (id),
    CONSTRAINT fk_venta_usuario_anulacion FOREIGN KEY (usuario_anulacion_id) REFERENCES usuarios (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS detalle_ventas (
                                              id BINARY(16) NOT NULL,
    venta_id BINARY(16) NOT NULL,
    producto_id BINARY(16) NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(12,2) NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_detalle_venta FOREIGN KEY (venta_id) REFERENCES ventas (id),
    CONSTRAINT fk_detalle_producto FOREIGN KEY (producto_id) REFERENCES productos (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;