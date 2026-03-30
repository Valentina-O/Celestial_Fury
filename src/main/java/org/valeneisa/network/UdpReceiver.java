package org.valeneisa.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Hilo dedicado exclusivamente a la recepción de paquetes UDP.
 * Implementa el Patrón Observer para notificar a la interfaz.
 */
public class UdpReceiver implements Runnable {
    private DatagramSocket socket;
    private boolean running;
    private INetworkObserver observer;

    public UdpReceiver(DatagramSocket socket, INetworkObserver observer) {
        this.socket = socket;
        this.observer = observer;
        this.running = true;
    }

    @Override
    public void run() {
        while (running) {
            try {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                // Espera activa de paquetes (Requisito 2.3)
                socket.receive(packet);

                String msg = new String(packet.getData(), 0, packet.getLength());

                // Notifica al observador sin acoplamiento (SOLID - D)
                if (observer != null) {
                    observer.onMessageReceived(msg);
                }
            } catch (Exception e) {
                // Si el socket se cierra, detenemos el hilo suavemente
                running = false;
            }
        }
    }

    public void stop() {
        this.running = false;
    }
}