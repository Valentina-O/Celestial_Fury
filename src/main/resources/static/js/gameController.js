/**
 * @fileoverview Controlador del Juego
 * Orquesta la lógica del juego y la comunicación entre componentes
 * Sigue principio SOLID: Dependency Injection, Interface Segregation
 * @author Celestial Fury Team
 */

/**
 * Controlador del Juego
 * Gestiona el flujo y estado del juego
 * @class
 */
class GameController {
  /**
   * Constructor
   * @param {string} gameContainerId - ID del contenedor del juego
   * @param {string} controllerContainerId - ID del contenedor del controlador
   * @param {SocketClient} socketClient - Cliente WebSocket
   * @param {CharacterManager} characterManager - Gestor de personajes
   */
  constructor(gameContainerId, controllerContainerId, socketClient, characterManager) {
    this.gameContainer = Utils.getElementById(gameContainerId);
    this.controllerContainer = Utils.getElementById(controllerContainerId);
    this.socketClient = socketClient;
    this.characterManager = characterManager;

    this.virtualController = null;
    this.gameState = 'waiting'; // waiting, playing, ended
    this.playerCharacters = [];
    this.opponentCharacters = [];
    this.playerHealth = GAME_CONFIG.DEFAULT_HEALTH;
    this.opponentHealth = GAME_CONFIG.DEFAULT_HEALTH;
    this.observers = [];

    this._setupGame();
  }

  /**
   * Configura el juego
   * @private
   */
  _setupGame() {
    this._setupSocketListeners();
    this._createGameUI();
    this._initializeControllers();
  }

  /**
   * Configura los listeners del WebSocket
   * @private
   */
  _setupSocketListeners() {
    this.socketClient.on(MESSAGE_TYPES.GAME_START, (data) => {
      this._handleGameStart(data);
    });

    this.socketClient.on(MESSAGE_TYPES.PLAYER_HIT, (data) => {
      this._handlePlayerHit(data);
    });

    this.socketClient.on(MESSAGE_TYPES.GAME_END, (data) => {
      this._handleGameEnd(data);
    });
  }

  /**
   * Crea la interfaz del juego
   * @private
   */
  _createGameUI() {
    if (!this.gameContainer) {
      Utils.log('Contenedor del juego no encontrado', 'error');
      return;
    }

    this.gameContainer.innerHTML = `
      <div class="game-board">
        <div class="player-area">
          <div class="player-characters" id="playerCharacters">
            <h2>Tus Personajes</h2>
            <div class="characters-display" id="playerDisplay"></div>
            <div class="health-bar">
              <div class="health-fill" id="playerHealthFill" style="width: 100%"></div>
              <span class="health-text" id="playerHealthText">100 / 100</span>
            </div>
          </div>
        </div>

        <div class="battle-zone" id="battleZone">
          <h1>⚡ CELESTIAL FURY ⚡</h1>
          <div class="battle-status" id="battleStatus">Esperando al oponente...</div>
        </div>

        <div class="opponent-area">
          <div class="opponent-characters" id="opponentCharacters">
            <h2>Personajes del Oponente</h2>
            <div class="characters-display" id="opponentDisplay"></div>
            <div class="health-bar">
              <div class="health-fill" id="opponentHealthFill" style="width: 100%"></div>
              <span class="health-text" id="opponentHealthText">100 / 100</span>
            </div>
          </div>
        </div>
      </div>
    `;

    this._updateCharacterDisplays();
  }

  /**
   * Inicializa los controladores
   * @private
   */
  _initializeControllers() {
    if (!this.controllerContainer) {
      Utils.log('Contenedor del controlador no encontrado', 'error');
      return;
    }

    this.virtualController = new VirtualController(
      this.controllerContainer.id,
      this.socketClient
    );

    // Suscribirse a eventos del controlador virtual
    this.virtualController.subscribe((event) => {
      if (event.type === 'attack') {
        this._handleLocalAttack(event.data);
      }
    });
  }

  /**
   * Actualiza las pantallas de personajes
   * @private
   */
  _updateCharacterDisplays() {
    const playerDisplay = Utils.getElementById('#playerDisplay');
    const opponentDisplay = Utils.getElementById('#opponentDisplay');

    if (!playerDisplay || !opponentDisplay) {
      return;
    }

    // Mostrar personajes del jugador
    playerDisplay.innerHTML = '';
    this.playerCharacters.forEach(characterId => {
      const character = this.characterManager.getCharacterInfo(characterId);
      if (character) {
        const charElement = Utils.createElement('div', {
          class: 'character-card player-character',
          style: { backgroundColor: character.color }
        }, `
          <div class="character-emoji">${character.emoji}</div>
          <div class="character-name">${character.name}</div>
        `);
        playerDisplay.appendChild(charElement);
      }
    });

    // Mostrar personajes del oponente
    opponentDisplay.innerHTML = '';
    this.opponentCharacters.forEach(characterId => {
      const character = this.characterManager.getCharacterInfo(characterId);
      if (character) {
        const charElement = Utils.createElement('div', {
          class: 'character-card opponent-character',
          style: { backgroundColor: character.color }
        }, `
          <div class="character-emoji">${character.emoji}</div>
          <div class="character-name">${character.name}</div>
        `);
        opponentDisplay.appendChild(charElement);
      }
    });
  }

