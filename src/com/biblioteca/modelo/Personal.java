package com.biblioteca.modelo;

public abstract class Personal {

    private String nombre;
    private String id;
    private String area;

    public Personal(String nombre, String id, String area) {
        Validador.validarNombre(nombre);
        Validador.validarCodigo(id);
        Validador.validarTexto(area, "área");

        this.nombre = nombre.trim();
        this.id = id.trim();
        this.area = area.trim();
    }

    public String getNombre() {
        return nombre;
    }

    public String getId() {
        return id;
    }

    public String getArea() {
        return area;
    }

    public abstract String generarReporte();
}