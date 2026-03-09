package com.oracleone.conversor;

public enum OpcionMoneda {
    USD("USD", "Dólar estadounidense"),
    EUR("EUR", "Euro"),
    BRL("BRL", "Real brasileño"),
    ARS("ARS", "Peso argentino"),
    CLP("CLP", "Peso chileno"),
    MXN("MXN", "Peso mexicano"),
    PEN("PEN", "Sol peruano"),
    UYU("UYU", "Peso uruguayo");

    private final String codigo;
    private final String descripcion;

    OpcionMoneda(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

