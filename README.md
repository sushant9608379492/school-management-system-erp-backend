# School Management System ERP – Backend

A backend system built using Spring Boot and microservices architecture to manage core school operations such as student management and admissions.

This project is designed with clean layering, service isolation, and real-world backend practices in mind.

---

## 🏗 Architecture Overview

The system follows a microservices-based design with independent Spring Boot services:

- **student-service**
- **admission-service**
- **school-payment-api**

Each service has its own database layer, business logic, and REST APIs.  
Services communicate synchronously using REST APIs via Spring WebClient.

---

## 🧩 Services

### 1️⃣ Student Service
Responsible for managing student lifecycle and academic data.

**Key Responsibilities**
- Create student records
- Fetch student details
- Assign and promote students to classes
- Maintain student class history

**Tech Highlights**
- Spring Boot
- Spring Data JPA
- REST APIs
- Global exception handling
- DTO-based request/response design

---

### 2️⃣ Admission Service
Handles the admission workflow and integrates with Student Service.

**Key Responsibilities**
- Apply for admission
- Approve admission
- On approval, create student record in Student Service

**Integration**
- Uses Spring `WebClient` to communicate with Student Service
- External service URLs are configurable (no hardcoding)

---

## 🔁 Service Communication Flow

1. Admission is applied via Admission Service
2. Admission is approved
3. Admission Service calls Student Service to create a student
4. Student Service persists student data and returns response

---

## 🛠 Tech Stack

- Java 8+
- Spring Boot
- Spring Data JPA
- Hibernate
- RESTful APIs
- WebClient
- Maven
- MySQL (configurable)
- Git

---

## 📂 Project Structure (Mono Repo)
