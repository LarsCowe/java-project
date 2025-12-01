# Match Tracker - Volleyball Live Score Backend

Een Spring Boot applicatie die live volleybal wedstrijdgegevens scraped van een externe website en deze via WebSocket naar geauthenticeerde clients broadcast.

## Overzicht

Deze applicatie haalt automatisch volleybal wedstrijdgegevens op van een externe bron, verwerkt deze data en stuurt real-time updates naar verbonden clients via WebSocket. De applicatie gebruikt JWT (JSON Web Token) voor authenticatie en beveiliging.

## Features

- **Live Match Scraping**: Automatische scraping van volleyball wedstrijdgegevens met configureerbaar interval
- **WebSocket Broadcasting**: Real-time updates naar alle verbonden clients
- **JWT Authenticatie**: Beveiligde endpoints en WebSocket verbindingen
- **REST API**: Endpoints voor authenticatie en match data
- **Scheduled Updates**: Periodieke updates via Spring Scheduler
- **WebClient Integration**: Moderne reactive HTTP client voor scraping

## Technologieën

- **Java 21**
- **Spring Boot 4.0.0**
  - Spring Security
  - Spring WebSocket
  - Spring WebFlux
- **JSoup 1.21.2** - HTML parsing voor web scraping
- **JWT (JJWT 0.12.3)** - Token-based authenticatie
- **Maven** - Build & dependency management
- **Spring Boot DevTools** - Hot reload tijdens development
- **Spring Dotenv 4.0.0** - Environment variabelen beheer

## Project Structuur

```
src/main/java/org/example/javaproject/
├── config/
│   ├── SecurityConfig.java          # Spring Security configuratie
│   ├── WebClientConfig.java         # WebClient bean configuratie
│   └── WebSocketConfig.java         # WebSocket endpoint configuratie
├── controller/
│   ├── AuthController.java          # JWT authenticatie endpoints
│   └── MatchRestController.java     # Match data REST endpoints
├── dto/
│   ├── AuthRequest.java             # Login request DTO
│   ├── AuthResponse.java            # Login response DTO
│   └── MatchUpdateMessage.java      # WebSocket message DTO
├── model/
│   ├── Match.java                   # Match data model
│   └── SetScore.java                # Set score model
├── security/
│   ├── JwtAuthenticationFilter.java # JWT filter voor REST
│   └── JwtWebSocketInterceptor.java # JWT interceptor voor WebSocket
├── service/
│   ├── MatchBroadcastService.java   # WebSocket broadcast service
│   ├── MatchScraperScheduler.java   # Scheduled scraping service
│   └── VolleyScraperService.java    # JSoup scraping logic
├── util/
│   └── JwtUtil.java                 # JWT token utilities
└── JavaProjectApplication.java      # Main application entry point
```

## Installatie & Setup

### Vereisten

- Java 21 of hoger
- Maven 3.6+
- Git

### Stappen

1. **Clone de repository**
   ```bash
   git clone https://github.com/LarsCowe/java-project.git
   cd java-project
   ```

2. **Configureer environment variabelen**
   
   Maak een `.env` bestand in de root directory:
   ```properties
   JWT_SECRET=jouw-geheime-sleutel-minimaal-32-characters-lang
   JWT_EXPIRATION=86400000
   ```

3. **Configureer application.properties** (optioneel)
   
   Pas indien nodig de configuratie aan in `src/main/resources/application.properties`:
   ```properties
   # Application
   spring.application.name=match-tracker
   server.port=8080

   # Match Configuration
   match.url=https://volleymatch-livescore-webclient.vercel.app/match/volleyball-match-2025
   match.scrape.interval=30000

   # JWT Configuration
   jwt.secret=${JWT_SECRET:default-dev-secret-key-minimum-32-chars}
   jwt.expiration=${JWT_EXPIRATION:86400000}
   ```

4. **Build het project**
   ```bash
   ./mvnw clean install
   ```

5. **Run de applicatie**
   ```bash
   ./mvnw spring-boot:run
   ```

   Of op Windows:
   ```cmd
   mvnw.cmd spring-boot:run
   ```

De applicatie start op `http://localhost:8080`

## API Endpoints

### Authenticatie

#### Login
```http
POST /api/auth
Content-Type: application/json

{
  "username": "jouw-username"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "jouw-username"
}
```

