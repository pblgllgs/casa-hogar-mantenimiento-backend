INSERT INTO medications (resident_id, medication_name, dosage, frequency_hours, administration_route, start_date, end_date, instructions, prescribed_by, status, notes) VALUES
-- Sofía Martínez (id=1)
(1, 'Paracetamol', '500mg', 8, 'ORAL', '2025-06-01', '2025-12-31', 'Tomar con alimentos', 'Dra. Carolina Paredes', 'ACTIVE', 'Para dolores leves y fiebre'),
(1, 'Loratadina', '10mg', 24, 'ORAL', '2025-03-15', NULL, 'En caso de reacción alérgica', 'Dr. Rodrigo Vargas', 'ACTIVE', 'Alergia a mariscos - uso según necesidad'),
(1, 'Suplemento de Hierro', '30mg', 24, 'ORAL', '2025-09-01', '2026-03-01', 'En ayunas, con jugo de naranja', 'Dra. Carolina Paredes', 'ACTIVE', 'Déficit leve de hierro detectado en control'),

-- Diego Hernández (id=2)
(2, 'Ibuprofeno', '200mg', 12, 'ORAL', '2025-07-01', '2025-08-15', 'Tomar después de alimentos', 'Dr. Felipe Morales', 'COMPLETED', 'Tratamiento post-molestia dental'),
(2, 'Vitamina D', '1000 UI', 24, 'ORAL', '2025-01-01', NULL, 'Una tableta al día con el almuerzo', 'Dra. Carolina Paredes', 'ACTIVE', 'Prevención - nivel bajo de vitamina D'),

-- Valentina Ruíz (id=3)
(3, 'Salbutamol', '100mcg', 8, 'INHALADO', '2025-04-01', NULL, 'Inhalar 2 puff al presentar dificultad respiratoria', 'Dr. Felipe Morales', 'ACTIVE', 'Asma leve - control según necesidad'),
(3, 'Cetirizina', '5mg', 24, 'ORAL', '2025-06-01', '2025-12-31', 'Tomar antes de dormir', 'Dr. Felipe Morales', 'ACTIVE', 'Rinitis alérgica estacional'),
(3, 'Amoxicilina', '250mg', 8, 'ORAL', '2025-10-15', '2025-10-25', 'Completar el tratamiento completo de 10 días', 'Dra. Carolina Paredes', 'COMPLETED', 'Infección respiratoria - tratamiento completado');
