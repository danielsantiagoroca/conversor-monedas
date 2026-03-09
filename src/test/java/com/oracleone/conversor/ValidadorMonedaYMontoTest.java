package com.oracleone.conversor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ValidadorMonedaYMonto")
class ValidadorMonedaYMontoTest {

    @Nested
    @DisplayName("parsearOpcionMoneda")
    class ParsearOpcionMoneda {

        @Test
        @DisplayName("acepta opciones válidas 1 a 8")
        void opcionesValidas() {
            assertEquals(OpcionMoneda.USD, ValidadorMonedaYMonto.parsearOpcionMoneda("1").orElseThrow());
            assertEquals(OpcionMoneda.EUR, ValidadorMonedaYMonto.parsearOpcionMoneda("2").orElseThrow());
            assertEquals(OpcionMoneda.UYU, ValidadorMonedaYMonto.parsearOpcionMoneda("8").orElseThrow());
        }

        @Test
        @DisplayName("acepta espacios alrededor")
        void espaciosAlrededor() {
            assertEquals(OpcionMoneda.USD, ValidadorMonedaYMonto.parsearOpcionMoneda("  1  ").orElseThrow());
        }

        @Test
        @DisplayName("rechaza null")
        void rechazaNull() {
            assertTrue(ValidadorMonedaYMonto.parsearOpcionMoneda(null).isEmpty());
        }

        @Test
        @DisplayName("rechaza cadena vacía")
        void rechazaVacio() {
            assertTrue(ValidadorMonedaYMonto.parsearOpcionMoneda("").isEmpty());
            assertTrue(ValidadorMonedaYMonto.parsearOpcionMoneda("   ").isEmpty());
        }

        @Test
        @DisplayName("rechaza opción 0 o negativa")
        void rechazaCeroONegativo() {
            assertTrue(ValidadorMonedaYMonto.parsearOpcionMoneda("0").isEmpty());
            assertTrue(ValidadorMonedaYMonto.parsearOpcionMoneda("-1").isEmpty());
        }

        @Test
        @DisplayName("rechaza opción mayor a 8")
        void rechazaMayorA8() {
            assertTrue(ValidadorMonedaYMonto.parsearOpcionMoneda("9").isEmpty());
            assertTrue(ValidadorMonedaYMonto.parsearOpcionMoneda("10").isEmpty());
        }

        @Test
        @DisplayName("rechaza texto no numérico")
        void rechazaNoNumerico() {
            assertTrue(ValidadorMonedaYMonto.parsearOpcionMoneda("abc").isEmpty());
            assertTrue(ValidadorMonedaYMonto.parsearOpcionMoneda("uno").isEmpty());
        }

        @Test
        @DisplayName("rechaza número con decimales")
        void rechazaDecimales() {
            assertTrue(ValidadorMonedaYMonto.parsearOpcionMoneda("1.5").isEmpty());
        }
    }

    @Nested
    @DisplayName("parsearMonto")
    class ParsearMonto {

        @Test
        @DisplayName("acepta números positivos con punto decimal")
        void aceptaPuntoDecimal() {
            assertEquals(100.0, ValidadorMonedaYMonto.parsearMonto("100").getAsDouble());
            assertEquals(1.5, ValidadorMonedaYMonto.parsearMonto("1.5").getAsDouble());
        }

        @Test
        @DisplayName("acepta coma como separador decimal")
        void aceptaComaDecimal() {
            assertEquals(1.5, ValidadorMonedaYMonto.parsearMonto("1,5").getAsDouble());
            assertEquals(1000.99, ValidadorMonedaYMonto.parsearMonto("1000,99").getAsDouble());
        }

        @Test
        @DisplayName("acepta espacios alrededor")
        void espaciosAlrededor() {
            assertEquals(50.0, ValidadorMonedaYMonto.parsearMonto("  50  ").getAsDouble());
        }

        @Test
        @DisplayName("rechaza null o vacío")
        void rechazaNullOVacio() {
            assertTrue(ValidadorMonedaYMonto.parsearMonto(null).isEmpty());
            assertTrue(ValidadorMonedaYMonto.parsearMonto("").isEmpty());
            assertTrue(ValidadorMonedaYMonto.parsearMonto("   ").isEmpty());
        }

        @Test
        @DisplayName("rechaza cero o negativo")
        void rechazaCeroONegativo() {
            assertTrue(ValidadorMonedaYMonto.parsearMonto("0").isEmpty());
            assertTrue(ValidadorMonedaYMonto.parsearMonto("-1").isEmpty());
            assertTrue(ValidadorMonedaYMonto.parsearMonto("-0.5").isEmpty());
        }

        @Test
        @DisplayName("rechaza texto no numérico")
        void rechazaNoNumerico() {
            assertTrue(ValidadorMonedaYMonto.parsearMonto("abc").isEmpty());
            assertTrue(ValidadorMonedaYMonto.parsearMonto("1.2.3").isEmpty());
        }
    }

    @Nested
    @DisplayName("parsearSiNo")
    class ParsearSiNo {

        @Test
        @DisplayName("acepta s y n")
        void aceptaSyN() {
            assertTrue(ValidadorMonedaYMonto.parsearSiNo("s").orElseThrow());
            assertFalse(ValidadorMonedaYMonto.parsearSiNo("n").orElseThrow());
        }

        @Test
        @DisplayName("acepta si y no")
        void aceptaSiYNo() {
            assertTrue(ValidadorMonedaYMonto.parsearSiNo("si").orElseThrow());
            assertFalse(ValidadorMonedaYMonto.parsearSiNo("no").orElseThrow());
        }

        @Test
        @DisplayName("ignora mayúsculas")
        void ignoraMayusculas() {
            assertTrue(ValidadorMonedaYMonto.parsearSiNo("S").orElseThrow());
            assertFalse(ValidadorMonedaYMonto.parsearSiNo("N").orElseThrow());
        }

        @Test
        @DisplayName("rechaza valores no reconocidos")
        void rechazaOtros() {
            assertTrue(ValidadorMonedaYMonto.parsearSiNo("").isEmpty());
            assertTrue(ValidadorMonedaYMonto.parsearSiNo("y").isEmpty());
            assertTrue(ValidadorMonedaYMonto.parsearSiNo("1").isEmpty());
        }
    }
}
