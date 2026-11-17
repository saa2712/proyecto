package com.biblioteca.modelo;

public class Libro extends Recurso {

    private String autor;
    private int paginas;

    public Libro(String titulo, String codigo, int anio,
                 String autor, int paginas) {
        super(titulo, codigo, anio);

        Validador.validarTexto(autor, "autor");

        if (paginas <= 0) {
            throw new IllegalArgumentException(
                "El número de páginas debe ser mayor a 0."
            );
        }

        this.autor = autor.trim();
        this.paginas = paginas;
    }

    @Override
    public String obtenerInfo() {
        return "Libro: " + getTitulo()
            + " | Código: " + getCodigo()
            + " | Año: " + getAnio()
            + " | Autor: " + autor
            + " | Páginas: " + paginas;
    }
}