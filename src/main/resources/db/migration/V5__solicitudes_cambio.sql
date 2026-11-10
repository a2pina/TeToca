CREATE TABLE solicitud_cambio (
                                  id               BIGSERIAL PRIMARY KEY,
                                  asignacion_id    BIGINT      NOT NULL REFERENCES asignacion(id) ON DELETE CASCADE,
                                  solicitante_id   BIGINT      NOT NULL REFERENCES conviviente(id),
                                  destinatario_id  BIGINT      NOT NULL REFERENCES conviviente(id),
                                  estado           VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
                                  fecha_solicitud  TIMESTAMP   NOT NULL
);