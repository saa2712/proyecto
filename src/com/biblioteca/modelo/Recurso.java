package com.biblioteca.modelo;

public abstract class Recurso {

    private String titulo;
    private String codigo;
    private int anio;

    public Recurso(String titulo, String codigo, int anio) {
        Validador.validarTexto(titulo, "título");
        Validador.validarCodigo(codigo);

        if (anio < 1450 || anio > 2026) {
            throw new IllegalArgumentException("Año inválido: " + anio);
        }

        this.titulo = titulo.trim();
        this.codigo = codigo.trim();
        this.anio = anio;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getCodigo() {
        return codigo;
    }

    public int getAnio() {
        return anio;
    }

    public abstract String obtenerInfo();
}