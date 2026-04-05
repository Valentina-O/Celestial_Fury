package org.valeneisa.network;

import org.valeneisa.exceptions.NetworkException;

/**
 * Interfaz para servicios de red.
 * Define contrato para envío y recepción de datos.
 *
 * SOLID - Dependency Inversion
 *
 * @author Valentina
 * @version 1.0
 */
public interface INetworkService {
    /**
     * Envía datos a una dirección IP.
     *
     * @param mensaje Mensaje a enviar
     * @param ip Dirección IP destino
     * @throws NetworkException Si hay error de red
     */
    void enviarDato(String mensaje, String ip) throws NetworkException;

    /**
     * Inicia escucha de mensajes.
     *
     * @param observer Observer a notificar
     */
    void iniciarEscucha(INetworkObserver observer);

    /**
     * Detiene el servicio de red.
     */
    void detenerServicio();
}