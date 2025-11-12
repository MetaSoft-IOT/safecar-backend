# SafeCar Backend Platform

## Summary
SafeCar Backend Platform is a comprehensive IoT vehicle telemetry and workshop management platform, illustrating development with Java, Spring Boot 3.5.7, and Spring Data JPA on MySQL Database. The platform implements Domain-Driven Design (DDD) with CQRS patterns and Anti-Corruption Layers (ACL) to provide intelligent vehicle monitoring and workshop operations management.

## Features
- RESTful API with 32+ endpoints
- OpenAPI 3.0 Documentation 
- Swagger UI Integration
- Spring Boot 3.5.7 Framework
- Spring Data JPA with MySQL
- JWT Bearer Token Authentication
- Domain-Driven Design (DDD)
- CQRS Pattern Implementation
- Anti-Corruption Layer (ACL)
- Real-time IoT Telemetry Processing
- Event-Driven Architecture
- Workshop Appointment Management
- Vehicle Fleet Management
- Predictive Maintenance Alerts

## Bounded Contexts
This version of SafeCar Backend Platform is divided into five main bounded contexts: IAM, Profiles, Devices, Workshop, and Payments, plus a Shared context for common infrastructure.

### Identity and Access Management (IAM) Context

The IAM Context is responsible for managing platform users, including the sign-up and sign-in processes. It applies JSON Web Token based authorization and Password hashing. Its capabilities include:

- Create a new User (Sign Up).
- Authenticate a User (Sign In).
- Get a User by Email.
- Get All Users.
- Get All Roles.
- Use Spring Security features to implement an authorization pipeline based on request filtering.
- Generate and validate JSON Web Tokens.
- Apply Password hashing.

This version implements the following roles: ROLE_ADMIN (system administrators), ROLE_CLIENT (basic users), ROLE_DRIVER (vehicle operators), ROLE_MECHANIC (maintenance technicians), and ROLE_WORKSHOP (workshop owners). The roles are used to manage access to platform features according to business rules.

This context includes also an anti-corruption layer to communicate with other bounded contexts, providing capabilities to:
- Create a new User, returning ID of the created User on success.
- Get a User by Email, returning the associated User information.
- Validate User credentials and generate JWT tokens.

### Profiles Context

The Profiles Context is responsible for managing profiles of users including personal information and business profiles. It includes the following features:

- Create a new Person Profile (for Drivers, Mechanics, etc.).
- Create a new Business Profile (for Workshop Owners).
- Get a Person Profile by User Email.
- Get a Business Profile by Email.
- Update existing profiles.
- Automatic Driver and Mechanic profile creation via event handling.

This context includes also an anti-corruption layer to communicate with other bounded contexts. The anti-corruption layer is responsible for managing the communication between the Profiles Context and other contexts. It offers the following capabilities:
- Create a new Person Profile, returning ID of the created Profile on success.
- Create a new Business Profile for workshop owners.
- Get a Profile by User Email, returning the associated Profile information.

### Devices Context

The Devices Context is responsible for managing vehicles and drivers. It handles vehicle registration, fleet management, and driver-vehicle relationships. Its features include:

- Register a new Vehicle.
- Get a Vehicle by ID.
- Get all Vehicles by Driver ID.
- Automatic Driver profile creation when Person Profile is created.
- Automatic vehicle count updates via event handling.
- Vehicle validation and fleet management.

This context includes also an anti-corruption layer to communicate with the Profiles Context. The anti-corruption layer consumes capabilities offered by the Profiles Context to:
- Validate Driver existence before vehicle registration.
- Create Driver profiles automatically via events.
- Maintain referential integrity between drivers and vehicles.

### Workshop Context

The Workshop Context is responsible for managing workshop operations, appointments, telemetry processing, and mechanic management. This is the core business context for SafeCar platform. Its comprehensive features include:

- Manage Workshop entities (query and update).
- Register and configure Mechanics with specializations.
- Create and manage Appointments with complete lifecycle (PENDING → CONFIRMED → IN_PROGRESS → COMPLETED/CANCELLED).
- Assign/unassign Mechanics to Appointments.
- Process real-time IoT telemetry data from vehicles (speed, GPS, diagnostic codes).
- Generate predictive maintenance alerts based on telemetry severity (INFO, WARNING, CRITICAL).
- Event-driven architecture for cross-context communication.

This context includes anti-corruption layers to communicate with Profiles, IAM, and Devices contexts. The ACL consumes capabilities from other contexts to:
- Validate user profiles and permissions via ProfilesContextFacade.
- Verify vehicle and driver information via DevicesContextFacade.
- Authenticate users and validate permissions via IamContextFacade.

The Workshop Context processes telemetry samples including:
- Speed monitoring with GPS coordinates
- Engine diagnostic codes (DTC OBD2 standard)
- Predictive maintenance triggers
- Real-time vehicle health monitoring

### Payments Context

The Payments Context is responsible for managing subscription billing through Stripe integration. This context handles payment processing, subscription lifecycle management, and plan limitations enforcement. Its features include:

- Create Stripe checkout sessions for subscription plans (BASIC, PROFESSIONAL, PREMIUM).
- Handle Stripe webhook events for subscription lifecycle.
- Manage subscription records linked to workshop owners.
- Enforce plan-based limitations (e.g., maximum number of mechanics per plan).
- Integration with Stripe API for secure payment processing.

The Payments Context provides the following subscription plans:
- **BASIC**: Up to 3 mechanics per workshop
- **PROFESSIONAL**: Up to 10 mechanics per workshop
- **PREMIUM**: Up to 30 mechanics per workshop

This context operates independently with minimal coupling to other bounded contexts, following the principle of separation of concerns. Payment processing is handled entirely through Stripe's secure infrastructure, and subscription data is stored locally for plan enforcement.

### Shared Context

The Shared Context provides common infrastructure components, domain model elements, and cross-cutting concerns used by all other bounded contexts. It includes:

- Common Value Objects and base entities.
- Database configuration and JPA mappings.
- OpenAPI documentation configuration.
- Cross-cutting infrastructure services.

## Configuration and Setup

### Prerequisites
- Java 21+
- Maven 3.9+
- MySQL 8.0+

### Database Configuration
```properties
# src/main/resources/application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/safecar-db
spring.datasource.username=${MYSQL_ROOT_USER}
spring.datasource.password=${MYSQL_ROOT_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.hibernate.naming.physical-strategy=com.safecar.platform.shared.infrastructure.persistence.jpa.configuration.strategy.SnakeCaseWithPluralizedTablePhysicalNamingStrategy
```

### Stripe Payment Configuration

**⚠️ IMPORTANTE**: El contexto de Payments requiere configuración de Stripe API para funcionar correctamente.

#### 1. Variables de Entorno Requeridas

```bash
# Stripe API Keys (obtener desde: https://dashboard.stripe.com/test/apikeys)
export STRIPE_SECRET_KEY=sk_test_your_secret_key_here

# Webhook Secret (obtener al crear webhook endpoint en Stripe Dashboard)
export STRIPE_WEBHOOK_SECRET=whsec_test_your_webhook_secret_here

# Frontend URL para redirecciones post-pago (opcional)
export FRONTEND_URL=http://localhost:8081  # Default si no se especifica
```

#### 2. Configuración en application.properties

```properties
# Stripe API Configuration
stripe.secret-key=${STRIPE_SECRET_KEY:sk_test_default_key}
stripe.webhook-secret=${STRIPE_WEBHOOK_SECRET:whsec_test_default}

# Frontend URL for payment redirects
app.frontend-url=${FRONTEND_URL:http://localhost:8081}

# Plan pricing (Stripe Price IDs - configurados en Stripe Dashboard)
plans.basic.price-id=price_1SQbsT3l890Fc29CerlSwh4r
plans.professional.price-id=price_1SQbt23l890Fc29CqoqLYCnu
plans.premium.price-id=price_1SQbtK3l890Fc29COSEZ6iK4

# Plan mechanic limits (enforcement local)
plans.basic.mechanics-limit=3
plans.professional.mechanics-limit=10
plans.premium.mechanics-limit=30
```

#### 3. Setup de Stripe Webhook (para desarrollo local)

Para recibir webhooks de Stripe en desarrollo local, usa Stripe CLI:

```bash
# Instalar Stripe CLI
brew install stripe/stripe-cli/stripe

# Login a tu cuenta Stripe
stripe login

# Escuchar webhooks y reenviar a localhost
stripe listen --forward-to localhost:8080/webhooks/stripe

# Output esperado:
# > Ready! Your webhook signing secret is whsec_xxxxx
# Copiar este secret a STRIPE_WEBHOOK_SECRET
```

#### 4. Obtener Stripe API Keys

1. **Crear cuenta Stripe**: https://dashboard.stripe.com/register
2. **Modo Test**: Cambiar a "Test mode" en el dashboard (toggle superior derecho)
3. **API Keys**: Navegar a Developers → API keys
   - **Secret Key**: `sk_test_51...` (usar en `STRIPE_SECRET_KEY`)
   - **Publishable Key**: `pk_test_...` (usar en frontend)
4. **Webhook Secret**: 
   - Navegar a Developers → Webhooks → Add endpoint
   - URL: `https://your-domain.com/webhooks/stripe` (o usar Stripe CLI para localhost)
   - Eventos: Seleccionar `customer.subscription.created`
   - Copiar signing secret: `whsec_...`

#### 5. Configurar Price IDs (Planes de Suscripción)

En Stripe Dashboard → Products:
1. Crear 3 productos: "Basic", "Professional", "Premium"
2. Agregar precio recurrente mensual a cada uno (ejemplo: S/. 29, S/. 99, S/. 299)
3. Copiar los Price IDs (formato: `price_xxx`) a `application.properties`

