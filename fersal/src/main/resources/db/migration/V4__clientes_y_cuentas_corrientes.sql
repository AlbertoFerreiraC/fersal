CREATE TABLE IF NOT EXISTS clientes (
                                        id BINARY(16) NOT NULL,
    empresa_id BINARY(16) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100),
    documento VARCHAR(40),
    telefono VARCHAR(30),
    email VARCHAR(100),
    direccion VARCHAR(255),
    activo BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (id),
    CONSTRAINT fk_cliente_empresa FOREIGN KEY (empresa_id) REFERENCES empresas (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS cuentas_corrientes (
                                                  id BINARY(16) NOT NULL,
    empresa_id BINARY(16) NOT NULL,
    cliente_id BINARY(16) NOT NULL,
    saldo DECIMAL(12,2) DEFAULT 0,
    limite_credito DECIMAL(12,2) DEFAULT 0,
    activo BOOLEAN DEFAULT TRUE,
    created_at DATETIME,
    updated_at DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT fk_cuenta_empresa FOREIGN KEY (empresa_id) REFERENCES empresas (id),
    CONSTRAINT fk_cuenta_cliente FOREIGN KEY (cliente_id) REFERENCES clientes (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;