package com.pisocompartido.backendtetoca.exception;

import com.pisocompartido.backendtetoca.dto.RespuestaError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ManejadorGlobalErrores {

    private static final Logger log = LoggerFactory.getLogger(ManejadorGlobalErrores.class);

    // 1. Errores que lanzan nuestros servicios: 404, 409...
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<RespuestaError> errorControlado(ResponseStatusException ex) {
        int estado = ex.getStatusCode().value();
        return ResponseEntity.status(estado)
                .body(new RespuestaError(estado, ex.getReason(), null));
    }

    // 2. Fallos de validación (@Valid): dice qué campo está mal
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespuestaError> datosNoValidos(MethodArgumentNotValidException ex) {
        Map<String, String> campos = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> campos.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest()
                .body(new RespuestaError(400, "Datos no válidos", campos));
    }

    // 3. JSON mal escrito o parámetro con formato incorrecto
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<RespuestaError> formatoIncorrecto(Exception ex) {
        return ResponseEntity.badRequest()
                .body(new RespuestaError(400, "Formato de la petición incorrecto", null));
    }

    // 4. Cualquier otro error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RespuestaError> errorGeneral(Exception ex) {
        if (ex instanceof ErrorResponse errorConocido) {
            int estado = errorConocido.getStatusCode().value();
            return ResponseEntity.status(estado)
                    .body(new RespuestaError(estado, errorConocido.getBody().getDetail(), null));
        }
        log.error("Error inesperado", ex);
        return ResponseEntity.internalServerError()
                .body(new RespuestaError(500, "Error interno del servidor", null));
    }
}