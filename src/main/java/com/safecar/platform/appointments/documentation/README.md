# Appointments Module - SafeCar Backend

## 📋 Tabla de Contenidos

1. [Descripción General](#descripción-general)
2. [Arquitectura](#arquitectura)
3. [Modelo de Dominio](#modelo-de-dominio)
4. [API REST Endpoints](#api-rest-endpoints)
5. [Anti-Corruption Layer (ACL)](#anti-corruption-layer-acl)
6. [Base de Datos](#base-de-datos)
7. [Ejemplos de Uso](#ejemplos-de-uso)
8. [Validaciones de Negocio](#validaciones-de-negocio)
9. [Integración con Otros Contextos](#integración-con-otros-contextos)
10. [Componentes Implementados](#componentes-implementados)

---

## Descripción General

Módulo de gestión de citas para el sistema SafeCar. Implementa el ciclo completo de vida de una cita de mantenimiento vehicular, desde su creación hasta su finalización, siguiendo los principios de **Domain-Driven Design (DDD)** y **CQRS**.

### Características Principales

- ✅ Gestión completa del ciclo de vida de citas
- ✅ Máquina de estados para transiciones controladas
- ✅ Anti-Corruption Layer para integración con otros contextos
- ✅ API REST completa con 14 endpoints
- ✅ Validaciones de negocio robustas
- ✅ Separación de comandos y consultas (CQRS)

---

## Arquitectura

Este módulo sigue los principios de **Domain-Driven Design (DDD)** y **CQRS**:

```
appointments/
├── domain/                          - Lógica de negocio y reglas del dominio
│   ├── model/
│   │   ├── aggregates/              - Appointment (agregado raíz)
│   │   ├── entities/                - AppointmentNote
│   │   ├── commands/                - 9 comandos
│   │   ├── queries/                 - 5 consultas
│   │   └── valueobjects/            - AppointmentStatus
│   ├── services/                    - Interfaces de servicios
│   └── exceptions/                  - Excepciones personalizadas
│
├── application/                     - Orquestación y casos de uso
│   └── internal/
│       ├── commandservices/         - Implementación de comandos
│       └── queryservices/           - Implementación de consultas
│
├── infrastructure/                  - Persistencia y servicios externos
│   └── persistence/jpa/
│       └── repositories/            - AppointmentRepository
│
├── interfaces/                      - API REST y DTOs
│   ├── rest/
│   │   ├── AppointmentController    - 14 endpoints REST
│   │   ├── resources/               - 8 DTOs (Request/Response)
│   │   └── transform/               - 8 Assemblers
│   └── acl/                         - Anti-Corruption Layer
│       ├── AppointmentsContextFacade
│       └── dto/                     - DTOs desacoplados
│
└── documentation/                   - Documentación centralizada
    └── README.md                    - Este archivo
```

---

## Modelo de Dominio

### Agregado: Appointment

Representa una cita de servicio con los siguientes estados:

#### Estados Disponibles

- `PENDING` - Cita creada, pendiente de confirmación
- `CONFIRMED` - Cita confirmada por el cliente/taller
- `IN_PROGRESS` - Servicio en curso
- `COMPLETED` - Servicio completado
- `CANCELLED` - Cita cancelada

#### Máquina de Estados

```
PENDING → CONFIRMED → IN_PROGRESS → COMPLETED
    ↓
CANCELLED (desde cualquier estado excepto COMPLETED)
```

#### Atributos del Agregado

| Atributo | Tipo | Descripción |
|----------|------|-------------|
| id | UUID | Identificador único |
| code | String | Código único de la cita (ej: APT-2025-0001) |
| scheduledDate | LocalDateTime | Fecha y hora programada |
| endDate | LocalDateTime | Fecha y hora de finalización |
| status | AppointmentStatus | Estado actual de la cita |
| serviceType | String | Tipo de servicio (ej: mantenimiento, diagnóstico) |
| description | String | Descripción detallada |
| customerId | UUID | Referencia al cliente |
| vehicleId | UUID | Referencia al vehículo |
| mechanicId | UUID | Referencia al mecánico asignado |
| workshopId | UUID | Referencia al taller |
| notes | List<AppointmentNote> | Notas asociadas |

#### Métodos de Negocio

- `confirm()` - Confirma la cita (PENDING → CONFIRMED)
- `start()` - Inicia el servicio (CONFIRMED → IN_PROGRESS)
- `complete()` - Completa el servicio (IN_PROGRESS → COMPLETED)
- `cancel(String reason)` - Cancela la cita con motivo
- `reschedule(LocalDateTime newDate)` - Reprograma la fecha
- `assignMechanic(UUID mechanicId)` - Asigna un mecánico
- `addNote(String content, UUID authorId)` - Agrega una nota
- `updateInformation(String serviceType, String description)` - Actualiza información

### Entidad: AppointmentNote

Notas asociadas a una cita para seguimiento y comunicación.

| Atributo | Tipo | Descripción |
|----------|------|-------------|
| id | Long | Identificador único |
| content | String | Contenido de la nota |
| authorId | UUID | ID del autor |
| appointment | Appointment | Referencia al appointment |
| createdAt | Date | Fecha de creación |
| updatedAt | Date | Fecha de actualización |

---

## API REST Endpoints

**Base URL:** `/api/v1/appointments`

### Gestión de Citas

| Método | Endpoint | Descripción | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| POST | `/` | Crear nueva cita | CreateAppointmentResource | AppointmentResource |
| GET | `/{id}` | Obtener cita por ID (UUID) | - | AppointmentResource |
| GET | `/code/{code}` | Obtener cita por código | - | AppointmentResource |
| PUT | `/{id}` | Actualizar información | UpdateAppointmentInformationResource | AppointmentResource |

### Consultas

| Método | Endpoint | Descripción | Response |
|--------|----------|-------------|----------|
| GET | `/customer/{customerId}` | Citas de un cliente (UUID) | List<AppointmentResource> |
| GET | `/workshop/{workshopId}` | Citas de un taller (UUID) | List<AppointmentResource> |
| GET | `/status/{status}` | Citas por estado | List<AppointmentResource> |

### Operaciones de Estado

| Método | Endpoint | Descripción | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| POST | `/{id}/confirm` | Confirmar cita | - | AppointmentResource |
| POST | `/{id}/start` | Iniciar servicio | - | AppointmentResource |
| POST | `/{id}/complete` | Completar servicio | - | AppointmentResource |
| POST | `/{id}/cancel` | Cancelar cita | CancelAppointmentResource | AppointmentResource |

### Operaciones Adicionales

| Método | Endpoint | Descripción | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| POST | `/{id}/reschedule` | Reprogramar | RescheduleAppointmentResource | AppointmentResource |
| POST | `/{id}/assign-mechanic` | Asignar mecánico | AssignMechanicResource | AppointmentResource |
| POST | `/{id}/notes` | Agregar nota | AddAppointmentNoteResource | 201 Created |

### Documentación Swagger

La documentación interactiva de la API está disponible en:

```
http://localhost:8080/swagger-ui.html
```

---

## Anti-Corruption Layer (ACL)

El ACL proporciona una interfaz limpia y desacoplada para que otros bounded contexts puedan interactuar con las citas sin acoplarse a los detalles internos del dominio.

### Arquitectura del ACL

```
Otros Bounded Contexts
        ↓
AppointmentsContextFacade (ACL)
        ↓
AppointmentCommandService / AppointmentQueryService
        ↓
Domain Model (Appointments)
```

### Componentes del ACL

#### 1. AppointmentsContextFacade

**Ubicación:** `interfaces.acl.AppointmentsContextFacade`

Servicio principal que expone 13 métodos públicos:

**Comandos:**
- `createAppointment(String code, ...)` → UUID
- `createAppointment(CreateAppointmentDto)` → Optional<UUID>

**Consultas:**
- `getAppointmentById(UUID)` → Optional<AppointmentDto>
- `getAppointmentsByCustomer(UUID)` → List<AppointmentDto>
- `getAppointmentsByWorkshop(UUID)` → List<AppointmentDto>
- `getAppointmentStatus(UUID)` → String
- `getAppointmentScheduledDate(UUID)` → LocalDateTime
- `getAppointmentCountByCustomer(UUID)` → int

**Validaciones:**
- `appointmentExists(UUID)` → boolean
- `customerHasPendingAppointments(UUID)` → boolean

#### 2. DTOs Desacoplados

**CreateAppointmentDto**
```java
public record CreateAppointmentDto(
    String code,
    LocalDateTime scheduledDate,
    String serviceType,
    String description,
    UUID customerId,
    UUID vehicleId,
    UUID workshopId
)
```

**AppointmentDto**
```java
public record AppointmentDto(
    UUID id,
    String code,
    LocalDateTime scheduledDate,
    String status,
    UUID customerId,
    UUID vehicleId,
    UUID workshopId
)
```

### Ventajas del ACL

1. **Desacoplamiento** - Contextos externos no conocen detalles internos
2. **Estabilidad** - Cambios internos no afectan a otros contextos
3. **Simplicidad** - API clara y enfocada
4. **Evolución Independiente** - Cada contexto evoluciona sin romper otros
5. **Testing** - Fácil de mockear para pruebas

---

## Base de Datos

### Tabla: appointments

| Campo | Tipo | Constraints | Descripción |
|-------|------|-------------|-------------|
| id | UUID | PK | Identificador único |
| code | VARCHAR(20) | UNIQUE, NOT NULL | Código único |
| scheduled_date | TIMESTAMP | NOT NULL | Fecha programada |
| end_date | TIMESTAMP | NULL | Fecha de finalización |
| status | VARCHAR(20) | NOT NULL | Estado de la cita |
| service_type | VARCHAR(100) | NOT NULL | Tipo de servicio |
| description | VARCHAR(500) | NULL | Descripción |
| customer_id | UUID | NOT NULL | ID del cliente |
| vehicle_id | UUID | NOT NULL | ID del vehículo |
| mechanic_id | UUID | NULL | ID del mecánico |
| workshop_id | UUID | NOT NULL | ID del taller |
| created_at | TIMESTAMP | NOT NULL | Fecha de creación |
| updated_at | TIMESTAMP | NOT NULL | Fecha de actualización |

### Tabla: appointment_notes

| Campo | Tipo | Constraints | Descripción |
|-------|------|-------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | Identificador único |
| content | VARCHAR(1000) | NOT NULL | Contenido de la nota |
| author_id | UUID | NOT NULL | ID del autor |
| appointment_id | UUID | NOT NULL, FK | FK a appointments |
| created_at | TIMESTAMP | NOT NULL | Fecha de creación |
| updated_at | TIMESTAMP | NOT NULL | Fecha de actualización |

---

## Ejemplos de Uso

### 1. Crear una Cita

**Request:**
```bash
POST /api/v1/appointments
Content-Type: application/json

{
  "code": "APT-2025-0001",
  "scheduledDate": "2025-11-15T10:00:00",
  "serviceType": "Mantenimiento preventivo",
  "description": "Cambio de aceite y revisión general",
  "customerId": "123e4567-e89b-12d3-a456-426614174000",
  "vehicleId": "123e4567-e89b-12d3-a456-426614174001",
  "workshopId": "123e4567-e89b-12d3-a456-426614174002"
}
```

**Response:**
```json
{
  "id": "987f6543-e21b-34c5-a678-426614174003",
  "code": "APT-2025-0001",
  "scheduledDate": "2025-11-15T10:00:00",
  "endDate": null,
  "status": "PENDING",
  "serviceType": "Mantenimiento preventivo",
  "description": "Cambio de aceite y revisión general",
  "customerId": "123e4567-e89b-12d3-a456-426614174000",
  "vehicleId": "123e4567-e89b-12d3-a456-426614174001",
  "mechanicId": null,
  "workshopId": "123e4567-e89b-12d3-a456-426614174002",
  "notes": []
}
```

### 2. Flujo Completo de una Cita

```bash
# 1. Cliente crea una cita
POST /api/v1/appointments
# Estado: PENDING

# 2. Taller confirma la cita
POST /api/v1/appointments/987f6543-e21b-34c5-a678-426614174003/confirm
# Estado: CONFIRMED

# 3. Asignar mecánico
POST /api/v1/appointments/987f6543-e21b-34c5-a678-426614174003/assign-mechanic
{
  "mechanicId": "456e7890-e12b-45d6-a789-426614174004"
}

# 4. Mecánico inicia el servicio
POST /api/v1/appointments/987f6543-e21b-34c5-a678-426614174003/start
# Estado: IN_PROGRESS

# 5. Agregar nota durante el servicio
POST /api/v1/appointments/987f6543-e21b-34c5-a678-426614174003/notes
{
  "content": "Se detectó desgaste en frenos, se recomienda cambio",
  "authorId": "456e7890-e12b-45d6-a789-426614174004"
}

# 6. Completar servicio
POST /api/v1/appointments/987f6543-e21b-34c5-a678-426614174003/complete
# Estado: COMPLETED
```

### 3. Reprogramar una Cita

```bash
POST /api/v1/appointments/987f6543-e21b-34c5-a678-426614174003/reschedule
Content-Type: application/json

{
  "newScheduledDate": "2025-11-16T14:00:00"
}
```

### 4. Cancelar una Cita

```bash
POST /api/v1/appointments/987f6543-e21b-34c5-a678-426614174003/cancel
Content-Type: application/json

{
  "reason": "Cliente canceló por motivos personales"
}
```

---

## Validaciones de Negocio

### Creación de Citas

- ✅ La fecha programada debe ser futura
- ✅ El código debe ser único
- ✅ Todos los IDs de referencia son obligatorios (customerId, vehicleId, workshopId)
- ✅ El tipo de servicio es obligatorio

### Transiciones de Estado

| Estado Actual | Transición Válida | Estado Siguiente |
|---------------|-------------------|------------------|
| PENDING | confirm() | CONFIRMED |
| CONFIRMED | start() | IN_PROGRESS |
| IN_PROGRESS | complete() | COMPLETED |
| Cualquiera (excepto COMPLETED) | cancel() | CANCELLED |

**Restricciones:**
- ❌ Solo se pueden confirmar citas en estado `PENDING`
- ❌ Solo se pueden iniciar citas en estado `CONFIRMED`
- ❌ Solo se pueden completar citas en estado `IN_PROGRESS`
- ❌ No se pueden cancelar citas ya completadas

### Reprogramación

- ✅ No se pueden reprogramar citas completadas o canceladas
- ✅ La nueva fecha debe ser futura
- ✅ Debe haber al menos 1 hora de anticipación

### Asignación de Mecánico

- ✅ El mechanicId no puede ser nulo
- ✅ Se puede asignar en cualquier estado excepto COMPLETED o CANCELLED

---

## Integración con Otros Contextos

### Ejemplo 1: Customers Context

**Escenario:** Verificar antes de eliminar un cliente

```java
@Service
public class CustomerService {
    
    @Autowired
    private AppointmentsContextFacade appointmentsContextFacade;
    
    public void deleteCustomer(UUID customerId) {
        // Validar antes de eliminar
        if (appointmentsContextFacade.customerHasPendingAppointments(customerId)) {
            throw new CustomerDeletionException(
                "No se puede eliminar cliente con citas pendientes"
            );
        }
        
        // Proceder con eliminación
        customerRepository.delete(customerId);
    }
}
```

### Ejemplo 2: Vehicles Context

**Escenario:** Obtener historial de mantenimiento de un vehículo

```java
@Service
public class VehicleMaintenanceService {
    
    @Autowired
    private AppointmentsContextFacade appointmentsContextFacade;
    
    public MaintenanceHistory getVehicleHistory(UUID vehicleId, UUID customerId) {
        List<AppointmentDto> allCustomerAppointments = 
            appointmentsContextFacade.getAppointmentsByCustomer(customerId);
        
        var vehicleAppointments = allCustomerAppointments.stream()
            .filter(app -> app.vehicleId().equals(vehicleId))
            .collect(Collectors.toList());
        
        return new MaintenanceHistory(vehicleId, vehicleAppointments);
    }
}
```

### Ejemplo 3: Workshops Context

**Escenario:** Obtener agenda del taller para un día específico

```java
@Service
public class WorkshopScheduleService {
    
    @Autowired
    private AppointmentsContextFacade appointmentsContextFacade;
    
    public DailySchedule getSchedule(UUID workshopId, LocalDate date) {
        List<AppointmentDto> appointments = 
            appointmentsContextFacade.getAppointmentsByWorkshop(workshopId);
        
        var dailyAppointments = appointments.stream()
            .filter(app -> app.scheduledDate().toLocalDate().equals(date))
            .sorted(Comparator.comparing(AppointmentDto::scheduledDate))
            .collect(Collectors.toList());
        
        return new DailySchedule(workshopId, date, dailyAppointments);
    }
}
```

### Ejemplo 4: Notifications Context

**Escenario:** Enviar recordatorios de citas programadas

```java
@Service
public class NotificationService {
    
    @Autowired
    private AppointmentsContextFacade appointmentsContextFacade;
    
    @Scheduled(cron = "0 0 8 * * *") // Daily at 8 AM
    public void sendAppointmentReminders() {
        customerRepository.findAll().forEach(customer -> {
            var appointments = appointmentsContextFacade
                .getAppointmentsByCustomer(customer.getId());
            
            appointments.stream()
                .filter(this::isWithin24Hours)
                .forEach(app -> sendReminderEmail(customer, app));
        });
    }
    
    private boolean isWithin24Hours(AppointmentDto appointment) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime scheduledDate = appointment.scheduledDate();
        return scheduledDate.isAfter(now) && 
               scheduledDate.isBefore(now.plusHours(24));
    }
}
```

### Ejemplo 5: Payments Context

**Escenario:** Crear factura al completar una cita

```java
@Service
public class InvoiceService {
    
    @Autowired
    private AppointmentsContextFacade appointmentsContextFacade;
    
    @EventListener
    public void onAppointmentCompleted(AppointmentCompletedEvent event) {
        Optional<AppointmentDto> appointment = 
            appointmentsContextFacade.getAppointmentById(event.getAppointmentId());
        
        if (appointment.isPresent()) {
            createInvoice(
                appointment.get().customerId(),
                appointment.get().workshopId(),
                event.getAppointmentId()
            );
        }
    }
}
```

---

## Testing

### Testing del ACL

Ejemplo de cómo testear servicios que usan el ACL:

```java
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    
    @Mock
    private AppointmentsContextFacade appointmentsContextFacade;
    
    @InjectMocks
    private CustomerService customerService;
    
    @Test
    void shouldNotDeleteCustomerWithPendingAppointments() {
        // Given
        UUID customerId = UUID.randomUUID();
        when(appointmentsContextFacade.customerHasPendingAppointments(customerId))
            .thenReturn(true);
        
        // When & Then
        assertThrows(CustomerDeletionException.class, 
            () -> customerService.deleteCustomer(customerId));
    }
    
    @Test
    void shouldDeleteCustomerWithoutPendingAppointments() {
        // Given
        UUID customerId = UUID.randomUUID();
        when(appointmentsContextFacade.customerHasPendingAppointments(customerId))
            .thenReturn(false);
        
        // When & Then
        assertDoesNotThrow(() -> customerService.deleteCustomer(customerId));
    }
}
```

---

## Estado del Módulo

### ✅ Completado (100%)

- ✅ **Domain Layer** - Agregados, entidades, value objects, commands, queries
- ✅ **Application Layer** - Command y Query Services implementados
- ✅ **Infrastructure Layer** - Repository JPA con UUID
- ✅ **Interfaces Layer** - REST API con 14 endpoints
- ✅ **ACL** - Facade con 13 métodos para integración
- ✅ **Validaciones** - Bean Validation + validaciones de negocio
- ✅ **Documentación** - Centralizada y completa

### 📊 Métricas

| Componente | Cantidad |
|------------|----------|
| Agregados | 1 |
| Entidades | 1 |
| Value Objects | 1 |
| Commands | 9 |
| Queries | 5 |
| Domain Services | 2 |
| Excepciones | 3 |
| Endpoints REST | 14 |
| Resources (DTOs) | 8 |
| Assemblers | 8 |
| Métodos ACL | 13 |
| **Total Archivos** | **47** |

---

## Dependencias Externas

Este módulo tiene referencias a otros bounded contexts mediante UUIDs para mantener el desacoplamiento:

- `customerId` → Referencia al módulo de Customers
- `vehicleId` → Referencia al módulo de Vehicles
- `mechanicId` → Referencia al módulo de Mechanics
- `workshopId` → Referencia al módulo de Workshops

El uso de UUIDs permite que el módulo funcione independientemente sin acoplamiento directo a otros contextos.

---

## Acceso a la Documentación API

**Swagger UI:** http://localhost:8080/swagger-ui.html

Para acceder a Swagger, primero inicia la aplicación:

```bash
cd C:\Users\janov\Desktop\develop\safecar-backend
.\mvnw.cmd spring-boot:run
```

Luego abre el navegador en la URL de Swagger UI para ver y probar todos los endpoints interactivamente.

---

**Módulo:** Appointments  
**Versión:** 1.0.0  
**Última Actualización:** Noviembre 2025  
**Estado:** ✅ Implementado y Funcional

