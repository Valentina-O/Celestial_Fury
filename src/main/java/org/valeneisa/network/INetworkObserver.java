package org.valeneisa.network;


/**
 * Interfaz para el Patrón Observer.
 * Permite que la UI se actualice cuando la RED recibe datos.
 */

public interface INetworkObserver {
    void onMessageReceived(String mensaje);
}
