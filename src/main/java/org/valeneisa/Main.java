package org.valeneisa;

/**
 * La clase principal que sirve como punto de entrada para la aplicación Celestial Fury.
 * Proporciona un ejemplo básico de salida por consola y una estructura de control de ciclo.
 * * @author Valentina-O
 * @version 2.0
 */
public class Main {

    /**
     * Método principal que inicia la ejecución de la aplicación.
     * Imprime un mensaje de bienvenida y una secuencia de números en la consola.
     * * @param args Argumentos de la línea de comandos (no utilizados en esta implementación).
     */
    public static void main(String[] args) {
        // Usamos System.out para corregir el error de "IO"
        System.out.println(String.format("Hello and welcome!"));

        for (int i = 1; i <= 5; i++) {
            /*
             * Imprime el valor actual del contador en cada iteración.
             */
            System.out.println("i = " + i);
        }
    }
}