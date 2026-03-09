package com.oracleone.conversor;

import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("RespuestaConversionPar (deserialización JSON)")
class RespuestaConversionParTest {

    private final Gson gson = new Gson();

    @Test
    @DisplayName("parsea respuesta exitosa con conversion_result")
    void parseaExitoConResultadoConversion() {
        String json = """
                {
                  "result": "success",
                  "base_code": "USD",
                  "target_code": "EUR",
                  "conversion_rate": 0.8616,
                  "conversion_result": 86.16
                }
                """;
        RespuestaConversionPar dto = gson.fromJson(json, RespuestaConversionPar.class);
        assertEquals("success", dto.getResultado());
        assertEquals("USD", dto.getCodigoBase());
        assertEquals("EUR", dto.getCodigoDestino());
        assertEquals(0.8616, dto.getTasaConversion());
        assertEquals(86.16, dto.getResultadoConversion());
    }

    @Test
    @DisplayName("parsea respuesta exitosa solo con conversion_rate")
    void parseaExitoSoloTasaConversion() {
        String json = """
                {
                  "result": "success",
                  "base_code": "EUR",
                  "target_code": "BRL",
                  "conversion_rate": 5.45
                }
                """;
        RespuestaConversionPar dto = gson.fromJson(json, RespuestaConversionPar.class);
        assertEquals("success", dto.getResultado());
        assertEquals(5.45, dto.getTasaConversion());
        assertNull(dto.getResultadoConversion());
    }

    @Test
    @DisplayName("parsea respuesta de error de la API")
    void parseaError() {
        String json = """
                {
                  "result": "error",
                  "error-type": "invalid-key"
                }
                """;
        RespuestaConversionPar dto = gson.fromJson(json, RespuestaConversionPar.class);
        assertEquals("error", dto.getResultado());
        assertEquals("invalid-key", dto.getTipoError());
        assertNull(dto.getResultadoConversion());
    }
}
