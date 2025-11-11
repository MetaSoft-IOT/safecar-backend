# SafeCar Backend Platform

## Summary
SafeCar Backend Platform is a comprehensive IoT vehicle telemetry and workshop management platform, illustrating development with Java, Spring Boot 3.5.7, and Spring Data JPA on MySQL Database. The platform implements Domain-Driven Design (DDD) with CQRS patterns and Anti-Corruption Layers (ACL) to provide intelligent vehicle monitoring and workshop operations management.

## Features
- RESTful API with 40+ endpoints
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
- Workshop Operations Management
- Vehicle Fleet Management
- Predictive Maintenance Alerts

## Bounded Contexts
This version of SafeCar Backend Platform is divided into four main bounded contexts: IAM, Profiles, Devices, and Workshop, plus a Shared context for common infrastructure.

### Identity and Access Management (IAM) Context

The IAM Context is responsible for managing platform users, including the sign-up and sign-in processes. It applies JSON Web Token based authorization and Password hashing. Its capabilities include:

- Create a new User (Sign Up).
- Authenticate a User (Sign In).
- Get a User by ID.
- Get All Users.
- Get All Roles.
- Use Spring Security features to implement an authorization pipeline based on request filtering.
- Generate and validate JSON Web Tokens.
- Apply Password hashing.

This version implements the following roles: ROLE_ADMIN (system administrators), ROLE_CLIENT (basic users), ROLE_DRIVER (vehicle operators), ROLE_MECHANIC (maintenance technicians), and ROLE_WORKSHOP (workshop owners). The roles are used to manage access to platform features according to business rules.

This context includes also an anti-corruption layer to communicate with other bounded contexts, providing capabilities to:
- Create a new User, returning ID of the created User on success.
- Get a User by ID, returning the associated User ID on success.
- Get a User by Email, returning the associated User ID on success.

### Profiles Context

The Profiles Context is responsible for managing profiles of users including personal information and business profiles. It includes the following features:

- Create a new Person Profile.
- Create a new Business Profile.
- Get a profile by ID.
- Get all profiles by User ID.
- Automatic Driver and Mechanic profile creation via event handling.

This context includes also an anti-corruption layer to communicate with other bounded contexts. The anti-corruption layer is responsible for managing the communication between the Profiles Context and other contexts. It offers the following capabilities:
- Create a new Person Profile, returning ID of the created Profile on success.
- Create a new Business Profile for workshop owners.
- Get a Profile by User ID, returning the associated Profile information.

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

The Workshop Context is responsible for managing workshop operations, appointments, service orders, telemetry processing, and mechanic management. This is the core business context for SafeCar platform. Its comprehensive features include:

- Create and manage Workshop entities.
- Register Mechanics and assign them to workshops.
- Create and manage Appointments with complete lifecycle (PENDING → CONFIRMED → IN_PROGRESS → COMPLETED/CANCELLED).
- Manage Service Orders with automatic counters and validations.
- Process real-time IoT telemetry data from vehicles (speed, GPS, diagnostic codes).
- Generate predictive maintenance alerts based on telemetry severity (INFO, WARNING, CRITICAL).
- Link appointments to service orders with workshop ID validation.
- Workshop operations metrics and efficiency calculations.
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

## Core Feature Testing Flows

### 1. Authentication and User Management (IAM Context)

#### Complete User Registration and Authentication Flow

**Step 1: Register Driver User**
```bash
curl -X POST http://localhost:8080/api/v1/authentication/sign-up \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.driver@email.com",
    "password": "SecurePass123!",
    "confirmPassword": "SecurePass123!",
    "roles": ["ROLE_CLIENT"]
  }'
```

**Expected Response:**
```json
{
  "id": 1,
  "email": "john.driver@email.com"
}
```