**Nota**: Los Price IDs hardcodeados en `PlanType.java` deben coincidir con `application.properties`.

### Running the Application

#### Build and Run
```bash
# Compile the project
./mvnw clean compile

# Run the application
./mvnw spring-boot:run

# Application will be available at: http://localhost:8080
```

#### Verify Installation
```bash
# Health check
curl http://localhost:8080/actuator/health

# Access Swagger UI Documentation
open http://localhost:8080/swagger-ui.html
```

---

## 🧪 FLUJOS DE PRUEBA END-TO-END

Esta sección provee flujos completos y **validados contra la implementación real** para probar todos los bounded contexts de SafeCar Platform. Los flujos están organizados por caso de uso y verificados contra los controladores actuales.

### 📝 Notas Importantes
- **IDs dinámicos**: Los IDs en los ejemplos son ilustrativos. Ajusta según tu base de datos.
- **Tokens JWT**: Reemplaza `$TOKEN` con tokens reales obtenidos del endpoint de autenticación.
- **Timestamps**: Usa formato ISO-8601 (`2025-11-12T10:00:00Z`).
- **Eventos**: Muchas operaciones publican eventos de dominio para otros BCs.

---

## 🔐 **FLUJO 1: Configuración Inicial (IAM + Profiles + Devices)**

Este flujo establece los actores necesarios: conductores, mecánicos, talleres y vehículos.

### **1.1. Registro de Usuarios (IAM Context)**

```bash
# ============================================================
# PASO 1A: Registrar Driver (Conductor)
# ============================================================
curl -X POST http://localhost:8080/api/v1/authentication/sign-up \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "driver1@safecar.com",
    "password": "Driver123!",
    "confirmPassword": "Driver123!",
    "roles": ["ROLE_DRIVER"]
  }'

# Respuesta esperada: { "id": 1, "email": "driver1@safecar.com", "roles": ["ROLE_DRIVER"] }

# ============================================================
# PASO 1B: Registrar Mechanic (Mecánico)
# ============================================================
curl -X POST http://localhost:8080/api/v1/authentication/sign-up \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "mechanic1@safecar.com",
    "password": "Mechanic123!",
    "confirmPassword": "Mechanic123!",
    "roles": ["ROLE_MECHANIC"]
  }'

# Respuesta esperada: { "id": 2, "email": "mechanic1@safecar.com", "roles": ["ROLE_MECHANIC"] }

# ============================================================
# PASO 1C: Registrar Workshop Owner (Dueño de Taller)
# ============================================================
curl -X POST http://localhost:8080/api/v1/authentication/sign-up \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "owner1@safecar.com",
    "password": "Owner123!",
    "confirmPassword": "Owner123!",
    "roles": ["ROLE_WORKSHOP"]
  }'

# Respuesta esperada: { "id": 3, "email": "owner1@safecar.com", "roles": ["ROLE_WORKSHOP"] }
```

### **1.2. Autenticación (Obtener Tokens JWT)**

```bash
# ============================================================
# PASO 2A: Login Driver
# ============================================================
DRIVER_TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/authentication/sign-in \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "driver1@safecar.com",
    "password": "Driver123!"
  }' | jq -r '.token')

echo "Driver Token: $DRIVER_TOKEN"

# ============================================================
# PASO 2B: Login Mechanic
# ============================================================
MECHANIC_TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/authentication/sign-in \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "mechanic1@safecar.com",
    "password": "Mechanic123!"
  }' | jq -r '.token')

echo "Mechanic Token: $MECHANIC_TOKEN"

# ============================================================
# PASO 2C: Login Workshop Owner
# ============================================================
OWNER_TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/authentication/sign-in \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "owner1@safecar.com",
    "password": "Owner123!"
  }' | jq -r '.token')

echo "Owner Token: $OWNER_TOKEN"
```

### **1.3. Consultar Usuarios y Roles (Endpoints Adicionales)**

```bash
# ============================================================
# PASO 3A: Listar todos los usuarios (admin)
# ============================================================
curl -X GET http://localhost:8080/api/v1/users \
  -H "Authorization: Bearer $OWNER_TOKEN"

# Respuesta: Array de usuarios con sus roles

# ============================================================
# PASO 3B: Obtener usuario específico por email
# ============================================================
curl -X GET http://localhost:8080/api/v1/users/driver1@safecar.com \
  -H "Authorization: Bearer $DRIVER_TOKEN"

# Respuesta: Usuario con email driver1@safecar.com

# ============================================================
# PASO 3C: Listar todos los roles disponibles
# ============================================================
curl -X GET http://localhost:8080/api/v1/roles \
  -H "Authorization: Bearer $OWNER_TOKEN"

# Respuesta: ["ROLE_ADMIN", "ROLE_CLIENT", "ROLE_DRIVER", "ROLE_MECHANIC", "ROLE_WORKSHOP"]
```

### **1.4. Crear Perfiles (Profiles Context)**

**⚠️ IMPORTANTE**: Los endpoints de perfiles usan rutas separadas para Person y Business profiles.

```bash
# ============================================================
# PASO 4A: Crear Person Profile para Driver
# ============================================================
curl -X POST "http://localhost:8080/api/v1/person-profiles?userEmail=driver1@safecar.com" \
  -H "Authorization: Bearer $DRIVER_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "fullName": "Juan Pérez",
    "city": "Lima",
    "phone": "987654321",
    "country": "Peru",
    "dni": "12345678"
  }'

# Respuesta esperada: { "id": 1, "fullName": "Juan Pérez", ... }
# Efecto secundario: Se crea automáticamente un Driver en el BC Devices (vía evento)

# ============================================================
# PASO 4B: Crear Person Profile para Mechanic
# ============================================================
curl -X POST "http://localhost:8080/api/v1/person-profiles?userEmail=mechanic1@safecar.com" \
  -H "Authorization: Bearer $MECHANIC_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "fullName": "Carlos Rodríguez",
    "city": "Lima",
    "phone": "912345678",
    "country": "Peru",
    "dni": "87654321"
  }'

# Respuesta esperada: { "id": 2, "fullName": "Carlos Rodríguez", ... }
# Efecto secundario: Se crea automáticamente un Mechanic en el BC Workshop (vía evento)

# ============================================================
# PASO 4C: Crear Business Profile para Workshop Owner
# ============================================================
curl -X POST "http://localhost:8080/api/v1/business-profiles?userEmail=owner1@safecar.com" \
  -H "Authorization: Bearer $OWNER_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "businessName": "Taller El Buen Mecánico",
    "ruc": "20123456789",
    "businessAddress": "Av. Venezuela 789, Lima, Peru",
    "contactPhone": "987654321",
    "contactEmail": "maria.gonzalez@safecar.com"
  }'

# Respuesta esperada: { "id": 3, "fullName": "María González", ... }
```

### **1.5. Consultar y Actualizar Perfiles**

```bash
# ============================================================
# PASO 5A: Obtener Person Profile por email
# ============================================================
curl -X GET "http://localhost:8080/api/v1/person-profiles?userEmail=driver1@safecar.com" \
  -H "Authorization: Bearer $DRIVER_TOKEN"

# Respuesta: Perfil completo del conductor

# ============================================================
# PASO 5B: Obtener Business Profile por email
# ============================================================
curl -X GET "http://localhost:8080/api/v1/business-profiles?email=owner1@safecar.com" \
  -H "Authorization: Bearer $OWNER_TOKEN"

# Nota: Este endpoint usa 'email' sin el prefijo 'user'

# ============================================================
# PASO 5C: Actualizar Person Profile
# ============================================================
curl -X PUT "http://localhost:8080/api/v1/person-profiles/1" \
  -H "Authorization: Bearer $DRIVER_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "firstName": "Juan Carlos",
    "lastName": "Pérez Gonzales",
    "street": "Av. Arequipa 1234 - Dpto 501",
    "city": "Lima",
    "state": "Lima",
    "country": "Peru",
    "dni": "12345678"
  }'

# Respuesta: Perfil actualizado

# ============================================================
# PASO 5D: Actualizar Business Profile
# ============================================================
curl -X PUT "http://localhost:8080/api/v1/business-profiles/3" \
  -H "Authorization: Bearer $OWNER_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "firstName": "María Elena",
    "lastName": "González Ruiz",
    "street": "Av. Venezuela 789 - Of. 302",
    "city": "Lima",
    "state": "Lima",
    "country": "Peru",
    "dni": "11223344"
  }'
```

### **1.6. Registrar Vehículos (Devices Context)**

```bash
# ============================================================
# PASO 6: Registrar Vehículo del Driver
# ============================================================
curl -X POST http://localhost:8080/api/v1/vehicles \
  -H "Authorization: Bearer $DRIVER_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "driverId": 1,
    "licensePlate": "ABC-123",
    "brand": "Toyota",
    "model": "Corolla",
    "year": 2023
  }'

# Respuesta esperada: { "id": 1, "licensePlate": "ABC-123", "brand": "Toyota", "model": "Corolla", ... }
# Efecto secundario: Incrementa contador totalVehicles del Driver (vía evento)

# ============================================================
# PASO 7A: Obtener vehículo por ID
# ============================================================
curl -X GET http://localhost:8080/api/v1/vehicles/1 \
  -H "Authorization: Bearer $DRIVER_TOKEN"

# ============================================================
# PASO 7B: Listar vehículos del driver
# ============================================================
curl -X GET http://localhost:8080/api/v1/drivers/1/vehicles \
  -H "Authorization: Bearer $DRIVER_TOKEN"

# Respuesta: Array de vehículos del driver
```

