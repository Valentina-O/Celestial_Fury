package org.valeneisa.server.handler;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manejador de mensajes WebSocket
 *
 * Responsabilidades:
 * - Procesar mensajes de clientes
 * - Dirigir a GameSession
 * - Sincronizar sesiones de 2 jugadores
 *
 * SOLID:
 * - Single Responsibility: Solo procesa mensajes
 * - Open/Closed: Extensible sin modificación
 */

public class MessageHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            String payload = message.getPayload();
            logger.info("Mensaje recibido: {}", payload);
            // TODO: Procesar mensaje
        } catch (Exception e) {
            logger.error("Error procesando mensaje: {}", e.getMessage());
        }
    }
}