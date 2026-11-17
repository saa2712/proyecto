package com.biblioteca.app;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.biblioteca.modelo.*;
import com.biblioteca.excepciones.*;

public class App {

    private static Scanner sc = new Scanner(System.in);

    public static int leerEntero(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);

            try {
                int numero = sc.nextInt();
                sc.nextLine();
                return numero;

            } catch (InputMismatchException e) {
                System.out.println("Error: debes ingresar un número entero.");
                sc.nextLine();
            }
        }
    }

    public static String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = sc.nextLine();

            if (texto != null && !texto.trim().isEmpty()) {
                return texto.trim();
            }

            System.out.println("Error: este campo no puede estar vacío.");
        }
    }

    public static boolean leerSiNo(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String respuesta = sc.nextLine().trim();

            if (respuesta.equalsIgnoreCase("s")) {
                return true;
            }

            if (respuesta.equalsIgnoreCase("n")) {
                return false;
            }

            System.out.println("Respuesta inválida. Escribe S o N.");
        }
    }

    public static void mostrarMenu() {
        System.out.println("\n========== SISTEMA DE BIBLIOTECA ==========");
        System.out.println("1. Registrar recurso");
        System.out.println("2. Listar recursos registrados");
        System.out.println("3. Ubicar recurso en una sección");
        System.out.println("4. Registrar préstamo");
        System.out.println("5. Ver recursos de una sección");
        System.out.println("6. Ver préstamos de un bibliotecario");
        System.out.println("7. Dar de baja un recurso de una sección");
        System.out.println("8. Buscar recurso por código");
        System.out.println("9. Reporte general de la biblioteca");
        System.out.println("10. Crear nueva sección");
        System.out.println("0. Salir");
        System.out.println("===========================================");
    }

    public static boolean existeRecurso(
            ArrayList<Recurso> recursos, String codigo) {

        for (Recurso recurso : recursos) {
            if (recurso.getCodigo().equalsIgnoreCase(codigo)) {
                return true;
            }
        }

        return false;
    }

    public static Recurso buscarRecursoRegistrado(
            ArrayList<Recurso> recursos, String codigo) {

        for (Recurso recurso : recursos) {
            if (recurso.getCodigo().equalsIgnoreCase(codigo)) {
                return recurso;
            }
        }

        return null;
    }

    public static void registrarRecurso(
            ArrayList<Recurso> recursosRegistrados) {

        System.out.println("\n--- REGISTRAR RECURSO ---");
        System.out.println("1. Libro");
        System.out.println("2. Recurso digital");

        int tipo = leerEntero(sc, "Selecciona el tipo: ");

        if (tipo != 1 && tipo != 2) {
            System.out.println("Tipo inválido.");
            return;
        }

        try {
            String titulo = leerTexto("Título: ");
            String codigo = leerTexto("Código (sin espacios): ");

            if (existeRecurso(recursosRegistrados, codigo)) {
                System.out.println("Error: ya existe un recurso con ese código.");
                return;
            }

            int anio = leerEntero(sc, "Año de publicación: ");

            if (tipo == 1) {
                String autor = leerTexto("Autor: ");
                int paginas = leerEntero(sc, "Número de páginas: ");

                Libro libro = new Libro(
                    titulo, codigo, anio, autor, paginas
                );

                recursosRegistrados.add(libro);
                System.out.println("Libro registrado correctamente.");

            } else {
                String formato = leerTexto("Formato (PDF, EPUB, etc.): ");
                int tamano = leerEntero(sc, "Tamaño en MB: ");

                RecursoDigital digital = new RecursoDigital(
                    titulo, codigo, anio, formato, tamano
                );

                recursosRegistrados.add(digital);
                System.out.println("Recurso digital registrado correctamente.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Error al registrar el recurso: " + e.getMessage());
        }
    }

    public static void listarRecursos(
            ArrayList<Recurso> recursosRegistrados) {

        System.out.println("\n--- RECURSOS REGISTRADOS ---");

        if (recursosRegistrados.isEmpty()) {
            System.out.println("No hay recursos registrados.");
            return;
        }

        for (Recurso recurso : recursosRegistrados) {
            System.out.println(recurso.obtenerInfo());
        }
    }

    public static void ubicarRecurso(
            Biblioteca biblioteca,
            ArrayList<Recurso> recursosRegistrados) {

        System.out.println("\n--- UBICAR RECURSO ---");

        String codigo = leerTexto("Código del recurso: ");
        Recurso recurso = buscarRecursoRegistrado(recursosRegistrados, codigo);

        if (recurso == null) {
            System.out.println("Error: el recurso no está registrado.");
            return;
        }

        String nombreSeccion = leerTexto("Nombre de la sección: ");
        Seccion seccion = biblioteca.buscarSeccion(nombreSeccion);

        if (seccion == null) {
            System.out.println("Error: la sección no existe.");
            return;
        }

        try {
            // Evita ubicar el mismo recurso en otra sección.
            try {
                biblioteca.buscarRecurso(codigo);
                System.out.println("Error: el recurso ya está ubicado en una sección.");
                return;
            } catch (RecursoNoEncontradoException e) {
                // Si no está ubicado, se puede agregar.
            }

            seccion.agregarRecurso(recurso);

        } catch (SeccionLlenaException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void registrarPrestamo(Biblioteca biblioteca) {
        System.out.println("\n--- REGISTRAR PRÉSTAMO ---");

        biblioteca.listarBibliotecarios();

        String id = leerTexto("ID del bibliotecario: ");
        String codigo = leerTexto("Código del recurso: ");

        String fecha = leerTexto("Fecha del préstamo: ");
        String lector = leerTexto("Nombre del lector: ");

        boolean conObservacion = leerSiNo(
            "¿Deseas agregar una observación? (S/N): "
        );

        String observacion = null;

        if (conObservacion) {
            observacion = leerTexto("Observación: ");
        }

        try {
            Bibliotecario bibliotecario = biblioteca.buscarBibliotecario(id);
            Recurso recurso = biblioteca.buscarRecurso(codigo);

            if (conObservacion) {
                bibliotecario.registrarPrestamo(
                    fecha, lector, observacion, recurso
                );
            } else {
                bibliotecario.registrarPrestamo(
                    fecha, lector, recurso
                );
            }

        } catch (PersonalNoEncontradoException
                | RecursoNoEncontradoException
                | PrestamoInvalidoException e) {

            System.out.println("Error: " + e.getMessage());

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void verRecursosSeccion(Biblioteca biblioteca) {
        System.out.println("\n--- VER RECURSOS DE UNA SECCIÓN ---");

        String nombre = leerTexto("Nombre de la sección: ");
        Seccion seccion = biblioteca.buscarSeccion(nombre);

        if (seccion == null) {
            System.out.println("Error: la sección no existe.");
            return;
        }

        System.out.println("\nSección: " + seccion.getNombre());
        seccion.listarRecursos();
    }

    public static void verPrestamos(Biblioteca biblioteca) {
        System.out.println("\n--- PRÉSTAMOS DE BIBLIOTECARIO ---");

        biblioteca.listarBibliotecarios();
        String id = leerTexto("ID del bibliotecario: ");

        try {
            Bibliotecario bibliotecario = biblioteca.buscarBibliotecario(id);
            ArrayList<Prestamo> prestamos = bibliotecario.getPrestamos();

            if (prestamos.isEmpty()) {
                System.out.println("Este bibliotecario no tiene préstamos.");
                return;
            }

            for (Prestamo prestamo : prestamos) {
                System.out.println(prestamo.getInfo());
            }

        } catch (PersonalNoEncontradoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void darDeBajaRecurso(Biblioteca biblioteca) {
        System.out.println("\n--- DAR DE BAJA RECURSO ---");

        String nombre = leerTexto("Nombre de la sección: ");
        Seccion seccion = biblioteca.buscarSeccion(nombre);

        if (seccion == null) {
            System.out.println("Error: la sección no existe.");
            return;
        }

        String codigo = leerTexto("Código del recurso que deseas retirar: ");

        try {
            seccion.retirarRecurso(codigo);

        } catch (RecursoNoEncontradoException e) {
            System.out.println("Error: " + e.getMessage());

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void buscarRecurso(Biblioteca biblioteca) {
        System.out.println("\n--- BUSCAR RECURSO ---");

        String codigo = leerTexto("Código del recurso: ");

        try {
            Recurso recurso = biblioteca.buscarRecurso(codigo);
            System.out.println(recurso.obtenerInfo());

        } catch (RecursoNoEncontradoException e) {
            System.out.println("Error: " + e.getMessage());

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void crearSeccion(Biblioteca biblioteca) {
        System.out.println("\n--- CREAR NUEVA SECCIÓN ---");

        try {
            String nombre = leerTexto("Nombre de la sección: ");
            int capacidad = leerEntero(sc, "Capacidad de la sección: ");

            biblioteca.agregarSeccion(nombre, capacidad);

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {

        ArrayList<Recurso> recursosRegistrados = new ArrayList<>();

        Biblioteca biblioteca = new Biblioteca(
            "Biblioteca Central Los Libertadores"
        );

        Bibliotecario b1 = new Bibliotecario(
            "Marta Rojas", "B001", "Circulación"
        );

        Bibliotecario b2 = new Bibliotecario(
            "Julián Pardo", "B002", "Referencia"
        );

        Auxiliar a1 = new Auxiliar(
            "Diana Cruz", "A001", "Catalogación", "Tarde"
        );

        biblioteca.agregarPersonal(b1);
        biblioteca.agregarPersonal(b2);
        biblioteca.agregarPersonal(a1);

        int opcion;

        do {
            mostrarMenu();
            opcion = leerEntero(sc, "Selecciona una opción: ");

            switch (opcion) {

                case 1:
                    registrarRecurso(recursosRegistrados);
                    break;

                case 2:
                    listarRecursos(recursosRegistrados);
                    break;

                case 3:
                    ubicarRecurso(biblioteca, recursosRegistrados);
                    break;

                case 4:
                    registrarPrestamo(biblioteca);
                    break;

                case 5:
                    verRecursosSeccion(biblioteca);
                    break;

                case 6:
                    verPrestamos(biblioteca);
                    break;

                case 7:
                    darDeBajaRecurso(biblioteca);
                    break;

                case 8:
                    buscarRecurso(biblioteca);
                    break;

                case 9:
                    biblioteca.generarReporteGeneral();
                    break;

                case 10:
                    crearSeccion(biblioteca);
                    break;

                case 0:
                    System.out.println(
                        "Gracias por utilizar el sistema de biblioteca."
                    );
                    break;

                default:
                    System.out.println(
                        "Opción inválida. Selecciona una opción del menú."
                    );
                    break;
            }

        } while (opcion != 0);

        sc.close();
    }
}