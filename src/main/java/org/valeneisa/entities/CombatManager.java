package org.valeneisa.entities;

import java.util.HashMap;
import java.util.Map;

/**
 * Gestor de combate entre dos jugadores.
 * Controla lógica de golpes, poderes y vida.
 *
 * SOLID:
 * - S: Responsable solo de la lógica de combate
 * - O: Extensible para nuevos tipos de ataque
 * - L: Implementa contrato de CombatLogic
 * - I: Interfaz mínima
 * - D: Depende de abstracciones (Jugador)
 *
 * @author Camilo / Combat Team
 * @version 1.0
 */
public class CombatManager {
    private Jugador jugador1;
    private Jugador jugador2;
    private boolean peleasActiva;
    private int turno; // 0 = jugador1, 1 = jugador2

    // Control de enfriamiento de poderes
    private Map<String, Long> ultimoPoderUsado;
    private static final long PODER_COOLDOWN = 5000; // 5 segundos

    /**
     * Constructor del gestor de combate.
     *
     * @param jugador1 Primer jugador
     * @param jugador2 Segundo jugador
     */
    public CombatManager(Jugador jugador1, Jugador jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.peleasActiva = false;
        this.turno = 0;
        this.ultimoPoderUsado = new HashMap<>();
    }

    /**
     * Inicia una nueva pelea.
     */
    public void iniciarPelea() {
        jugador1.resetear();
        jugador2.resetear();
        this.peleasActiva = true;
        this.turno = 0;
        System.out.println("⚔️ ¡PELEA INICIADA! " + jugador1.getNombre() + " vs " + jugador2.getNombre());
    }

    /**
     * Procesa un golpe básico.
     *
     * @param nombreJugador Nombre del jugador que golpea
     * @return true si el golpe conectó
     */
    public boolean procesarGolpe(String nombreJugador) {
        if (!peleasActiva) return false;

        Jugador atacante = obtenerJugador(nombreJugador);
        Jugador defensor = obtenerOtroJugador(nombreJugador);

        if (atacante == null || defensor == null) return false;

        atacante.setAtacando(true);

        // Simular conexión del golpe (50% de probabilidad)
        boolean golpeConecta = Math.random() > 0.5;

        if (golpeConecta) {
            int daño = Jugador.DAÑO_GOLPE;
            boolean defensorVivo = defensor.recibirDaño(daño);

            System.out.println("💥 " + atacante.getNombre() + " golpea a " + defensor.getNombre() +
                    " por " + daño + " de daño. Vida restante: " + defensor.getVida());

            atacante.incrementarPuntaje(5);

            if (!defensorVivo) {
                terminarPelea(atacante.getNombre());
            }

            return true;
        } else {
            System.out.println("⚠️ " + atacante.getNombre() + " falló su golpe!");
            return false;
        }
    }

    /**
     * Procesa un poder especial.
     *
     * @param nombreJugador Nombre del jugador que usa el poder
     * @return true si el poder se activó
     */
    public boolean procesarPoder(String nombreJugador) {
        if (!peleasActiva) return false;

        Jugador atacante = obtenerJugador(nombreJugador);
        Jugador defensor = obtenerOtroJugador(nombreJugador);

        if (atacante == null || defensor == null) return false;

        // Verificar cooldown
        long ahora = System.currentTimeMillis();
        if (ultimoPoderUsado.containsKey(nombreJugador)) {
            long ultimoUso = ultimoPoderUsado.get(nombreJugador);
            if (ahora - ultimoUso < PODER_COOLDOWN) {
                System.out.println("⏳ Poder en cooldown. Espera " +
                        ((PODER_COOLDOWN - (ahora - ultimoUso)) / 1000) + " segundos.");
                return false;
            }
        }

        atacante.setUsandoPoder(true);
        int daño = Jugador.DAÑO_PODER;
        boolean defensorVivo = defensor.recibirDaño(daño);

        System.out.println("⭐ " + atacante.getNombre() + " ¡ACTIVA PODER ESPECIAL! " +
                defensor.getNombre() + " recibe " + daño + " de daño. Vida: " + defensor.getVida());

        atacante.incrementarPuntaje(15);
        ultimoPoderUsado.put(nombreJugador, ahora);

        if (!defensorVivo) {
            terminarPelea(atacante.getNombre());
        }

        return true;
    }

    /**
     * Procesa movimiento del jugador.
     *
     * @param nombreJugador Nombre del jugador
     * @param datos Coordenadas en formato "x,y"
     */
    public void procesarMovimiento(String nombreJugador, String datos) {
        if (!peleasActiva) return;

        Jugador jugador = obtenerJugador(nombreJugador);
        if (jugador == null) return;

        try {
            String[] coords = datos.split(",");
            if (coords.length == 2) {
                int x = Integer.parseInt(coords[0].trim());
                int y = Integer.parseInt(coords[1].trim());
                jugador.mover(x, y);
                System.out.println("📍 " + nombreJugador + " se movió a (" + x + ", " + y + ")");
            }
        } catch (NumberFormatException e) {
            System.err.println("⚠️ Formato de movimiento inválido: " + datos);
        }
    }

    /**
     * Termina la pelea y corona un ganador.
     *
     * @param ganador Nombre del ganador
     */
    private void terminarPelea(String ganador) {
        this.peleasActiva = false;
        System.out.println("🏆 ¡PELEA TERMINADA! Ganador: " + ganador);
    }

    /**
     * Obtiene un jugador por nombre.
     *
     * @param nombre Nombre del jugador
     * @return El jugador, o null si no existe
     */
    private Jugador obtenerJugador(String nombre) {
        if (jugador1.getNombre().equalsIgnoreCase(nombre)) return jugador1;
        if (jugador2.getNombre().equalsIgnoreCase(nombre)) return jugador2;
        return null;
    }

    /**
     * Obtiene el otro jugador.
     *
     * @param nombre Nombre del jugador actual
     * @return El otro jugador
     */
    private Jugador obtenerOtroJugador(String nombre) {
        if (jugador1.getNombre().equalsIgnoreCase(nombre)) return jugador2;
        if (jugador2.getNombre().equalsIgnoreCase(nombre)) return jugador1;
        return null;
    }

    // ============ GETTERS ============

    public Jugador getJugador1() { return jugador1; }
    public Jugador getJugador2() { return jugador2; }
    public boolean isPeleasActiva() { return peleasActiva; }

    public String getGanador() {
        if (!peleasActiva) {
            if (!jugador1.estaVivo()) return jugador2.getNombre();
            if (!jugador2.estaVivo()) return jugador1.getNombre();
        }
        return null;
    }
}