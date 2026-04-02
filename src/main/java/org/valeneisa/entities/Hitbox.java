package org.valeneisa.entities;

/**
 * Representa el área de colisión de un personaje.
 * Se usa para detectar si un golpe o poder especial conectó.
 */
public class Hitbox {
    private int x, y;         // Posición de la hitbox
    private int ancho, alto;  // Tamaño de la hitbox

    public Hitbox(int x, int y, int ancho, int alto) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
    }

    /**
     * Detecta si esta hitbox colisiona con otra.
     */
    public boolean colisionaCon(Hitbox otra) {
        return this.x < otra.x + otra.ancho &&
                this.x + this.ancho > otra.x &&
                this.y < otra.y + otra.alto &&
                this.y + this.alto > otra.y;
    }

    /**
     * Actualiza la posición de la hitbox según el personaje.
     */
    public void actualizarPosicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getAncho() { return ancho; }
    public int getAlto() { return alto; }

    @Override
    public String toString() {
        return String.format("Hitbox[x=%d, y=%d, ancho=%d, alto=%d]", x, y, ancho, alto);
    }
}