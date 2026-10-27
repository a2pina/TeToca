package com.pisocompartido.backendtetoca.dto;

import java.util.Map;

public record RespuestaError(int estado, String mensaje, Map<String, String> campos) { }