---

## 🏭 **FLUJO 2: Gestión de Workshop y Mecánicos (Workshop Context)**

Este flujo gestiona talleres y mecánicos.

### **2.1. Consultar Workshop**

**⚠️ NOTA IMPORTANTE**: Actualmente NO existe endpoint `POST /workshops` para crear talleres. Los talleres deben crearse mediante otro mecanismo (migración, eventos, o implementación futura).

```bash
# ============================================================
# PASO 1: Consultar Workshop por ID (asumiendo que existe)
# ============================================================
curl -X GET http://localhost:8080/api/v1/workshops/1 \
  -H "Authorization: Bearer $OWNER_TOKEN"

# Respuesta esperada:
# {
#   "id": 1,
#   "businessProfileId": 3,
#   "workshopDescription": "Taller especializado en diagnóstico electrónico",
#   "totalMechanics": 0
# }
```

### **2.2. Actualizar Descripción del Workshop**

```bash
# ============================================================
# PASO 2: Actualizar descripción (PATCH)
# ============================================================
curl -X PATCH http://localhost:8080/api/v1/workshops/1 \
  -H "Authorization: Bearer $OWNER_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "workshopDescription": "Taller especializado en diagnóstico electrónico y mecánica general. Atención 24/7."
  }'

# Respuesta: Workshop con descripción actualizada
```

### **2.3. Configurar Mechanic (Especializations)**

```bash
# ============================================================
# PASO 3: Actualizar especializaciones del Mechanic
# ============================================================
# Nota: El ID del Mechanic (2) fue creado automáticamente cuando se creó el Person Profile

curl -X PATCH http://localhost:8080/api/v1/mechanics/2 \
  -H "Authorization: Bearer $OWNER_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "specializations": ["Electrónica automotriz", "Diagnóstico OBD2", "Mecánica general"],
    "workshopId": 1
  }'

# Respuesta: Mechanic con especializaciones actualizadas
# Efecto secundario: Incrementa totalMechanics del Workshop (si está asignado)
```

---

## 📅 **FLUJO 3: Gestión de Citas (Appointments - CORE BUSINESS)**

Este es el flujo principal del contexto Workshop: crear, consultar, reprogramar y gestionar citas.

### **3.1. Crear Appointment (Cita)**

```bash
# ============================================================
# PASO 1: Crear cita para el vehículo del driver
# ============================================================
curl -X POST http://localhost:8080/api/v1/workshops/1/appointments \
  -H "Authorization: Bearer $OWNER_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "workshopId": 1,
    "vehicleId": 1,
    "driverId": 1,
    "scheduledStartAt": "2025-11-15T10:00:00Z",
    "scheduledEndAt": "2025-11-15T11:30:00Z",
    "description": "Mantenimiento preventivo de 10,000 km"
  }'

# Respuesta esperada:
# {
#   "id": 1,
#   "workshopId": 1,
#   "vehicleId": 1,
#   "driverId": 1,
#   "scheduledStartAt": "2025-11-15T10:00:00Z",
#   "scheduledEndAt": "2025-11-15T11:30:00Z",
#   "status": "PENDING",
#   "description": "Mantenimiento preventivo de 10,000 km",
#   "notes": []
# }

# Efectos secundarios:
# - Publica AppointmentCreatedEvent
```

### **3.2. Consultar Appointments**

```bash
# ============================================================
# PASO 2A: Obtener cita por ID
# ============================================================
curl -X GET http://localhost:8080/api/v1/workshops/1/appointments/1 \
  -H "Authorization: Bearer $OWNER_TOKEN"

# Respuesta: JSON con todos los detalles de la cita

# ============================================================
# PASO 2B: Listar citas del taller por rango de fechas
# ============================================================
curl -X GET "http://localhost:8080/api/v1/workshops/1/appointments?from=2025-11-15T00:00:00Z&to=2025-11-15T23:59:59Z" \
  -H "Authorization: Bearer $OWNER_TOKEN"

# Respuesta: Array de citas en el rango especificado
```

### **3.3. Actualizar Estado de la Cita (Lifecycle)**

```bash
# ============================================================
# PASO 3A: Confirmar cita (PENDING → CONFIRMED)
# ============================================================
curl -X PATCH http://localhost:8080/api/v1/workshops/1/appointments/1/status \
  -H "Authorization: Bearer $OWNER_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "status": "CONFIRMED"
  }'

# Respuesta: Cita con status="CONFIRMED"

# ============================================================
# PASO 3B: Iniciar servicio (CONFIRMED → IN_PROGRESS)
# ============================================================
curl -X PATCH http://localhost:8080/api/v1/workshops/1/appointments/1/status \
  -H "Authorization: Bearer $OWNER_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "status": "IN_PROGRESS"
  }'

# Respuesta: Cita con status="IN_PROGRESS"

# ============================================================
# PASO 3C: Completar servicio (IN_PROGRESS → COMPLETED)
# ============================================================
curl -X PATCH http://localhost:8080/api/v1/workshops/1/appointments/1/status \
  -H "Authorization: Bearer $OWNER_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "status": "COMPLETED"
  }'

# Respuesta: Cita con status="COMPLETED"

# ============================================================
# PASO 3D: Cancelar cita (cualquier estado → CANCELLED)
# ============================================================
curl -X PATCH http://localhost:8080/api/v1/workshops/1/appointments/2/status \
  -H "Authorization: Bearer $OWNER_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "status": "CANCELLED"
  }'

# Respuesta: Cita con status="CANCELLED"
# Efecto: Publica AppointmentCanceledEvent
```

### **3.4. Reprogramar Cita**

```bash
# ============================================================
# PASO 4: Cambiar horario de la cita
# ============================================================
curl -X PATCH http://localhost:8080/api/v1/workshops/1/appointments/1 \
  -H "Authorization: Bearer $OWNER_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "scheduledStartAt": "2025-11-16T14:00:00Z",
    "scheduledEndAt": "2025-11-16T15:30:00Z"
  }'

# Respuesta: Cita con nuevo horario
# Efecto: Publica AppointmentRescheduledEvent
```

### **3.5. Agregar Notas Colaborativas**

```bash
# ============================================================
# PASO 5: Agregar nota a la cita
# ============================================================
curl -X POST http://localhost:8080/api/v1/workshops/1/appointments/1/notes \
  -H "Authorization: Bearer $MECHANIC_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "content": "Se encontró desgaste en pastillas de freno delanteras. Se recomienda cambio en próximo servicio.",
    "author": "Carlos Rodríguez"
  }'

# Respuesta: 204 No Content (nota agregada exitosamente)

# ============================================================
# PASO 6: Consultar cita con notas
# ============================================================
curl -X GET http://localhost:8080/api/v1/workshops/1/appointments/1 \
  -H "Authorization: Bearer $OWNER_TOKEN"

# Respuesta: Cita con array de notas incluido
```

### **3.6. Asignar/Desasignar Mecánicos a Citas**

**NUEVOS ENDPOINTS** no documentados en README original:

```bash
# ============================================================
# PASO 7A: Asignar mecánico a cita
# ============================================================
curl -X PATCH http://localhost:8080/api/v1/workshops/1/appointments/1/mechanics/2 \
  -H "Authorization: Bearer $OWNER_TOKEN"

# Respuesta: Cita con mechanicId=2 asignado
# Caso de uso: Asignar mecánico especializado a la cita

# ============================================================
# PASO 7B: Desasignar mecánico específico (con validación)
# ============================================================
curl -X DELETE http://localhost:8080/api/v1/workshops/1/appointments/1/mechanics/2 \
  -H "Authorization: Bearer $OWNER_TOKEN"

# Respuesta: Cita con mechanicId=null
# Validación: El mechanicId en la URL debe coincidir con el asignado
# Caso de uso: Reasignar mecánico por cambio de turno

# ============================================================
# PASO 7C: Desasignar mecánico actual (sin validación)
# ============================================================
curl -X DELETE http://localhost:8080/api/v1/workshops/1/appointments/1/mechanic \
  -H "Authorization: Bearer $OWNER_TOKEN"

# Respuesta: Cita con mechanicId=null
# Diferencia: No requiere saber qué mecánico estaba asignado
# Caso de uso: Eliminar asignación rápida sin validación
```

---

## 📡 **FLUJO 4: Procesamiento de Telemetría IoT (CORE BUSINESS)**

Este es el segundo flujo principal del contexto Workshop: ingestar, procesar y consultar telemetría de vehículos conectados.

### **4.1. Ingestar Telemetría NORMAL (Velocidad + GPS)**

```bash
# ============================================================
# PASO 1: Enviar muestra de telemetría tipo SPEED
# ============================================================
curl -X POST http://localhost:8080/api/v1/telemetry \
  -H "Authorization: Bearer $DRIVER_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "vehicleId": 1,
    "type": "SPEED",
    "severity": "INFO",
    "timestamp": "2025-11-15T10:30:00Z",
    "data": {
      "speed": 65.5,
      "location": {
        "latitude": -12.0464,
        "longitude": -77.0428
      },
      "odometer": 10523.8
    }
  }'

# Respuesta: 201 Created
# Efectos secundarios:
# - Almacena TelemetryRecord en la base de datos
# - Publica TelemetrySampleIngestedEvent
# - Incrementa recordCount del agregado VehicleTelemetry
# - Actualiza lastIngestedAt timestamp
```

### **4.2. Ingestar Telemetría CRÍTICA (Alerta de Falla)**

