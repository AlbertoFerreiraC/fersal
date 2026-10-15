CREATE TABLE IF NOT EXISTS compras (
                                       id BINARY(16) NOT NULL,
    empresa_id BINARY(16) NOT NULL,
    sucursal_id BINARY(16) NOT NULL,
    usuario_id BINARY(16) NOT NULL,
    proveedor_id BINARY(16) NOT NULL,
    numero_factura VARCHAR(60),
    condicion_pago VARCHAR(30) NOT NULL, -- CONTADO / CREDITO
    total DECIMAL(12,2) NOT NULL DEFAULT 0,
    estado VARCHAR(30) NOT NULL, -- COMPLETADA / ANULADA
    observacion TEXT,
    fecha DATETIME NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_compra_empresa FOREIGN KEY (empresa_id) REFERENCES empresas (id),
    CONSTRAINT fk_compra_sucursal FOREIGN KEY (sucursal_id) REFERENCES sucursales (id),
    CONSTRAINT fk_compra_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id),
    CONSTRAINT fk_compra_proveedor FOREIGN KEY (proveedor_id) REFERENCES proveedores (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS detalle_compras (
                                               id BINARY(16) NOT NULL,
    compra_id BINARY(16) NOT NULL,
    producto_id BINARY(16) NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(12,2) NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_detalle_compra FOREIGN KEY (compra_id) REFERENCES compras (id),
    CONSTRAINT fk_detalle_compra_producto FOREIGN KEY (producto_id) REFERENCES productos (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS cuentas_por_pagar (
                                                 id BINARY(16) NOT NULL,
    empresa_id BINARY(16) NOT NULL,
    proveedor_id BINARY(16) NOT NULL,
    compra_id BINARY(16) NOT NULL,
    monto_total DECIMAL(12,2) NOT NULL,
    saldo_pendiente DECIMAL(12,2) NOT NULL,
    fecha_emision DATETIME NOT NULL,
    fecha_vencimiento DATETIME,
    estado VARCHAR(30) NOT NULL, -- PENDIENTE / PAGADO
    activo BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (id),
    CONSTRAINT fk_cxp_empresa FOREIGN KEY (empresa_id) REFERENCES empresas (id),
    CONSTRAINT fk_cxp_proveedor FOREIGN KEY (proveedor_id) REFERENCES proveedores (id),
    CONSTRAINT fk_cxp_compra FOREIGN KEY (compra_id) REFERENCES compras (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;