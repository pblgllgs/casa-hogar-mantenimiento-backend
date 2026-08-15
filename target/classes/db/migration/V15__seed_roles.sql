CREATE TABLE roles (
    code VARCHAR(30) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO roles (code, name, description) VALUES
    ('ADMIN',       'Administrador',              'Acceso total al sistema'),
    ('SUPERVISOR',  'Supervisor',                 'Supervisión de operaciones y aprobación de órdenes'),
    ('MAINTENANCE', 'Técnico de Mantenimiento',   'Ejecución y registro de órdenes de trabajo'),
    ('INVENTORY',   'Encargado de Inventario',    'Gestión de stock, ingresos y egresos de inventario'),
    ('RESIDENTS',   'Encargado de Residentes',    'Gestión de residentes y fichas clínicas'),
    ('HR',          'Recursos Humanos',           'Gestión de personal y turnos'),
    ('VIEWER',      'Visualizador',               'Acceso de solo lectura');

ALTER TABLE user_roles
    ADD CONSTRAINT fk_user_roles_role FOREIGN KEY (role) REFERENCES roles(code);
