package com.oracleone.conversor;

import com.google.gson.annotations.SerializedName;

/**
 * DTO de la respuesta del endpoint /pair de ExchangeRate-API.
 * Nombres en español; mapeo JSON con @SerializedName.
 */
public class RespuestaConversionPar {

    @SerializedName("result")
    private String resultado;

    @SerializedName("error-type")
    private String tipoError;

    @SerializedName("base_code")
    private String codigoBase;

    @SerializedName("target_code")
    private String codigoDestino;

    @SerializedName("conversion_rate")
    private Double tasaConversion;

    @SerializedName("conversion_result")
    private Double resultadoConversion;

    public String getResultado() {
        return resultado;
    }

    public String getTipoError() {
        return tipoError;
    }

    public String getCodigoBase() {
        return codigoBase;
    }

    public String getCodigoDestino() {
        return codigoDestino;
    }

    public Double getTasaConversion() {
        return tasaConversion;
    }

    public Double getResultadoConversion() {
        return resultadoConversion;
    }
}
