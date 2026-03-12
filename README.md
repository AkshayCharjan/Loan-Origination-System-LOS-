# **Loan Origination System (LOS)**

A backend service built with **Java Spring Boot** that simulates the lifecycle of a loan application.

The system processes loans automatically, assigns agents for manual review when required, and allows agents to make final approval decisions.

---

## **Key Features**

- Concurrent background processing
- Database-level locking for safe parallel processing
- Modular service-based architecture
- REST APIs for loan and agent operations

---

## **Tech Stack**

- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**
- **SLF4J Logging**
- **JUnit 5 + Mockito** (Unit Testing)

---

## **System Workflow**

The system processes loans through the following lifecycle:

```
Customer submits loan
        ↓
Loan stored with status APPLIED
        ↓
Background worker processes loan
        ↓
System decision:
   APPROVED_BY_SYSTEM
   REJECTED_BY_SYSTEM
   UNDER_REVIEW
        ↓
If UNDER_REVIEW → agent assignment
        ↓
Agent reviews and approves/rejects
```

---

## **Project Structure**

```
LoanOriginationSystem
│
├── src/main/java/LoanOriginationSystem
│
│   ├── controller
│   │   ├── AgentController
│   │   ├── AssignmentController
│   │   ├── CustomerController
│   │   └── LoanController
│
│   ├── dto
│   │   ├── AgentDecisionRequest
│   │   ├── AgentRequest
│   │   ├── LoanAssignmentViewDTO
│   │   ├── LoanRequestDTO
│   │   ├── LoanStatusCountProjection
│   │   └── TopCustomerProjection
│
│   ├── entity
│   │   ├── Agent
│   │   ├── Loan
│   │   └── LoanAssignment
│
│   ├── enums
│   │   ├── AgentStatus
│   │   ├── ApplicationStatus
│   │   └── LoanType
│
│   ├── exception
│   │   ├── AgentNotAuthorizedException
│   │   ├── InvalidDecisionException
│   │   ├── LoanAlreadyDecidedException
│   │   ├── LoanNotAssignedException
│   │   ├── LoanNotFoundException
│   │   └── GlobalExceptionHandler
│
│   ├── repository
│   │   ├── AgentRepository
│   │   ├── LoanRepository
│   │   └── LoanAssignmentRepository
│
│   ├── service
│   │   ├── AgentAssignmentService
│   │   ├── AgentDecisionService
│   │   ├── AgentService
│   │   ├── CustomerQueryService
│   │   ├── LoanMonitoringService
│   │   ├── LoanProcessorService
│   │   ├── LoanRegistrationService
│   │   ├── LoanViewService
│   │   └── notification
│
│   ├── worker
│   │   ├── AgentAssignmentWorker
│   │   └── LoanProcessorWorker
│
│   └── LoanOriginationSystemApplication
│
├── src/main/resources
│   └── application.properties
│
├── src/test/java/LoanOriginationSystem
│   ├── service
│   │   ├── AgentAssignmentServiceTest
│   │   ├── AgentDecisionServiceTest
│   │   ├── AgentServiceTest
│   │   ├── LoanMonitoringServiceTest
│   │   ├── LoanProcessorServiceTest
│   │   └── LoanRegistrationServiceTest
│
│   └── worker
│       ├── AgentAssignmentWorkerTest
│       └── LoanProcessorWorkerTest
│
├── postman
│   ├── loan-origination-system.postman_collection.json
│   └── loan-origination-local.postman_environment.json
│
├── pom.xml
└── README.md
```

---

## **Core Components**

### **LoanRegistrationService**

Handles new loan applications.

Creates a loan with initial status:

```
APPLIED
```

---

### **LoanProcessorWorker + LoanProcessorService**

Background workers process loans automatically.

Each worker repeatedly:

- Fetches loans with status **APPLIED**
- Processes them
- Updates the final status

Possible outcomes:

