package org.valeneisa.entities;

// Representa a un jugador en Celestial Fury.
// Sigue el principio de Responsabilidad Única: solo almacena datos del jugador.
 //

public class Jugador {
    private String nombre;
    private int puntaje;
    private int x, y; // Posición para el movimiento con mouse

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.puntaje = 0;
        this.x = 0;
        this.y = 0;
    }

    // Getters y Setters para que Camilo y Julián puedan actualizar la lógica
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getPuntaje() { return puntaje; }
    public void setPuntaje(int puntaje) { this.puntaje = puntaje; }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public void incrementarPuntaje(int puntos) {
        this.puntaje += puntos;
    }
}
