package com.biblioteca.modelo;

import java.util.ArrayList;
import com.biblioteca.excepciones.SeccionLlenaException;
import com.biblioteca.excepciones.RecursoNoEncontradoException;

public class Seccion {

    private String nombre;
    private int capacidad;
    private ArrayList<Recurso> recursos;

    public Seccion(String nombre, int capacidad) {
        Validador.validarTexto(nombre, "nombre de sección");

        if (capacidad <= 0) {
            throw new IllegalArgumentException(
                "Capacidad debe ser mayor a 0."
            );
        }

        this.nombre = nombre.trim();
        this.capacidad = capacidad;
        this.recursos = new ArrayList<>();
    }

    public void agregarRecurso(Recurso recurso)
            throws SeccionLlenaException {

        if (recurso == null) {
            throw new IllegalArgumentException(
                "No se puede agregar un recurso nulo."
            );
        }

        for (Recurso existente : recursos) {
            if (existente.getCodigo().equalsIgnoreCase(recurso.getCodigo())) {
                throw new IllegalArgumentException(
                    "Ya existe un recurso con ese código en esta sección."
                );
            }
        }

        if (recursos.size() >= capacidad) {
            throw new SeccionLlenaException(
                "Sección " + nombre + " sin espacio disponible."
            );
        }

        recursos.add(recurso);
        System.out.println(
            "Recurso " + recurso.getTitulo()
            + " ubicado en sección " + nombre
        );
    }

    public void retirarRecurso(String codigo)
            throws RecursoNoEncontradoException {

        Validador.validarCodigo(codigo);

        for (int i = 0; i < recursos.size(); i++) {
            if (recursos.get(i).getCodigo().equalsIgnoreCase(codigo)) {
                recursos.remove(i);
                System.out.println("Recurso dado de baja.");
                return;
            }
        }

        throw new RecursoNoEncontradoException(codigo);
    }

    public Recurso buscarRecurso(String codigo)
            throws RecursoNoEncontradoException {

        Validador.validarCodigo(codigo);

        for (Recurso recurso : recursos) {
            if (recurso.getCodigo().equalsIgnoreCase(codigo)) {
                return recurso;
            }
        }

        throw new RecursoNoEncontradoException(codigo);
    }

    public void listarRecursos() {
        if (recursos.isEmpty()) {
            System.out.println("No hay recursos en esta sección.");
            return;
        }

        for (Recurso recurso : recursos) {
            System.out.println(recurso.obtenerInfo());
        }
    }

    public String getNombre() {
        return nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public ArrayList<Recurso> getRecursos() {
        return new ArrayList<>(recursos);
    }
}