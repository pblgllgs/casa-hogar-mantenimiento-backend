-- V4__seed_shift_logs.sql
-- Datos de muestra para shift_logs (Historia de turnos)

INSERT INTO shift_logs (shift_id, staff_id, log_date, comment, created_at)
SELECT sh.id, s.id, '2026-07-20', 'Inicio de turno sin novedades. Se realiza recorrido matutino por todas las instalaciones.', '2026-07-20 08:15:00'
FROM shifts sh, staff s WHERE sh.name = 'Diurno Lunes' AND s.employee_code = 'EMP-001';

INSERT INTO shift_logs (shift_id, staff_id, log_date, comment, created_at)
SELECT sh.id, s.id, '2026-07-20', 'Se detectó una fuga de agua en el baño de la habitación 103. Se reportó a mantenimiento.', '2026-07-20 10:30:00'
FROM shifts sh, staff s WHERE sh.name = 'Diurno Lunes' AND s.employee_code = 'EMP-001';

INSERT INTO shift_logs (shift_id, staff_id, log_date, comment, created_at)
SELECT sh.id, s.id, '2026-07-20', 'Llegó visita de la asistente social para evaluar a dos nuevos posibles residentes.', '2026-07-20 14:00:00'
FROM shifts sh, staff s WHERE sh.name = 'Diurno Lunes' AND s.employee_code = 'EMP-002';

INSERT INTO shift_logs (shift_id, staff_id, log_date, comment, created_at)
SELECT sh.id, s.id, '2026-07-20', 'Reunión de personal para coordinar actividades de la semana. Todo en orden.', '2026-07-20 16:00:00'
FROM shifts sh, staff s WHERE sh.name = 'Diurno Lunes' AND s.employee_code = 'EMP-001';

INSERT INTO shift_logs (shift_id, staff_id, log_date, comment, created_at)
SELECT sh.id, s.id, '2026-07-20', 'Cierre de turno. No hubo incidentes mayores. Se entrega novedad de la fuga en habitación 103.', '2026-07-20 19:45:00'
FROM shifts sh, staff s WHERE sh.name = 'Diurno Lunes' AND s.employee_code = 'EMP-001';

INSERT INTO shift_logs (shift_id, staff_id, log_date, comment, created_at)
SELECT sh.id, s.id, '2026-07-20', 'Relevo recibido sin novedades. Se realiza ronda nocturna.', '2026-07-20 20:10:00'
FROM shifts sh, staff s WHERE sh.name = 'Nocturno Lunes' AND s.employee_code = 'EMP-004';

INSERT INTO shift_logs (shift_id, staff_id, log_date, comment, created_at)
SELECT sh.id, s.id, '2026-07-20', 'Uno de los residentes (Diego Hernández) presentó malestar estomacal. Se administró medicación y se encuentra estable.', '2026-07-21 01:30:00'
FROM shifts sh, staff s WHERE sh.name = 'Nocturno Lunes' AND s.employee_code = 'EMP-004';

INSERT INTO shift_logs (shift_id, staff_id, log_date, comment, created_at)
SELECT sh.id, s.id, '2026-07-21', 'Se realizó la limpieza general de la cocina y comedor. Todo en orden.', '2026-07-21 08:30:00'
FROM shifts sh, staff s WHERE sh.name = 'Diurno Martes' AND s.employee_code = 'EMP-002';

INSERT INTO shift_logs (shift_id, staff_id, log_date, comment, created_at)
SELECT sh.id, s.id, '2026-07-21', 'Se recibió cargamento de suministros de limpieza. Se almacenó en el depósito.', '2026-07-21 11:00:00'
FROM shifts sh, staff s WHERE sh.name = 'Diurno Martes' AND s.employee_code = 'EMP-002';

INSERT INTO shift_logs (shift_id, staff_id, log_date, comment, created_at)
SELECT sh.id, s.id, '2026-07-21', 'Personal de mantenimiento reportó que la reparación de la fuga en habitación 103 está completa.', '2026-07-21 15:30:00'
FROM shifts sh, staff s WHERE sh.name = 'Diurno Martes' AND s.employee_code = 'EMP-001';

INSERT INTO shift_logs (shift_id, staff_id, log_date, comment, created_at)
SELECT sh.id, s.id, '2026-07-22', 'Se realizó el inventario mensual de la cocina. Faltan algunos insumos, se solicitará reposición.', '2026-07-22 09:00:00'
FROM shifts sh, staff s WHERE sh.name = 'Diurno Miercoles' AND s.employee_code = 'EMP-003';

INSERT INTO shift_logs (shift_id, staff_id, log_date, comment, created_at)
SELECT sh.id, s.id, '2026-07-22', 'Actividad recreativa en el patio con los residentes. Participaron 8 niños.', '2026-07-22 14:00:00'
FROM shifts sh, staff s WHERE sh.name = 'Diurno Miercoles' AND s.employee_code = 'EMP-003';

