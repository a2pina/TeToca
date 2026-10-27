CREATE TABLE piso (
                      id              BIGSERIAL PRIMARY KEY,
                      nombre          VARCHAR(100) NOT NULL,
                      fecha_creacion  TIMESTAMP    NOT NULL
);

CREATE TABLE conviviente (
                             id       BIGSERIAL PRIMARY KEY,
                             nombre   VARCHAR(100) NOT NULL,
                             email    VARCHAR(150) NOT NULL UNIQUE,
                             piso_id  BIGINT       NOT NULL REFERENCES piso(id)
);

CREATE TABLE tarea (
                       id       BIGSERIAL PRIMARY KEY,
                       nombre   VARCHAR(100) NOT NULL,
                       puntos   INT          NOT NULL CHECK (puntos BETWEEN 1 AND 5),
                       piso_id  BIGINT       NOT NULL REFERENCES piso(id)
);

CREATE TABLE asignacion (
                            id                BIGSERIAL PRIMARY KEY,
                            tarea_id          BIGINT  NOT NULL REFERENCES tarea(id),
                            conviviente_id    BIGINT  NOT NULL REFERENCES conviviente(id),
                            fecha_limite      DATE    NOT NULL,
                            completada        BOOLEAN NOT NULL DEFAULT FALSE,
                            fecha_completada  TIMESTAMP
);