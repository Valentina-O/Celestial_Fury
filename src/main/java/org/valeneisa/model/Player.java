package org.valeneisa.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Modelo de jugador
 *
 * Responsabilidades:
 * - Almacenar estado del jugador
 * - Gestionar personajes seleccionados
 * - Controlar salud y puntuación
 *
 * SOLID:
 * - Single Responsibility: Solo el estado del jugador
 */
public class Player {

    private String playerId;
    private Set<String> selectedCharacters = new HashSet<>();
    private Integer health = 100;
    private Integer score = 0;

    public Player() {}

    public Player(String playerId) {
        this.playerId = playerId;
    }

    public void takeDamage(Integer damage) {
        this.health = Math.max(0, health - damage);
    }

    public void addScore(Integer points) {
        this.score += points;
    }

    public boolean isAlive() {
        return health > 0;
    }

    // Getters y Setters
    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }

    public Set<String> getSelectedCharacters() { return selectedCharacters; }
    public void setSelectedCharacters(Set<String> selectedCharacters) { this.selectedCharacters = selectedCharacters; }

    public Integer getHealth() { return health; }
    public void setHealth(Integer health) { this.health = health; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
}
