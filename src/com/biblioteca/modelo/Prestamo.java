package com.biblioteca.modelo;

public class Prestamo {

    private String fecha;
    private String observacion;
    private String lector;
    private Recurso recurso;
    private Bibliotecario bibliotecario;

    public Prestamo(String fecha, String observacion, String lector,
                    Recurso recurso, Bibliotecario bibliotecario) {

        Validador.validarTexto(fecha, "fecha");
        Validador.validarTexto(observacion, "observación");
        Validador.validarTexto(lector, "lector");

        if (recurso == null || bibliotecario == null) {
            throw new IllegalArgumentException(
                "El recurso y el bibliotecario son obligatorios."
            );
        }

        this.fecha = fecha.trim();
        this.observacion = observacion.trim();
        this.lector = lector.trim();
        this.recurso = recurso;
        this.bibliotecario = bibliotecario;
    }

    public String getInfo() {
        return "Préstamo: " + fecha
            + " | Lector: " + lector
            + " | Recurso: " + recurso.getTitulo()
            + " | Bibliotecario: " + bibliotecario.getNombre()
            + " | Nota: " + observacion;
    }

    public Recurso getRecurso() {
        return recurso;
    }

    public Bibliotecario getBibliotecario() {
        return bibliotecario;
    }

    public String getFecha() {
        return fecha;
    }
}