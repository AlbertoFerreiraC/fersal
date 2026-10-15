CREATE TABLE IF NOT EXISTS categorias (
                                          id BINARY(16) NOT NULL,
    empresa_id BINARY(16) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    activo BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (id),
    CONSTRAINT fk_categoria_empresa FOREIGN KEY (empresa_id) REFERENCES empresas (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS proveedores (
                                           id BINARY(16) NOT NULL,
    empresa_id BINARY(16) NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    ruc VARCHAR(30),
    telefono VARCHAR(30),
    email VARCHAR(100),
    direccion VARCHAR(255),
    activo BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (id),
    CONSTRAINT fk_proveedor_empresa FOREIGN KEY (empresa_id) REFERENCES empresas (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS productos (
                                         id BINARY(16) NOT NULL,
    empresa_id BINARY(16) NOT NULL,
    categoria_id BINARY(16),
    proveedor_id BINARY(16),
    codigo VARCHAR(60) NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio_compra DECIMAL(12,2) DEFAULT 0,
    precio_venta DECIMAL(12,2) DEFAULT 0,
    stock_minimo INT DEFAULT 0,
    imagen_url VARCHAR(255),
    activo BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (id),
    CONSTRAINT fk_producto_empresa FOREIGN KEY (empresa_id) REFERENCES empresas (id),
    CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id) REFERENCES categorias (id),
    CONSTRAINT fk_producto_proveedor FOREIGN KEY (proveedor_id) REFERENCES proveedores (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;