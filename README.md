# Digital Banking API Demo

A simplified microservices-based digital banking backend demonstrating core banking operations.

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        API Gateway                               │
└──────────┬──────────────────┬──────────────────┬─────────────────┘
           │                  │                  │
           ▼                  ▼                  ▼
┌──────────────────┐ ┌──────────────────┐ ┌────────────────────┐
│   Account        │ │   Transaction    │ │   Payment API      │
│   Service        │ │   Service        │ │   (PayNow-style)   │
│   :8081          │ │   :8082          │ │   :8083            │
└──────────────────┘ └──────────────────┘ └────────────────────┘
```

## 🚀 Features

- **Account Service** - Account creation, balance inquiry, status management
- **Transaction Service** - Fund transfers, transaction history, audit logging
- **Payment API** - PayNow-style QR payments, payment confirmation

## 🛠️ Tech Stack

- Java 17, Spring Boot 3.2
- REST APIs, Maven
- MySQL, JPA/Hibernate
- Microservices architecture

## 📁 Project Structure

```
digital-banking-api-demo/
├── account-service/           # Account management microservice
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
├── payment-api/               # PayNow-style payment API
│   ├── src/main/java/com/banking/payment/
│   │   ├── controller/PaymentController.java
│   │   ├── service/PaymentService.java
│   │   ├── model/Payment.java
│   │   └── dto/GenerateQrRequest.java, PaymentResponse.java
│   └── pom.xml
├── system-design/             # System design documentation
│   └── SYSTEM_DESIGN.md
├── docs/                      # Additional documentation
│   ├── API_DOCUMENTATION.md
│   └── DEPLOYMENT_GUIDE.md
└── README.md
```

## 📌 Purpose

This project showcases:
- Microservices architecture design
- REST API development
- System design thinking relevant to digital banking platforms
- Senior backend engineering skills

## 🔧 Quick Start

See [DEPLOYMENT_GUIDE.md](docs/DEPLOYMENT_GUIDE.md) for detailed setup instructions.

```bash
# Build all services
cd account-service && mvn clean package -DskipTests
cd transaction-service && mvn clean package -DskipTests
cd payment-api && mvn clean package -DskipTests
```

## 📡 API Endpoints

| Service | Endpoint | Description |
|---------|----------|-------------|
| Account | POST /api/accounts | Create account |
| Account | GET /api/accounts/{accNo} | Get account details |
| Account | GET /api/accounts/{accNo}/balance | Get balance |
| Transaction | POST /api/transfers | Initiate transfer |
| Transaction | GET /api/transactions/{ref} | Get transaction |
| Payment | POST /api/payments/generate-qr | Generate PayNow QR |
| Payment | POST /api/payments/confirm | Confirm payment |

See [API_DOCUMENTATION.md](docs/API_DOCUMENTATION.md) for full API reference.
