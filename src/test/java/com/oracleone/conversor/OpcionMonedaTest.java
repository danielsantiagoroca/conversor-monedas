package com.oracleone.conversor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("OpcionMoneda")
class OpcionMonedaTest {

    @Test
    @DisplayName("debe tener exactamente 8 monedas")
    void cantidadDeMonedas() {
        assertEquals(8, OpcionMoneda.values().length);
    }

    @Test
    @DisplayName("códigos y descripciones no nulos para todas las monedas")
    void codigosYDescripciones() {
        for (OpcionMoneda opcion : OpcionMoneda.values()) {
            assertNotNull(opcion.getCodigo());
            assertNotNull(opcion.getDescripcion());
            assertEquals(3, opcion.getCodigo().length());
        }
    }

    @Test
    @DisplayName("USD es la primera opción con código USD")
    void usdPrimeraOpcion() {
        assertEquals(OpcionMoneda.USD, OpcionMoneda.values()[0]);
        assertEquals("USD", OpcionMoneda.USD.getCodigo());
        assertEquals("Dólar estadounidense", OpcionMoneda.USD.getDescripcion());
    }

    @Test
    @DisplayName("UYU es la última opción con código UYU")
    void uyuUltimaOpcion() {
        assertEquals(OpcionMoneda.UYU, OpcionMoneda.values()[7]);
        assertEquals("UYU", OpcionMoneda.UYU.getCodigo());
        assertEquals("Peso uruguayo", OpcionMoneda.UYU.getDescripcion());
    }
}
