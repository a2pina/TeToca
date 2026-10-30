INSERT INTO piso (nombre, fecha_creacion) VALUES ('Calle Luna 5', NOW());

INSERT INTO conviviente (nombre, email, piso_id) VALUES
                                                     ('Ana',   'ana@ejemplo.com',   (SELECT id FROM piso WHERE nombre = 'Calle Luna 5')),
                                                     ('Luis',  'luis@ejemplo.com',  (SELECT id FROM piso WHERE nombre = 'Calle Luna 5')),
                                                     ('Marta', 'marta@ejemplo.com', (SELECT id FROM piso WHERE nombre = 'Calle Luna 5'));

INSERT INTO tarea (nombre, puntos, piso_id) VALUES
                                                ('Fregar platos',   1, (SELECT id FROM piso WHERE nombre = 'Calle Luna 5')),
                                                ('Sacar la basura', 2, (SELECT id FROM piso WHERE nombre = 'Calle Luna 5')),
                                                ('Limpiar el baño', 4, (SELECT id FROM piso WHERE nombre = 'Calle Luna 5'));

INSERT INTO asignacion (tarea_id, conviviente_id, fecha_limite, completada, fecha_completada) VALUES
                                                                                                  ((SELECT id FROM tarea WHERE nombre = 'Fregar platos'),
                                                                                                   (SELECT id FROM conviviente WHERE email = 'ana@ejemplo.com'),
                                                                                                   CURRENT_DATE + 3, FALSE, NULL),

                                                                                                  ((SELECT id FROM tarea WHERE nombre = 'Sacar la basura'),
                                                                                                   (SELECT id FROM conviviente WHERE email = 'luis@ejemplo.com'),
                                                                                                   CURRENT_DATE - 1, TRUE, NOW()),

                                                                                                  ((SELECT id FROM tarea WHERE nombre = 'Limpiar el baño'),
                                                                                                   (SELECT id FROM conviviente WHERE email = 'marta@ejemplo.com'),
                                                                                                   CURRENT_DATE - 2, FALSE, NULL);