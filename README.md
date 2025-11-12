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
This version of SafeCar Backend Platform is divided into four main bounded contexts: IAM, Profiles, Devices, and Workshop, plus a Shared context for common infrastructure.

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
spring.datasource.url=jdbc:mysql://localhost:3306/safecar_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy
```

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

## 📊 **Resumen de Endpoints Implementados**

| **Contexto** | **Controlador** | **Base Path** | **Endpoints** |
|--------------|----------------|---------------|---------------|
| **IAM** | Authentication | `/api/v1/authentication` | 2 |
| | Users | `/api/v1/users` | 2 |
| | Roles | `/api/v1/roles` | 1 |
| **Profiles** | PersonProfiles | `/api/v1/person-profiles` | 3 |
| | BusinessProfiles | `/api/v1/business-profiles` | 3 |
| **Devices** | Vehicles | `/api/v1/` | 3 |
| **Workshop** | Workshops | `/api/v1/workshops` | 2 |
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