#### Valideer Token
```http
GET /api/auth/validate?token=<jwt-token>
Authorization: Bearer <jwt-token>
```

**Response:**
```json
true
```

### Match Data

#### Huidige Match Data
```http
GET /api/match
Authorization: Bearer <jwt-token>
```

**Response:**
```json
{
  "homeTeam": "Team A",
  "awayTeam": "Team B",
  "homeScore": 2,
  "awayScore": 1,
  "sets": [
    {
      "setNumber": 1,
      "homeScore": 25,
      "awayScore": 20
    }
  ],
  "lastUpdated": "2025-01-15T14:30:00"
}
```

### Health Check

#### Service Status
```http
GET /api/health
```

**Response:**
```json
{
  "status": "UP",
  "service": "Match Tracker",
  "hasMatchData": true
}
```

## WebSocket Verbinding

### Verbinden met WebSocket

```javascript
const token = 'jouw-jwt-token';
const socket = new SockJS('http://localhost:8080/ws?token=' + token);
const stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
    console.log('Connected: ' + frame);
    
    stompClient.subscribe('/topic/match', function(message) {
        const matchUpdate = JSON.parse(message.body);
        console.log('Match update:', matchUpdate);
        // matchUpdate bevat: homeTeam, awayTeam, homeScore, awayScore, sets, timestamp, message
    });
});
```

### WebSocket Message Format

```json
{
  "homeTeam": "Team A",
  "awayTeam": "Team B",
  "homeScore": 2,
  "awayScore": 1,
  "sets": [
    {
      "setNumber": 1,
      "homeScore": 25,
      "awayScore": 20
    }
  ],
  "timestamp": "2025-01-15T14:30:00",
  "message": "Match update"
}
```

### WebSocket Topics

- `/topic/match` - Ontvangt real-time match updates

## Configuratie

| Property | Beschrijving | Default |
|----------|--------------|---------|
| `server.port` | Server poort | 8080 |
| `match.url` | URL voor match scraping | https://volleymatch-livescore-webclient.vercel.app/match/volleyball-match-2025 |
| `match.scrape.interval` | Scrape interval in milliseconden | 30000 (30 sec) |
| `jwt.secret` | JWT signing key | Uit .env bestand |
| `jwt.expiration` | Token expiration tijd in ms | 86400000 (24 uur) |

## Security

- Alle REST endpoints (behalve `/api/auth` en `/api/health`) vereisen een geldig JWT token
- WebSocket verbindingen vereisen een geldig JWT token via query parameter
- JWT tokens worden gesigned met HMAC-SHA256
- Standaard token expiration is 24 uur

## Development

### Hot Reload

De applicatie gebruikt Spring Boot DevTools voor automatische herstart tijdens development.

### Logging

De applicatie logt belangrijke events naar de console, zoals:
- Match scraping activities
- WebSocket verbindingen
- Authenticatie pogingen

## Bronnen

De volgende documentatie en resources zijn gebruikt tijdens de ontwikkeling:

- [JSoup Cookbook](https://jsoup.org/cookbook/) - HTML parsing en web scraping
- [JWT Introduction](https://www.jwt.io/introduction#what-is-json-web-token) - JSON Web Token specificatie
- [JJWT GitHub](https://github.com/jwtk/jjwt) - Java JWT implementatie
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/index.html) - Security configuratie
- [Spring WebFlux WebClient](https://docs.spring.io/spring-framework/reference/web/webflux-webclient.html) - Reactive HTTP client
- [Spring Dotenv](https://github.com/paulschwarz/spring-dotenv) - Environment variabelen
- [WebSocketMessageBrokerConfigurer](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/socket/config/annotation/WebSocketMessageBrokerConfigurer.html) - WebSocket configuratie
- [Spring STOMP WebSocket Guide](https://spring.io/guides/gs/messaging-stomp-websocket) - STOMP messaging
- [HandshakeInterceptor](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/socket/server/HandshakeInterceptor.html) - WebSocket handshake interceptor

Tijdens het programmeren werd AI gebruikt als hulpmiddel voor codevoorstellen en foutanalyse. Alle gegenereerde oplossingen werden gecontroleerd, aangepast en nagekeken.

## License

Dit project is open source en beschikbaar onder de MIT License.

---

**Gemaakt met Spring Boot**