```bash
# ============================================================
# PASO 2: Enviar alerta CRITICAL con código DTC (diagnóstico)
# ============================================================
curl -X POST http://localhost:8080/api/v1/telemetry \
  -H "Authorization: Bearer $MECHANIC_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "vehicleId": 1,
    "type": "DIAGNOSTIC",
    "severity": "CRITICAL",
    "timestamp": "2025-11-15T14:25:00Z",
    "data": {
      "dtc": "P0301",
      "description": "Cylinder 1 Misfire Detected",
      "location": {
        "latitude": -12.0464,
        "longitude": -77.0428
      },
      "odometer": 10532.1
    }
  }'

# Respuesta: 201 Created
# Código DTC P0301: Falla de encendido en cilindro 1 (misfire)
# Efectos secundarios:
# - Almacena TelemetryRecord con severity=CRITICAL
# - Publica TelemetrySampleIngestedEvent
# - En producción: Debería disparar creación automática de Appointment (lógica pendiente)
```

### **4.3. Consultar Telemetría por Vehículo**

```bash
# ============================================================
# PASO 3: Obtener historial de telemetría por vehículo y rango
# ============================================================
curl -X GET "http://localhost:8080/api/v1/telemetry?vehicleId=1&from=2025-11-15T00:00:00Z&to=2025-11-15T23:59:59Z" \
  -H "Authorization: Bearer $MECHANIC_TOKEN"

# Respuesta: Array de TelemetryRecord con:
# - id, type, severity, timestamp
# - speed, location, odometer, dtc (si aplica)
```

### **4.4. Consultar Alertas Críticas (Predictive Maintenance)**

```bash
# ============================================================
# PASO 4: Obtener alertas CRITICAL para mantenimiento predictivo
# ============================================================
curl -X GET "http://localhost:8080/api/v1/telemetry/alerts?severity=CRITICAL&from=2025-11-15T00:00:00Z&to=2025-11-15T23:59:59Z" \
  -H "Authorization: Bearer $MECHANIC_TOKEN"

# Respuesta: Array de TelemetryRecord filtrados por severity=CRITICAL
# Caso de uso: Dashboard del mecánico para priorizar servicios urgentes

# ============================================================
# PASO 5: Obtener alertas WARNING
# ============================================================
curl -X GET "http://localhost:8080/api/v1/telemetry/alerts?severity=WARNING&from=2025-11-15T00:00:00Z&to=2025-11-15T23:59:59Z" \
  -H "Authorization: Bearer $MECHANIC_TOKEN"

# Caso de uso: Alertas preventivas (temperatura alta, presión baja, etc.)
```

### **4.5. Consultar Registro Específico**

```bash
# ============================================================
# PASO 6: Obtener detalle de un registro de telemetría
# ============================================================
curl -X GET http://localhost:8080/api/v1/telemetry/1 \
  -H "Authorization: Bearer $MECHANIC_TOKEN"

# Respuesta: Detalle completo del TelemetryRecord incluyendo:
# - Timestamp exacto
# - Ubicación GPS
# - Código DTC (si es diagnóstico)
# - Velocidad y odómetro
```

### **4.6. Flush de Registros (Limpieza Masiva)**

```bash
# ============================================================
# PASO 7: Eliminar registros antiguos del agregado
# ============================================================
curl -X DELETE http://localhost:8080/api/v1/telemetry/bulk \
  -H "Authorization: Bearer $OWNER_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "vehicleId": 1,
    "before": "2025-11-01T00:00:00Z"
  }'

# Respuesta: 245 (número de registros eliminados)
# Efecto: Publica TelemetryFlushedEvent
# Caso de uso: Limpieza periódica de datos procesados/archivados
```

---

## 🔗 **FLUJO 5: Integración Completa (Appointment + Telemetry)**

Este flujo demuestra cómo los diferentes componentes del contexto Workshop trabajan juntos.

### **Escenario**: Vehículo con falla crítica detectada por telemetría → Crear cita urgente → Asignar mecánico → Resolver

```bash
# ============================================================
# PASO 1: Vehículo envía alerta crítica (DTC P0420: Catalizador)
# ============================================================
curl -X POST http://localhost:8080/api/v1/telemetry \
  -H "Authorization: Bearer $DRIVER_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "vehicleId": 1,
    "type": "DIAGNOSTIC",
    "severity": "CRITICAL",
    "timestamp": "2025-11-15T16:00:00Z",
    "data": {
      "dtc": "P0420",
      "description": "Catalyst System Efficiency Below Threshold",
      "location": { "latitude": -12.0464, "longitude": -77.0428 },
      "odometer": 10545.3
    }
  }'

# ============================================================
# PASO 2: Mecánico consulta alertas críticas
# ============================================================
curl -X GET "http://localhost:8080/api/v1/telemetry/alerts?severity=CRITICAL&from=2025-11-15T00:00:00Z&to=2025-11-15T23:59:59Z" \
  -H "Authorization: Bearer $MECHANIC_TOKEN"

# Respuesta: Detecta alerta P0420 para vehicleId=1

# ============================================================
# PASO 3: Crear cita urgente para diagnóstico
# ============================================================
curl -X POST http://localhost:8080/api/v1/workshops/1/appointments \
  -H "Authorization: Bearer $OWNER_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "workshopId": 1,
    "vehicleId": 1,
    "driverId": 1,
    "scheduledStartAt": "2025-11-16T08:00:00Z",
    "scheduledEndAt": "2025-11-16T10:00:00Z",
    "description": "URGENTE: Falla crítica P0420 - Catalizador"
  }'

# Respuesta: { "id": 3, "status": "PENDING", ... }

# ============================================================
# PASO 4: Asignar mecánico especializado en electrónica
# ============================================================
curl -X PATCH http://localhost:8080/api/v1/workshops/1/appointments/3/mechanics/2 \
  -H "Authorization: Bearer $OWNER_TOKEN"

# Respuesta: Cita con mechanicId=2 asignado

# ============================================================
# PASO 5: Confirmar cita
# ============================================================
curl -X PATCH http://localhost:8080/api/v1/workshops/1/appointments/3/status \
  -H "Authorization: Bearer $OWNER_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"status": "CONFIRMED"}'

# ============================================================
# PASO 6: Iniciar servicio
# ============================================================
curl -X PATCH http://localhost:8080/api/v1/workshops/1/appointments/3/status \
  -H "Authorization: Bearer $OWNER_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"status": "IN_PROGRESS"}'

# ============================================================
# PASO 7: Agregar notas durante el servicio
# ============================================================
curl -X POST http://localhost:8080/api/v1/workshops/1/appointments/3/notes \
  -H "Authorization: Bearer $MECHANIC_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "content": "Catalizador dañado confirmado. Se reemplazó unidad completa. Código P0420 resuelto.",
    "author": "Carlos Rodríguez"
  }'

# ============================================================
# PASO 8: Completar servicio
# ============================================================
curl -X PATCH http://localhost:8080/api/v1/workshops/1/appointments/3/status \
  -H "Authorization: Bearer $OWNER_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"status": "COMPLETED"}'

# ============================================================
# PASO 9: Vehículo envía telemetría normal (confirma reparación)
# ============================================================
curl -X POST http://localhost:8080/api/v1/telemetry \
  -H "Authorization: Bearer $DRIVER_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "vehicleId": 1,
    "type": "SPEED",
    "severity": "INFO",
    "timestamp": "2025-11-16T11:00:00Z",
    "data": {
      "speed": 60.0,
      "location": { "latitude": -12.0464, "longitude": -77.0428 },
      "odometer": 10555.0
    }
  }'

# Flujo completado: Desde detección de falla hasta resolución
```

---

## � **FLUJO 6: Gestión de Suscripciones y Pagos (Payments Context)**

Este flujo demuestra cómo los dueños de talleres suscriben su negocio a los planes de SafeCar (BASIC, PROFESSIONAL, PREMIUM) usando Stripe como pasarela de pagos.

### **6.1. Verificar Estado del Sistema de Pagos**

```bash
# ============================================================
# PASO 1: Debug endpoint - Verificar configuración
# ============================================================
curl -X GET http://localhost:8080/api/v1/payments/debug \
  -H "Authorization: Bearer $OWNER_TOKEN"

# Respuesta esperada:
# {
#   "status": "Payment controller is working",
#   "timestamp": "2025-11-15T10:00:00",
#   "testUserId": "31303200000000000000000000000000",
#   "availablePlans": ["BASIC", "PROFESSIONAL", "PREMIUM"],
#   "testResponse": {
#     "sessionId": "debug-session-123",
#     "class": "CheckoutSessionResource"
#   }
# }
```

### **6.2. Crear Checkout Session (Iniciar Flujo de Pago)**

```bash
# ============================================================
# PASO 2A: Workshop Owner crea sesión de pago para plan BASIC
# ============================================================
curl -X POST http://localhost:8080/api/v1/payments/checkout-session \
  -H "Authorization: Bearer $OWNER_TOKEN" \
  -H "X-User-Id: owner1@safecar.com" \
  -H 'Content-Type: application/json' \
  -d '{
    "planType": "BASIC"
  }'

# Respuesta esperada:
# {
#   "sessionId": "cs_test_a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0"
# }

# Efectos secundarios:
# - Crea Stripe Checkout Session con metadata:
#   * user_id: owner1@safecar.com
#   * plan_type: BASIC
# - Session configurada con:
#   * Price ID: price_1SQbsT3l890Fc29CerlSwh4r (BASIC plan)
#   * Mode: SUBSCRIPTION (recurrente mensual)
#   * Success URL: http://localhost:8081/payment/success?session_id={CHECKOUT_SESSION_ID}
#   * Cancel URL: http://localhost:8081/payment/cancel

# ============================================================
# PASO 2B: Frontend redirige al usuario a Stripe Checkout
# ============================================================
# URL de Stripe Checkout (construida en frontend):
# https://checkout.stripe.com/c/pay/{sessionId}

# Usuario completa el pago en Stripe (ingresa datos de tarjeta)
# Stripe procesa el pago y crea la subscription
```

