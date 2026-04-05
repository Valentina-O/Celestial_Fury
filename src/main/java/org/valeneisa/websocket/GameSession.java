package org.valeneisa.websocket;

import org.valeneisa.model.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * Sesión de juego para 2 jugadores
 *
 * Responsabilidades:
 * - Gestionar estado de ambos jugadores
 * - Validar selección de personajes
 * - Procesar ataques
 * - Determinar ganador
 *
 * SOLID:
 * - Single Responsibility: Solo gestiona la sesión
 * - Open/Closed: Estados extensibles
 */
public class GameSession {

    public enum SessionState {
        WAITING,
        CHARACTER_SELECTION,
        PLAYING,
        COMPLETED
    }

    private Player player1;
    private Player player2;
    private SessionState state = SessionState.WAITING;
    private Set<String> selectedCharacters = new HashSet<>();

    public GameSession() {}

    /**
     * Añade un jugador a la sesión.
     * @return true si el jugador fue añadido, false si la sesión está llena
     */
    public synchronized boolean addPlayer(Player player) {
        if (player1 == null) {
            player1 = player;
            return true;
        } else if (player2 == null) {
            player2 = player;
            state = SessionState.CHARACTER_SELECTION;
            return true;
        }
        return false;
    }

    /**
     * Selecciona un personaje para un jugador.
     * Valida que el personaje no esté ya seleccionado.
     */
    public synchronized boolean selectCharacter(String playerId, String characterId) {
        if (selectedCharacters.contains(characterId)) {
            return false;
        }
        selectedCharacters.add(characterId);
        return true;
    }

    /**
     * Verifica si la sesión tiene los dos jugadores listos.
     */
    public boolean isReady() {
        return player1 != null && player2 != null;
    }

    // Getters y Setters
    public Player getPlayer1() { return player1; }
    public void setPlayer1(Player player1) { this.player1 = player1; }

    public Player getPlayer2() { return player2; }
    public void setPlayer2(Player player2) { this.player2 = player2; }

    public SessionState getState() { return state; }
    public void setState(SessionState state) { this.state = state; }

    public Set<String> getSelectedCharacters() { return selectedCharacters; }
}
