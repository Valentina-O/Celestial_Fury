package org.valeneisa.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.valeneisa.server.handler.MessageHandler;

/**
 * Servidor WebSocket principal de Celestial Fury
 *
 * Responsabilidades:
 * - Configurar Spring Boot
 * - Registrar endpoint WebSocket en /celestial
 * - Manejar conexiones/desconexiones
 *
 * SOLID:
 * - Single Responsibility: Solo configura el servidor
 * - Dependency Inversion: Spring inyecta beans
 */
@SpringBootApplication
@Configuration
@EnableWebSocket
public class GameServer implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(gameWebSocketHandler(), "/celestial")
                .setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler gameWebSocketHandler() {
        return new MessageHandler();
    }

    public static void main(String[] args) {
        SpringApplication.run(GameServer.class, args);
        System.out.println("✅ Servidor WebSocket en ws://localhost:8080/celestial");
    }
}
