package com.biblioteca.modelo;

import java.util.ArrayList;
import com.biblioteca.excepciones.RecursoNoEncontradoException;
import com.biblioteca.excepciones.PersonalNoEncontradoException;

public class Biblioteca {

    private String nombre;
    private ArrayList<Seccion> secciones;
    private ArrayList<Personal> personal;

    public Biblioteca(String nombre) {
        Validador.validarTexto(nombre, "nombre de biblioteca");

        this.nombre = nombre.trim();
        this.secciones = new ArrayList<>();
        this.personal = new ArrayList<>();

        secciones.add(new Seccion("Referencia", 10));
        secciones.add(new Seccion("Literatura", 8));
        secciones.add(new Seccion("Ciencias", 6));
    }

    public void agregarSeccion(String nombre, int capacidad) {
        Validador.validarTexto(nombre, "nombre de sección");

        if (buscarSeccion(nombre) != null) {
            throw new IllegalArgumentException(
                "Ya existe una sección con ese nombre."
            );
        }

        Seccion seccion = new Seccion(nombre, capacidad);
        secciones.add(seccion);

        System.out.println(
            "Sección '" + nombre + "' creada con capacidad " + capacidad
        );
    }

    public void agregarPersonal(Personal persona) {
        if (persona == null) {
            throw new IllegalArgumentException(
                "No se puede agregar personal nulo."
            );
        }

        for (Personal existente : personal) {
            if (existente.getId().equalsIgnoreCase(persona.getId())) {
                throw new IllegalArgumentException(
                    "Ya existe personal con ese ID."
                );
            }
        }

        personal.add(persona);
        System.out.println(
            persona.getNombre() + " registrado en la biblioteca."
        );
    }

    public Seccion buscarSeccion(String nombre) {
        if (nombre == null) {
            return null;
        }

        for (Seccion seccion : secciones) {
            if (seccion.getNombre().equalsIgnoreCase(nombre.trim())) {
                return seccion;
            }
        }

        return null;
    }

    public Recurso buscarRecurso(String codigo)
            throws RecursoNoEncontradoException {

        for (Seccion seccion : secciones) {
            try {
                return seccion.buscarRecurso(codigo);
            } catch (RecursoNoEncontradoException e) {
                // Continúa buscando en las demás secciones.
            }
        }

        throw new RecursoNoEncontradoException(codigo);
    }

    public Bibliotecario buscarBibliotecario(String id)
            throws PersonalNoEncontradoException {

        for (Personal persona : personal) {
            if (persona instanceof Bibliotecario
                    && persona.getId().equalsIgnoreCase(id)) {
                return (Bibliotecario) persona;
            }
        }

        throw new PersonalNoEncontradoException(id);
    }

    public void listarBibliotecarios() {
        boolean encontrado = false;

        System.out.println("\n--- BIBLIOTECARIOS ---");

        for (Personal persona : personal) {
            if (persona instanceof Bibliotecario) {
                System.out.println(
                    persona.getId() + " - " + persona.getNombre()
                    + " (" + persona.getArea() + ")"
                );
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("No hay bibliotecarios registrados.");
        }
    }

    public void generarReporteGeneral() {
        System.out.println("\n===== " + nombre.toUpperCase() + " =====");

        for (Seccion seccion : secciones) {
            System.out.println(
                "Sección: " + seccion.getNombre()
                + " | Ocupación: " + seccion.getRecursos().size()
                + "/" + seccion.getCapacidad()
            );
        }

        System.out.println("\nTotal de personal: " + personal.size());

        for (Personal persona : personal) {
            System.out.println("\n" + persona.generarReporte());
        }
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Seccion> getSecciones() {
        return new ArrayList<>(secciones);
    }

    public ArrayList<Personal> getPersonal() {
        return new ArrayList<>(personal);
    }
}