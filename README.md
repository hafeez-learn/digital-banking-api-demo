# Digital Banking API Demo

A simplified microservices-based digital banking backend demonstrating core banking operations with proper security and inter-service communication.

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        API Gateway (:8080)                       │
└──────────┬──────────────────┬──────────────────┬─────────────────┘
           │                  │                  │
           ▼                  ▼                  ▼
┌──────────────────┐ ┌──────────────────┐ ┌────────────────────┐
│   Account        │ │   Transaction    │ │   Payment API      │
│   Service        │ │   Service        │ │   (PayNow-style)   │
│   :8081          │ │   :8082          │ │   :8083            │
└──────────────────┘ └──────────────────┘ └────────────────────┘
           │                  │
           └──────────────────┘
                    │
              ┌─────┴─────┐
              │   MySQL   │
              │   :3306   │
              └───────────┘
```

## 🚀 Features

- **API Gateway** - Single entry point for all services, routes requests to appropriate microservices
- **Account Service** - Account creation, balance inquiry, status management
- **Transaction Service** - Fund transfers with balance validation via Account Service, transaction history, audit logging
- **Payment API** - PayNow-style QR payments, payment confirmation

## 🔒 Security

- **NRIC Protection** - NRIC is never exposed in API responses; only masked values (****XXXX) are returned for display
- **Balance Validation** - Transaction service validates balances via Account Service before processing transfers
- **Input Validation** - All requests validated with Jakarta Bean Validation
- **Transactional Integrity** - Fund transfers are atomic with @Transactional

## 🛠️ Tech Stack

- Java 17, Spring Boot 3.2
- REST APIs, Maven
- MySQL 8.0, JPA/Hibernate
- Spring WebFlux for inter-service communication
- Microservices architecture

## 📁 Project Structure

```
digital-banking-api-demo/
├── api-gateway/                 # API Gateway - single entry point
│   ├── src/main/java/com/banking/gateway/
│   │   ├── controller/GatewayController.java
│   │   └── config/GatewayConfig.java
│   └── pom.xml
├── account-service/            # Account management microservice
│   ├── src/main/java/com/banking/account/
│   │   ├── controller/AccountController.java
│   │   ├── service/AccountService.java
│   │   ├── repository/AccountRepository.java
│   │   ├── model/Account.java
│   │   └── dto/AccountRequest.java, AccountResponse.java
│   └── pom.xml
├── transaction-service/        # Transaction management microservice
│   ├── src/main/java/com/banking/transaction/
│   │   ├── controller/TransactionController.java
│   │   ├── service/TransactionService.java
│   │   ├── repository/TransactionRepository.java
│   │   ├── model/Transaction.java
│   │   └── dto/TransferRequest.java, TransactionResponse.java
│   └── pom.xml
├── payment-api/                # PayNow-style payment API
│   ├── src/main/java/com/banking/payment/
│   │   ├── controller/PaymentController.java
│   │   ├── service/PaymentService.java
│   │   ├── model/Payment.java
│   │   └── dto/GenerateQrRequest.java, PaymentResponse.java
│   └── pom.xml
├── system-design/              # System design documentation
│   └── SYSTEM_DESIGN.md
├── docs/                       # Additional documentation
│   ├── API_DOCUMENTATION.md
│   └── DEPLOYMENT_GUIDE.md
├── docker-compose.yml          # Container orchestration
└── README.md
```

## 📌 Purpose

This project showcases:
- Microservices architecture with API Gateway pattern
- Inter-service communication (Transaction → Account)
- Security best practices (PII masking)
- REST API development with Spring Boot
- System design thinking for digital banking platforms
- Senior backend engineering skills

## 🔧 Quick Start

See [DEPLOYMENT_GUIDE.md](docs/DEPLOYMENT_GUIDE.md) for detailed setup instructions.

### Using Docker (Recommended)

```bash
docker-compose up --build
```

### Manual Build

```bash
# Build all services
cd api-gateway && mvn clean package -DskipTests
cd account-service && mvn clean package -DskipTests
cd transaction-service && mvn clean package -DskipTests
cd payment-api && mvn clean package -DskipTests
```

## 📡 API Endpoints

### Via API Gateway (Port 8080)

| Service | Endpoint | Method | Description |
|---------|----------|--------|-------------|
| Gateway | `/api/health` | GET | Health check |
| Account | `/api/accounts` | POST | Create account |
| Account | `/api/accounts/{accNo}` | GET | Get account details |
| Account | `/api/accounts/{accNo}/balance` | GET | Get balance |
| Account | `/api/accounts/{accNo}/status` | PUT | Update account status |
| Transaction | `/api/transfers` | POST | Initiate transfer |
| Transaction | `/api/transactions/{ref}` | GET | Get transaction |
| Transaction | `/api/accounts/{accNo}/transactions` | GET | Account transaction history |
| Payment | `/api/payments/generate-qr` | POST | Generate PayNow QR |
| Payment | `/api/payments/confirm` | POST | Confirm payment |

### Direct Service Access (Development only)

| Service | Port | Base Path |
|---------|------|-----------|
| Account Service | 8081 | `/api/accounts` |
| Transaction Service | 8082 | `/api/transfers`, `/api/transactions` |
| Payment API | 8083 | `/api/payments` |

## 🔐 Security Notes

- NRIC data is stored but **never exposed** in API responses
- Balance validation is performed server-side via inter-service calls
- All monetary amounts use `BigDecimal` for precision
- Transactions are atomic with proper rollback support

See [API_DOCUMENTATION.md](docs/API_DOCUMENTATION.md) for full API reference.