INSERT INTO shift_logs (shift_id, staff_id, log_date, comment, created_at)
SELECT sh.id, s.id, '2026-07-23', 'Se realizó la compra semanal de víveres. Se almacenó todo correctamente.', '2026-07-23 08:45:00'
FROM shifts sh, staff s WHERE sh.name = 'Diurno Jueves' AND s.employee_code = 'EMP-005';

INSERT INTO shift_logs (shift_id, staff_id, log_date, comment, created_at)
SELECT sh.id, s.id, '2026-07-23', 'Visita del médico general para revisión periódica de los residentes. Todos en buen estado de salud.', '2026-07-23 10:30:00'
FROM shifts sh, staff s WHERE sh.name = 'Diurno Jueves' AND s.employee_code = 'EMP-005';

INSERT INTO shift_logs (shift_id, staff_id, log_date, comment, created_at)
SELECT sh.id, s.id, '2026-07-23', 'Se reporta que el calentador de agua del segundo piso no funciona correctamente. Se notificó a mantenimiento.', '2026-07-23 16:00:00'
FROM shifts sh, staff s WHERE sh.name = 'Diurno Jueves' AND s.employee_code = 'EMP-001';

INSERT INTO shift_logs (shift_id, staff_id, log_date, comment, created_at)
SELECT sh.id, s.id, '2026-07-24', 'Se inició el turno con la reparación del calentador de agua del segundo piso.', '2026-07-24 08:00:00'
FROM shifts sh, staff s WHERE sh.name = 'Diurno Viernes' AND s.employee_code = 'EMP-006';

INSERT INTO shift_logs (shift_id, staff_id, log_date, comment, created_at)
SELECT sh.id, s.id, '2026-07-24', 'Calentador reparado exitosamente. Se reemplazó el termostato y se purgó el sistema.', '2026-07-24 11:30:00'
FROM shifts sh, staff s WHERE sh.name = 'Diurno Viernes' AND s.employee_code = 'EMP-006';

INSERT INTO shift_logs (shift_id, staff_id, log_date, comment, created_at)
SELECT sh.id, s.id, '2026-07-24', 'Se organizaron las actividades del fin de semana con los residentes. Película el sábado, paseo al parque el domingo.', '2026-07-24 15:00:00'
FROM shifts sh, staff s WHERE sh.name = 'Diurno Viernes' AND s.employee_code = 'EMP-001';

INSERT INTO shift_logs (shift_id, staff_id, log_date, comment, created_at)
SELECT sh.id, s.id, '2026-07-24', 'Cierre de semana. Todo en orden. Se entregan las novedades para el fin de semana.', '2026-07-24 19:30:00'
FROM shifts sh, staff s WHERE sh.name = 'Diurno Viernes' AND s.employee_code = 'EMP-006';

INSERT INTO shift_logs (shift_id, staff_id, log_date, comment, created_at)
SELECT sh.id, s.id, '2026-07-25', 'Fin de semana tranquilo. Actividades recreativas en la sala de juegos.', '2026-07-25 10:00:00'
FROM shifts sh, staff s WHERE sh.name = 'Diurno Sabado' AND s.employee_code = 'EMP-009';

INSERT INTO shift_logs (shift_id, staff_id, log_date, comment, created_at)
SELECT sh.id, s.id, '2026-07-25', 'Se preparó comida especial para los residentes. Menú: pollo al horno con verduras.', '2026-07-25 13:00:00'
FROM shifts sh, staff s WHERE sh.name = 'Diurno Sabado' AND s.employee_code = 'EMP-009';

INSERT INTO shift_logs (shift_id, staff_id, log_date, comment, created_at)
SELECT sh.id, s.id, '2026-07-26', 'Paseo dominical al parque. Asistieron 6 residentes y 2 cuidadores.', '2026-07-26 09:30:00'
FROM shifts sh, staff s WHERE sh.name = 'Diurno Domingo' AND s.employee_code = 'EMP-001';

INSERT INTO shift_logs (shift_id, staff_id, log_date, comment, created_at)
SELECT sh.id, s.id, '2026-07-26', 'Se realizó una limpieza profunda de la cocina después del desayuno.', '2026-07-26 10:30:00'
FROM shifts sh, staff s WHERE sh.name = 'Diurno Domingo' AND s.employee_code = 'EMP-010';

INSERT INTO shift_logs (shift_id, staff_id, log_date, comment, created_at)
SELECT sh.id, s.id, '2026-07-26', 'Se reporta que el foco del patio central está fundido. Se reemplazará mañana.', '2026-07-26 18:00:00'
FROM shifts sh, staff s WHERE sh.name = 'Nocturno Domingo' AND s.employee_code = 'EMP-010';
