-- Datos de ejemplo idempotentes: no se duplican en cada arranque.
INSERT INTO usuario(nombre,email,rol)
VALUES
    ('Carlos Xolalpa','caxt113200@gmail.com','CLIENTE'),
    ('Juan Torres','jpTorres200@gmail.com','CLIENTE'),
    ('Martha Jimenez','jjim@gmail.com','TECNICO'),
    ('Pedro Alvarez','paxt0@gmail.com','TECNICO')
ON CONFLICT (email) DO NOTHING;

INSERT INTO ticket (titulo, descripcion, fecha_creacion, estado, prioridad, tipo, id_cliente, id_tecnico)
SELECT 'Falla en red',
       'El puerto Ethernet no responde en la oficina principal',
       TIMESTAMP '2026-10-19 09:00:00',
       'ABIERTO',
       'ALTA',
       'INCIDENTE',
       (SELECT id FROM usuario WHERE email = 'caxt113200@gmail.com'),
       (SELECT id FROM usuario WHERE email = 'jjim@gmail.com')
WHERE NOT EXISTS (SELECT 1 FROM ticket WHERE titulo = 'Falla en red');

INSERT INTO ticket (titulo, descripcion, fecha_creacion, estado, prioridad, tipo, id_cliente, id_tecnico)
SELECT 'Configuración de correo',
       'Ayuda para configurar el cliente de correo institucional',
       TIMESTAMP '2026-10-19 11:30:00',
       'EN_PROCESO',
       'MEDIA',
       'SOLICITUD',
       (SELECT id FROM usuario WHERE email = 'jpTorres200@gmail.com'),
       (SELECT id FROM usuario WHERE email = 'paxt0@gmail.com')
WHERE NOT EXISTS (SELECT 1 FROM ticket WHERE titulo = 'Configuración de correo');
