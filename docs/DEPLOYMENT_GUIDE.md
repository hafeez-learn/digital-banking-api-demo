# Deployment Guide

## Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8.0+
- Docker (optional)

## Local Development Setup

### 1. Database Setup
```sql
CREATE DATABASE account_db;
CREATE DATABASE transaction_db;
CREATE DATABASE payment_db;

CREATE USER 'banking_user'@'localhost' IDENTIFIED BY 'banking_password';
GRANT ALL PRIVILEGES ON account_db.* TO 'banking_user'@'localhost';
GRANT ALL PRIVILEGES ON transaction_db.* TO 'banking_user'@'localhost';
GRANT ALL PRIVILEGES ON payment_db.* TO 'banking_user'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Build Services
```bash
# Account Service
cd account-service
mvn clean package -DskipTests
java -jar target/account-service-1.0.0.jar

# Transaction Service (in new terminal)
cd transaction-service
mvn clean package -DskipTests
java -jar target/transaction-service-1.0.0.jar

# Payment API (in new terminal)
cd payment-api
mvn clean package -DskipTests
java -jar target/payment-api-1.0.0.jar
```

### 3. Verify Services
```bash
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
curl http://localhost:8083/actuator/health
```

## Docker Deployment

### Build Images
```bash
docker build -t digital-banking/account-service ./account-service
docker build -t digital-banking/transaction-service ./transaction-service
docker build -t digital-banking/payment-api ./payment-api
```

### Run with Docker Compose
```bash
docker-compose up -d
```

## Production Considerations

### Security
- Enable JWT authentication
- Use HTTPS for all communications
- Implement rate limiting at API Gateway
- Add circuit breakers (Resilience4j)
- Enable request/response logging and audit trails

### Scalability
- Container orchestration with Kubernetes
- Database connection pooling
- Redis caching for frequently accessed data
- Read replicas for balance inquiries
- Message queues for async transaction processing

### Monitoring
- Implement distributed tracing (Zipkin/Jaeger)
- Set up metrics collection (Prometheus/Grafana)
- Configure alerting for critical issues
- Log aggregation (ELK Stack)
