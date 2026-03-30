package org.valeneisa.network;
import org.valeneisa.exceptions.NetworkException;



/**
 * Define el comportamiento para la comunicación Peer-to-Peer.
 * Cumple con SOLID al abstraer la implementación del socket.
 */

public interface INetworkService {
    void enviarDato(String msj, String ip) throws NetworkException;
    // IMPORTANTE: Aquí debe recibir al observer para que coincida con el Manager
    void iniciarEscucha(INetworkObserver observer);
    void detenerServicio();

}