### **6.3. Webhook - Stripe Notifica Creación de Suscripción**

**⚠️ IMPORTANTE**: Este paso es automático, NO requiere llamada manual.

```bash
# ============================================================
# PASO 3: Stripe envía webhook al backend (automático)
# ============================================================
# POST http://localhost:8080/webhooks/stripe
# Headers:
#   Stripe-Signature: t=xxx,v1=yyy (firma HMAC del payload)
# Body (JSON):
#   {
#     "type": "customer.subscription.created",
#     "data": {
#       "object": {
#         "id": "sub_1SQbsT3l890Fc29CerlSwh4r",
#         "customer": "cus_xxx",
#         "status": "active",
#         "metadata": {
#           "user_id": "owner1@safecar.com",
#           "plan_type": "BASIC"
#         }
#       }
#     }
#   }

# Backend procesa webhook:
# 1. Verifica firma HMAC con webhook-secret
# 2. Extrae metadata: user_id, plan_type
# 3. Crea CreateSubscriptionCommand
# 4. Persiste Subscription en base de datos:
#    - userId: owner1@safecar.com
#    - stripeSubscriptionId: sub_1SQbsT3l890Fc29CerlSwh4r
#    - planType: BASIC
#    - status: ACTIVE
# 5. Responde 200 OK a Stripe

# Efectos en el sistema:
# - Workshop Owner ahora tiene subscripción ACTIVE
# - Puede agregar hasta 3 mecánicos (límite del plan BASIC)
```

### **6.4. Consultar Suscripción del Usuario**

```bash
# ============================================================
# PASO 4: Obtener suscripción activa del workshop owner
# ============================================================
# Nota: Este endpoint aún no está implementado en PaymentsController
# pero la lógica existe en PaymentQueryService

# Implementación futura esperada:
# GET /api/v1/payments/subscription?userId=owner1@safecar.com

# Workaround actual: Consultar directamente en base de datos
# SELECT * FROM subscriptions WHERE user_id = 'owner1@safecar.com' AND status = 'ACTIVE';

# Respuesta esperada (estructura SubscriptionResource):
# {
#   "id": 1,
#   "userId": "owner1@safecar.com",
#   "planType": "BASIC",
#   "status": "ACTIVE",
#   "stripeSubscriptionId": "sub_1SQbsT3l890Fc29CerlSwh4r"
# }
```

### **6.5. Upgrade de Plan (BASIC → PROFESSIONAL)**

```bash
# ============================================================
# PASO 5: Workshop Owner mejora su plan
# ============================================================
curl -X POST http://localhost:8080/api/v1/payments/checkout-session \
  -H "Authorization: Bearer $OWNER_TOKEN" \
  -H "X-User-Id: owner1@safecar.com" \
  -H 'Content-Type: application/json' \
  -d '{
    "planType": "PROFESSIONAL"
  }'

# Respuesta: Nueva sessionId para plan PROFESSIONAL
# Frontend redirige a Stripe Checkout
# Usuario completa pago
# Webhook crea NUEVA subscription (Stripe maneja la cancelación de la anterior)

# Nueva capacidad:
# - Límite de mecánicos: 10 (antes 3)
# - Price ID: price_1SQbt23l890Fc29CqoqLYCnu
```

### **6.6. Plan PREMIUM (Máxima Capacidad)**

```bash
# ============================================================
# PASO 6: Workshop Owner selecciona plan empresarial
# ============================================================
curl -X POST http://localhost:8080/api/v1/payments/checkout-session \
  -H "Authorization: Bearer $OWNER_TOKEN" \
  -H "X-User-Id: owner1@safecar.com" \
  -H 'Content-Type: application/json' \
  -d '{
    "planType": "PREMIUM"
  }'

# Plan PREMIUM incluye:
# - Límite de mecánicos: 30
# - Price ID: price_1SQbtK3l890Fc29COSEZ6iK4
# - Facturación mensual recurrente vía Stripe
```

### **6.7. Testing sin Stripe (Endpoint de Prueba)**

**⚠️ Solo para desarrollo - Deshabilitar en producción**

```bash
# ============================================================
# PASO 7: Crear sesión de prueba sin autenticación
# ============================================================
curl -X POST http://localhost:8080/api/v1/payments/test-session \
  -H 'Content-Type: application/json'

# Respuesta:
# "Session created: cs_test_xxxxxxxxxxxxx"

# Crea checkout session con userId hardcodeado: "test-user-123"
# Plan por defecto: BASIC
# No requiere autenticación (útil para debugging)
```

---

## 🔗 **FLUJO 7: Integración Workshop + Payments (Enforcement de Límites)**

Este flujo demuestra cómo el plan de suscripción afecta las operaciones del Workshop Context.

### **Escenario**: Workshop Owner con plan BASIC intenta agregar más de 3 mecánicos

```bash
# ============================================================
# CONTEXTO: Workshop Owner tiene subscription BASIC activa
# ============================================================
# Subscription actual:
# - planType: BASIC
# - mechanicsLimit: 3
# - status: ACTIVE

# ============================================================
# PASO 1: Agregar 1er mecánico (✅ Permitido)
# ============================================================
curl -X POST "http://localhost:8080/api/v1/person-profiles?userEmail=mechanic1@safecar.com" \
  -H "Authorization: Bearer $MECHANIC_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "firstName": "Carlos",
    "lastName": "Rodríguez",
    "email": "mechanic1@safecar.com",
    "phone": "+51987654321",
    "city": "Lima"
  }'

# Respuesta: 201 Created
# totalMechanics del Workshop: 1/3

# ============================================================
# PASO 2: Agregar 2do mecánico (✅ Permitido)
# ============================================================
curl -X POST "http://localhost:8080/api/v1/person-profiles?userEmail=mechanic2@safecar.com" \
  -H "Authorization: Bearer $MECHANIC2_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "firstName": "Ana",
    "lastName": "Torres",
    "email": "mechanic2@safecar.com",
    "phone": "+51987654322",
    "city": "Lima"
  }'

# Respuesta: 201 Created
# totalMechanics del Workshop: 2/3

# ============================================================
# PASO 3: Agregar 3er mecánico (✅ Permitido - Límite alcanzado)
# ============================================================
curl -X POST "http://localhost:8080/api/v1/person-profiles?userEmail=mechanic3@safecar.com" \
  -H "Authorization: Bearer $MECHANIC3_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "firstName": "Luis",
    "lastName": "Martínez",
    "email": "mechanic3@safecar.com",
    "phone": "+51987654323",
    "city": "Lima"
  }'

# Respuesta: 201 Created
# totalMechanics del Workshop: 3/3 (LÍMITE ALCANZADO)

# ============================================================
# PASO 4: Intentar agregar 4to mecánico (❌ BLOQUEADO)
# ============================================================
curl -X POST "http://localhost:8080/api/v1/person-profiles?userEmail=mechanic4@safecar.com" \
  -H "Authorization: Bearer $MECHANIC4_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "firstName": "Pedro",
    "lastName": "Sánchez",
    "email": "mechanic4@safecar.com",
    "phone": "+51987654324",
    "city": "Lima"
  }'

# Respuesta esperada (implementación futura):
# 403 Forbidden
# {
#   "error": "Subscription limit exceeded",
#   "message": "Your BASIC plan allows up to 3 mechanics. Upgrade to PROFESSIONAL for 10 mechanics.",
#   "currentPlan": "BASIC",
#   "currentLimit": 3,
#   "suggestedPlan": "PROFESSIONAL"
# }

# ============================================================
# PASO 5: Upgrade a plan PROFESSIONAL
# ============================================================
curl -X POST http://localhost:8080/api/v1/payments/checkout-session \
  -H "Authorization: Bearer $OWNER_TOKEN" \
  -H "X-User-Id: owner1@safecar.com" \
  -H 'Content-Type: application/json' \
  -d '{
    "planType": "PROFESSIONAL"
  }'

# Usuario completa pago en Stripe
# Webhook actualiza subscription:
# - planType: PROFESSIONAL
# - mechanicsLimit: 10

# ============================================================
# PASO 6: Reintentar agregar 4to mecánico (✅ PERMITIDO)
# ============================================================
curl -X POST "http://localhost:8080/api/v1/person-profiles?userEmail=mechanic4@safecar.com" \
  -H "Authorization: Bearer $MECHANIC4_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "firstName": "Pedro",
    "lastName": "Sánchez",
    "email": "mechanic4@safecar.com",
    "phone": "+51987654324",
    "city": "Lima"
  }'

# Respuesta: 201 Created
# totalMechanics del Workshop: 4/10
```

---

## �📊 **Resumen de Endpoints Implementados**

| **Contexto** | **Controlador** | **Base Path** | **Endpoints** |
|--------------|----------------|---------------|---------------|
| **IAM** | Authentication | `/api/v1/authentication` | 2 |
| | Users | `/api/v1/users` | 2 |
| | Roles | `/api/v1/roles` | 1 |
| **Profiles** | PersonProfiles | `/api/v1/person-profiles` | 3 |
| | BusinessProfiles | `/api/v1/business-profiles` | 3 |
| **Devices** | Vehicles | `/api/v1/` | 3 |
| **Workshop** | Workshops | `/api/v1/workshops` | 2 |
| | Mechanics | `/api/v1/mechanics` | 2 |
| | Appointments | `/api/v1/workshops/{id}/appointments` | 9 |
| | Telemetry | `/api/v1/telemetry` | 6 |
| **Payments** | Payments | `/api/v1/payments` | 3 |
| | Webhooks | `/webhooks/stripe` | 1 |

