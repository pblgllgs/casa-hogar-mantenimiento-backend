-- V1__create_initial_schema.sql
-- Esquema inicial para Casa Hogar Mantenimiento

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- Tabla de usuarios
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    document_number VARCHAR(20) UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    last_login_at TIMESTAMP NULL,
    failed_login_attempts INT NOT NULL DEFAULT 0,
    locked_until TIMESTAMP NULL,
    password_changed_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_at TIMESTAMP NULL,
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    INDEX idx_users_username (username),
    INDEX idx_users_email (email),
    INDEX idx_users_document (document_number),
    INDEX idx_users_active (is_active),
    INDEX idx_users_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de roles de usuario
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role VARCHAR(30) NOT NULL,
    PRIMARY KEY (user_id, role),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de ubicaciones
CREATE TABLE locations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    type VARCHAR(30) NOT NULL,
    parent_id BIGINT,
    floor VARCHAR(20),
    wing VARCHAR(50),
    room_number VARCHAR(20),
    capacity INT,
    area_sqm DECIMAL(10,2),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_at TIMESTAMP NULL,
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    INDEX idx_locations_code (code),
    INDEX idx_locations_parent (parent_id),
    INDEX idx_locations_type (type),
    INDEX idx_locations_active (is_active),
    INDEX idx_locations_deleted (deleted),
    CONSTRAINT fk_locations_parent FOREIGN KEY (parent_id) REFERENCES locations(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de activos
CREATE TABLE assets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    asset_code VARCHAR(30) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    category VARCHAR(30) NOT NULL,
    subcategory VARCHAR(30),
    brand VARCHAR(100),
    model VARCHAR(100),
    serial_number VARCHAR(100),
    manufacture_year INT,
    purchase_date DATE,
    purchase_cost DECIMAL(12,2),
    warranty_expiry_date DATE,
    location_id BIGINT,
    status VARCHAR(20) NOT NULL DEFAULT 'OPERATIONAL',
    criticality VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    expected_life_years INT,
    last_maintenance_date DATE,
    next_maintenance_date DATE,
    maintenance_interval_days INT,
    qr_code VARCHAR(500),
    manual_url VARCHAR(500),
    specifications JSON,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_at TIMESTAMP NULL,
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    INDEX idx_assets_code (asset_code),
    INDEX idx_assets_location (location_id),
    INDEX idx_assets_category (category),
    INDEX idx_assets_status (status),
    INDEX idx_assets_criticality (criticality),
    INDEX idx_assets_next_maintenance (next_maintenance_date),
    INDEX idx_assets_deleted (deleted),
    CONSTRAINT fk_assets_location FOREIGN KEY (location_id) REFERENCES locations(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de órdenes de trabajo
CREATE TABLE work_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(20) NOT NULL UNIQUE,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    type VARCHAR(20) NOT NULL,
    priority VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    location_id BIGINT,
    location_name VARCHAR(200),
    asset_id BIGINT,
    asset_name VARCHAR(200),
    requested_by_id BIGINT NOT NULL,
    requested_by_name VARCHAR(200),
    assigned_to_id BIGINT,
    assigned_to_name VARCHAR(200),
    supervisor_id BIGINT,
    supervisor_name VARCHAR(200),
    estimated_hours DECIMAL(5,2),
    actual_hours DECIMAL(5,2),
    scheduled_start_date DATE,
    scheduled_end_date DATE,
    actual_start_date TIMESTAMP NULL,
    actual_end_date TIMESTAMP NULL,
    cost_materials DECIMAL(12,2),
    cost_labor DECIMAL(12,2),
    cost_total DECIMAL(12,2),
    completion_notes TEXT,
    requires_external_vendor BOOLEAN NOT NULL DEFAULT FALSE,
    vendor_name VARCHAR(200),
    vendor_contact VARCHAR(200),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_at TIMESTAMP NULL,
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    INDEX idx_wo_number (order_number),
    INDEX idx_wo_status (status),
    INDEX idx_wo_type (type),
    INDEX idx_wo_priority (priority),
    INDEX idx_wo_location (location_id),
    INDEX idx_wo_asset (asset_id),
    INDEX idx_wo_requested_by (requested_by_id),
    INDEX idx_wo_assigned_to (assigned_to_id),
    INDEX idx_wo_scheduled (scheduled_start_date, scheduled_end_date),
    INDEX idx_wo_deleted (deleted),
    CONSTRAINT fk_wo_location FOREIGN KEY (location_id) REFERENCES locations(id),
    CONSTRAINT fk_wo_asset FOREIGN KEY (asset_id) REFERENCES assets(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de comentarios de órdenes de trabajo
CREATE TABLE work_order_comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    work_order_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    author_name VARCHAR(200),
    author_role VARCHAR(50),
    content TEXT NOT NULL,
    comment_type VARCHAR(30),
    is_internal BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_at TIMESTAMP NULL,
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    INDEX idx_woc_work_order (work_order_id),
    INDEX idx_woc_author (author_id),
    INDEX idx_woc_created (created_at),
    INDEX idx_woc_deleted (deleted),
    CONSTRAINT fk_woc_work_order FOREIGN KEY (work_order_id) REFERENCES work_orders(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de materiales/inventario
CREATE TABLE inventory_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(30) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    category VARCHAR(50),
    unit_of_measure VARCHAR(20) NOT NULL DEFAULT 'UN',
    current_stock DECIMAL(10,2) NOT NULL DEFAULT 0,
    minimum_stock DECIMAL(10,2) NOT NULL DEFAULT 0,
    maximum_stock DECIMAL(10,2),
    reorder_point DECIMAL(10,2),
    unit_cost DECIMAL(12,2),
    location_id BIGINT,
    supplier_name VARCHAR(200),
    supplier_contact VARCHAR(200),
    supplier_sku VARCHAR(100),
    last_purchase_date DATE,
    last_purchase_cost DECIMAL(12,2),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_at TIMESTAMP NULL,
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    INDEX idx_inventory_code (code),
    INDEX idx_inventory_category (category),
    INDEX idx_inventory_stock (current_stock, minimum_stock),
    INDEX idx_inventory_location (location_id),
    INDEX idx_inventory_deleted (deleted),
    CONSTRAINT fk_inventory_location FOREIGN KEY (location_id) REFERENCES locations(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de movimientos de inventario
CREATE TABLE inventory_movements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    inventory_item_id BIGINT NOT NULL,
    movement_type VARCHAR(20) NOT NULL, -- IN, OUT, ADJUSTMENT, TRANSFER
    quantity DECIMAL(10,2) NOT NULL,
    unit_cost DECIMAL(12,2),
    total_cost DECIMAL(12,2),
    reference_type VARCHAR(30), -- WORK_ORDER, PURCHASE_ORDER, ADJUSTMENT
    reference_id BIGINT,
    notes TEXT,
    performed_by_id BIGINT NOT NULL,
    performed_by_name VARCHAR(200),
    movement_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_at TIMESTAMP NULL,
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    INDEX idx_im_item (inventory_item_id),
    INDEX idx_im_type (movement_type),
    INDEX idx_im_date (movement_date),
    INDEX idx_im_reference (reference_type, reference_id),
    CONSTRAINT fk_im_item FOREIGN KEY (inventory_item_id) REFERENCES inventory_items(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de residentes
CREATE TABLE residents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    document_type VARCHAR(20),
    document_number VARCHAR(20) UNIQUE,
    birth_date DATE,
    gender VARCHAR(10),
    entry_date DATE NOT NULL,
    exit_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    room_id BIGINT,
    guardian_name VARCHAR(200),
    guardian_phone VARCHAR(20),
    guardian_email VARCHAR(150),
    guardian_relationship VARCHAR(50),
    medical_info TEXT,
    dietary_restrictions TEXT,
    notes TEXT,
    photo_url VARCHAR(500),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_at TIMESTAMP NULL,
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    INDEX idx_residents_code (code),
    INDEX idx_residents_document (document_number),
    INDEX idx_residents_status (status),
    INDEX idx_residents_room (room_id),
    INDEX idx_residents_active (is_active),
    INDEX idx_residents_deleted (deleted),
    CONSTRAINT fk_residents_room FOREIGN KEY (room_id) REFERENCES locations(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de personal (HR)
CREATE TABLE staff (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_code VARCHAR(20) NOT NULL UNIQUE,
    user_id BIGINT UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    document_type VARCHAR(20),
    document_number VARCHAR(20) UNIQUE,
    birth_date DATE,
    gender VARCHAR(10),
    hire_date DATE NOT NULL,
    termination_date DATE,
    position VARCHAR(100),
    department VARCHAR(100),
    shift VARCHAR(20),
    phone VARCHAR(20),
    emergency_contact_name VARCHAR(200),
    emergency_contact_phone VARCHAR(20),
    bank_account VARCHAR(50),
    salary DECIMAL(12,2),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_at TIMESTAMP NULL,
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    INDEX idx_staff_code (employee_code),
    INDEX idx_staff_user (user_id),
    INDEX idx_staff_document (document_number),
    INDEX idx_staff_dept (department),
    INDEX idx_staff_status (status),
    INDEX idx_staff_active (is_active),
    INDEX idx_staff_deleted (deleted),
    CONSTRAINT fk_staff_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de turnos
CREATE TABLE shifts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    days_of_week VARCHAR(50) NOT NULL, -- MON,TUE,WED,THU,FRI,SAT,SUN
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de asignación de personal a turnos
CREATE TABLE staff_shifts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    staff_id BIGINT NOT NULL,
    shift_id BIGINT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_ss_staff (staff_id),
    INDEX idx_ss_shift (shift_id),
    INDEX idx_ss_dates (start_date, end_date),
    CONSTRAINT fk_ss_staff FOREIGN KEY (staff_id) REFERENCES staff(id) ON DELETE CASCADE,
    CONSTRAINT fk_ss_shift FOREIGN KEY (shift_id) REFERENCES shifts(id) ON DELETE CASCADE,
    UNIQUE KEY uk_staff_shift_date (staff_id, shift_id, start_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de mantenimientos preventivos programados
CREATE TABLE preventive_maintenance_schedules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    asset_id BIGINT NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    frequency_type VARCHAR(20) NOT NULL, -- DAILY, WEEKLY, MONTHLY, QUARTERLY, SEMI_ANNUAL, ANNUAL
    frequency_value INT NOT NULL DEFAULT 1,
    day_of_week INT, -- 1=Lunes, 7=Domingo
    day_of_month INT,
    month_of_year INT,
    start_date DATE NOT NULL,
    end_date DATE,
    estimated_duration_hours DECIMAL(5,2),
    required_skills TEXT,
    required_tools TEXT,
    checklist JSON,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    last_generated_date DATE,
    next_generation_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_at TIMESTAMP NULL,
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    INDEX idx_pms_asset (asset_id),
    INDEX idx_pms_next_gen (next_generation_date),
    INDEX idx_pms_active (is_active),
    INDEX idx_pms_deleted (deleted),
    CONSTRAINT fk_pms_asset FOREIGN KEY (asset_id) REFERENCES assets(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS = 1;

-- Datos iniciales
INSERT INTO users (username, email, password_hash, first_name, last_name, document_number, is_active, is_email_verified, created_at, deleted)
VALUES ('admin', 'admin@casahogar.org', '$2a$12$KGsV2.UiqUlr1s2ZHaQdEei4IpWICOA/E6/mdMxEUr1wamkqZEN6C', 'Admin', 'Sistema', '12345678', true, true, NOW(), false)
ON DUPLICATE KEY UPDATE username=username;

INSERT INTO user_roles (user_id, role)
SELECT id, 'ADMIN' FROM users WHERE username = 'admin'
ON DUPLICATE KEY UPDATE role=role;

INSERT INTO locations (code, name, type, description, is_active)
VALUES 
('EDF-PRINCIPAL', 'Edificio Principal', 'BUILDING', 'Edificio principal de la casa hogar', true),
('EDF-PRINCIPAL-P1', 'Planta 1', 'FLOOR', 'Primera planta', true),
('EDF-PRINCIPAL-P2', 'Planta 2', 'FLOOR', 'Segunda planta', true),
('COCINA', 'Cocina Principal', 'KITCHEN', 'Cocina y comedor principal', true),
('LAVANDERIA', 'Lavandería', 'LAUNDRY', 'Área de lavado', true),
('ENFERMERIA', 'Enfermería', 'MEDICAL_ROOM', 'Área médica', true),
('OFICINA-ADMIN', 'Oficina de Administración', 'OFFICE', 'Oficina administrativa', true),
('ALMACEN', 'Almacén General', 'STORAGE', 'Almacén de materiales y suministros', true)
ON DUPLICATE KEY UPDATE name=name;

INSERT INTO shifts (name, start_time, end_time, days_of_week, is_active)
VALUES 
('Diurno Lunes', '08:00:00', '20:00:00', 'MON', true),
('Nocturno Lunes', '20:00:00', '08:00:00', 'MON', true),
('Diurno Martes', '08:00:00', '20:00:00', 'TUE', true),
('Nocturno Martes', '20:00:00', '08:00:00', 'TUE', true),
('Diurno Miercoles', '08:00:00', '20:00:00', 'WED', true),
('Nocturno Miercoles', '20:00:00', '08:00:00', 'WED', true),
('Diurno Jueves', '08:00:00', '20:00:00', 'THU', true),
('Nocturno Jueves', '20:00:00', '08:00:00', 'THU', true),
('Diurno Viernes', '08:00:00', '20:00:00', 'FRI', true),
('Nocturno Viernes', '20:00:00', '08:00:00', 'FRI', true),
('Diurno Sabado', '08:00:00', '20:00:00', 'SAT', true),
('Nocturno Sabado', '20:00:00', '08:00:00', 'SAT', true),
('Diurno Domingo', '08:00:00', '20:00:00', 'SUN', true),
('Nocturno Domingo', '20:00:00', '08:00:00', 'SUN', true)
ON DUPLICATE KEY UPDATE name=name;

-- ============================================================
-- Usuarios adicionales (todos con password: admin123)
-- ============================================================
INSERT INTO users (username, email, password_hash, first_name, last_name, phone, document_number, is_active, is_email_verified, created_at, deleted)
VALUES
('supervisor', 'supervisor@casahogar.org', '$2a$12$KGsV2.UiqUlr1s2ZHaQdEei4IpWICOA/E6/mdMxEUr1wamkqZEN6C', 'Estrella', 'Cabrera', '555-0101', '87654321', true, true, NOW(), false),
('mantenimiento', 'mantenimiento@casahogar.org', '$2a$12$KGsV2.UiqUlr1s2ZHaQdEei4IpWICOA/E6/mdMxEUr1wamkqZEN6C', 'Carlos', 'Rodriguez', '555-0102', '11223344', true, true, NOW(), false),
('inventario', 'inventario@casahogar.org', '$2a$12$KGsV2.UiqUlr1s2ZHaQdEei4IpWICOA/E6/mdMxEUr1wamkqZEN6C', 'Ana', 'Martinez', '555-0103', '55667788', true, true, NOW(), false),
('residencias', 'residencias@casahogar.org', '$2a$12$KGsV2.UiqUlr1s2ZHaQdEei4IpWICOA/E6/mdMxEUr1wamkqZEN6C', 'Luis', 'Hernandez', '555-0104', '99887766', true, true, NOW(), false),
('rrhh', 'rrhh@casahogar.org', '$2a$12$KGsV2.UiqUlr1s2ZHaQdEei4IpWICOA/E6/mdMxEUr1wamkqZEN6C', 'Laura', 'Lopez', '555-0105', '33445566', true, true, NOW(), false),
('visualizar', 'visualizar@casahogar.org', '$2a$12$KGsV2.UiqUlr1s2ZHaQdEei4IpWICOA/E6/mdMxEUr1wamkqZEN6C', 'Pedro', 'Sanchez', '555-0106', '77889900', true, true, NOW(), false),
('tecnico1', 'tecnico1@casahogar.org', '$2a$12$KGsV2.UiqUlr1s2ZHaQdEei4IpWICOA/E6/mdMxEUr1wamkqZEN6C', 'Miguel', 'Flores', '555-0107', '12345679', true, true, NOW(), false),
('tecnico2', 'tecnico2@casahogar.org', '$2a$12$KGsV2.UiqUlr1s2ZHaQdEei4IpWICOA/E6/mdMxEUr1wamkqZEN6C', 'Rosa', 'Torres', '555-0108', '98765432', true, true, NOW(), false)
ON DUPLICATE KEY UPDATE username=username;

INSERT INTO user_roles (user_id, role)
SELECT id, 'SUPERVISOR' FROM users WHERE username = 'supervisor'
ON DUPLICATE KEY UPDATE role=role;
INSERT INTO user_roles (user_id, role)
SELECT id, 'MAINTENANCE' FROM users WHERE username = 'mantenimiento'
ON DUPLICATE KEY UPDATE role=role;
INSERT INTO user_roles (user_id, role)
SELECT id, 'INVENTORY' FROM users WHERE username = 'inventario'
ON DUPLICATE KEY UPDATE role=role;
INSERT INTO user_roles (user_id, role)
SELECT id, 'RESIDENTS' FROM users WHERE username = 'residencias'
ON DUPLICATE KEY UPDATE role=role;
INSERT INTO user_roles (user_id, role)
SELECT id, 'HR' FROM users WHERE username = 'rrhh'
ON DUPLICATE KEY UPDATE role=role;
INSERT INTO user_roles (user_id, role)
SELECT id, 'VIEWER' FROM users WHERE username = 'visualizar'
ON DUPLICATE KEY UPDATE role=role;
INSERT INTO user_roles (user_id, role)
SELECT id, 'MAINTENANCE' FROM users WHERE username = 'tecnico1'
ON DUPLICATE KEY UPDATE role=role;
INSERT INTO user_roles (user_id, role)
SELECT id, 'MAINTENANCE' FROM users WHERE username = 'tecnico2'
ON DUPLICATE KEY UPDATE role=role;
INSERT INTO user_roles (user_id, role)
SELECT id, 'SUPERVISOR' FROM users WHERE username = 'tecnico1'
ON DUPLICATE KEY UPDATE role=role;

-- ============================================================
-- Ubicaciones adicionales (habitaciones, areas especificas)
-- ============================================================
INSERT INTO locations (code, name, type, description, parent_id, floor, room_number, capacity, area_sqm, is_active)
VALUES
('HAB-101', 'Habitacion 101', 'ROOM', 'Habitacion para 4 ninos', 2, 'P1', '101', 4, 25.00, true),
('HAB-102', 'Habitacion 102', 'ROOM', 'Habitacion para 4 ninos', 2, 'P1', '102', 4, 25.00, true),
('HAB-103', 'Habitacion 103', 'ROOM', 'Habitacion para 6 ninos', 2, 'P1', '103', 6, 35.00, true),
('HAB-201', 'Habitacion 201', 'ROOM', 'Habitacion para 4 ninas', 3, 'P2', '201', 4, 25.00, true),
('HAB-202', 'Habitacion 202', 'ROOM', 'Habitacion para 4 ninas', 3, 'P2', '202', 4, 25.00, true),
('SALA-EST', 'Sala de Estudio', 'STUDY_ROOM', 'Sala de estudio y tareas', NULL, 'P1', NULL, 20, 60.00, true),
('SALA-REC', 'Sala de Recreacion', 'RECREATION', 'Area de juegos y recreation', NULL, 'P1', NULL, 30, 80.00, true),
('COMEDOR', 'Comedor', 'DINING_ROOM', 'Comedor principal', NULL, 'P1', NULL, 40, 100.00, true),
('JARDIN', 'Jardin', 'OUTDOOR', 'Area exterior y jardin', NULL, NULL, NULL, NULL, 200.00, true),
('PARKING', 'Estacionamiento', 'PARKING', 'Estacionamiento de vehiculos', NULL, NULL, NULL, 10, 150.00, true),
('PATIO', 'Patio Central', 'OUTDOOR', 'Patio central de la casa hogar', NULL, NULL, NULL, NULL, 120.00, true),
('BANO-COM', 'Banos Comunes', 'BATHROOM', 'Banos del primer piso', 2, 'P1', NULL, NULL, 15.00, true)
ON DUPLICATE KEY UPDATE name=name;

-- ============================================================
-- Personal (Staff)
-- ============================================================
INSERT INTO staff (employee_code, first_name, last_name, document_type, document_number, birth_date, gender, hire_date, position, department, shift, phone, emergency_contact_name, emergency_contact_phone, salary, status, is_active, created_at, deleted)
VALUES
('EMP-001', 'Maria', 'Garcia', 'DNI', '87654321', '1985-03-15', 'FEMALE', '2020-01-15', 'Supervisora General', 'Administracion', 'MANANA', '555-0101', 'Juan Garcia', '555-0201', 3500.00, 'ACTIVE', true, NOW(), false),
('EMP-002', 'Carlos', 'Rodriguez', 'DNI', '11223344', '1990-07-22', 'MALE', '2021-06-01', 'Tecnico de Mantenimiento', 'Mantenimiento', 'MANANA', '555-0102', 'Sofia Rodriguez', '555-0202', 2800.00, 'ACTIVE', true, NOW(), false),
('EMP-003', 'Ana', 'Martinez', 'DNI', '55667788', '1988-11-10', 'FEMALE', '2021-03-20', 'Encargada de Inventario', 'Inventario', 'MANANA', '555-0103', 'Pedro Martinez', '555-0203', 2600.00, 'ACTIVE', true, NOW(), false),
('EMP-004', 'Luis', 'Hernandez', 'DNI', '99887766', '1992-05-08', 'MALE', '2022-01-10', 'Cuidador de Residentes', 'Residencias', 'TARDE', '555-0104', 'Carmen Hernandez', '555-0204', 2400.00, 'ACTIVE', true, NOW(), false),
('EMP-005', 'Laura', 'Lopez', 'DNI', '33445566', '1987-09-25', 'FEMALE', '2020-08-05', 'Coordinadora de RRHH', 'Recursos Humanos', 'MANANA', '555-0105', 'Roberto Lopez', '555-0205', 3200.00, 'ACTIVE', true, NOW(), false),
('EMP-006', 'Pedro', 'Sanchez', 'DNI', '77889900', '1995-01-30', 'MALE', '2023-02-15', 'Asistente Administrativo', 'Administracion', 'MANANA', '555-0106', 'Ana Sanchez', '555-0206', 2200.00, 'ACTIVE', true, NOW(), false),
('EMP-007', 'Miguel', 'Flores', 'DNI', '12345679', '1993-04-12', 'MALE', '2022-07-01', 'Tecnico Electricista', 'Mantenimiento', 'TARDE', '555-0107', 'Elena Flores', '555-0207', 2700.00, 'ACTIVE', true, NOW(), false),
('EMP-008', 'Rosa', 'Torres', 'DNI', '98765432', '1991-12-18', 'FEMALE', '2022-09-10', 'Tecnico de Plomeria', 'Mantenimiento', 'NOCHE', '555-0108', 'Jorge Torres', '555-0208', 2700.00, 'ACTIVE', true, NOW(), false),
('EMP-009', 'Elena', 'Ruiz', 'DNI', '44556677', '1989-06-20', 'FEMALE', '2021-11-15', 'Cocinera Principal', 'Cocina', 'MANANA', '555-0109', 'Manuel Ruiz', '555-0209', 2300.00, 'ACTIVE', true, NOW(), false),
('EMP-010', 'Jorge', 'Diaz', 'DNI', '22334455', '1994-08-03', 'MALE', '2023-05-01', 'Auxiliar de Cocina', 'Cocina', 'TARDE', '555-0110', 'Laura Diaz', '555-0210', 2000.00, 'ACTIVE', true, NOW(), false)
ON DUPLICATE KEY UPDATE employee_code=employee_code;

-- ============================================================
-- Activos (Assets)
-- ============================================================
INSERT INTO assets (asset_code, name, description, category, subcategory, brand, model, serial_number, manufacture_year, purchase_date, purchase_cost, location_id, status, criticality, expected_life_years, last_maintenance_date, next_maintenance_date, maintenance_interval_days, created_at, deleted)
VALUES
('ACT-001', 'Aire Acondicionado Central', 'Sistema de aire acondicionado central para el edificio principal', 'HVAC', 'AC_UNIT', 'Carrier', 'XCT-200', 'SN-AC-001', 2019, '2019-06-15', 15000.00, 1, 'OPERATIONAL', 'HIGH', 15, '2026-01-15', '2026-04-15', 90, NOW(), false),
('ACT-002', 'Calentador de Agua 1', 'Calentador de agua para habitaciones primer piso', 'PLUMBING', 'WATER_HEATER', 'Rheem', 'PROG-50', 'SN-WH-001', 2020, '2020-03-10', 2500.00, 2, 'OPERATIONAL', 'HIGH', 12, '2026-02-20', '2026-08-20', 180, NOW(), false),
('ACT-003', 'Calentador de Agua 2', 'Calentador de agua para habitaciones segundo piso', 'PLUMBING', 'WATER_HEATER', 'Rheem', 'PROG-50', 'SN-WH-002', 2020, '2020-03-10', 2500.00, 3, 'OPERATIONAL', 'HIGH', 12, '2026-02-20', '2026-08-20', 180, NOW(), false),
('ACT-004', 'Generador Electrico', 'Generador de emergencia 50KVA', 'ELECTRICAL', 'GENERATOR', 'Caterpillar', 'DE50', 'SN-GEN-001', 2018, '2018-01-20', 45000.00, 8, 'OPERATIONAL', 'CRITICAL', 20, '2026-03-01', '2026-06-01', 90, NOW(), false),
('ACT-005', 'Lavadora Industrial', 'Lavadora industrial para lavanderia', 'LAUNDRY', 'WASHER', 'Speed Queen', 'MSA-30', 'SN-LW-001', 2021, '2021-05-12', 8000.00, 5, 'OPERATIONAL', 'MEDIUM', 10, '2026-01-10', '2026-07-10', 180, NOW(), false),
('ACT-006', 'Secadora Industrial', 'Secadora industrial para lavanderia', 'LAUNDRY', 'DRYER', 'Speed Queen', 'ASA-30', 'SN-LD-001', 2021, '2021-05-12', 6000.00, 5, 'OPERATIONAL', 'MEDIUM', 10, '2026-01-10', '2026-07-10', 180, NOW(), false),
('ACT-007', 'Bomba de Agua Principal', 'Bomba de suministro de agua', 'PLUMBING', 'WATER_PUMP', 'Grundfos', 'CM3-5', 'SN-WP-001', 2019, '2019-09-01', 3500.00, 8, 'NEEDS_MAINTENANCE', 'HIGH', 15, '2025-12-01', '2026-03-01', 90, NOW(), false),
('ACT-008', 'Sistema de Seguridad CCTV', 'Sistema de camaras de seguridad', 'SECURITY', 'CCTV', 'Hikvision', 'DS-2CD2T47', 'SN-CC-001', 2022, '2022-02-28', 5000.00, 1, 'OPERATIONAL', 'HIGH', 8, '2026-04-01', '2026-10-01', 180, NOW(), false),
('ACT-009', 'Refrigerador Comercial 1', 'Refrigerador para cocina', 'KITCHEN', 'REFRIGERATOR', 'Whirlpool', 'WRF535SWHZ', 'SN-RF-001', 2022, '2022-06-15', 1800.00, 4, 'OPERATIONAL', 'MEDIUM', 12, '2026-03-15', '2026-09-15', 180, NOW(), false),
('ACT-010', 'Refrigerador Comercial 2', 'Refrigerador para cocina', 'KITCHEN', 'REFRIGERATOR', 'Whirlpool', 'WRF535SWHZ', 'SN-RF-002', 2022, '2022-06-15', 1800.00, 4, 'OPERATIONAL', 'MEDIUM', 12, '2026-03-15', '2026-09-15', 180, NOW(), false),
('ACT-011', 'Horno Industrial', 'Horno para cocina comercial', 'KITCHEN', 'OVEN', 'Rational', 'iCombi Pro', 'SN-OV-001', 2021, '2021-09-20', 12000.00, 4, 'OPERATIONAL', 'MEDIUM', 15, '2026-02-10', '2026-08-10', 180, NOW(), false),
('ACT-012', 'Camioneta de Servicio', 'Vehiculo para transporte y compras', 'VEHICLE', 'PICKUP', 'Nissan', 'NP300', 'SN-VH-001', 2021, '2021-01-15', 35000.00, 10, 'OPERATIONAL', 'MEDIUM', 10, '2026-04-01', '2026-07-01', 90, NOW(), false),
('ACT-013', 'Extintor Bloque A', 'Extintor ABC para primer piso', 'FIRE_SAFETY', 'EXTINGUISHER', 'Amerex', 'B402T', 'SN-EX-001', 2023, '2023-01-10', 150.00, 2, 'OPERATIONAL', 'CRITICAL', 15, '2026-01-10', '2027-01-10', 365, NOW(), false),
('ACT-014', 'UPS Data Center', 'UPS para equipo de computo', 'ELECTRICAL', 'UPS', 'APC', 'SMT3000IC', 'SN-UPS-001', 2023, '2023-03-20', 3500.00, 7, 'DECOMMISSIONED', 'LOW', 8, '2025-06-01', '2026-06-01', 180, NOW(), false),
('ACT-015', 'Aire Acondicionado Sala', 'Mini split para sala de estudio', 'HVAC', 'MINI_SPLIT', 'Daikin', 'FTXB35', 'SN-AC-002', 2023, '2023-08-10', 1200.00, 6, 'OPERATIONAL', 'MEDIUM', 12, '2026-02-01', '2026-08-01', 180, NOW(), false)
ON DUPLICATE KEY UPDATE asset_code=asset_code;

-- ============================================================
-- Ordenes de trabajo (Work Orders)
-- ============================================================
INSERT INTO work_orders (order_number, title, description, type, priority, status, location_id, location_name, asset_id, asset_name, requested_by_id, requested_by_name, assigned_to_id, assigned_to_name, supervisor_id, supervisor_name, estimated_hours, actual_hours, scheduled_start_date, scheduled_end_date, actual_start_date, actual_end_date, cost_materials, cost_labor, cost_total, completion_notes, requires_external_vendor, vendor_name, created_at, deleted)
VALUES
('WO-2026-001', 'Reparacion de aire acondicionado', 'El aire central no enfría correctamente, necesita revisión del gas refrigerante', 'CORRECTIVE', 'HIGH', 'COMPLETED', 1, 'Edificio Principal', 1, 'Aire Acondicionado Central', 1, 'Admin Sistema', 2, 'Carlos Rodriguez', 1, 'Maria Garcia', 8.00, 6.50, '2026-05-01', '2026-05-03', '2026-05-01 08:00:00', '2026-05-02 14:30:00', 250.00, 500.00, 750.00, 'Se recargó gas refrigerante R-410A y se limpiaron los filtros. Funcionamiento normal.', false, NULL, NOW(), false),
('WO-2026-002', 'Mantenimiento preventivo calentador', 'Mantenimiento programado semestral del calentador primer piso', 'PREVENTIVE', 'MEDIUM', 'COMPLETED', 2, 'Planta 1', 2, 'Calentador de Agua 1', 1, 'Admin Sistema', 7, 'Miguel Flores', 1, 'Maria Garcia', 4.00, 3.00, '2026-05-10', '2026-05-10', '2026-05-10 09:00:00', '2026-05-10 12:00:00', 80.00, 200.00, 280.00, 'Se limpió el tanque, se reemplazó ánodo de magnesio y se verificó la temperatura.', false, NULL, NOW(), false),
('WO-2026-003', 'Fuga de agua en habitacion 102', 'Reporte de fuga de agua en el techo de la habitacion 102', 'CORRECTIVE', 'URGENT', 'IN_PROGRESS', 2, 'Planta 1', NULL, NULL, 4, 'Luis Hernandez', 8, 'Rosa Torres', 1, 'Maria Garcia', 6.00, NULL, '2026-07-24', '2026-07-26', '2026-07-24 08:00:00', NULL, 150.00, NULL, NULL, NULL, false, NULL, NOW(), false),
('WO-2026-004', 'Pintura de sala de recreacion', 'Pintar las paredes de la sala de recreacion que estan desgastadas', 'CORRECTIVE', 'LOW', 'PENDING', 7, 'Sala de Recreacion', NULL, NULL, 1, 'Admin Sistema', NULL, NULL, NULL, NULL, 16.00, NULL, '2026-08-01', '2026-08-05', NULL, NULL, 500.00, NULL, NULL, NULL, false, NULL, NOW(), false),
('WO-2026-005', 'Inspeccion sistema de seguridad CCTV', 'Revision trimestral del sistema de camaras de seguridad', 'INSPECTION', 'MEDIUM', 'ASSIGNED', 1, 'Edificio Principal', 8, 'Sistema de Seguridad CCTV', 1, 'Admin Sistema', 2, 'Carlos Rodriguez', 1, 'Maria Garcia', 3.00, NULL, '2026-07-28', '2026-07-28', NULL, NULL, NULL, NULL, NULL, NULL, false, NULL, NOW(), false),
('WO-2026-006', 'Reparacion lavadora industrial', 'La lavadora hace ruido excesivo durante el centrifugado', 'CORRECTIVE', 'HIGH', 'PENDING_REVIEW', 5, 'Lavanderia', 5, 'Lavadora Industrial', 9, 'Elena Ruiz', 7, 'Miguel Flores', 1, 'Maria Garcia', 5.00, 4.50, '2026-07-20', '2026-07-22', '2026-07-20 08:00:00', '2026-07-21 12:30:00', 120.00, 300.00, 420.00, 'Se reemplazaron rodamientos y correa del tambor. Se realizó prueba de funcionamiento.', false, NULL, NOW(), false),
('WO-2026-007', 'Calibracion generador de emergencia', 'Calibración trimestral del generador', 'CALIBRATION', 'HIGH', 'SCHEDULED', 8, 'Almacen General', 4, 'Generador Electrico', 1, 'Admin Sistema', 2, 'Carlos Rodriguez', 1, 'Maria Garcia', 4.00, NULL, '2026-08-15', '2026-08-15', NULL, NULL, NULL, NULL, NULL, NULL, true, 'Generadores del Norte SA', NOW(), false),
('WO-2026-008', 'Sustitucion de bomba de agua', 'La bomba principal presenta fallas recurrentes, necesita reemplazo', 'CORRECTIVE', 'CRITICAL', 'ASSIGNED', 8, 'Almacen General', 7, 'Bomba de Agua Principal', 1, 'Admin Sistema', 8, 'Rosa Torres', 1, 'Maria Garcia', 12.00, NULL, '2026-07-30', '2026-08-02', NULL, NULL, 3500.00, NULL, NULL, NULL, true, 'Plomeros Profesionales SA', NOW(), false),
('WO-2026-009', 'Revision horno industrial', 'El horno no mantiene temperatura constante', 'CORRECTIVE', 'MEDIUM', 'ON_HOLD', 4, 'Cocina Principal', 11, 'Horno Industrial', 9, 'Elena Ruiz', 2, 'Carlos Rodriguez', 1, 'Maria Garcia', 6.00, NULL, '2026-07-22', '2026-07-24', NULL, NULL, NULL, NULL, NULL, NULL, true, 'Rational Service', NOW(), false),
('WO-2026-010', 'Poda del jardin', 'Mantenimiento del jardin y areas verdes', 'PREVENTIVE', 'LOW', 'COMPLETED', 9, 'Jardin', NULL, NULL, 1, 'Admin Sistema', 2, 'Carlos Rodriguez', 1, 'Maria Garcia', 8.00, 7.00, '2026-07-01', '2026-07-02', '2026-07-01 07:00:00', '2026-07-01 14:00:00', 50.00, 350.00, 400.00, 'Poda completa de arboles, corte de cesped y limpieza de areas verdes.', false, NULL, NOW(), false),
('WO-2026-011', 'Mantenimiento preventivo A/C sala estudio', 'Mantenimiento semestral del mini split de la sala', 'PREVENTIVE', 'MEDIUM', 'COMPLETED', 6, 'Sala de Estudio', 15, 'Aire Acondicionado Sala', 1, 'Admin Sistema', 2, 'Carlos Rodriguez', 1, 'Maria Garcia', 2.00, 1.50, '2026-06-15', '2026-06-15', '2026-06-15 10:00:00', '2026-06-15 11:30:00', 30.00, 100.00, 130.00, 'Limpieza de filtros y serpentines. Gas refrigerante en niveles normales.', false, NULL, NOW(), false),
('WO-2026-012', 'Instalacion extintores nuevos', 'Instalar extintores actualizados en todas las habitaciones', 'CORRECTIVE', 'HIGH', 'CANCELLED', NULL, NULL, NULL, NULL, 5, 'Laura Lopez', NULL, NULL, 1, 'Maria Garcia', 8.00, NULL, '2026-06-01', '2026-06-03', NULL, NULL, 600.00, NULL, NULL, NULL, false, NULL, NOW(), false)
ON DUPLICATE KEY UPDATE order_number=order_number;

-- ============================================================
-- Comentarios de ordenes de trabajo
-- ============================================================
INSERT INTO work_order_comments (work_order_id, author_id, author_name, author_role, content, comment_type, is_internal, created_at, deleted)
VALUES
(1, 1, 'Admin Sistema', 'ADMIN', 'Se detectó que el gas refrigerante estaba bajo. Programar recarga urgente.', 'OBSERVATION', false, '2026-05-01 08:30:00', false),
(1, 2, 'Carlos Rodriguez', 'MAINTENANCE', 'Se completó la recarga de gas y limpieza de filtros. El equipo funciona correctamente.', 'RESOLUTION', false, '2026-05-02 14:30:00', false),
(3, 4, 'Luis Hernandez', 'RESIDENTS', 'La fuga parece venir del baño de arriba. El agua está goteando por el techo.', 'OBSERVATION', false, '2026-07-24 07:15:00', false),
(3, 8, 'Rosa Torres', 'MAINTENANCE', 'Iniciando diagnóstico. Es posible que sea una tubería del segundo piso.', 'OBSERVATION', true, '2026-07-24 08:00:00', false),
(5, 2, 'Carlos Rodriguez', 'MAINTENANCE', 'Necesito acceder a todas las cámaras. Programar revisión para el lunes temprano.', 'OBSERVATION', true, '2026-07-25 09:00:00', false),
(6, 7, 'Miguel Flores', 'MAINTENANCE', 'Encontrados rodamientos desgastados y correa floja. Solicité piezas de reemplazo.', 'OBSERVATION', false, '2026-07-20 15:00:00', false),
(8, 1, 'Admin Sistema', 'ADMIN', 'Se contactó a Plomeros Profesionales. Cotización: $3,500 MXN. Aprobado por dirección.', 'OBSERVATION', false, '2026-07-26 10:00:00', false),
(9, 2, 'Carlos Rodriguez', 'MAINTENANCE', 'En espera de técnico especializado de Rational. El sensor de temperatura parece defectuoso.', 'OBSERVATION', true, '2026-07-23 11:00:00', false),
(10, 1, 'Admin Sistema', 'ADMIN', 'Trabajo completado satisfactoriamente. El jardin está en buenas condiciones.', 'RESOLUTION', false, '2026-07-02 14:30:00', false),
(12, 5, 'Laura Lopez', 'HR', 'Cancelado por cambio en la normativa de seguridad. Se reprogramará con el nuevo proveedor.', 'CANCELLATION_NOTE', false, '2026-06-02 09:00:00', false)
ON DUPLICATE KEY UPDATE work_order_id=work_order_id;

-- ============================================================
-- Items de inventario
-- ============================================================
INSERT INTO inventory_items (code, name, description, category, unit_of_measure, current_stock, minimum_stock, maximum_stock, reorder_point, unit_cost, location_id, supplier_name, supplier_sku, last_purchase_date, last_purchase_cost, is_active, created_at, deleted)
VALUES
('INV-001', 'Jabon para Ropa', 'Jabon liquido para lavadora industrial', 'LIMPIEZA', 'LITRO', 50.00, 20.00, 100.00, 30.00, 45.00, 8, 'Distribuidora Limpia', 'SKU-JAB-001', '2026-06-15', 45.00, true, NOW(), false),
('INV-002', 'Desinfectante Multiusos', 'Desinfectante para superficies', 'LIMPIEZA', 'LITRO', 30.00, 15.00, 60.00, 20.00, 35.00, 8, 'Distribuidora Limpia', 'SKU-DES-001', '2026-06-10', 35.00, true, NOW(), false),
('INV-003', 'Papel Higienico', 'Rollo doble hoja - 48 rollos por caja', 'HIGIENE', 'CAJA', 15.00, 10.00, 40.00, 12.00, 180.00, 8, 'Suministros Sanitarios SA', 'SKU-PAP-001', '2026-07-01', 180.00, true, NOW(), false),
('INV-004', 'Jabon de Manos', 'Jabon antibacterial para lavamanos', 'HIGIENE', 'LITRO', 25.00, 10.00, 50.00, 15.00, 30.00, 8, 'Suministros Sanitarios SA', 'SKU-JBM-001', '2026-06-20', 30.00, true, NOW(), false),
('INV-005', 'Toallas de Papel', 'Toallas interfoliadas - 200 hojas', 'HIGIENE', 'CAJA', 8.00, 5.00, 25.00, 8.00, 120.00, 8, 'Suministros Sanitarios SA', 'SKU-TOW-001', '2026-06-25', 120.00, true, NOW(), false),
('INV-006', 'Filtros de Aire AC', 'Filtros para aire acondicionado central', 'MANTENIMIENTO', 'PIEZA', 6.00, 4.00, 20.00, 6.00, 85.00, 8, 'Filtros Industriales', 'SKU-FAC-001', '2026-05-01', 85.00, true, NOW(), false),
('INV-007', 'Bulbos LED', 'Bulbo LED 12W base E27', 'ELECTRICOS', 'PIEZA', 30.00, 10.00, 50.00, 15.00, 25.00, 8, 'ElectroSuministros', 'SKU-BUL-001', '2026-06-01', 25.00, true, NOW(), false),
('INV-008', 'Cinta Adhesiva', 'Cinta adhesiva multiusos 48mm', 'MANTENIMIENTO', 'PIEZA', 20.00, 8.00, 40.00, 10.00, 35.00, 8, 'Ferreteria Central', 'SKU-CIN-001', '2026-04-15', 35.00, true, NOW(), false),
('INV-009', 'Pintura Blanca', 'Pintura vinílica blanca 19L', 'MANTENIMIENTO', 'GALON', 5.00, 3.00, 15.00, 4.00, 650.00, 8, 'Pinturas del Norte', 'SKU-PIN-001', '2026-03-20', 650.00, true, NOW(), false),
('INV-010', 'Tubos PVC 1/2', 'Tubos PVC de media pulgada 6m', 'PLOMERIA', 'PIEZA', 12.00, 5.00, 30.00, 8.00, 85.00, 8, 'Plomeria Total', 'SKU-TUB-001', '2026-05-10', 85.00, true, NOW(), false),
('INV-011', 'Aceite para Motor', 'Aceite sintetico 5W-30 4L', 'VEHICULOS', 'LITRO', 8.00, 4.00, 20.00, 6.00, 450.00, 8, 'AutoPartes Express', 'SKU-OIL-001', '2026-06-01', 450.00, true, NOW(), false),
('INV-012', 'Gas Refrigerante R-410A', 'Gas refrigerante para aire acondicionado', 'MANTENIMIENTO', 'KILOGRAMO', 3.00, 2.00, 10.00, 3.00, 350.00, 8, 'Climatizacion Pro', 'SKU-GAS-001', '2026-05-01', 350.00, true, NOW(), false),
('INV-013', 'Guantes de Proteccion', 'Guantes de nitrilo talle M', 'SEGURIDAD', 'PAR', 50.00, 20.00, 100.00, 25.00, 12.00, 8, 'Seguridad Industrial SA', 'SKU-GUA-001', '2026-07-01', 12.00, true, NOW(), false),
('INV-014', 'Desinfectante de Piso', 'Gallón de descentrate para pisos', 'LIMPIEZA', 'GALON', 2.00, 3.00, 10.00, 4.00, 280.00, 8, 'Distribuidora Limpia', 'SKU-DPI-001', '2026-04-01', 280.00, true, NOW(), false),
('INV-015', 'Correa para Lavadora', 'Correa de transmision para lavadora Speed Queen', 'MANTENIMIENTO', 'PIEZA', 1.00, 2.00, 5.00, 2.00, 150.00, 8, 'Speed Queen Repuestos', 'SKU-COR-001', '2026-03-15', 150.00, true, NOW(), false)
ON DUPLICATE KEY UPDATE code=code;

-- ============================================================
-- Movimientos de inventario
-- ============================================================
INSERT INTO inventory_movements (inventory_item_id, movement_type, quantity, unit_cost, total_cost, reference_type, reference_id, notes, performed_by_id, performed_by_name, movement_date, created_at, deleted)
VALUES
(1, 'IN', 30.00, 45.00, 1350.00, 'PURCHASE_ORDER', NULL, 'Compra trimestral de jabon para ropa', 3, 'Ana Martinez', '2026-06-15 10:00:00', '2026-06-15 10:00:00', false),
(1, 'OUT', 5.00, 45.00, 225.00, 'WORK_ORDER', 10, 'Entregado para limpieza del jardin', 3, 'Ana Martinez', '2026-07-01 08:00:00', '2026-07-01 08:00:00', false),
(2, 'IN', 20.00, 35.00, 700.00, 'PURCHASE_ORDER', NULL, 'Reposicion de desinfectante', 3, 'Ana Martinez', '2026-06-10 09:00:00', '2026-06-10 09:00:00', false),
(2, 'OUT', 3.00, 35.00, 105.00, NULL, NULL, 'Limpieza semanal de areas comunes', 3, 'Ana Martinez', '2026-07-15 08:00:00', '2026-07-15 08:00:00', false),
(3, 'IN', 20.00, 180.00, 3600.00, 'PURCHASE_ORDER', NULL, 'Compra mensual de papel higienico', 3, 'Ana Martinez', '2026-07-01 09:00:00', '2026-07-01 09:00:00', false),
(3, 'OUT', 5.00, 180.00, 900.00, NULL, NULL, 'Distribucion a habitaciones y banos', 3, 'Ana Martinez', '2026-07-01 14:00:00', '2026-07-01 14:00:00', false),
(6, 'IN', 8.00, 85.00, 680.00, 'PURCHASE_ORDER', NULL, 'Filtros para mantenimiento trimestral AC', 3, 'Ana Martinez', '2026-05-01 08:00:00', '2026-05-01 08:00:00', false),
(6, 'OUT', 2.00, 85.00, 170.00, 'WORK_ORDER', 1, 'Utilizados en reparacion AC central', 3, 'Ana Martinez', '2026-05-01 10:00:00', '2026-05-01 10:00:00', false),
(7, 'IN', 20.00, 25.00, 500.00, 'PURCHASE_ORDER', NULL, 'Reposicion de bulbos LED', 3, 'Ana Martinez', '2026-06-01 09:00:00', '2026-06-01 09:00:00', false),
(7, 'OUT', 4.00, 25.00, 100.00, NULL, NULL, 'Reemplazo de bulbos quemados habitaciones', 3, 'Ana Martinez', '2026-06-15 11:00:00', '2026-06-15 11:00:00', false),
(9, 'IN', 3.00, 650.00, 1950.00, 'PURCHASE_ORDER', NULL, 'Pintura para proyecto de pintura sala recreacion', 3, 'Ana Martinez', '2026-03-20 09:00:00', '2026-03-20 09:00:00', false),
(10, 'IN', 10.00, 85.00, 850.00, 'PURCHASE_ORDER', NULL, 'Tubos PVC para reparaciones de plomeria', 3, 'Ana Martinez', '2026-05-10 08:00:00', '2026-05-10 08:00:00', false),
(10, 'OUT', 3.00, 85.00, 255.00, 'WORK_ORDER', 3, 'Utilizados en reparacion fuga habitacion 102', 3, 'Ana Martinez', '2026-07-24 10:00:00', '2026-07-24 10:00:00', false),
(11, 'IN', 6.00, 450.00, 2700.00, 'PURCHASE_ORDER', NULL, 'Aceite para mantenimiento de camioneta', 3, 'Ana Martinez', '2026-06-01 09:00:00', '2026-06-01 09:00:00', false),
(12, 'IN', 4.00, 350.00, 1400.00, 'PURCHASE_ORDER', NULL, 'Gas refrigerante para mantenimiento AC', 3, 'Ana Martinez', '2026-05-01 08:00:00', '2026-05-01 08:00:00', false),
(12, 'OUT', 1.00, 350.00, 350.00, 'WORK_ORDER', 1, 'Recarga de gas AC central', 3, 'Ana Martinez', '2026-05-01 10:00:00', '2026-05-01 10:00:00', false),
(13, 'IN', 30.00, 12.00, 360.00, 'PURCHASE_ORDER', NULL, 'Guantes de proteccion para personal', 3, 'Ana Martinez', '2026-07-01 09:00:00', '2026-07-01 09:00:00', false),
(14, 'ADJUSTMENT', 2.00, 280.00, 560.00, NULL, NULL, 'Ajuste por derrame accidentado', 3, 'Ana Martinez', '2026-05-15 10:00:00', '2026-05-15 10:00:00', false),
(15, 'IN', 3.00, 150.00, 450.00, 'PURCHASE_ORDER', NULL, 'Correas de repuesto para lavadora', 3, 'Ana Martinez', '2026-03-15 08:00:00', '2026-03-15 08:00:00', false),
(15, 'OUT', 1.00, 150.00, 150.00, 'WORK_ORDER', 6, 'Correa reemplazada en lavadora industrial', 3, 'Ana Martinez', '2026-07-20 10:00:00', '2026-07-20 10:00:00', false)
ON DUPLICATE KEY UPDATE inventory_item_id=inventory_item_id;

-- ============================================================
-- Residentes
-- ============================================================
INSERT INTO residents (code, first_name, last_name, document_type, document_number, birth_date, gender, entry_date, status, room_id, guardian_name, guardian_phone, guardian_email, guardian_relationship, medical_info, dietary_restrictions, is_active, created_at, deleted)
VALUES
('RES-001', 'Sofia', 'Martinez Lopez', 'CURP', 'SOML050115MDFRRL09', '2005-01-15', 'FEMALE', '2023-09-01', 'ACTIVE', 9, 'Carmen Lopez', '555-3001', 'carmen.lopez@email.com', 'Madre', 'Sin alergias conocidas. Tratamiento dermatitis leve.', 'Ninguna', true, NOW(), false),
('RES-002', 'Diego', 'Hernandez Garcia', 'CURP', 'DEHG040620HDFRRG08', '2004-06-20', 'MALE', '2023-09-01', 'ACTIVE', 9, 'Roberto Hernandez', '555-3002', 'roberto.h@email.com', 'Padre', 'Asma leve, usa inhalador cuando es necesario.', 'Sin mariscos', true, NOW(), false),
('RES-003', 'Valentina', 'Ruiz Torres', 'CURP', 'VART060310MDFRRS05', '2006-03-10', 'FEMALE', '2024-01-15', 'ACTIVE', 11, 'Elena Torres', '555-3003', 'elena.t@email.com', 'Tia', 'Ninguna condicion especial.', 'Ninguna', true, NOW(), false)
-- ('RES-004', 'Andres', 'Diaz Flores', 'CURP', 'ADHF030815HDFRRL02', '2003-08-15', 'MALE', '2024-01-15', 'ACTIVE', 9, 'Patricia Flores', '555-3004', 'patricia.f@email.com', 'Tia', 'Restriccion deportiva por lesion de rodilla.', 'Ninguna', true, NOW(), false),
-- ('RES-005', 'Camila', 'Sanchez Rivera', 'CURP', 'CASR050525MDFRVN07', '2005-05-25', 'FEMALE', '2024-03-01', 'ACTIVE', 11, 'Jorge Sanchez', '555-3005', 'jorge.s@email.com', 'Padre', 'Ninguna condicion especial.', 'Vegetariana', true, NOW(), false),
-- ('RES-006', 'Mateo', 'Torres Martinez', 'CURP', 'METM070930HDFRRL01', '2007-09-30', 'MALE', '2024-06-01', 'ACTIVE', 10, 'Laura Martinez', '555-3006', 'laura.m@email.com', 'Madre', 'Alergia a la penicilina.', 'Sin lactosa', true, NOW(), false),
-- ('RES-007', 'Isabella', 'Flores Garcia', 'CURP', 'ISFG080212MDFRRS03', '2008-02-12', 'FEMALE', '2024-06-01', 'ACTIVE', 11, 'Miguel Garcia', '555-3007', 'miguel.g@email.com', 'Padre', 'Ninguna condicion especial.', 'Ninguna', true, NOW(), false),
-- ('RES-008', 'Sebastian', 'Garcia Lopez', 'CURP', 'SEGL060705HDFRRL06', '2006-07-05', 'MALE', '2025-01-10', 'ACTIVE', 10, 'Rosa Lopez', '555-3008', 'rosa.l@email.com', 'Abuela', 'TDAH, toma medicamento diario.', 'Ninguna', true, NOW(), false),
-- ('RES-009', 'Mariana', 'Rodriguez Diaz', 'CURP', 'MARD090118MDFRRS04', '2009-01-18', 'FEMALE', '2025-03-01', 'ACTIVE', 12, 'Fernando Rodriguez', '555-3009', 'fernando.r@email.com', 'Padre', 'Sin alergias.', 'Sin gluten', true, NOW(), false),
-- ('RES-010', 'Daniel', 'Lopez Martinez', 'CURP', 'DOLM040322HDFRRL10', '2004-03-22', 'MALE', '2025-06-15', 'INACTIVE', NULL, 'Adriana Martinez', '555-3010', 'adriana.m@email.com', 'Madre', 'Se dio de baja por reubicacion familiar.', 'Ninguna', false, NOW(), false)
ON DUPLICATE KEY UPDATE code=code;

-- ============================================================
-- Asignaciones de personal a turnos
-- ============================================================
INSERT INTO staff_shifts (staff_id, shift_id, start_date, end_date, is_active)
SELECT s.id, sh.id, '2026-01-01', NULL, true
FROM staff s, shifts sh
WHERE s.employee_code = 'EMP-001' AND sh.name = 'Diurno Lunes'
ON DUPLICATE KEY UPDATE is_active=true;
INSERT INTO staff_shifts (staff_id, shift_id, start_date, end_date, is_active)
SELECT s.id, sh.id, '2026-01-01', NULL, true
FROM staff s, shifts sh
WHERE s.employee_code = 'EMP-002' AND sh.name = 'Diurno Martes'
ON DUPLICATE KEY UPDATE is_active=true;
INSERT INTO staff_shifts (staff_id, shift_id, start_date, end_date, is_active)
SELECT s.id, sh.id, '2026-01-01', NULL, true
FROM staff s, shifts sh
WHERE s.employee_code = 'EMP-003' AND sh.name = 'Diurno Miercoles'
ON DUPLICATE KEY UPDATE is_active=true;
INSERT INTO staff_shifts (staff_id, shift_id, start_date, end_date, is_active)
SELECT s.id, sh.id, '2026-01-01', NULL, true
FROM staff s, shifts sh
WHERE s.employee_code = 'EMP-004' AND sh.name = 'Nocturno Lunes'
ON DUPLICATE KEY UPDATE is_active=true;
INSERT INTO staff_shifts (staff_id, shift_id, start_date, end_date, is_active)
SELECT s.id, sh.id, '2026-01-01', NULL, true
FROM staff s, shifts sh
WHERE s.employee_code = 'EMP-005' AND sh.name = 'Diurno Jueves'
ON DUPLICATE KEY UPDATE is_active=true;
INSERT INTO staff_shifts (staff_id, shift_id, start_date, end_date, is_active)
SELECT s.id, sh.id, '2026-01-01', NULL, true
FROM staff s, shifts sh
WHERE s.employee_code = 'EMP-006' AND sh.name = 'Diurno Viernes'
ON DUPLICATE KEY UPDATE is_active=true;
INSERT INTO staff_shifts (staff_id, shift_id, start_date, end_date, is_active)
SELECT s.id, sh.id, '2026-01-01', NULL, true
FROM staff s, shifts sh
WHERE s.employee_code = 'EMP-007' AND sh.name = 'Nocturno Martes'
ON DUPLICATE KEY UPDATE is_active=true;
INSERT INTO staff_shifts (staff_id, shift_id, start_date, end_date, is_active)
SELECT s.id, sh.id, '2026-01-01', NULL, true
FROM staff s, shifts sh
WHERE s.employee_code = 'EMP-008' AND sh.name = 'Nocturno Miercoles'
ON DUPLICATE KEY UPDATE is_active=true;
INSERT INTO staff_shifts (staff_id, shift_id, start_date, end_date, is_active)
SELECT s.id, sh.id, '2026-01-01', NULL, true
FROM staff s, shifts sh
WHERE s.employee_code = 'EMP-009' AND sh.name = 'Diurno Sabado'
ON DUPLICATE KEY UPDATE is_active=true;
INSERT INTO staff_shifts (staff_id, shift_id, start_date, end_date, is_active)
SELECT s.id, sh.id, '2026-01-01', NULL, true
FROM staff s, shifts sh
WHERE s.employee_code = 'EMP-010' AND sh.name = 'Nocturno Domingo'
ON DUPLICATE KEY UPDATE is_active=true;

-- ============================================================
-- Mantenimientos preventivos programados
-- ============================================================
INSERT INTO preventive_maintenance_schedules (asset_id, name, description, frequency_type, frequency_value, day_of_week, day_of_month, start_date, estimated_duration_hours, required_skills, required_tools, checklist, is_active, last_generated_date, next_generation_date, created_at, deleted)
VALUES
(1, 'Mantenimiento trimestral AC Central', 'Limpieza de filtros, revision de gas y limpieza de serpentines', 'QUARTERLY', 1, NULL, NULL, '2026-01-01', 4.00, 'Tecnico HVAC, Certificado EPA', 'Manometro, Kit de recarga R-410A, Multimetro', '["Limpiar filtros", "Verificar nivel de gas", "Limpiar serpentines del evaporador", "Limpiar serpentines del condensador", "Verificar termostato", "Probar funcionamiento"]', true, '2026-04-01', '2026-07-01', NOW(), false),
(4, 'Mantenimiento trimestral Generador', 'Revision de aceite, filtros y prueba de carga', 'QUARTERLY', 1, NULL, NULL, '2026-01-01', 4.00, 'Tecnico Electrico', 'Multimetro, Kit de herramientas', '["Cambiar aceite del motor", "Cambiar filtro de aire", "Revisar nivel de refrigerante", "Probar con carga del 75%", "Verificar nivel de bateria", "Inspeccionar conexiones"]', true, '2026-04-01', '2026-07-01', NOW(), false),
(5, 'Mantenimiento semestral Lavadora', 'Revision de rodamientos, correa y limpieza del tanque', 'SEMI_ANNUAL', 1, NULL, NULL, '2026-01-01', 3.00, 'Tecnico Mecanico', 'Llaves ajustables, Kit de rodamientos', '["Inspeccionar rodamientos", "Verificar tensión de correa", "Limpiar tamiz", "Limpiar filtro de agua", "Probar ciclo de centrifugado"]', true, '2026-01-10', '2026-07-10', NOW(), false),
(7, 'Mantenimiento trimestral Bomba de Agua', 'Revision de sellos, aceite y funcionamiento', 'QUARTERLY', 1, NULL, NULL, '2026-01-01', 3.00, 'Tecnico de Plomeria', 'Llaves de tubo, Multimetro', '["Verificar fugas en sellos", "Revisar presion de salida", "Inspeccionar vibraciones", "Limpiar filtros de aspiracion", "Probar presostato"]', true, '2026-04-01', '2026-07-01', NOW(), false),
(8, 'Mantenimiento semestral CCTV', 'Limpieza de lentes, verificacion de grabacion y actualizacion', 'SEMI_ANNUAL', 1, 1, NULL, '2026-01-01', 3.00, 'Tecnico de Seguridad', 'Herramienta basica, Laptop', '["Limpiar lentes de camaras", "Verificar grabacion 24h", "Revisar espacio en disco", "Actualizar firmware", "Probar alertas de movimiento", "Verificar alimentacion electrica"]', true, '2026-04-01', '2026-10-01', NOW(), false)
ON DUPLICATE KEY UPDATE asset_id=asset_id;