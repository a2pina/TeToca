-- Código de invitación de cada piso
ALTER TABLE piso ADD COLUMN codigo VARCHAR(10);
UPDATE piso SET codigo = 'LUNA5' WHERE nombre = 'Calle Luna 5';
UPDATE piso SET codigo = UPPER(SUBSTRING(MD5(id::text) FROM 1 FOR 6)) WHERE codigo IS NULL;
ALTER TABLE piso ALTER COLUMN codigo SET NOT NULL;
ALTER TABLE piso ADD CONSTRAINT piso_codigo_unique UNIQUE (codigo);

-- Credenciales y rol de cada conviviente
ALTER TABLE conviviente ADD COLUMN username VARCHAR(50);
ALTER TABLE conviviente ADD COLUMN password VARCHAR(100);
ALTER TABLE conviviente ADD COLUMN rol VARCHAR(20) NOT NULL DEFAULT 'CONVIVIENTE';
ALTER TABLE conviviente ADD CONSTRAINT conviviente_username_unique UNIQUE (username);

-- El administrador de la app no pertenece a ningún piso
ALTER TABLE conviviente ALTER COLUMN piso_id DROP NOT NULL;

-- Usuarios de ejemplo (contraseña "1234", cifrada con BCrypt)
UPDATE conviviente
SET username = LOWER(nombre),
    password = '$2a$10$cRU3oJQRmvU3/.FWJw6wK.OzUkDnwkCzhobtGfNJNXNINR55Afktm'
WHERE email IN ('ana@ejemplo.com', 'luis@ejemplo.com', 'marta@ejemplo.com');

-- Administrador de la app (contraseña "admin1234", cifrada con BCrypt)
INSERT INTO conviviente (nombre, email, username, password, rol, piso_id)
VALUES ('Administrador', 'admin@tetoca.app', 'admin',
        '$2a$10$3G/TrCtRRclLfxbhBSwxIOxN1Xeew6Q2Cb/XzoffgVzdWpKZZY8Sm', 'ADMIN', NULL);