  /**
   * Maneja el inicio del juego
   * @private
   * @param {Object} data - Datos del evento
   */
  _handleGameStart(data) {
    this.gameState = 'playing';
    this.playerCharacters = data.playerCharacters || [];
    this.opponentCharacters = data.opponentCharacters || [];

    this._updateCharacterDisplays();
    this._updateBattleStatus('¡BATALLA INICIADA!');
    this.virtualController.enable();

    Utils.log('Juego iniciado', 'info');
    this._notifyObservers('gameStart', data);
  }

  /**
   * Maneja cuando el jugador es golpeado
   * @private
   * @param {Object} data - Datos del golpe
   */
  async _handlePlayerHit(data) {
    const damage = data.damage || 10;
    this.playerHealth = Math.max(0, this.playerHealth - damage);

    this._updateHealthBar('player', this.playerHealth);

    // Animar impacto
    await this._animateImpact('player');

    if (this.playerHealth <= 0) {
      this._handleGameEnd({ winner: 'opponent' });
    }

    Utils.playSound('assets/sounds/impact.mp3');
    this._notifyObservers('playerHit', data);
  }

  /**
   * Maneja el fin del juego
   * @private
   * @param {Object} data - Datos del evento
   */
  _handleGameEnd(data) {
    this.gameState = 'ended';
    this.virtualController.disable();

    const winner = data.winner || 'opponent';
    const message = winner === 'player' ? '¡GANASTE!' : 'Perdiste...';

    this._updateBattleStatus(message);
    this._notifyObservers('gameEnd', data);

    Utils.log(`Juego finalizado. Ganador: ${winner}`, 'info');
  }

  /**
   * Maneja un ataque local
   * @private
   * @param {Object} data - Datos del ataque
   */
  _handleLocalAttack(data) {
    // Animar ataque local
    this._animateLocalAttack(data.position);

    Utils.log(`Ataque local: ${data.name}`, 'info');
  }

  /**
   * Anima el impacto en el jugador
   * @private
   * @param {string} target - 'player' u 'opponent'
   * @returns {Promise}
   */
  async _animateImpact(target) {
    const selector = target === 'player' ? '#playerCharacters' : '#opponentCharacters';
    const element = Utils.getElementById(selector);

    if (element) {
      element.classList.add('impact-animation');
      await Utils.delay(ANIMATION_CONFIG.IMPACT_DURATION);
      element.classList.remove('impact-animation');
    }
  }

  /**
   * Anima un ataque local
   * @private
   * @param {string} position - Posición del ataque (left, center, right)
   */
  _animateLocalAttack(position) {
    const battleZone = Utils.getElementById('#battleZone');
    if (!battleZone) return;

    const attack = Utils.createElement('div', {
      class: 'attack-animation ' + position + '-attack'
    }, '⚡');

    battleZone.appendChild(attack);

    setTimeout(() => {
      if (attack.parentNode) {
        attack.parentNode.removeChild(attack);
      }
    }, ANIMATION_CONFIG.PUNCH_DURATION);
  }

  /**
   * Actualiza la barra de vida
   * @private
   * @param {string} target - 'player' u 'opponent'
   * @param {number} health - Valor de salud
   */
  _updateHealthBar(target, health) {
    const fillId = target === 'player' ? '#playerHealthFill' : '#opponentHealthFill';
    const textId = target === 'player' ? '#playerHealthText' : '#opponentHealthText';

    const fillElement = Utils.getElementById(fillId);
    const textElement = Utils.getElementById(textId);

    if (fillElement) {
      const percent = Math.max(0, (health / GAME_CONFIG.DEFAULT_HEALTH) * 100);
      fillElement.style.width = percent + '%';
    }

    if (textElement) {
      textElement.textContent = `${health} / ${GAME_CONFIG.DEFAULT_HEALTH}`;
    }
  }

  /**
   * Actualiza el estado de batalla
   * @private
   * @param {string} message - Mensaje a mostrar
   */
  _updateBattleStatus(message) {
    const battleStatus = Utils.getElementById('#battleStatus');
    if (battleStatus) {
      battleStatus.textContent = message;
    }
  }

  /**
   * Obtiene el estado actual del juego
   * @returns {string}
   */
  getGameState() {
    return this.gameState;
  }

  /**
   * Se suscribe a eventos del juego
   * @param {Function} callback - Función a ejecutar
   * @returns {Function} Función para desuscribirse
   */
  subscribe(callback) {
    this.observers.push(callback);
    return () => {
      this.observers = this.observers.filter(cb => cb !== callback);
    };
  }

  /**
   * Notifica a observadores
   * @private
   * @param {string} eventType - Tipo de evento
   * @param {Object} data - Datos del evento
   */
  _notifyObservers(eventType, data) {
    this.observers.forEach(callback => {
      try {
        callback({ type: eventType, data });
      } catch (error) {
        Utils.log(`Error en observador: ${error.message}`, 'error');
      }
    });
  }

  /**
   * Limpia el controlador
   */
  destroy() {
    if (this.virtualController) {
      this.virtualController.destroy();
    }
    this.gameContainer.innerHTML = '';
    this.observers = [];
  }
}

// Exportar para uso en módulos
if (typeof module !== 'undefined' && module.exports) {
  module.exports = GameController;
}