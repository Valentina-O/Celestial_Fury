package org.valeneisa.entities;

/**
 * Representa a un jugador en Celestial Fury.
 * Ahora incluye atributos de combate: vida, estado y hitbox.
 */
public class Jugador {
    private String nombre;
    private int puntaje;
    private int x, y;

    // Atributos de combate
    private int vida;
    private int vidaMaxima;
    private boolean atacando;
    private boolean usandoPoder;
    private Hitbox hitboxCuerpo;   // Área del cuerpo (recibe daño)
    private Hitbox hitboxGolpe;    // Área del puño/patada (hace daño)

    // Constantes de combate
    public static final int DAÑO_GOLPE = 10;
    public static final int DAÑO_PODER = 25;
    public static final int VIDA_INICIAL = 100;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.puntaje = 0;
        this.x = 0;
        this.y = 0;

        // Inicializar combate
        this.vida = VIDA_INICIAL;
        this.vidaMaxima = VIDA_INICIAL;
        this.atacando = false;
        this.usandoPoder = false;

        // Hitbox del cuerpo: 50x80 píxeles
        this.hitboxCuerpo = new Hitbox(x, y, 50, 80);
        // Hitbox del golpe: 30x30 píxeles (al lado del cuerpo)
        this.hitboxGolpe = new Hitbox(x + 50, y + 20, 30, 30);
    }

    /**
     * Mueve al jugador y actualiza sus hitboxes.
     */
    public void mover(int nuevoX, int nuevoY) {
        this.x = nuevoX;
        this.y = nuevoY;
        hitboxCuerpo.actualizarPosicion(x, y);
        hitboxGolpe.actualizarPosicion(x + 50, y + 20);
    }

    /**
     * Recibe daño y devuelve true si el jugador sigue vivo.
     */
    public boolean recibirDaño(int daño) {
        this.vida -= daño;
        if (this.vida < 0) this.vida = 0;
        return this.vida > 0;
    }

    /**
     * Verifica si el golpe de este jugador conecta con otro.
     */
    public boolean golpeConecta(Jugador otro) {
        return this.hitboxGolpe.colisionaCon(otro.getHitboxCuerpo());
    }

    /**
     * Resetea el estado del jugador para una nueva pelea.
     */
    public void resetear() {
        this.vida = VIDA_INICIAL;
        this.atacando = false;
        this.usandoPoder = false;
        this.puntaje = 0;
    }

    public boolean estaVivo() {
        return this.vida > 0;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getPuntaje() { return puntaje; }
    public void setPuntaje(int puntaje) { this.puntaje = puntaje; }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public int getVida() { return vida; }
    public int getVidaMaxima() { return vidaMaxima; }

    public boolean isAtacando() { return atacando; }
    public void setAtacando(boolean atacando) { this.atacando = atacando; }

    public boolean isUsandoPoder() { return usandoPoder; }
    public void setUsandoPoder(boolean usandoPoder) { this.usandoPoder = usandoPoder; }

    public Hitbox getHitboxCuerpo() { return hitboxCuerpo; }
    public Hitbox getHitboxGolpe() { return hitboxGolpe; }

    public void incrementarPuntaje(int puntos) {
        this.puntaje += puntos;
    }
}