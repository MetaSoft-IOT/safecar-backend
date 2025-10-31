<h1>SafeCar Backend Platform</h1>

## Summary
<p align="justify">
SafeCar Backend Platform is part of the SafeCar IoT platform for intelligent vehicle maintenance. It connects drivers with workshops, collects real-time vehicle data (engine, brakes, tires, consumption, and driving habits), and generates <b>preventive alerts</b> and <b>automatic reports</b> to optimize repairs. This backend is developed with <b>Java 17</b>, <b>Spring Boot Framework</b>, <b>Spring Data JPA</b> on <b>MySQL Database</b>, and includes <b>OpenAPI/Swagger UI</b> documentation.
</p>

---

## Features

### 🚀 Features

- RESTful API
- OpenAPI Documentation (Swagger UI)
- Spring Boot Framework
- Spring Data JPA
- Validation
- MySQL Database
- Domain-Driven Design (DDD) approach
- Environment configuration with `.env`

## Project Setup

### 📦 Requirements
* Java 17+
* Maven 3+
* MySQL 8+
* IDE (IntelliJ, Eclipse, VSCode)

### 🏁 Running the Project

#### Using Maven Wrapper
```bash
./mvnw spring-boot:run
```

### 🛠️ Environment Configuration
Create a `.env` file in the project root to configure database credentials:

```env
# Database Configuration
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
```

---

## 📚 API Documentation

The backend provides interactive API documentation using **Swagger UI**. After starting the application, access the documentation at:

```
http://localhost:8080/swagger-ui.html
```