**Step 2: Register Mechanic User**
```bash
curl -X POST http://localhost:8080/api/v1/authentication/sign-up \
  -H "Content-Type: application/json" \
  -d '{
    "email": "carlos.mechanic@workshop.com", 
    "password": "MechanicPass456!",
    "confirmPassword": "MechanicPass456!",
    "roles": ["ROLE_MECHANIC"]
  }'
```

**Step 3: Register Workshop Owner**
```bash
curl -X POST http://localhost:8080/api/v1/authentication/sign-up \
  -H "Content-Type: application/json" \
  -d '{
    "email": "maria.owner@workshop.com",
    "password": "WorkshopPass789!",
    "confirmPassword": "WorkshopPass789!",
    "roles": ["ROLE_WORKSHOP"]
  }'
```

**Step 4: User Authentication**
```bash
curl -X POST http://localhost:8080/api/v1/authentication/sign-in \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.driver@email.com",
    "password": "SecurePass123!"
  }'
```

**Response with JWT Token:**
```json
{
  "id": 1,
  "email": "john.driver@email.com", 
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "roles": ["ROLE_CLIENT"]
}
```

### 2. Profile Management (Profiles Context)

#### Complete Person Profile Creation Flow

**Create Person Profile** (Use JWT token from previous login):
```bash
curl -X POST "http://localhost:8080/api/v1/profiles?userEmail=john.driver@email.com" \
  -H "Authorization: Bearer your_jwt_token_here" \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "John Carlos Smith",
    "city": "Lima",
    "country": "Peru", 
    "phone": "+51987654321",
    "dni": "12345678"
  }'
```

**Expected Response:**
```json
{
  "id": 1,
  "fullName": "John Carlos Smith",
  "city": "Lima",
  "country": "Peru",
  "phone": "+51987654321",
  "dni": "12345678",
  "userEmail": "john.driver@email.com"
}
```

**Get Profile by User Email:**
```bash
curl -X GET "http://localhost:8080/api/v1/profiles?userEmail=john.driver@email.com" \
  -H "Authorization: Bearer your_jwt_token_here"
```

### 3. Vehicle Management (Devices Context)

#### Complete Vehicle Registration and Management Flow

**Register First Vehicle for Driver:**
```bash
curl -X POST http://localhost:8080/api/v1/vehicles \
  -H "Authorization: Bearer driver_jwt_token" \
  -H "Content-Type: application/json" \
  -d '{
    "driverId": 1,
    "licensePlate": "ABC-123", 
    "brand": "Toyota",
    "model": "Corolla 2020"
  }'
```

**Expected Response:**
```json
{
  "id": 1,
  "licensePlate": "ABC-123",
  "brand": "Toyota",
  "model": "Corolla 2020",
  "driverId": 1
}
```

**Note:** Vehicle creation automatically updates the Driver's `totalVehicles` counter via event handling.

**Register Additional Vehicle:**
```bash
curl -X POST http://localhost:8080/api/v1/vehicles \
  -H "Authorization: Bearer driver_jwt_token" \
  -H "Content-Type: application/json" \
  -d '{
    "driverId": 1,
    "licensePlate": "XYZ-789",
    "brand": "Honda", 
    "model": "Civic 2021"
  }'
```

**Get Specific Vehicle:**
```bash
curl -X GET http://localhost:8080/api/v1/vehicles/1 \
  -H "Authorization: Bearer driver_jwt_token"
```

**List All Vehicles by Driver:**
```bash
curl -X GET http://localhost:8080/api/v1/vehicles/driver/1 \
  -H "Authorization: Bearer driver_jwt_token"
```

**Expected Response (Vehicle List):**
```json
[
  {
    "id": 1,
    "licensePlate": "ABC-123",
    "brand": "Toyota", 
    "model": "Corolla 2020",
    "driverId": 1
  },
  {
    "id": 2,
    "licensePlate": "XYZ-789",
    "brand": "Honda",
    "model": "Civic 2021", 
    "driverId": 1
  }
]
```

