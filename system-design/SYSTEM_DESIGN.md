# Digital Banking API Demo — System Design

## 1. Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                        API Gateway                               │
│                   (Spring Cloud Gateway)                        │
└──────────┬──────────────────┬──────────────────┬─────────────────┘
           │                  │                  │
           ▼                  ▼                  ▼
┌──────────────────┐ ┌──────────────────┐ ┌────────────────────┐
│   Account        │ │   Transaction    │ │   Payment API      │
│   Service        │ │   Service        │ │   (PayNow-style)   │
│   :8081          │ │   :8082          │ │   :8083            │
└────────┬─────────┘ └────────┬─────────┘ └─────────┬──────────┘
         │                    │                     │
         ▼                    ▼                     ▼
┌─────────────────────────────────────────────────────────────────┐
│                      MySQL Database                              │
│           (account_db / transaction_db / payment_db)            │
└─────────────────────────────────────────────────────────────────┘
```

## 2. Microservices Design

### Account Service (Port 8081)
- Manages customer accounts
- Handles account creation, balance inquiry, account activation/deactivation
- Owns the `accounts` table

### Transaction Service (Port 8082)
- Manages all financial transactions
- Handles fund transfers, transaction history, audit logging
- Owns the `transactions` table
- Publishes events for async processing

### Payment API (Port 8083)
- PayNow-style real-time payment simulation
- QR code generation for payments
- UEN/NRIC lookup simulation
- Payment confirmation and receipt generation

## 3. Database Schema

### accounts table
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT PK | Auto-increment |
| account_number | VARCHAR(20) UNIQUE | Account identifier |
| holder_name | VARCHAR(100) | Account holder |
| nric | VARCHAR(10) | National ID |
| balance | DECIMAL(15,2) | Current balance |
| status | ENUM | ACTIVE, INACTIVE, FROZEN |
| created_at | TIMESTAMP | Creation time |
| updated_at | TIMESTAMP | Last update |

### transactions table
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT PK | Auto-increment |
| transaction_ref | VARCHAR(30) UNIQUE | Unique reference |
| from_account | VARCHAR(20) | Source account |
| to_account | VARCHAR(20) | Destination account |
| amount | DECIMAL(15,2) | Transaction amount |
| type | ENUM | TRANSFER, DEPOSIT, WITHDRAWAL |
| status | ENUM | PENDING, COMPLETED, FAILED |
| created_at | TIMESTAMP | Transaction time |

### payments table
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT PK | Auto-increment |
| payment_ref | VARCHAR(30) UNIQUE | Payment reference |
| payer_account | VARCHAR(20) | Payer account |
| payee_account | VARCHAR(20) | Payee account |
| amount | DECIMAL(15,2) | Payment amount |
| qr_data | TEXT | QR code payload |
| status | ENUM | PENDING, COMPLETED, EXPIRED |
| expiry_time | TIMESTAMP | QR expiration |
| created_at | TIMESTAMP | Creation time |

## 4. API Endpoints

### Account Service
```
POST   /api/accounts              - Create account
GET    /api/accounts/{accountNumber} - Get account details
GET    /api/accounts/{accountNumber}/balance - Get balance
PUT    /api/accounts/{accountNumber}/status  - Update status
```

### Transaction Service
```
POST   /api/transfers             - Initiate fund transfer
GET    /api/transactions/{ref}     - Get transaction by ref
GET    /api/accounts/{accNo}/transactions - Transaction history
```

### Payment API
```
POST   /api/payments/generate-qr   - Generate PayNow QR
POST   /api/payments/confirm       - Confirm payment
GET    /api/payments/{ref}         - Get payment status
```

## 5. Key Design Decisions

### Why Microservices?
- Independent scaling per service
- Isolated failure domains
- Technology flexibility
- Team autonomy

### Why Synchronous Communication?
- simplicity for demo purposes
- HTTP/REST between services
- Can be enhanced with async messaging (Kafka) for production

### Security Considerations (Production)
- JWT-based authentication
- API gateway with rate limiting
- End-to-end encryption
- Audit logging
- Circuit breakers (Resilience4j)

## 6. Scalability Considerations

- Database sharding by account number prefix
- Read replicas for balance inquiries
- Message queue for transaction processing
- Caching layer (Redis) for frequently accessed data
- Container orchestration (Kubernetes) for auto-scaling