**Total**: 37 endpoints REST + 1 webhook

---

## 🔒 **Detalles Técnicos del Payments Context**

### **Arquitectura DDD/CQRS Implementada**

```
payments/
├── domain/
│   ├── model/
│   │   ├── aggregates/
│   │   │   └── Subscription.java          // Aggregate Root (@Entity)
│   │   ├── commands/
│   │   │   ├── CreateCheckoutSessionCommand.java
│   │   │   └── CreateSubscriptionCommand.java
│   │   ├── queries/
│   │   │   └── GetSubscriptionByUserIdQuery.java
│   │   └── valueobjects/
│   │       └── PlanType.java               // Enum (BASIC/PROFESSIONAL/PREMIUM)
│   └── services/
│       ├── PaymentCommandService.java      // Interface
│       └── PaymentQueryService.java        // Interface
├── application/
│   └── internal/
│       ├── commandservices/
│       │   └── PaymentCommandServiceImpl.java  // @Transactional
│       └── queryservices/
│           └── PaymentQueryServiceImpl.java
├── infrastructure/
│   ├── external/
│   │   └── StripePaymentGateway.java       // Stripe SDK integration
│   └── persistence/jpa/repositories/
│       └── SubscriptionRepository.java     // JpaRepository<Subscription, Long>
└── interfaces/rest/
    ├── resources/
    │   ├── CreateCheckoutSessionResource.java   // Request DTO
    │   ├── CheckoutSessionResource.java         // Response DTO
    │   └── SubscriptionResource.java            // Subscription DTO
    ├── transform/
    │   ├── CreateCheckoutSessionCommandFromResourceAssembler.java
    │   ├── CheckoutSessionResourceFromSessionIdAssembler.java
    │   └── SubscriptionResourceFromAggregateAssembler.java
    ├── PaymentsController.java            // REST API
    └── StripeWebhooksController.java      // Webhook handler
```

### **Subscription Aggregate (Domain Model)**

```java
@Entity
@Table(name = "subscriptions")
public class Subscription extends AuditableAbstractAggregateRoot<Subscription> {
    private String userId;                    // Owner email
    
    @Enumerated(EnumType.STRING)
    private PlanType planType;                // BASIC/PROFESSIONAL/PREMIUM
    
    private String status;                    // ACTIVE/CANCELLED/EXPIRED
    
    @Column(unique = true)
    private String stripeSubscriptionId;      // Stripe reference
    
    // Domain methods
    public void cancel() { this.status = "CANCELLED"; }
    public void activate() { this.status = "ACTIVE"; }
    public void expire() { this.status = "EXPIRED"; }
    public boolean isActive() { return "ACTIVE".equals(this.status); }
    public int getMechanicsLimit() { return this.planType.getMechanicsLimit(); }
}
```

### **PlanType Value Object**

```java
public enum PlanType {
    BASIC("price_1SQbsT3l890Fc29CerlSwh4r", 3),
    PROFESSIONAL("price_1SQbt23l890Fc29CqoqLYCnu", 10),
    PREMIUM("price_1SQbtK3l890Fc29COSEZ6iK4", 30);

    private final String stripePriceId;     // Stripe Price ID
    private final int mechanicsLimit;       // Business rule
    
    public String getStripePriceId() { return stripePriceId; }
    public int getMechanicsLimit() { return mechanicsLimit; }
}
```

### **Flujo de Datos - Checkout Session**

```
1. POST /api/v1/payments/checkout-session
   → PaymentsController.createCheckoutSession()
   
2. Validación @Valid CreateCheckoutSessionResource
   
3. CreateCheckoutSessionCommandFromResourceAssembler
   → CreateCheckoutSessionCommand(userId, PlanType)
   
4. PaymentCommandService.handle(command)
   → PaymentCommandServiceImpl.handle(command)
   
5. StripePaymentGateway.createCheckoutSession(userId, planType)
   → Stripe API: POST /v1/checkout/sessions
     * mode: subscription
     * metadata: { user_id, plan_type }
     * line_items: [{ price: planType.stripePriceId }]
     * success_url: {FRONTEND_URL}/payment/success
     * cancel_url: {FRONTEND_URL}/payment/cancel
   
6. Stripe Response: { id: "cs_test_xxx" }
   
7. CheckoutSessionResourceFromSessionIdAssembler
   → CheckoutSessionResource(sessionId)
   
8. Response 200 OK { "sessionId": "cs_test_xxx" }
```

### **Flujo de Datos - Webhook Subscription Created**

```
1. POST /webhooks/stripe (Stripe → Backend)
   Headers: Stripe-Signature: t=xxx,v1=yyy
   Body: { type: "customer.subscription.created", data: {...} }
   
2. StripeWebhooksController.handleWebhook(payload, signature)
   
3. Webhook.constructEvent(payload, signature, webhookSecret)
   → Verifica firma HMAC SHA-256
   → Si falla: Response 400 "Invalid signature"
   
4. Extrae metadata del evento:
   - userId = subscription.metadata.get("user_id")
   - planType = PlanType.valueOf(subscription.metadata.get("plan_type"))
   - stripeSubscriptionId = subscription.id
   
5. CreateSubscriptionCommand(userId, stripeSubscriptionId, planType)
   
6. PaymentCommandService.handle(command)
   → @Transactional
   → new Subscription(command)
   → subscriptionRepository.save(subscription)
   
7. Response 200 OK (Stripe marca webhook como exitoso)
```

### **Tabla de Base de Datos**

```sql
CREATE TABLE subscriptions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    plan_type VARCHAR(20) NOT NULL,  -- 'BASIC', 'PROFESSIONAL', 'PREMIUM'
    status VARCHAR(20) NOT NULL,     -- 'ACTIVE', 'CANCELLED', 'EXPIRED'
    stripe_subscription_id VARCHAR(255) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_stripe_subscription_id (stripe_subscription_id),
    INDEX idx_status (status)
);
```

### **Queries JPA Disponibles**

```java
// SubscriptionRepository.java
Optional<Subscription> findByUserId(String userId);
Optional<Subscription> findByStripeSubscriptionId(String stripeSubscriptionId);
boolean existsByUserIdAndStatus(String userId, String status);
```

### **Manejo de Errores**

| **Escenario** | **HTTP Status** | **Respuesta** |
|---------------|-----------------|---------------|
| Stripe API error | 500 | `{"error": "Failed to create checkout session: {message}"}` |
| Invalid signature (webhook) | 400 | `"Invalid signature"` |
| Webhook processing error | 400 | `"Error processing webhook"` |
| Invalid plan type | 400 | Validación de `@Valid` (Spring) |
| Duplicate subscription | 500 | DataIntegrityViolationException (unique constraint) |

### **Seguridad y Validaciones**

1. **Webhook Signature Verification**: Usa HMAC SHA-256 con `webhook-secret`
2. **HTTPS Required**: En producción, webhooks requieren HTTPS
3. **Idempotency**: Stripe reintenta webhooks fallidos (hasta 3 días)
4. **Metadata Validation**: Fallback a "BASIC" si `plan_type` falta
5. **User ID Validation**: Header `X-User-Id` debe coincidir con usuario autenticado

### **Limitaciones Conocidas**

1. **No hay endpoint GET /subscription**: Query service implementado pero no expuesto en controller
2. **No hay cancelación manual**: Las cancelaciones deben hacerse en Stripe Dashboard
3. **No hay manejo de upgrades/downgrades**: Stripe crea nueva subscription, pero no se cancela la anterior automáticamente
4. **Enforcement de límites pendiente**: Workshop Context aún no valida `mechanicsLimit` antes de agregar mecánicos
5. **Test endpoint en producción**: `/test-session` debe deshabilitarse con profiles

### **Mejoras Futuras Sugeridas**

- [ ] Implementar endpoint `GET /api/v1/payments/subscription?userId={userId}`
- [ ] Implementar endpoint `DELETE /api/v1/payments/subscription/{id}` (cancela en Stripe)
- [ ] Agregar webhook handler para `customer.subscription.updated` (cambios de plan)
- [ ] Agregar webhook handler para `customer.subscription.deleted` (expiración)
- [ ] Implementar validación en Workshop Context: verificar `subscription.mechanicsLimit` antes de crear Mechanic
- [ ] Agregar Spring Profile para deshabilitar `/test-session` en producción
- [ ] Implementar retry logic en webhook processing (idempotency keys)
- [ ] Agregar métricas de Stripe (total_revenue, active_subscriptions, churn_rate)

---

---

## 💳 **FLUJO 6: Gestión de Pagos y Suscripciones (Payments Context)**

Este flujo demuestra la integración con Stripe para gestionar suscripciones de talleres.

### **6.1. Verificar Sistema de Pagos (Debug)**

```bash
# ============================================================
# PASO 1: Verificar que el sistema de pagos esté funcionando
# ============================================================
curl -X GET http://localhost:8080/api/v1/payments/debug

# Respuesta esperada:
# {
#   "status": "Payment controller is working",
#   "timestamp": "2025-11-12T04:45:00",
#   "testUserId": "31303200000000000000000000000000",
#   "availablePlans": ["BASIC", "PROFESSIONAL", "PREMIUM"],
#   "testResponse": {
#     "sessionId": "debug-session-123",
#     "class": "com.safecar.platform.payments.application.dtos.CheckoutSessionResponse"
#   }
# }
```

