package org.valeneisa;

import org.valeneisa.network.UdpManager;
import org.valeneisa.network.Protocolo;
import org.valeneisa.network.INetworkObserver;
import org.valeneisa.exceptions.NetworkException;

public class MainPrueba {
    public static void main(String[] args) {
        try {
            System.out.println("--- CELESTIAL FURY: Módulo de Red ---");

            // 1. Obtener la instancia del Manager (Singleton)
            UdpManager red = UdpManager.getInstance();

            // 2. Definir un Observador temporal para ver qué llega
            INetworkObserver observadorDePrueba = mensaje -> {
                System.out.println("[RED] Nuevo mensaje recibido: " + mensaje);
            };

            // 3. Iniciar la escucha en el puerto 5000
            red.iniciarEscucha(observadorDePrueba);
            System.out.println("ESTADO: Escuchando en puerto 5000...");

            // 4. Prueba de envío (nos enviamos un mensaje a nosotros mismos)
            // Usamos el Protocolo que tú definiste
            String prueba = Protocolo.formatear(Protocolo.MOVER, "Valentina", "200,300");
            System.out.println("ENVIANDO: " + prueba);

            red.enviarDato(prueba, "127.0.0.1"); // 127.0.0.1 es tu propia PC

        } catch (NetworkException e) {
            System.err.println("ERROR CRÍTICO: " + e.getMessage());
        }
    }
}