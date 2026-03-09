package com.oracleone.conversor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ServicioTipoCambio")
class ServicioTipoCambioTest {

    @Mock
    private HttpClient clienteHttp;

    @Mock
    private HttpResponse<String> respuestaHttp;

    private ServicioTipoCambio servicio;

    @BeforeEach
    void setUp() {
        servicio = new ServicioTipoCambio(clienteHttp);
    }

    @Nested
    @DisplayName("convertirMonto - validación de parámetros")
    class ValidacionParametros {

        @Test
        @DisplayName("lanza si codigoOrigen es null")
        void codigoOrigenNulo() throws Exception {
            assertThrows(IllegalArgumentException.class,
                    () -> servicio.convertirMonto(null, "EUR", 100));
            verify(clienteHttp, never()).send(any(), any());
        }

        @Test
        @DisplayName("lanza si codigoOrigen está vacío")
        void codigoOrigenVacio() throws Exception {
            assertThrows(IllegalArgumentException.class,
                    () -> servicio.convertirMonto("", "EUR", 100));
            assertThrows(IllegalArgumentException.class,
                    () -> servicio.convertirMonto("   ", "EUR", 100));
        }

        @Test
        @DisplayName("lanza si codigoDestino es null o vacío")
        void codigoDestinoNuloOVacio() throws Exception {
            assertThrows(IllegalArgumentException.class,
                    () -> servicio.convertirMonto("USD", null, 100));
            assertThrows(IllegalArgumentException.class,
                    () -> servicio.convertirMonto("USD", "", 100));
        }

        @Test
        @DisplayName("lanza si monto es cero o negativo")
        void montoCeroONegativo() throws Exception {
            assertThrows(IllegalArgumentException.class,
                    () -> servicio.convertirMonto("USD", "EUR", 0));
            assertThrows(IllegalArgumentException.class,
                    () -> servicio.convertirMonto("USD", "EUR", -1));
        }

        @Test
        @DisplayName("lanza si monto es NaN o infinito")
        void montoNaNOInfinito() throws Exception {
            assertThrows(IllegalArgumentException.class,
                    () -> servicio.convertirMonto("USD", "EUR", Double.NaN));
            assertThrows(IllegalArgumentException.class,
                    () -> servicio.convertirMonto("USD", "EUR", Double.POSITIVE_INFINITY));
        }
    }

    @Nested
    @DisplayName("convertirMonto - respuesta API")
    class RespuestaApi {

        @Test
        @DisplayName("devuelve resultadoConversion cuando la API lo incluye")
        void devuelveResultadoConversion() throws Exception {
            when(respuestaHttp.statusCode()).thenReturn(200);
            when(respuestaHttp.body()).thenReturn("""
                    {"result":"success","base_code":"USD","target_code":"EUR",
                    "conversion_rate":0.86,"conversion_result":86.0}
                    """);
            when(clienteHttp.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(respuestaHttp);

            double resultado = servicio.convertirMonto("USD", "EUR", 100);
            assertEquals(86.0, resultado);
        }

        @Test
        @DisplayName("calcula con tasaConversion cuando no hay resultadoConversion")
        void calculaConTasaConversion() throws Exception {
            when(respuestaHttp.statusCode()).thenReturn(200);
            when(respuestaHttp.body()).thenReturn("""
                    {"result":"success","base_code":"USD","target_code":"EUR","conversion_rate":0.86}
                    """);
            when(clienteHttp.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(respuestaHttp);

            double resultado = servicio.convertirMonto("USD", "EUR", 100);
            assertEquals(86.0, resultado);
        }

        @Test
        @DisplayName("lanza IOException si la API devuelve error")
        void lanzaSiApiError() throws Exception {
            when(respuestaHttp.statusCode()).thenReturn(200);
            when(respuestaHttp.body()).thenReturn("{\"result\":\"error\",\"error-type\":\"invalid-key\"}");
            when(clienteHttp.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(respuestaHttp);

            assertThrows(IOException.class, () -> servicio.convertirMonto("USD", "EUR", 100));
        }

        @Test
        @DisplayName("lanza IOException si status HTTP no es 200")
        void lanzaSiStatusNo200() throws Exception {
            when(respuestaHttp.statusCode()).thenReturn(401);
            when(clienteHttp.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(respuestaHttp);

            assertThrows(IOException.class, () -> servicio.convertirMonto("USD", "EUR", 100));
        }

        @Test
        @DisplayName("construye URL con codigoOrigen, codigoDestino y monto")
        void urlCorrecta() throws Exception {
            when(respuestaHttp.statusCode()).thenReturn(200);
            when(respuestaHttp.body()).thenReturn("""
                    {"result":"success","conversion_result":25.5}
                    """);
            when(clienteHttp.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(respuestaHttp);

            servicio.convertirMonto("EUR", "BRL", 5.0);

            ArgumentCaptor<HttpRequest> captor = ArgumentCaptor.forClass(HttpRequest.class);
            verify(clienteHttp).send(captor.capture(), any());
            URI uri = captor.getValue().uri();
            assertTrue(uri.toString().contains("/pair/"));
            assertTrue(uri.toString().contains("EUR"));
            assertTrue(uri.toString().contains("BRL"));
            assertTrue(uri.toString().contains("5"));
        }
    }
}
