package com.biblioteca.modelo;

public class Validador {

    private Validador() {
    }

    public static void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }

        if (nombre.matches(".*\\d.*")) {
            throw new IllegalArgumentException(
                "El nombre no puede contener números: " + nombre
            );
        }
    }

    public static void validarCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()
                || codigo.matches(".*\\s+.*")) {
            throw new IllegalArgumentException(
                "Código inválido: no puede ser vacío ni tener espacios."
            );
        }
    }

    public static void validarTexto(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "El campo " + campo + " no puede estar vacío."
            );
        }
    }
}