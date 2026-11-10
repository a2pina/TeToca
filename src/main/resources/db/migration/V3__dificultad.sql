-- Sustituir los puntos (1-5) por dificultad (1-3) y añadir la frecuencia semanal
ALTER TABLE tarea ADD COLUMN dificultad INT;
UPDATE tarea SET dificultad = CASE WHEN puntos <= 2 THEN 1 WHEN puntos = 3 THEN 2 ELSE 3 END;
ALTER TABLE tarea ALTER COLUMN dificultad SET NOT NULL;
ALTER TABLE tarea ADD CONSTRAINT tarea_dificultad_check CHECK (dificultad BETWEEN 1 AND 3);

ALTER TABLE tarea ADD COLUMN frecuencia_semanal INT NOT NULL DEFAULT 1;
ALTER TABLE tarea ADD CONSTRAINT tarea_frecuencia_check CHECK (frecuencia_semanal BETWEEN 1 AND 21);

ALTER TABLE tarea DROP COLUMN puntos;

-- Frecuencia de las tareas de ejemplo
UPDATE tarea SET frecuencia_semanal = 7 WHERE nombre IN ('Fregar platos', 'Sacar la basura');
UPDATE tarea SET frecuencia_semanal = 2 WHERE nombre = 'Limpiar el baño';

-- Catálogo de tareas de ejemplo
INSERT INTO tarea (nombre, dificultad, frecuencia_semanal, piso_id)
SELECT t.nombre, t.dificultad, t.frecuencia, p.id
FROM piso p,
     (VALUES ('Barrer y fregar zonas comunes', 3, 2),
             ('Sacar al perro',                3, 21),
             ('Poner y tender la lavadora',    3, 2),
             ('Limpiar el polvo',              2, 1),
             ('Regar las plantas',             2, 1),
             ('Cambiar las sábanas',           2, 1),
             ('Doblar la ropa',                2, 2),
             ('Poner el lavavajillas',         2, 7),
             ('Vaciar el lavavajillas',        1, 7)) AS t(nombre, dificultad, frecuencia)
WHERE p.nombre = 'Calle Luna 5';