**Casos de uso disponibles:**
- ✅ Registrar múltiples vehículos por conductor
- ✅ **Actualización automática de métricas de Driver via eventos**
- ✅ Validación de placas únicas en el sistema
- ✅ Validación de existencia de Driver antes de crear vehículo
- ✅ Consultar información completa de vehículos
- ✅ Listar fleet completa de un conductor
- ✅ Integración con perfiles via ACL

---

### 4. Workshop Operations and IoT Telemetry (Workshop Context)

#### Complete Workshop Setup and Telemetry Processing Flow

**Step 1: Create Workshop Business Profile**
```bash
# Login as workshop owner
curl -X POST http://localhost:8080/api/v1/authentication/sign-in \
  -H "Content-Type: application/json" \
  -d '{
    "email": "maria.owner@workshop.com",
    "password": "WorkshopPass789!"
  }'
# Response: JWT token with ROLE_WORKSHOP

# Create Business Profile (Note: Check actual endpoint in ProfilesController)
curl -X POST "http://localhost:8080/api/v1/profiles?userEmail=maria.owner@workshop.com" \
  -H "Authorization: Bearer workshop_owner_jwt_token" \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Maria Workshop Owner",
    "city": "Lima",
    "country": "Peru",
    "phone": "+51-1-444-5555",
    "dni": "87654321"
  }'
```

**Step 2: Create Workshop Entity**
```bash
curl -X POST http://localhost:8080/api/v1/workshop \
  -H "Authorization: Bearer workshop_owner_jwt_token" \
  -H "Content-Type: application/json" \
  -d '{
    "businessProfileId": 1,
    "workshopName": "San Martin Auto Workshop - Main",
    "workshopAddress": "123 Main St, Lima",
    "workshopPhone": "+51-1-444-5555",
    "workshopDescription": "Specialized in automotive diagnostics and IoT telemetry"
  }'
```

**Step 3: Process IoT Telemetry Data (Core Business)**
```bash
# Ingest real-time vehicle telemetry with GPS and speed data
curl -X POST http://localhost:8080/api/v1/telemetry \
  -H "Authorization: Bearer driver_jwt_token" \
  -H "Content-Type: application/json" \
  -d '{
    "sample": {
      "type": "SPEED",
      "severity": "INFO",
      "timestamp": {"occurredAt": "2024-12-15T10:30:00Z"},
      "vehicleId": {"vehicleId": 1},
      "driverId": {"driverId": 1},
      "speed": {"value": 85.5},
      "location": {"latitude": -12.0464, "longitude": -77.0428},
      "odometer": {"value": 25467.8},
      "dtc": null
    }
  }'
```

**Step 4: Ingest Engine Diagnostic Code (Critical Alert)**
```bash
curl -X POST http://localhost:8080/api/v1/telemetry \
  -H "Authorization: Bearer mechanic_jwt_token" \
  -H "Content-Type: application/json" \
  -d '{
    "sample": {
      "type": "DIAGNOSTIC",
      "severity": "CRITICAL",
      "timestamp": {"occurredAt": "2024-12-15T10:35:00Z"},
      "vehicleId": {"vehicleId": 1},
      "driverId": {"driverId": 1},
      "speed": {"value": 0.0},
      "location": {"latitude": -12.0464, "longitude": -77.0428},
      "odometer": {"value": 25467.8},
      "dtc": {"code": "P0301", "standard": "OBD2"}
    }
  }'
```

**Step 5: Query Telemetry Data and Alerts**
```bash
# Get specific telemetry record
curl -X GET http://localhost:8080/api/v1/telemetry/1 \
  -H "Authorization: Bearer mechanic_jwt_token"

# Query critical alerts for predictive maintenance
curl -X GET "http://localhost:8080/api/v1/telemetry/alerts?severity=CRITICAL&from=2024-12-15T00:00:00Z&to=2024-12-15T23:59:59Z" \
  -H "Authorization: Bearer mechanic_jwt_token"

# Get telemetry history by vehicle
curl -X GET "http://localhost:8080/api/v1/telemetry?vehicleId=1&from=2024-12-15T00:00:00Z&to=2024-12-15T23:59:59Z" \
  -H "Authorization: Bearer mechanic_jwt_token"
```

