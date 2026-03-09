package com.oracleone.conversor;

import java.util.Optional;
import java.util.OptionalDouble;

/**
 * Utilidad para parsear de forma segura la entrada del usuario (opción de moneda y monto).
 * Centraliza validaciones y manejo de formatos incorrectos.
 */
public final class ValidadorMonedaYMonto {

    private ValidadorMonedaYMonto() {
    }

    /**
     * Parsea una opción numérica de moneda (1 a 8).
     * Acepta números con espacios alrededor. Rechaza vacío, no numérico o fuera de rango.
     */
    public static Optional<OpcionMoneda> parsearOpcionMoneda(String entrada) {
        if (entrada == null) {
            return Optional.empty();
        }
        String recortado = entrada.trim();
        if (recortado.isEmpty()) {
            return Optional.empty();
        }
        try {
            int opcion = Integer.parseInt(recortado);
            OpcionMoneda[] opciones = OpcionMoneda.values();
            if (opcion < 1 || opcion > opciones.length) {
                return Optional.empty();
            }
            return Optional.of(opciones[opcion - 1]);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Parsea un monto a convertir.
     * Acepta punto o coma decimal. Rechaza vacío, no numérico, <= 0, NaN o infinito.
     */
    public static OptionalDouble parsearMonto(String entrada) {
        if (entrada == null) {
            return OptionalDouble.empty();
        }
        String normalizado = entrada.trim().replace(',', '.');
        if (normalizado.isEmpty()) {
            return OptionalDouble.empty();
        }
        try {
            double valor = Double.parseDouble(normalizado);
            if (Double.isNaN(valor) || Double.isInfinite(valor) || valor <= 0) {
                return OptionalDouble.empty();
            }
            return OptionalDouble.of(valor);
        } catch (NumberFormatException e) {
            return OptionalDouble.empty();
        }
    }

    /**
     * Parsea respuesta s/n para continuar.
     */
    public static Optional<Boolean> parsearSiNo(String entrada) {
        if (entrada == null) {
            return Optional.empty();
        }
        String valor = entrada.trim().toLowerCase();
        if ("s".equals(valor) || "si".equals(valor)) {
            return Optional.of(true);
        }
        if ("n".equals(valor) || "no".equals(valor)) {
            return Optional.of(false);
        }
        return Optional.empty();
    }
}
