package com.oracleone.conversor;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * DTO de la respuesta del endpoint /latest de ExchangeRate-API.
 * Nombres en español; mapeo JSON con @SerializedName.
 */
public class RespuestaTasaCambio {

    @SerializedName("result")
    private String resultado;

    @SerializedName("error-type")
    private String tipoError;

    @SerializedName("base_code")
    private String codigoBase;

    @SerializedName("conversion_rates")
    private Map<String, Double> tasasConversion;

    public String getResultado() {
        return resultado;
    }

    public String getTipoError() {
        return tipoError;
    }

    public String getCodigoBase() {
        return codigoBase;
    }

    public Map<String, Double> getTasasConversion() {
        return tasasConversion;
    }
}