### **6.2. Crear Sesión de Checkout (Testing)**

```bash
# ============================================================
# PASO 2: Crear sesión de prueba sin autenticación
# ============================================================
curl -X POST http://localhost:8080/api/v1/payments/test-session

# Respuesta esperada:
# "Session created: cs_test_a1b2c3d4..."
# 
# Nota: Este endpoint es para testing y usa valores predeterminados
```

### **6.3. Crear Sesión de Checkout (Producción)**

```bash
# ============================================================
# PASO 3: Crear sesión de checkout para suscripción BASIC
# ============================================================
curl -X POST http://localhost:8080/api/v1/payments/checkout-session \
  -H "Content-Type: application/json" \
  -H "X-User-Id: 3" \
  -d '{
    "planType": "BASIC"
  }'

# Respuesta esperada:
# {
#   "sessionId": "cs_test_a1B2c3D4e5F6g7H8i9J0k1L2m3N4o5P6"
# }

# ============================================================
# PASO 4: Redirigir al usuario a Stripe Checkout
# ============================================================
# En tu frontend, usa el sessionId para redirigir a Stripe:
# https://checkout.stripe.com/pay/{sessionId}
#
# El usuario completará el pago en Stripe y será redirigido a:
# - Success: http://localhost:8081/payment/success?session_id={CHECKOUT_SESSION_ID}
# - Cancel: http://localhost:8081/payment/cancel
```

### **6.4. Crear Sesiones para Otros Planes**

```bash
# ============================================================
# PASO 5A: Suscripción PROFESSIONAL (hasta 10 mecánicos)
# ============================================================
curl -X POST http://localhost:8080/api/v1/payments/checkout-session \
  -H "Content-Type: application/json" \
  -H "X-User-Id: 3" \
  -d '{
    "planType": "PROFESSIONAL"
  }'

# ============================================================
# PASO 5B: Suscripción PREMIUM (hasta 30 mecánicos)
# ============================================================
curl -X POST http://localhost:8080/api/v1/payments/checkout-session \
  -H "Content-Type: application/json" \
  -H "X-User-Id: 3" \
  -d '{
    "planType": "PREMIUM"
  }'
```

### **6.5. Webhook de Stripe (Procesamiento Automático)**

```bash
# ============================================================
# PASO 6: Stripe envía webhook cuando la suscripción se crea
# ============================================================
# Este endpoint es llamado automáticamente por Stripe, no manualmente
# POST /webhooks/stripe
# 
# Headers enviados por Stripe:
# - Content-Type: application/json
# - Stripe-Signature: t=1234567890,v1=abc123...
#
# Eventos manejados:
# - customer.subscription.created: Cuando el usuario completa el pago
# - customer.subscription.updated: Cuando cambia el plan
# - customer.subscription.deleted: Cuando se cancela la suscripción
#
# El webhook:
# 1. Verifica la firma de Stripe (seguridad)
# 2. Extrae metadata.user_id del evento
# 3. Crea registro de Subscription en la BD
# 4. Asocia la suscripción con el Workshop Owner

# ============================================================
# PASO 7: Configurar webhook en Stripe Dashboard
# ============================================================
# 1. Ir a https://dashboard.stripe.com/test/webhooks
# 2. Click "Add endpoint"
# 3. URL: https://your-domain.com/webhooks/stripe (o usar ngrok para local)
# 4. Eventos a escuchar:
#    - customer.subscription.created
#    - customer.subscription.updated
#    - customer.subscription.deleted
# 5. Copiar "Signing secret" (whsec_...) y configurarlo en STRIPE_WEBHOOK_SECRET
```

### **6.6. Flujo Completo de Suscripción**

**Escenario**: Workshop Owner se suscribe al plan PROFESSIONAL

```bash
# ============================================================
# Integración Frontend → Backend → Stripe → Webhook
# ============================================================

# 1. Frontend: Usuario selecciona plan PROFESSIONAL
# 2. Frontend: Llama al backend para crear sesión
OWNER_ID=3  # ID del workshop owner

SESSION_RESPONSE=$(curl -s -X POST http://localhost:8080/api/v1/payments/checkout-session \
  -H "Content-Type: application/json" \
  -H "X-User-Id: $OWNER_ID" \
  -d '{"planType": "PROFESSIONAL"}')

SESSION_ID=$(echo $SESSION_RESPONSE | jq -r '.sessionId')
echo "Stripe Session ID: $SESSION_ID"

# 3. Frontend: Redirige a Stripe Checkout
# window.location.href = `https://checkout.stripe.com/pay/${SESSION_ID}`

# 4. Usuario: Completa pago en Stripe
# - Ingresa datos de tarjeta
# - Stripe procesa el pago
# - Stripe crea la suscripción

# 5. Stripe: Envía webhook a /webhooks/stripe
# - Evento: customer.subscription.created
# - Metadata: { user_id: "3", plan: "PROFESSIONAL" }

# 6. Backend: Procesa webhook
# - Verifica firma de Stripe
# - Crea registro en tabla subscriptions
# - Asocia suscripción con Workshop Owner ID 3

# 7. Stripe: Redirige al usuario
# - Success: http://localhost:8081/payment/success?session_id=cs_test_...
# - Cancel: http://localhost:8081/payment/cancel

# 8. Resultado final:
# - Workshop puede registrar hasta 10 mecánicos (límite PROFESSIONAL)
# - Suscripción activa en Stripe y BD local
# - Renovación automática mensual
```

### **6.7. Testing con Tarjetas de Prueba de Stripe**

```bash
# ============================================================
# Tarjetas de prueba para diferentes escenarios
# ============================================================

# Pago exitoso:
# Número: 4242 4242 4242 4242
# Fecha: Cualquier fecha futura
# CVC: Cualquier 3 dígitos
# ZIP: Cualquier 5 dígitos

# Pago rechazado (insufficient funds):
# Número: 4000 0000 0000 9995

# Pago rechazado (generic decline):
# Número: 4000 0000 0000 0002

# Requiere autenticación 3D Secure:
# Número: 4000 0027 6000 3184

# Más tarjetas de prueba: https://stripe.com/docs/testing
```

---

## 🔧 **Configuración de Stripe (Setup Inicial)**

### **Prerequisitos para Pagos**

1. **Cuenta de Stripe (Test Mode)**
   ```bash
   # 1. Crear cuenta en https://stripe.com
   # 2. Activar modo test (toggle en dashboard)
   # 3. Obtener Secret Key: https://dashboard.stripe.com/test/apikeys
   # 4. Copiar: sk_test_51... → STRIPE_SECRET_KEY
   ```

2. **Crear Productos y Precios en Stripe**
   ```bash
   # Dashboard → Products → Add Product
   
   # Producto 1: SafeCar Basic
   # - Precio: $29/mes (o tu moneda)
   # - Recurring: Monthly
   # - Copiar Price ID → plans.basic.price-id
   
   # Producto 2: SafeCar Professional
   # - Precio: $79/mes
   # - Recurring: Monthly
   # - Copiar Price ID → plans.professional.price-id
   
   # Producto 3: SafeCar Premium
   # - Precio: $149/mes
   # - Recurring: Monthly
   # - Copiar Price ID → plans.premium.price-id
   ```

3. **Configurar Webhook**
   ```bash
   # Para desarrollo local con ngrok:
   ngrok http 8080
   
   # Copiar URL HTTPS generada, ejemplo:
   # https://abc123.ngrok.io
   
   # Dashboard → Webhooks → Add endpoint
   # URL: https://abc123.ngrok.io/webhooks/stripe
   # Eventos: customer.subscription.*
   # Copiar Signing Secret → STRIPE_WEBHOOK_SECRET
   ```

4. **Variables de Entorno**
   ```bash
   # Agregar a tu .env o configuración del sistema:
   export STRIPE_SECRET_KEY="sk_test_51..."
   export STRIPE_WEBHOOK_SECRET="whsec_test_..."
   export FRONTEND_URL="http://localhost:8081"
   ```

### **Arquitectura de Integración**

```
┌─────────────┐         ┌──────────────┐         ┌─────────────┐
│   Frontend  │────1───▶│   Backend    │────2───▶│   Stripe    │
│  (Angular)  │         │ (Spring Boot)│         │     API     │
└─────────────┘         └──────────────┘         └─────────────┘
       ▲                        ▲                        │
       │                        │                        │
       │                        └────────4───────────────┘
       │                            (Webhook)
       └────────────3────────────────────┘
          (Redirect after payment)

Flujo:
1. Usuario selecciona plan → Backend crea Stripe Checkout Session
2. Backend llama a Stripe API → Stripe devuelve sessionId
3. Frontend redirige a Stripe → Usuario paga → Stripe redirige de vuelta
4. Stripe envía webhook → Backend guarda suscripción en BD
```

---

## 📚 **Documentación API (Swagger)**

Todos los endpoints están documentados con OpenAPI 3.0 y disponibles en Swagger UI:

```bash
# Acceder a la documentación interactiva:
open http://localhost:8080/swagger-ui.html

