CREATE TABLE IF NOT EXISTS empresas (
                                        id BINARY(16) NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    ruc VARCHAR(30) UNIQUE,
    telefono VARCHAR(30),
    direccion VARCHAR(255),
    activo BOOLEAN DEFAULT TRUE,
    created_at DATETIME,
    updated_at DATETIME,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sucursales (
                                          id BINARY(16) NOT NULL,
    empresa_id BINARY(16) NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    direccion VARCHAR(255),
    telefono VARCHAR(30),
    activo BOOLEAN DEFAULT TRUE,
    created_at DATETIME,
    updated_at DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT fk_sucursal_empresa FOREIGN KEY (empresa_id) REFERENCES empresas (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;