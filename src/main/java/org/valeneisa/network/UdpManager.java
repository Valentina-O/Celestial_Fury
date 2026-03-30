package org.valeneisa.network;

import org.valeneisa.exceptions.NetworkException;
import java.net.*;

/**
 * Gestor principal de la comunicación P2P.
 * No mezcla lógica de juego con red (Requisito 3).
 */
public class UdpManager implements INetworkService {
    private static UdpManager instance;
    private DatagramSocket socket;
    private final int PORT = 5000;

    private UdpManager() throws NetworkException {
        try {
            this.socket = new DatagramSocket(PORT);
        } catch (SocketException e) {
            throw new NetworkException("No se pudo abrir el puerto " + PORT, e);
        }
    }

    // Patrón Singleton: Un solo punto de red en todo el programa
    public static UdpManager getInstance() throws NetworkException {
        if (instance == null) {
            instance = new UdpManager();
        }
        return instance;
    }

    @Override
    public void enviarDato(String msj, String ip) throws NetworkException {
        try {
            byte[] buf = msj.getBytes();
            InetAddress address = InetAddress.getByName(ip);
            DatagramPacket p = new DatagramPacket(buf, buf.length, address, PORT);
            socket.send(p); // Envío de información al otro jugador (Requisito 2.3)
        } catch (Exception e) {
            throw new NetworkException("Error al enviar paquete UDP", e);
        }
    }

    @Override
    public void iniciarEscucha(INetworkObserver observer) {
        // Delegamos la escucha a un hilo dedicado (Requisito 2.4)
        UdpReceiver receiver = new UdpReceiver(socket, observer);
        new Thread(receiver).start();
    }

    @Override
    public void detenerServicio() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}