# Ver especificación OpenAPI JSON:
curl http://localhost:8080/v3/api-docs
```

### **Tags Organizados por Contexto**

- **Authentication**: Sign up, Sign in
- **Users**: User management
- **Roles**: Role queries
- **Person Profiles**: Driver/Mechanic profiles
- **Business Profiles**: Workshop owner profiles
- **Vehicles**: Vehicle registration and management
- **Workshops**: Workshop management
- **Mechanics**: Mechanic operations
- **Appointments**: Appointment lifecycle
- **Telemetry**: IoT telemetry ingestion and queries
- **Payments**: Stripe checkout sessions
- **Stripe Webhooks**: Payment event handling

---
| | Appointments | `/api/v1/workshops/{wid}/appointments` | 9 |
| | Telemetries | `/api/v1/telemetry` | 5 |
| | Mechanics | `/api/v1/mechanics` | 1 |
| **TOTAL** | **10 controladores** | | **31 endpoints** |

---

## 📝 **Notas sobre Agregados del Dominio**

### Agregados Implementados:
1. **User** (IAM Context)
2. **Role** (IAM Context)
3. **PersonProfile** (Profiles Context)
4. **BusinessProfile** (Profiles Context)
5. **Driver** (Devices Context)
6. **Vehicle** (Devices Context)
7. **Workshop** (Workshop Context)
8. **Mechanic** (Workshop Context)
9. **Appointment** (Workshop Context)
10. **VehicleTelemetry** (Workshop Context)

### Agregados NO Implementados:
- ❌ **ServiceOrder** (mencionado en documentación original pero no existe)
- ❌ **WorkshopOperation** (mencionado en documentación original pero no existe)

---

## 🚀 **Scripts de Prueba Automatizados**

### Script Bash Completo (setup_safecar.sh)

```bash
#!/bin/bash
# setup_safecar.sh - Script de prueba completo para SafeCar Platform

BASE_URL="http://localhost:8080"
echo "🚗 SafeCar Platform - Test Suite"
echo "================================="

# Colores para output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Función para imprimir mensajes
print_step() {
    echo -e "${YELLOW}▶ $1${NC}"
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

# ============================================================
# FASE 1: REGISTRO DE USUARIOS
# ============================================================
print_step "FASE 1: Registrando usuarios..."

# Driver
DRIVER_RESPONSE=$(curl -s -X POST $BASE_URL/api/v1/authentication/sign-up \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "driver1@safecar.com",
    "password": "Driver123!",
    "roles": ["ROLE_DRIVER"]
  }')

if echo "$DRIVER_RESPONSE" | grep -q "id"; then
    print_success "Driver registrado"
else
    print_error "Error al registrar driver"
    exit 1
fi

# Mechanic
curl -s -X POST $BASE_URL/api/v1/authentication/sign-up \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "mechanic1@safecar.com",
    "password": "Mechanic123!",
    "roles": ["ROLE_MECHANIC"]
  }' > /dev/null

print_success "Mechanic registrado"

# Owner
curl -s -X POST $BASE_URL/api/v1/authentication/sign-up \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "owner1@safecar.com",
    "password": "Owner123!",
    "roles": ["ROLE_WORKSHOP"]
  }' > /dev/null

print_success "Owner registrado"

# ============================================================
# FASE 2: AUTENTICACIÓN
# ============================================================
print_step "FASE 2: Obteniendo tokens JWT..."

DRIVER_TOKEN=$(curl -s -X POST $BASE_URL/api/v1/authentication/sign-in \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "driver1@safecar.com",
    "password": "Driver123!"
  }' | jq -r '.token')

MECHANIC_TOKEN=$(curl -s -X POST $BASE_URL/api/v1/authentication/sign-in \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "mechanic1@safecar.com",
    "password": "Mechanic123!"
  }' | jq -r '.token')

OWNER_TOKEN=$(curl -s -X POST $BASE_URL/api/v1/authentication/sign-in \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "owner1@safecar.com",
    "password": "Owner123!"
  }' | jq -r '.token')

print_success "Tokens obtenidos"

# ============================================================
# FASE 3: CREAR PERFILES
# ============================================================
print_step "FASE 3: Creando perfiles..."

# Person Profile - Driver
curl -s -X POST "$BASE_URL/api/v1/person-profiles?userEmail=driver1@safecar.com" \
  -H "Authorization: Bearer $DRIVER_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "firstName": "Juan",
    "lastName": "Pérez",
    "street": "Av. Arequipa 1234",
    "city": "Lima",
    "state": "Lima",
    "country": "Peru",
    "dni": "12345678"
  }' > /dev/null

print_success "Person Profile (Driver) creado"

# Person Profile - Mechanic
curl -s -X POST "$BASE_URL/api/v1/person-profiles?userEmail=mechanic1@safecar.com" \
  -H "Authorization: Bearer $MECHANIC_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "firstName": "Carlos",
    "lastName": "Rodríguez",
    "street": "Jr. Lampa 456",
    "city": "Lima",
    "state": "Lima",
    "country": "Peru",
    "dni": "87654321"
  }' > /dev/null

print_success "Person Profile (Mechanic) creado"

# Business Profile - Owner
curl -s -X POST "$BASE_URL/api/v1/business-profiles?userEmail=owner1@safecar.com" \
  -H "Authorization: Bearer $OWNER_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "firstName": "María",
    "lastName": "González",
    "street": "Av. Venezuela 789",
    "city": "Lima",
    "state": "Lima",
    "country": "Peru",
    "dni": "11223344"
  }' > /dev/null

print_success "Business Profile creado"

# ============================================================
# FASE 4: REGISTRAR VEHÍCULO
# ============================================================
print_step "FASE 4: Registrando vehículo..."

VEHICLE_RESPONSE=$(curl -s -X POST $BASE_URL/api/v1/vehicles \
  -H "Authorization: Bearer $DRIVER_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "driverId": 1,
    "licensePlate": "ABC-123",
    "brand": "Toyota",
    "model": "Corolla",
    "year": 2023
  }')

if echo "$VEHICLE_RESPONSE" | grep -q "id"; then
    VEHICLE_ID=$(echo "$VEHICLE_RESPONSE" | jq -r '.id')
    print_success "Vehículo registrado (ID: $VEHICLE_ID)"
else
    print_error "Error al registrar vehículo"
fi

# ============================================================
# FASE 5: CREAR CITA
# ============================================================
print_step "FASE 5: Creando cita..."

APPOINTMENT_RESPONSE=$(curl -s -X POST $BASE_URL/api/v1/workshops/1/appointments \
  -H "Authorization: Bearer $OWNER_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "workshopId": 1,
    "vehicleId": 1,
    "driverId": 1,
    "scheduledStartAt": "2025-11-20T10:00:00Z",
    "scheduledEndAt": "2025-11-20T11:30:00Z",
    "description": "Mantenimiento preventivo de 10,000 km"
  }')

if echo "$APPOINTMENT_RESPONSE" | grep -q "id"; then
    APPOINTMENT_ID=$(echo "$APPOINTMENT_RESPONSE" | jq -r '.id')
    print_success "Cita creada (ID: $APPOINTMENT_ID)"
else
    print_error "Error al crear cita"
fi

# ============================================================
# FASE 6: ENVIAR TELEMETRÍA
# ============================================================
print_step "FASE 6: Enviando telemetría..."

curl -s -X POST $BASE_URL/api/v1/telemetry \
  -H "Authorization: Bearer $DRIVER_TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{
    "vehicleId": 1,
    "type": "SPEED",
    "severity": "INFO",
    "timestamp": "2025-11-15T10:30:00Z",
    "data": {
      "speed": 65.5,
      "location": {"latitude": -12.0464, "longitude": -77.0428},
      "odometer": 10523.8
    }
  }' > /dev/null

print_success "Telemetría enviada"

echo ""
echo -e "${GREEN}=================================${NC}"
echo -e "${GREEN}✓ Setup completado exitosamente${NC}"
echo -e "${GREEN}=================================${NC}"
echo ""
echo "Tokens guardados:"
echo "  DRIVER_TOKEN=$DRIVER_TOKEN"
echo "  MECHANIC_TOKEN=$MECHANIC_TOKEN"
echo "  OWNER_TOKEN=$OWNER_TOKEN"
echo ""
echo "IDs creados:"
echo "  VEHICLE_ID=$VEHICLE_ID"
echo "  APPOINTMENT_ID=$APPOINTMENT_ID"
```

**Uso del script:**
```bash
chmod +x setup_safecar.sh
./setup_safecar.sh
```

---

## 📚 **Recursos Adicionales**

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs
- **Health Check:** http://localhost:8080/actuator/health

---

## ⚠️ **Diferencias con Versión Anterior del README**

### Endpoints Eliminados (No implementados):
1. ❌ `POST /api/v1/workshops` - Crear workshop
2. ❌ Todos los endpoints de `/api/v1/workshops/{wid}/service-orders` (4 endpoints)
3. ❌ Todos los endpoints de `/api/v1/workshop-operations` (3 endpoints)

### Endpoints Agregados (Implementados pero no documentados):
1. ✅ `GET /api/v1/users/{email}` - Obtener usuario por email
2. ✅ `PUT /api/v1/person-profiles/{id}` - Actualizar perfil de persona
3. ✅ `PUT /api/v1/business-profiles/{id}` - Actualizar perfil de negocio
4. ✅ `PATCH /api/v1/workshops/{wid}/appointments/{id}/mechanics/{mid}` - Asignar mecánico
5. ✅ `DELETE /api/v1/workshops/{wid}/appointments/{id}/mechanics/{mid}` - Desasignar mecánico específico
6. ✅ `DELETE /api/v1/workshops/{wid}/appointments/{id}/mechanic` - Desasignar mecánico actual

### Rutas Corregidas:
- `/api/v1/profiles` → `/api/v1/person-profiles` y `/api/v1/business-profiles`

### Estructura de Datos Corregida:
- PersonProfile y BusinessProfile usan `firstName` + `lastName` en lugar de `fullName`
- Agregados campos obligatorios: `street`, `state`

---

**Documento actualizado:** 12 de noviembre de 2025  
**Versión:** 2.0 (Corregida y validada contra implementación real)