## Complete End-to-End Integration Tests

### Full Driver Onboarding with Vehicle Registration
```bash
# 1. Register and authenticate driver
curl -X POST http://localhost:8080/api/v1/authentication/sign-up \
  -H "Content-Type: application/json" \
  -d '{"email": "test.driver@email.com", "password": "SecurePass123!", "confirmPassword": "SecurePass123!", "roles": ["ROLE_CLIENT"]}'

# 2. Get JWT token
TOKEN=$(curl -X POST http://localhost:8080/api/v1/authentication/sign-in \
  -H "Content-Type: application/json" \
  -d '{"email": "test.driver@email.com", "password": "SecurePass123!"}' | jq -r '.token')

# 3. Create profile (triggers automatic Driver creation)
curl -X POST "http://localhost:8080/api/v1/profiles?userEmail=test.driver@email.com" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"fullName": "Test Driver", "city": "Lima", "country": "Peru", "phone": "+51987654321", "dni": "12345678"}'

# 4. Register vehicle (automatically updates Driver totalVehicles)
curl -X POST http://localhost:8080/api/v1/vehicles \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"driverId": 1, "licensePlate": "TEST-001", "brand": "Toyota", "model": "Corolla 2024"}'
```

### IoT Telemetry Processing with Workshop Operations
```bash
# 1. Ingest critical telemetry requiring immediate attention
curl -X POST http://localhost:8080/api/v1/telemetry \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "sample": {
      "type": "ENGINE_FAULT",
      "severity": "CRITICAL",
      "timestamp": {"occurredAt": "2024-12-15T14:30:00Z"},
      "vehicleId": {"vehicleId": 1},
      "driverId": {"driverId": 1},
      "speed": {"value": 0.0},
      "location": {"latitude": -12.0464, "longitude": -77.0428},
      "odometer": {"value": 25467.8},
      "dtc": {"code": "P0300", "standard": "OBD2"}
    }
  }'

# 2. Query alerts for workshop predictive maintenance
curl -X GET "http://localhost:8080/api/v1/telemetry/alerts?severity=CRITICAL" \
  -H "Authorization: Bearer $MECHANIC_TOKEN"
```

## API Documentation
The backend provides comprehensive API documentation using Swagger UI. After starting the application, access the interactive documentation at:

```
http://localhost:8080/swagger-ui.html
```

### Main API Endpoints
- **Authentication:** `POST /api/v1/authentication/sign-up` (requires email, password, confirmPassword, roles), `POST /api/v1/authentication/sign-in` (requires email, password)
- **Profiles:** `POST /api/v1/profiles?userEmail={email}`, `GET /api/v1/profiles?userEmail={email}`
- **Vehicles:** `POST /api/v1/vehicles` (requires driverId, licensePlate, brand, model), `GET /api/v1/vehicles/{id}`, `GET /api/v1/vehicles/driver/{driverId}`
- **Telemetry:** `POST /api/v1/telemetry` (IngestTelemetrySampleCommand), `GET /api/v1/telemetry/alerts?severity={severity}&from={instant}&to={instant}`, `GET /api/v1/telemetry?vehicleId={id}&from={instant}&to={instant}`
- **Workshop:** `POST /api/v1/workshop` (CreateWorkshopResource), `GET /api/v1/workshop/{id}`

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.7/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.7/maven-plugin/build-image.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.5.7/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.5.7/reference/htmlsingle/index.html#using.devtools)
* [Validation](https://docs.spring.io/spring-boot/docs/3.5.7/reference/htmlsingle/index.html#io.validation)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.5.7/reference/htmlsingle/index.html#web)
* [Spring Security](https://docs.spring.io/spring-boot/docs/3.5.7/reference/htmlsingle/index.html#web.security)

### Guides

The following guides illustrate how to use some features concretely:

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

