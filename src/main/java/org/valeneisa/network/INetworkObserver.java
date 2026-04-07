package org.valeneisa.network;

/**
 * Interfaz Observer para notificaciones de red.
 * Implementa patrón Observer - SOLID Principle D (Dependency Inversion)
 *
 * @author Valentina
 * @version 1.0
 */
public interface INetworkObserver {
    /**
     * Llamado cuando se recibe un mensaje por UDP.
     *
     * @param mensaje El mensaje recibido
     */
    void onMessageReceived(String mensaje);
}