-- V2__replace_shifts_14x12h.sql
-- Replace 3 generic shifts with 14 daily shifts (2 per day, 12h each)

DELETE FROM staff_shifts;
DELETE FROM shifts;

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
('Nocturno Domingo', '20:00:00', '08:00:00', 'SUN', true);

INSERT INTO staff_shifts (staff_id, shift_id, start_date, end_date, is_active)
SELECT s.id, sh.id, '2026-01-01', NULL, true
FROM staff s, shifts sh
WHERE s.employee_code = 'EMP-001' AND sh.name = 'Diurno Lunes';
INSERT INTO staff_shifts (staff_id, shift_id, start_date, end_date, is_active)
SELECT s.id, sh.id, '2026-01-01', NULL, true
FROM staff s, shifts sh
WHERE s.employee_code = 'EMP-002' AND sh.name = 'Diurno Martes';
INSERT INTO staff_shifts (staff_id, shift_id, start_date, end_date, is_active)
SELECT s.id, sh.id, '2026-01-01', NULL, true
FROM staff s, shifts sh
WHERE s.employee_code = 'EMP-003' AND sh.name = 'Diurno Miercoles';
INSERT INTO staff_shifts (staff_id, shift_id, start_date, end_date, is_active)
SELECT s.id, sh.id, '2026-01-01', NULL, true
FROM staff s, shifts sh
WHERE s.employee_code = 'EMP-004' AND sh.name = 'Nocturno Lunes';
INSERT INTO staff_shifts (staff_id, shift_id, start_date, end_date, is_active)
SELECT s.id, sh.id, '2026-01-01', NULL, true
FROM staff s, shifts sh
WHERE s.employee_code = 'EMP-005' AND sh.name = 'Diurno Jueves';
INSERT INTO staff_shifts (staff_id, shift_id, start_date, end_date, is_active)
SELECT s.id, sh.id, '2026-01-01', NULL, true
FROM staff s, shifts sh
WHERE s.employee_code = 'EMP-006' AND sh.name = 'Diurno Viernes';
INSERT INTO staff_shifts (staff_id, shift_id, start_date, end_date, is_active)
SELECT s.id, sh.id, '2026-01-01', NULL, true
FROM staff s, shifts sh
WHERE s.employee_code = 'EMP-007' AND sh.name = 'Nocturno Martes';
INSERT INTO staff_shifts (staff_id, shift_id, start_date, end_date, is_active)
SELECT s.id, sh.id, '2026-01-01', NULL, true
FROM staff s, shifts sh
WHERE s.employee_code = 'EMP-008' AND sh.name = 'Nocturno Miercoles';
INSERT INTO staff_shifts (staff_id, shift_id, start_date, end_date, is_active)
SELECT s.id, sh.id, '2026-01-01', NULL, true
FROM staff s, shifts sh
WHERE s.employee_code = 'EMP-009' AND sh.name = 'Diurno Sabado';
INSERT INTO staff_shifts (staff_id, shift_id, start_date, end_date, is_active)
SELECT s.id, sh.id, '2026-01-01', NULL, true
FROM staff s, shifts sh
WHERE s.employee_code = 'EMP-010' AND sh.name = 'Nocturno Domingo';
