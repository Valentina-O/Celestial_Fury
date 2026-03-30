# 🌸 Celestial Fury: Battle of Elegance 🌸

**Arquitecta de Conexiones & Core:** Valentina  
**Módulo:** Infraestructura de Red P2P y Entidades Base  
**Tecnologías:** Java 21, UDP Sockets, Threads (Hilos).

---

## 📑 Descripción del Proyecto
*Celestial Fury* es un juego de combate multijugador con una estética **Coquette/Pastel**. Este repositorio contiene la infraestructura de comunicación que permite el intercambio de datos en tiempo real entre dos instancias mediante una arquitectura **Peer-to-Peer (P2P)**.

---

## 🛠️ Mi Aporte Técnico (Valentina)
Como encargada de la arquitectura, implementé la base lógica bajo los siguientes estándares:

### 1. Comunicación en Tiempo Real (UDP)
Para cumplir con la baja latencia exigida, utilicé el protocolo **UDP** mediante `DatagramSocket`.
* **Escucha Multihilo:** Implementé un hilo independiente (`UdpReceiver`) para recibir paquetes sin bloquear el hilo principal (Main/UI).
* **Patrón Singleton:** La clase `UdpManager` garantiza una única instancia de red para gestionar el socket de forma eficiente.

### 2. Patrones de Diseño & SOLID
* **Patrón Observer:** Mediante la interfaz `INetworkObserver`, la red notifica a la interfaz gráfica sobre nuevos eventos (movimientos, golpes) de forma desacoplada.
* **Principio de Inversión de Dependencias (D):** El sistema depende de abstracciones, facilitando que el equipo (Camilo, Isabella, Julián) integre sus partes sin errores.

### 3. Protocolo de Mensajería
Diseñé un diccionario de comandos estandarizado para unificar el lenguaje del equipo:
* `MOV`: Movimiento de personajes.
* `HIT`: Acción de ataque.
* `SCORE`: Actualización de puntajes.

---

## 📂 Estructura de Clases Creadas
* `org.valeneisa.entities.Jugador`: Modelo de datos base.
* `org.valeneisa.network.UdpManager`: Gestor de red (Singleton).
* `org.valeneisa.network.UdpReceiver`: Hilo de escucha (Threads).
* `org.valeneisa.network.INetworkObserver`: Interfaz para actualizaciones de UI.
* `org.valeneisa.network.Protocolo`: Constantes para mensajería.

---

## 🚀 Cómo ejecutar la prueba de red
1. Abrir el proyecto en **IntelliJ IDEA**.
2. Ejecutar la clase `MainPrueba`.
3. La consola confirmará el inicio del servidor en el puerto **5000** y realizará una prueba automática de envío/recepción local.