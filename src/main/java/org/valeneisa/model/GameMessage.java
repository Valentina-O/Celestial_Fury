package org.valeneisa.model;

/**
 * Modelo de mensaje WebSocket
 *
 * Responsabilidades:
 * - Serializar/deserializar mensajes
 * - Validar formato
 *
 * SOLID:
 * - Single Responsibility: Solo mensaje
 */
public class GameMessage {

    public enum MessageType {
        CONNECT, DISCONNECT, SELECT_CHARACTER,
        CHARACTER_SELECTED, CHARACTER_UNAVAILABLE,
        ATTACK, PLAYER_HIT, GAME_START, GAME_END, ERROR
    }

    private String type;
    private String playerId;
    private Object data;
    private Long timestamp;

    public GameMessage() {}

    public GameMessage(String type, String playerId, Object data) {
        this.type = type;
        this.playerId = playerId;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public boolean isValid() {
        return type != null && playerId != null;
    }

    // Getters y Setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }

    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
}
