# API Documentation - Digital Banking API Demo

## Base URLs
- Account Service: `http://localhost:8081`
- Transaction Service: `http://localhost:8082`
- Payment API: `http://localhost:8083`

---

## Account Service API

### Create Account
```
POST /api/accounts
Content-Type: application/json

{
  "accountNumber": "ACC123456789",
  "holderName": "John Doe",
  "nric": "S123456789",
  "initialBalance": 1000.00
}

Response:
{
  "accountNumber": "ACC123456789",
  "holderName": "John Doe",
  "nric": "S123456789",
  "balance": 1000.00,
  "status": "ACTIVE",
  "createdAt": "2026-04-16T10:00:00"
}
```

### Get Account Details
```
GET /api/accounts/{accountNumber}

Response:
{
  "accountNumber": "ACC123456789",
  "holderName": "John Doe",
  "nric": "S123456789",
  "balance": 1000.00,
  "status": "ACTIVE",
  "createdAt": "2026-04-16T10:00:00"
}
```

### Get Account Balance
```
GET /api/accounts/{accountNumber}/balance

Response:
{
  "accountNumber": "ACC123456789",
  "balance": 1000.00
}
```

### Update Account Status
```
PUT /api/accounts/{accountNumber}/status?status=FROZEN

Response:
{
  "accountNumber": "ACC123456789",
  "status": "FROZEN",
  ...
}
```

---

## Transaction Service API

### Initiate Transfer
```
POST /api/transfers
Content-Type: application/json

{
  "fromAccount": "ACC123456789",
  "toAccount": "ACC987654321",
  "amount": 100.00
}

Response:
{
  "transactionRef": "TXN1234567890ABCD",
  "fromAccount": "ACC123456789",
  "toAccount": "ACC987654321",
  "amount": 100.00,
  "type": "TRANSFER",
  "status": "COMPLETED",
  "createdAt": "2026-04-16T10:05:00"
}
```

### Get Transaction
```
GET /api/transactions/{transactionRef}

Response:
{
  "transactionRef": "TXN1234567890ABCD",
  "fromAccount": "ACC123456789",
  "toAccount": "ACC987654321",
  "amount": 100.00,
  "type": "TRANSFER",
  "status": "COMPLETED",
  "createdAt": "2026-04-16T10:05:00"
}
```

### Get Account Transaction History
```
GET /api/accounts/{accountNumber}/transactions

Response:
[
  {
    "transactionRef": "TXN1234567890ABCD",
    "fromAccount": "ACC123456789",
    "toAccount": "ACC987654321",
    "amount": 100.00,
    "type": "TRANSFER",
    "status": "COMPLETED",
    "createdAt": "2026-04-16T10:05:00"
  }
]
```

---

## Payment API (PayNow-style)

### Generate Payment QR
```
POST /api/payments/generate-qr
Content-Type: application/json

{
  "payeeAccount": "ACC987654321",
  "amount": 50.00,
  "description": "Payment for order #12345"
}

Response:
{
  "paymentRef": "PAY1234567890ABCD",
  "payerAccount": null,
  "payeeAccount": "ACC987654321",
  "amount": 50.00,
  "qrData": "paynow://ACC987654321?amt=50.00&ref=PAY1234567890ABCD",
  "status": "PENDING",
  "expiryTime": "2026-04-16T10:10:00",
  "createdAt": "2026-04-16T10:05:00"
}
```

### Confirm Payment
```
POST /api/payments/confirm
Content-Type: application/json

{
  "paymentRef": "PAY1234567890ABCD",
  "payerAccount": "ACC123456789"
}

Response:
{
  "paymentRef": "PAY1234567890ABCD",
  "payerAccount": "ACC123456789",
  "payeeAccount": "ACC987654321",
  "amount": 50.00,
  "status": "COMPLETED",
  "createdAt": "2026-04-16T10:05:00"
}
```

### Get Payment Status
```
GET /api/payments/{paymentRef}

Response:
{
  "paymentRef": "PAY1234567890ABCD",
  "status": "PENDING",
  "amount": 50.00,
  ...
}
```

---

## Error Responses

All endpoints return appropriate HTTP status codes:
- `200 OK` - Success
- `400 Bad Request` - Invalid input
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

Error response format:
```json
{
  "error": "Error message description",
  "timestamp": "2026-04-16T10:00:00"
}
```
