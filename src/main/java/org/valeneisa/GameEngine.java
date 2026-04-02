package org.valeneisa;

import org.valeneisa.entities.CombatManager;
import org.valeneisa.entities.Jugador;
import org.valeneisa.exceptions.NetworkException;
import org.valeneisa.network.INetworkObserver;
import org.valeneisa.network.Protocolo;
import org.valeneisa.network.UdpManager;

/**
 * Motor principal del juego Celestial Fury.
 * Conecta la red (Valentina) con la lógica de combate (Camilo).
 * Implementa INetworkObserver para reaccionar a los comandos del celular.
 */
public class GameEngine implements INetworkObserver {

    private CombatManager combatManager;
    private UdpManager red;
    private boolean juegoActivo;

    public GameEngine(String nombreJugador1, String nombreJugador2) throws NetworkException {
        // Crear los dos jugadores
        Jugador jugador1 = new Jugador(nombreJugador1);
        Jugador jugador2 = new Jugador(nombreJugador2);

        // Crear el motor de combate
        this.combatManager = new CombatManager(jugador1, jugador2);

        // Conectar con la red de Valentina
        this.red = UdpManager.getInstance();
        this.juegoActivo = false;

        System.out.println("🎮 GameEngine iniciado con: " + nombreJugador1 + " vs " + nombreJugador2);
    }

    /**
     * Inicia el juego: arranca la pelea y comienza a escuchar comandos.
     */
    public void iniciar() {
        combatManager.iniciarPelea();
        red.iniciarEscucha(this); // "this" es el observer
        juegoActivo = true;
        System.out.println("📡 Escuchando comandos en puerto 5000...");
    }

    /**
     * Aquí llegan todos los mensajes desde los celulares.
     * Formato: "COMANDO:usuario:datos"
     * Ejemplo: "MOV:Valentina:100,250"
     */
    @Override
    public void onMessageReceived(String mensaje) {
        System.out.println("[ENGINE] Mensaje recibido: " + mensaje);

        try {
            // Separar el mensaje según el protocolo de Valentina
            String[] partes = mensaje.split(":");
            if (partes.length < 2) return;

            String comando = partes[0].trim();
            String usuario = partes[1].trim();
            String datos   = partes.length > 2 ? partes[2].trim() : "";

            // Procesar según el comando
            switch (comando) {
                case Protocolo.MOVER:
                    combatManager.procesarMovimiento(usuario, datos);
                    break;

                case Protocolo.GOLPE:
                    boolean golpeo = combatManager.procesarGolpe(usuario);
                    if (golpeo) notificarPuntaje(usuario);
                    break;

                case Protocolo.PODER:
                    boolean podero = combatManager.procesarPoder(usuario);
                    if (podero) notificarPuntaje(usuario);
                    break;

                case Protocolo.FINAL:
                    terminarJuego();
                    break;

                default:
                    System.out.println("⚠️ Comando desconocido: " + comando);
                    break;
            }

            // Verificar si alguien ganó después de cada acción
            if (!combatManager.isPeleasActiva() && juegoActivo) {
                terminarJuego();
            }

        } catch (Exception e) {
            System.out.println("⚠️ Error procesando mensaje: " + mensaje);
        }
    }

    /**
     * Notifica el puntaje actualizado por la red (para que Julián lo guarde).
     */
    private void notificarPuntaje(String nombreJugador) {
        try {
            Jugador jugador = nombreJugador.equals(combatManager.getJugador1().getNombre())
                    ? combatManager.getJugador1()
                    : combatManager.getJugador2();

            String mensaje = Protocolo.formatear(
                    Protocolo.PUNTAJE,
                    jugador.getNombre(),
                    String.valueOf(jugador.getPuntaje())
            );

            // Enviar puntaje actualizado (Julián lo recibirá para guardarlo en BD)
            red.enviarDato(mensaje, "127.0.0.1");
            System.out.println("📊 Puntaje enviado: " + mensaje);

        } catch (NetworkException e) {
            System.out.println("⚠️ Error enviando puntaje: " + e.getMessage());
        }
    }

    /**
     * Termina el juego y notifica a todos.
     */
    private void terminarJuego() {
        juegoActivo = false;
        String ganador = combatManager.getGanador();

        if (ganador != null) {
            System.out.println("🏆 Juego terminado. Ganador: " + ganador);
        } else {
            System.out.println("🏁 Juego terminado.");
        }

        red.detenerServicio();
    }

    // Getters útiles
    public CombatManager getCombatManager() { return combatManager; }
    public boolean isJuegoActivo() { return juegoActivo; }

    /**
     * Punto de entrada principal del motor.
     */
    public static void main(String[] args) {
        try {
            GameEngine engine = new GameEngine("Jugadora1", "Jugadora2");
            engine.iniciar();
        } catch (NetworkException e) {
            System.err.println("❌ Error iniciando el juego: " + e.getMessage());
        }
    }
}
