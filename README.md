# Factory Events Backend

Backend service for ingesting factory machine events, deduplicating data, and providing operational statistics.

---

## 🚀 Tech Stack
- Java 21
- Spring Boot 3.2.x
- Spring Data JPA
- MySQL (Production)
- H2 (Testing)
- Maven
- JUnit 5

---

## 📦 Features

### 1️⃣ Batch Event Ingest API
- Deduplicates events using `eventId`
- Detects payload changes using SHA-256 hash
- Updates latest events safely
- Handles validation & rejection
- Idempotent ingestion ensures safe retries

### 2️⃣ Statistics APIs
- Daily ingestion statistics
- Machine health stats
- Top defect lines aggregation

### 3️⃣ Concurrency Safe
- Handles concurrent batch ingestion
- Prevents duplicate persistence

---

## 📂 Project Structure

```text
factory-events-backend
├── .mvn/
│   └── wrapper/
├── src/
│   ├── main/
│   │   ├── java/com/events/
│   │   │   ├── controller/        # REST Controllers
│   │   │   │   ├── EventIngestController.java
│   │   │   │   └── StatsController.java
│   │   │   ├── dto/               # Request / Response DTOs
│   │   │   │   ├── EventRequestDTO.java
│   │   │   │   ├── DailyStatsDTO.java
│   │   │   │   ├── MachineStatsDTO.java
│   │   │   │   └── TopDefectLineDTO.java
│   │   │   ├── entity/            # JPA Entities
│   │   │   │   └── EventEntity.java
│   │   │   ├── repository/        # Spring Data JPA Repositories
│   │   │   │   └── EventRepository.java
│   │   │   ├── service/           # Business Logic
│   │   │   │   ├── EventIngestService.java
│   │   │   │   ├── StatsService.java
│   │   │   │   └── BatchResult.java
│   │   │   ├── validation/        # Validation Layer
│   │   │   │   ├── EventValidator.java
│   │   │   │   └── ValidationResult.java
│   │   │   ├── util/              # Utility Classes
│   │   │   │   └── HashUtil.java
│   │   │   └── FactoryEventsBackendApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       ├── java/com/events/
│       │   ├── controller/        # Controller Tests
│       │   ├── service/           # Service & Concurrency Tests
│       │   ├── repository/        # Repository Tests
│       │   └── FactoryEventsBackendApplicationTests.java
│       └── resources/
│           └── application-test.properties
├── pom.xml
├── mvnw / mvnw.cmd
└── README.md
```

---

🧩 **Architecture Overview**

- Controller Layer → Handles HTTP requests

- Service Layer → Business logic, concurrency handling

- Repository Layer → Database access & deduplication

- Validation Layer → Input validation & rule enforcement

- Test Layer → Unit, integration & concurrency tests

---

## 🔗 API Endpoints

### Batch Ingest

- POST /events/batch
<img width="1735" height="821" alt="Screenshot (4512)" src="https://github.com/user-attachments/assets/6b6d02c3-19dc-4357-a0fd-8cee2980db85" />

<img width="1731" height="824" alt="Screenshot (4513)" src="https://github.com/user-attachments/assets/d5f8c2b8-9d99-4683-a50d-4e4b71399e23" />

<img width="1747" height="608" alt="Screenshot (4514)" src="https://github.com/user-attachments/assets/73a097dc-8ab9-4308-97fa-e3a5656e5945" />

<img width="1733" height="853" alt="Screenshot (4515)" src="https://github.com/user-attachments/assets/88cc7a75-a4f3-4fb1-b398-75734e54d0b5" />


---

### Daily Stats


- GET /stats/daily

  <img width="1735" height="587" alt="Screenshot (4516)" src="https://github.com/user-attachments/assets/3c1498c5-63af-4597-849d-04e8b6315216" />

---

### Machine Stats


- GET /stats/machine?machineId=M-1&start=...&end=...

  <img width="1704" height="852" alt="Screenshot (4517)" src="https://github.com/user-attachments/assets/7fb5fb51-898f-4f60-92eb-da3cff28f39b" />

---

### Top Defect Lines


- GET /stats/top-defect-lines?from=...&to=...&limit=5

  <img width="1729" height="581" alt="Screenshot (4518)" src="https://github.com/user-attachments/assets/85ce1385-d530-4372-a8e5-5ecab76ab167" />

  <img width="1725" height="864" alt="Screenshot (4519)" src="https://github.com/user-attachments/assets/2eb22511-6173-4c72-b7f0-f5da5a61cec1" />


---

## 🧪 Running Tests

```bash
mvn clean test



Same for run command:

```md
## ▶ Run Application

```bash 
mvn spring-boot:run

App runs at:

http://localhost:8080
```
---

👤 **Author**
```
Krantikumar Dilip Patil
krantikumar4211@gmail.com
+91-7507494211
```
