package com.oracleone.conversor;

import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RespuestaTasaCambio (deserialización JSON endpoint /latest)")
class RespuestaTasaCambioTest {

    private final Gson gson = new Gson();

    @Test
    @DisplayName("parsea respuesta con conversion_rates")
    void parseaExito() {
        String json = """
                {
                  "result": "success",
                  "base_code": "USD",
                  "conversion_rates": {
                    "USD": 1.0,
                    "EUR": 0.86,
                    "BRL": 4.95
                  }
                }
                """;
        RespuestaTasaCambio dto = gson.fromJson(json, RespuestaTasaCambio.class);
        assertEquals("success", dto.getResultado());
        assertEquals("USD", dto.getCodigoBase());
        assertNotNull(dto.getTasasConversion());
        assertEquals(1.0, dto.getTasasConversion().get("USD"));
        assertEquals(0.86, dto.getTasasConversion().get("EUR"));
    }
}
