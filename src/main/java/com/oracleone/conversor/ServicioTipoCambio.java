package com.oracleone.conversor;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Servicio que consulta la API de ExchangeRate para convertir montos entre monedas.
 */
public class ServicioTipoCambio {

    private static final String CLAVE_API =
            System.getenv("EXCHANGE_RATE_API_KEY") != null
                    ? System.getenv("EXCHANGE_RATE_API_KEY")
                    : "AQUI VA LA API KEY";
    private static final String URL_BASE = "https://v6.exchangerate-api.com/v6/";

    private final HttpClient clienteHttp;
    private final Gson gson;

    public ServicioTipoCambio() {
        this(HttpClient.newHttpClient());
    }

    public ServicioTipoCambio(HttpClient clienteHttp) {
        this.clienteHttp = clienteHttp != null ? clienteHttp : HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    public double convertirMonto(String codigoOrigen, String codigoDestino, double monto)
            throws IOException, InterruptedException {
        if (codigoOrigen == null || codigoOrigen.isBlank()) {
            throw new IllegalArgumentException("El código de moneda origen no puede ser nulo ni vacío.");
        }
        if (codigoDestino == null || codigoDestino.isBlank()) {
            throw new IllegalArgumentException("El código de moneda destino no puede ser nulo ni vacío.");
        }
        if (Double.isNaN(monto) || Double.isInfinite(monto) || monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser un número positivo y finito.");
        }
        String url = URL_BASE + CLAVE_API + "/pair/" + codigoOrigen.trim() + "/" + codigoDestino.trim() + "/" + monto;

        HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> respuesta = clienteHttp.send(solicitud, HttpResponse.BodyHandlers.ofString());

        if (respuesta.statusCode() != 200) {
            throw new IOException("Respuesta HTTP inesperada: " + respuesta.statusCode());
        }

        RespuestaConversionPar dto = gson.fromJson(respuesta.body(), RespuestaConversionPar.class);

        if (!"success".equalsIgnoreCase(dto.getResultado())) {
            String tipoError = dto.getTipoError() != null ? dto.getTipoError() : "desconocido";
            throw new IOException("La API devolvió un error: " + tipoError);
        }

        Double resultadoConversion = dto.getResultadoConversion();
        if (resultadoConversion != null) {
            return resultadoConversion;
        }

        Double tasa = dto.getTasaConversion();
        if (tasa == null) {
            throw new IOException("La respuesta de la API no contiene la tasa de conversión.");
        }

        return monto * tasa;
    }
}
