package org.valeneisa.network;

/**
 * Esta clase es el "Diccionario" del juego.
 * Valentina define los comandos para que el equipo no cometa errores.
 */
public class Protocolo {

    // Comandos que enviará el control de Isabella o el juego de Camilo
    public static final String MOVER = "MOV";
    public static final String GOLPE = "HIT";
    public static final String PODER = "ULT";
    public static final String PUNTAJE = "SCORE";
    public static final String FINAL = "END";

    /**
     * Une los datos para enviarlos por el UdpManager.
     * Ejemplo: "MOV:Valentina:100,250"
     */
    public static String formatear(String comando, String usuario, String datos) {
        return comando + ":" + usuario + ":" + datos;
    }
}