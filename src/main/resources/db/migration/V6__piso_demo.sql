-- Piso de demostración
INSERT INTO piso (nombre, fecha_creacion, codigo) VALUES ('Lavapiés 23', LOCALTIMESTAMP, 'LAVAP-2026');

-- Convivientes (contraseña "1234", cifrada con BCrypt)
INSERT INTO conviviente (nombre, email, username, password, rol, piso_id)
SELECT v.nombre, v.email, v.username, '$2a$10$cRU3oJQRmvU3/.FWJw6wK.OzUkDnwkCzhobtGfNJNXNINR55Afktm', 'CONVIVIENTE', p.id
FROM piso p,
     (VALUES ('Lucía', 'lucia@ejemplo.com', 'lucia'),
             ('Javi',  'javi@ejemplo.com',  'javi'),
             ('Nerea', 'nerea@ejemplo.com', 'nerea'),
             ('Óscar', 'oscar@ejemplo.com', 'oscar')) AS v(nombre, email, username)
WHERE p.codigo = 'LAVAP-2026';

-- Catálogo de tareas
INSERT INTO tarea (nombre, dificultad, frecuencia_semanal, piso_id)
SELECT v.nombre, v.dificultad, v.frecuencia, p.id
FROM piso p,
     (VALUES ('Fregar los platos', 1, 7), ('Sacar la basura', 1, 7), ('Vaciar el lavavajillas', 1, 7),
             ('Poner el lavavajillas', 2, 7), ('Limpiar el polvo', 2, 1), ('Regar las plantas', 2, 1),
             ('Cambiar las sábanas', 2, 1), ('Doblar la ropa', 2, 2), ('Limpiar el baño', 3, 2),
             ('Barrer y fregar zonas comunes', 3, 2), ('Poner y tender la lavadora', 3, 2),
             ('Sacar al perro', 3, 21)) AS v(nombre, dificultad, frecuencia)
WHERE p.codigo = 'LAVAP-2026';

-- Asignaciones con fechas repartidas por la semana
-- (GREATEST evita que una tarea de esta semana caiga en la semana anterior)
INSERT INTO asignacion (tarea_id, conviviente_id, fecha_limite, completada, fecha_completada)
SELECT t.id, c.id, v.fecha_limite, v.completada, v.fecha_completada
FROM (VALUES
          -- Pendientes: de hoy en adelante
          ('Fregar los platos',             'lucia', CURRENT_DATE,     FALSE, NULL::timestamp),
          ('Poner el lavavajillas',         'oscar', CURRENT_DATE + 1, FALSE, NULL),
          ('Cambiar las sábanas',           'nerea', CURRENT_DATE + 2, FALSE, NULL),
          ('Limpiar el baño',               'javi',  CURRENT_DATE + 3, FALSE, NULL),
          ('Poner y tender la lavadora',    'oscar', CURRENT_DATE + 4, FALSE, NULL),
          -- Vencidas
          ('Sacar la basura',               'oscar', CURRENT_DATE - 1, FALSE, NULL),
          ('Limpiar el polvo',              'lucia', CURRENT_DATE - 2, FALSE, NULL),
          -- Completadas esta semana
          ('Vaciar el lavavajillas',        'nerea', GREATEST(CURRENT_DATE - 1, date_trunc('week', CURRENT_DATE)::date), TRUE, LOCALTIMESTAMP - INTERVAL '1 day'),
          ('Regar las plantas',             'javi',  GREATEST(CURRENT_DATE - 2, date_trunc('week', CURRENT_DATE)::date), TRUE, LOCALTIMESTAMP - INTERVAL '2 days'),
          ('Doblar la ropa',                'lucia', GREATEST(CURRENT_DATE - 1, date_trunc('week', CURRENT_DATE)::date), TRUE, LOCALTIMESTAMP - INTERVAL '1 day'),
          ('Barrer y fregar zonas comunes', 'nerea', GREATEST(CURRENT_DATE - 3, date_trunc('week', CURRENT_DATE)::date), TRUE, LOCALTIMESTAMP - INTERVAL '3 days'),
          ('Sacar al perro',                'javi',  CURRENT_DATE,                                                         TRUE, LOCALTIMESTAMP - INTERVAL '2 hours'),
          -- Completadas la semana pasada (también suman en el ranking del mes)
          ('Limpiar el baño',               'nerea', date_trunc('week', CURRENT_DATE)::date - 2, TRUE, LOCALTIMESTAMP - INTERVAL '6 days'),
          ('Sacar al perro',                'javi',  date_trunc('week', CURRENT_DATE)::date - 3, TRUE, LOCALTIMESTAMP - INTERVAL '6 days'),
          ('Poner y tender la lavadora',    'lucia', date_trunc('week', CURRENT_DATE)::date - 4, TRUE, LOCALTIMESTAMP - INTERVAL '7 days'),
          ('Barrer y fregar zonas comunes', 'javi',  date_trunc('week', CURRENT_DATE)::date - 1, TRUE, LOCALTIMESTAMP - INTERVAL '7 days')
     ) AS v(tarea, usuario, fecha_limite, completada, fecha_completada)
         JOIN piso p ON p.codigo = 'LAVAP-2026'
         JOIN tarea t ON t.piso_id = p.id AND t.nombre = v.tarea
         JOIN conviviente c ON c.username = v.usuario;

-- Óscar le pide a Lucía que haga la lavadora
INSERT INTO solicitud_cambio (asignacion_id, solicitante_id, destinatario_id, estado, fecha_solicitud)
SELECT a.id, so.id, de.id, 'PENDIENTE', LOCALTIMESTAMP
FROM asignacion a
         JOIN tarea t ON t.id = a.tarea_id
         JOIN piso p ON p.id = t.piso_id
         JOIN conviviente so ON so.username = 'oscar'
         JOIN conviviente de ON de.username = 'lucia'
WHERE p.codigo = 'LAVAP-2026' AND t.nombre = 'Poner y tender la lavadora' AND a.completada = FALSE;