```
APPROVED_BY_SYSTEM
REJECTED_BY_SYSTEM
UNDER_REVIEW
```

Database locking prevents multiple threads from processing the same loan.

---

### **AgentAssignmentWorker + AgentAssignmentService**

Loans that require manual review are assigned to agents.

Steps:

1. Fetch loans in **UNDER_REVIEW**
2. Find an **AVAILABLE** agent
3. Create a **LoanAssignment**
4. Update statuses

```
loan → ASSIGNED_TO_AGENT
agent → BUSY
```

If no agent is available, the worker retries later.

---

### **AgentDecisionService**

Allows the assigned agent to approve or reject the loan.

Checks performed:

- Loan exists
- Loan is assigned
- Requesting agent is the assigned agent
- Decision is valid
- Loan not already decided

Final statuses:

```
APPROVED_BY_AGENT
REJECTED_BY_AGENT
```

After the decision, the agent becomes **AVAILABLE** again.

---

## **Concurrency Approach**

Multiple worker threads run concurrently.

To prevent duplicate processing, the system uses:

```
FOR UPDATE SKIP LOCKED
```

This ensures:

- Each loan is locked during processing
- Other threads skip locked rows
- No duplicate processing occurs

Transactions ensure atomic updates.

---

## **Database Indexing**

Indexes optimize worker queries.

```sql
CREATE INDEX idx_loan_status_created
ON loan(application_status, created_at);

CREATE INDEX idx_agent_status
ON agent(status);
```

These indexes help:

- Workers fetch loans by status efficiently
- Quickly locate available agents

---

## **REST APIs**

### **Loan APIs**

Submit loan

```
POST /api/v1/loans
```

Get all loans

```
GET /api/v1/loans/all
```

Get loans by status

```
GET /api/v1/loans?status=UNDER_REVIEW
```

Loan status counts

```
GET /api/v1/loans/status-count
```

---

### **Agent APIs**

Create agent

```
POST /api/v1/agents
```

List agents

```
GET /api/v1/agents
```

Agent decision

```
PUT /api/v1/agents/{agentId}/loans/{loanId}/decision
```

---

### **Monitoring APIs**

Top customers

```
GET /api/v1/customers/top
```

Loan assignments

```
GET /api/v1/assignments
```

---

## **Error Handling**

Custom exceptions are used for common scenarios.

| Exception | Scenario |
|----------|----------|
| LoanNotFoundException | Loan ID does not exist |
| LoanNotAssignedException | Loan not assigned to agent |
| AgentNotAuthorizedException | Agent not assigned to the loan |
| LoanAlreadyDecidedException | Decision already recorded |
| InvalidDecisionException | Invalid decision value |

A **global exception handler** converts these into appropriate HTTP responses.

---

## **Running the Application**

### **1. Create Database**

```sql
CREATE DATABASE los_db;
```

### **2. Configure `application.properties`**

```
spring.datasource.url=jdbc:postgresql://localhost:5432/los_db
spring.datasource.username=postgres
spring.datasource.password=postgres

spring.jpa.hibernate.ddl-auto=update
```

### **3. Build Project**

```
mvn clean install
```

### **4. Run Application**

```
mvn spring-boot:run
```

Server runs on:

```
http://localhost:8080
```

---

## **Testing**

Unit tests are included for core services and worker components.

Tests use:

- **JUnit 5**
- **Mockito**

Run tests:

```
mvn test
```

---

## **Postman Collection**

A Postman collection is included for testing the APIs.

Location:

```
postman/
```

Steps:

1. Import the collection into Postman
2. Import the environment file
3. Select the environment
4. Run the requests

The collection includes both **successful and failure scenarios**.

---

## **Notes**

If no agents are available when a loan requires manual review, the system logs a warning and retries assignment later.

Once an agent becomes available, the loan can be assigned automatically.

---

## **Author**

**Akshay Charjan**
