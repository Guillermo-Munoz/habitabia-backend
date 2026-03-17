# Habitia 🏠

Plataforma de marketplace para alquiler de habitaciones entre particulares.
Los usuarios pueden publicar habitaciones, buscar disponibilidad y gestionar reservas.
**Los pagos se acuerdan directamente entre las partes** — la plataforma solo conecta hosts y guests.

---
![Diseño de Interfaz de Habitia](./assets/figma-preview.png)

## Stack tecnológico

| Capa | Tecnología |
|---|---|
| Backend | Java 21 + Spring Boot 4.x |
| Seguridad | Spring Security + JWT (JJWT 0.12.6) |
| Persistencia | Spring Data JPA + Hibernate |
| Base de datos | PostgreSQL 16 |
| Contenedores | Docker + Docker Compose |
| Orquestación | Kubernetes |

---

## Arquitectura

El proyecto implementa un **monolito modular** con principios de **Domain-Driven Design (DDD)** y **Arquitectura Hexagonal (Ports & Adapters)**, organizado por **Vertical Slicing**.
```
src/main/java/com/habitia/
├── shared/                        # Kernel compartido
│   ├── domain/
│   │   ├── valueobject/           # UserId, Money, DateRange
│   │   └── exception/             # DomainException, BusinessRuleException
│   └── infrastructure/
│       └── security/              # JWT, SecurityConfig
│
├── users/                         # Módulo usuarios
│   ├── domain/                    # User, UserRole, UserRepository (puerto)
│   ├── application/               # RegisterUserUseCase, AuthenticateUserUseCase
│   └── infrastructure/
│       ├── persistence/           # UserJpaEntity, UserRepositoryAdapter
│       └── web/                   # UserController, DTOs
│
├── rooms/                         # Módulo habitaciones
│   ├── domain/                    # Room, RoomStatus, RoomRepository (puerto)
│   ├── application/               # PublishRoomUseCase, SearchRoomsUseCase
│   └── infrastructure/
│       ├── persistence/           # RoomJpaEntity, RoomRepositoryAdapter
│       └── web/                   # RoomController, DTOs
│
├── bookings/                      # Módulo reservas
│   ├── domain/                    # Booking, BookingStatus, BookingRepository (puerto)
│   ├── application/               # RequestBookingUseCase, UpdateBookingStatusUseCase
│   └── infrastructure/
│       ├── persistence/           # BookingJpaEntity, BookingRepositoryAdapter
│       └── web/                   # BookingController, DTOs
│
└── reviews/                       # Módulo reseñas
    ├── domain/
    ├── application/
    └── infrastructure/
```

### Ciclo de vida de una reserva
```
REQUESTED → ACCEPTED → CONFIRMED → COMPLETED
              ↓              ↓
           CANCELLED      CANCELLED
```

---

## Requisitos previos

- Docker Desktop
- Java 21 (para desarrollo local)
- Maven 3.9+

---

## Arrancar con Docker Compose
```bash
# Clonar el repositorio
git clone https://github.com/tu-usuario/habitia.git
cd habitia

# Levantar la aplicación completa
docker-compose up --build
```

La API estará disponible en `http://localhost:8080`

---

## Arrancar con Kubernetes
```bash
# Construir la imagen
docker build -t habitia/app:latest .

# Aplicar los manifiestos
kubectl apply -f k8s/namespace.yml
kubectl apply -f k8s/postgres-secret.yml
kubectl apply -f k8s/app-secret.yml
kubectl apply -f k8s/postgres-pvc.yml
kubectl apply -f k8s/postgres-deployment.yml
kubectl apply -f k8s/app-deployment.yml

# Verificar el estado
kubectl get pods -n habitia
```

---

## API REST

### Autenticación

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/v1/auth/register` | Registro de usuario |
| POST | `/api/v1/auth/login` | Login → devuelve JWT |

### Habitaciones

| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| POST | `/api/v1/rooms` | ✅ | Publicar habitación |
| GET | `/api/v1/rooms?city=&guests=` | ✅ | Buscar habitaciones |
| GET | `/api/v1/rooms/{id}` | ✅ | Detalle de habitación |

### Reservas

| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| POST | `/api/v1/bookings` | ✅ | Solicitar reserva |
| PATCH | `/api/v1/bookings/{id}/accept` | ✅ | Aceptar reserva |
| PATCH | `/api/v1/bookings/{id}/reject` | ✅ | Rechazar reserva |
| PATCH | `/api/v1/bookings/{id}/confirm` | ✅ | Confirmar reserva |
| PATCH | `/api/v1/bookings/{id}/cancel` | ✅ | Cancelar reserva |
| PATCH | `/api/v1/bookings/{id}/complete` | ✅ | Completar estancia |

---

## Ejemplos de uso

### Registro
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Ana García",
    "email": "ana@example.com",
    "password": "12345678",
    "role": "GUEST"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "ana@example.com",
    "password": "12345678"
  }'
```

### Publicar habitación
```bash
curl -X POST http://localhost:8080/api/v1/rooms \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Habitación céntrica en Madrid",
    "description": "Luminosa y bien comunicada",
    "street": "Calle Gran Vía 10",
    "city": "Madrid",
    "country": "España",
    "latitude": 40.4168,
    "longitude": -3.7038,
    "priceAmount": 45.00,
    "priceCurrency": "EUR",
    "maxGuests": 2
  }'
```

---

## Variables de entorno

| Variable | Descripción | Default |
|----------|-------------|---------|
| `SPRING_DATASOURCE_URL` | URL de PostgreSQL | `jdbc:postgresql://localhost:5432/habitia` |
| `SPRING_DATASOURCE_USERNAME` | Usuario de BD | `habitia` |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de BD | `habitia` |
| `APP_JWT_SECRET` | Clave secreta JWT (mín. 256 bits) | — |
| `APP_JWT_EXPIRATION_MS` | Expiración del token en ms | `86400000` (24h) |

---

## Decisiones de diseño

- **Monolito modular primero** — menor complejidad operacional inicial manteniendo separación de dominio clara
- **Hexagonal por módulo** — los adaptadores JPA y REST son intercambiables sin tocar el dominio
- **Sin procesamiento de pagos** — la plataforma conecta usuarios; el pago se acuerda externamente
- **JWT stateless** — permite escalar horizontalmente sin sesión compartida
- **Virtual threads (Java 21)** — alto throughput con código síncrono

---

## Roadmap

- [ ] Módulo `reviews` — reseñas tras estancia completada
- [ ] Imágenes de habitaciones
- [ ] Notificaciones por email
- [ ] Panel de administración y moderación
- [ ] Migración a microservicios (bookings primero)

---

## Licencia

MIT
