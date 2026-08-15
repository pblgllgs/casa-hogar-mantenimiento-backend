-- V7: Seed clinical records for 3 residents

-- Resident 1: Sofia Martinez Lopez (id=1)
INSERT INTO clinical_records (resident_id, record_date, record_type, description, diagnosis, treatment, medication, dosage, doctor_name, notes, created_by) VALUES
(1, '2023-09-01', 'CONSULTA', 'Evaluacion medica de ingreso. Paciente femenina de 14 anos sin patologias cronicas.', 'Paciente sana.', 'Seguimiento pediatrico cada 6 meses.', NULL, NULL, 'Dra. Carolina Paredes', 'Peso: 48 kg, Talla: 156 cm, IMC normal.', 'admin');

INSERT INTO clinical_records (resident_id, record_date, record_type, description, diagnosis, treatment, medication, dosage, doctor_name, notes, created_by) VALUES
(1, '2023-10-12', 'ALERGIA', 'Urticaria leve tras consumir camarones. Cedio con antihistaminico.', 'Alergia alimentaria a mariscos.', 'Evitar consumo de mariscos.', 'Loratadina', '10 mg oral si presenta sintomas', 'Dr. Rodrigo Vargas', 'Episodio leve controlado.', 'admin');

INSERT INTO clinical_records (resident_id, record_date, record_type, description, medication, dosage, doctor_name, notes, created_by) VALUES
(1, '2024-04-10', 'VACUNA', 'Vacuna antigripal estacional 2024.', 'Vacuna anti-influenza trivalente', '0.5 mL IM deltoides izquierdo', 'Enf. Marcela Quiroga', 'Sin reacciones adversas.', 'admin');

INSERT INTO clinical_records (resident_id, record_date, record_type, description, doctor_name, notes, created_by) VALUES
(1, '2024-08-15', 'SIGNOS_VITALES', 'TA 105/65, FC 82, FR 18, Temp 36.4.', 'Enf. Paula Soto', 'Signos normales.', 'admin');

INSERT INTO clinical_records (resident_id, record_date, record_type, description, diagnosis, treatment, doctor_name, notes, created_by) VALUES
(1, '2025-01-12', 'NOTA', 'Estres academico pre-examenes con insomnio leve.', 'Ansiedad anticipatoria leve.', 'Rutina de sueno y apoyo emocional.', 'Psicologa Andrea Donoso', 'Revision en 3 semanas.', 'admin');

-- Resident 2: Diego Hernandez Garcia (id=2)
INSERT INTO clinical_records (resident_id, record_date, record_type, description, diagnosis, treatment, medication, dosage, doctor_name, notes, created_by) VALUES
(2, '2023-09-01', 'CONSULTA', 'Evaluacion de ingreso. Diagnostico previo de TDAH y asma intermitente.', 'TDAH + asma alergica estacional.', 'Metilfenidato 5mg BID + salbutamol PRN.', 'Metilfenidato', '5 mg cada 12h', 'Dra. Irene Maturana', 'Peso: 58 kg, Talla: 164 cm. Buen desempeno social.', 'admin');

INSERT INTO clinical_records (resident_id, record_date, record_type, description, diagnosis, treatment, medication, dosage, doctor_name, notes, created_by) VALUES
(2, '2024-03-10', 'CONSULTA', 'Reevaluacion neurologica. Mejoria en socializacion.', 'TDAH persistente sin nueva sintomatologia.', 'Mantener metilfenidato. Terapia semanal.', 'Metilfenidato', '5 mg AM + 5 mg PM', 'Dra. Irene Maturana', 'Hogar reporta mejoria escolar.', 'admin');

INSERT INTO clinical_records (resident_id, record_date, record_type, description, medication, dosage, doctor_name, notes, created_by) VALUES
(2, '2024-06-05', 'VACUNA', 'Segunda dosis vacuna VPH.', 'Gardasil 9', '0.5 mL IM deltoides derecha', 'Dr. Fernando Arellano', 'Sin reacciones adversas.', 'admin');

INSERT INTO clinical_records (resident_id, record_date, record_type, description, diagnosis, treatment, medication, dosage, doctor_name, notes, created_by) VALUES
(2, '2024-11-15', 'CONSULTA', 'Episodio asmatico leve en parque. Sibilancias tenues.', 'Asma intermitente leve.', 'Salbutamol 2 puff /4h PRN.', 'Salbutamol', '2 puff max 4 veces/dia', 'Dr. Manuel Figueroa', 'Antihistaminico profilactico sugerido.', 'admin');

INSERT INTO clinical_records (resident_id, record_date, record_type, description, doctor_name, notes, created_by) VALUES
(2, '2025-03-20', 'SIGNOS_VITALES', 'TA 115/70 mmHg, FC 72 lpm, FR 16 rpm, Temp 36.7.', 'Enf. Berta Rojas', 'Control dentro parametros normales.', 'admin');

-- Resident 3: Valentina Ruiz Torres (id=3)
INSERT INTO clinical_records (resident_id, record_date, record_type, description, diagnosis, treatment, medication, dosage, doctor_name, notes, created_by) VALUES
(3, '2024-01-13', 'CONSULTA', 'Ingreso. Hipotiroidismo subclinico leve.', 'Hipotiroidismo subclinico estable.', 'Levothyroxina 50 mcg diaria en ayuno.', 'Levothyroxina sodica', '50 mcg / dia en ayuno', 'Dra. Catalina Parra', 'Peso: 46.5 kg, Talla: 162 cm. Buen animo.', 'admin');

INSERT INTO clinical_records (resident_id, record_date, record_type, description, diagnosis, medication, doctor_name, notes, created_by) VALUES
(3, '2024-03-02', 'ALERGIA', 'Urticaria difusa toracica tras tomar amoxicilina 500mg.', 'Alergia a amoxicilina (penicilina).', 'Amoxicilina contraindicado', 'Dr. Jorge Henriquez', 'Emitir carnet de alergia. Evaluar cefalosporinas supervisadas.', 'admin');

INSERT INTO clinical_records (resident_id, record_date, record_type, description, diagnosis, doctor_name, notes, created_by) VALUES
(3, '2024-05-18', 'PROCEDIMIENTO', 'Extraccion de molar inferior derecha (muela del juicio) con anestesia local.', 'Molar impactado.', 'Dr. Felipe Lagos (Odontologo)', 'Sutura reabsorbible. Revision 5 dias.', 'admin');

INSERT INTO clinical_records (resident_id, record_date, record_type, description, diagnosis, treatment, medication, dosage, doctor_name, notes, created_by) VALUES
(3, '2024-09-05', 'CONSULTA', 'Control endocrinologico: TSH 2.4 mU/L, T4 libre 1.0 ng/dL.', 'Hipotiroidismo estable.', 'Seguir regimen actual.', 'Levothyroxina', '50 mcg diaria en ayuno', 'Dr. Patricio Ruenthal', 'Mejora autoestima referida. Control en 6 meses.', 'admin');

INSERT INTO clinical_records (resident_id, record_date, record_type, description, doctor_name, notes, created_by) VALUES
(3, '2025-01-20', 'SIGNOS_VITALES', 'TA 120/70 mmHg, HR 72, FR 14, Temp 36.5. Valores normales.', 'Enf. Fernanda Jaque', 'Continua controles rutinarios.', 'admin');

INSERT INTO clinical_records (resident_id, record_date, record_type, description, diagnosis, notes, created_by) VALUES
(3, '2025-07-20', 'NOTA', 'Buen curso adaptativo y social dentro del hogar.', 'Satisfactoria integracion social.', 'Evaluacion vocacional con trabajador social.', 'admin');