package com.oracleone.conversor;

import java.io.IOException;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Scanner;

public class Main {

    private static final Scanner LECTOR = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println(" Conversor de Monedas - Desafío ONE ");
        System.out.println("====================================");
        System.out.println();

        ServicioTipoCambio servicio = new ServicioTipoCambio();

        boolean continuar = true;
        while (continuar) {
            mostrarMenuMonedas();
            OpcionMoneda origen = leerOpcionMoneda("origen");
            if (origen == null) {
                System.out.println("Opción inválida. Intenta nuevamente.");
                continue;
            }

            OpcionMoneda destino = leerOpcionMoneda("destino");
            if (destino == null) {
                System.out.println("Opción inválida. Intenta nuevamente.");
                continue;
            }

            if (origen == destino) {
                System.out.println("Debes elegir monedas distintas para la conversión.");
                continue;
            }

            double monto = leerMonto();
            double resultado;
            try {
                resultado = servicio.convertirMonto(origen.getCodigo(), destino.getCodigo(), monto);
            } catch (IOException | InterruptedException e) {
                System.out.println("No fue posible realizar la conversión utilizando la API.");
                System.out.println("Detalle: " + e.getMessage());
                System.out.println("Verifica tu conexión a internet y tu API key.");
                continuar = preguntarSiContinuar();
                continue;
            }

            System.out.printf("Resultado: %.2f %s equivalen a %.2f %s%n",
                    monto, origen.getCodigo(), resultado, destino.getCodigo());

            continuar = preguntarSiContinuar();
        }

        System.out.println("Gracias por usar el conversor de monedas. ¡Hasta luego!");
        LECTOR.close();
    }

    private static void mostrarMenuMonedas() {
        System.out.println();
        System.out.println("Elige una moneda:");
        int indice = 1;
        for (OpcionMoneda opcion : OpcionMoneda.values()) {
            System.out.printf("%d - %s (%s)%n", indice, opcion.getCodigo(), opcion.getDescripcion());
            indice++;
        }
        System.out.println();
    }

    private static OpcionMoneda leerOpcionMoneda(String tipo) {
        System.out.printf("Introduce el número de la moneda de %s: ", tipo);
        String entrada = LECTOR.nextLine();
        return ValidadorMonedaYMonto.parsearOpcionMoneda(entrada).orElse(null);
    }

    private static double leerMonto() {
        while (true) {
            System.out.print("Introduce el monto a convertir: ");
            String entrada = LECTOR.nextLine();
            OptionalDouble monto = ValidadorMonedaYMonto.parsearMonto(entrada);
            if (monto.isEmpty()) {
                System.out.println("Valor inválido. Usa un número mayor que cero (puedes usar punto o coma decimal).");
            } else {
                return monto.getAsDouble();
            }
        }
    }

    private static boolean preguntarSiContinuar() {
        while (true) {
            System.out.print("¿Quieres realizar otra conversión? (s/n): ");
            String entrada = LECTOR.nextLine();
            Optional<Boolean> respuesta = ValidadorMonedaYMonto.parsearSiNo(entrada);
            if (respuesta.isPresent()) {
                return respuesta.get();
            }
            System.out.println("Opción no reconocida. Escribe 's' para sí o 'n' para no.");
        }
    }
}

