CREATE TABLE IF NOT EXISTS usuarios (
                                        id BINARY(16) NOT NULL,
    empresa_id BINARY(16),
    sucursal_id BINARY(16),
    nombre VARCHAR(100) NOT NULL,
    username VARCHAR(60) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    fcm_token TEXT,
    activo BOOLEAN DEFAULT TRUE,
    created_at DATETIME,
    updated_at DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT fk_usuario_empresa FOREIGN KEY (empresa_id) REFERENCES empresas (id),
    CONSTRAINT fk_usuario_sucursal FOREIGN KEY (sucursal_id) REFERENCES sucursales (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;