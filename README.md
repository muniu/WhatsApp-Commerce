# WhatsApp Commerce
WhatsApp powered Small Business OS

A WhatsApp commerce system leveraging WhatsApp Business API's native catalog feature, enabling businesses to manage orders through simple commands and automated workflows.

## Table of Contents
- [Features](#features)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Development Setup](#development-setup)
- [Configuration](#configuration)
- [Command System](#command-system)
- [Event System](#event-system)
- [Monitoring](#monitoring)
- [Troubleshooting](#troubleshooting)
- [Testing](#testing)
- [Deployment](#deployment)
- [Contributing](#contributing)
- [License](#license)

## Features

### Core Features
1. [x] 🛍️ Native WhatsApp Catalog integration
2. [x] 📦 Order management through WhatsApp
3. [ ] 🌍 Multi-language support (EN/SW)
4. [ ] 🔄 Event-driven architecture
5. [ ] 📊 Real-time monitoring

### Catalog Management
1. [x] WhatsApp Business Catalog sync
2. [x] External catalog integration (Webhook/REST/Kafka)
3. [x] Multi-currency pricing
4. [x] Multi-language descriptions
5. [x] Image handling

### Order Management
1. [ ] Order creation from catalog items
2. [ ] Status tracking
3. [ ] Order history
4. [ ] Cancellation handling
5. [ ] Support integration

### External Catalog Integration

#### 1. Webhook Integration
```bash
POST /api/v1/catalog/webhook
Content-Type: application/json
X-API-Key: your-api-key

{
  "action": "UPSERT|DELETE",
  "items": [{
    "externalId": "SKU123",
    "names": {
      "en": "Product Name",
      "sw": "Jina la Bidhaa"
    },
    "price": 99.99,
    "currency": "KES",
    "imageUrl": "https://..."
  }]
}
```

#### 2. REST API
```bash
# Create/Update Products
POST /api/v1/catalog/items
PUT /api/v1/catalog/items/{id}

# Retrieve Products
GET /api/v1/catalog/items
GET /api/v1/catalog/items/{id}

# Remove Products
DELETE /api/v1/catalog/items/{id}
```

#### 3. Kafka Events
Topic: `catalog-events`
```json
{
  "eventType": "CATALOG_UPDATED",
  "timestamp": "2024-11-20T10:00:00Z",
  "payload": {
    "action": "UPSERT|DELETE",
    "items": [{
      "externalId": "SKU123",
      "names": {
        "en": "Product Name",
        "sw": "Jina la Bidhaa"
      },
      "price": 99.99,
      "currency": "KES",
      "imageUrl": "https://..."
    }]
  }
}
```

## Prerequisites

* Java 17+
* Docker & Docker Compose
* MongoDB 5.0+
* Apache Kafka
* WhatsApp Business API access

## Quick Start

1. **Clone the Repository**
```bash
git clone git@github.com:muniu/WhatsApp-Commerce.git
cd whatsapp-commerce
```

2. **Configure Environment**
```bash
cp .env.example .env
```

Edit `.env` with your credentials:
```properties
# WhatsApp Configuration
WHATSAPP_BUSINESS_ACCOUNT_ID=your_account_id
WHATSAPP_ACCESS_TOKEN=your_access_token
WHATSAPP_PHONE_NUMBER_ID=your_phone_number_id

# MongoDB Configuration
MONGODB_URI=mongodb://localhost:27017/whatsapp_commerce

# Kafka Configuration
KAFKA_BROKERS=localhost:9092
KAFKA_SCHEMA_REGISTRY=http://localhost:8081
KAFKA_UI_PORT=8080
```

3. **Start Services**
```bash
docker-compose up -d
```

4. **Initialize Kafka Topics**
```bash
docker-compose exec kafka kafka-topics.sh \
  --create \
  --bootstrap-server localhost:9092 \
  --replication-factor 1 \
  --partitions 3 \
  --topic catalog-events

docker-compose exec kafka kafka-topics.sh \
  --create \
  --bootstrap-server localhost:9092 \
  --replication-factor 1 \
  --partitions 3 \
  --topic order-events
```

5. **Verify Installation**
```bash
# Check application health
curl http://localhost:8080/actuator/health

# Check Kafka UI
open http://localhost:8081

# Check Monitoring
open http://localhost:3000
```

## Development Setup

### Building
```bash
./mvnw clean package
```

### Running Locally
```bash
# Development profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Production profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

## Configuration

### Application Configuration
```yaml
whatsapp:
  business:
    account-id: ${WA_BUSINESS_ACCOUNT_ID}
    access-token: ${WA_ACCESS_TOKEN}
    phone-number-id: ${WA_PHONE_NUMBER_ID}
    api-version: v18.0
    base-url: https://graph.facebook.com

spring:
  data:
    mongodb:
      uri: ${MONGODB_URI:mongodb://localhost:27017/whatsapp_commerce}
  kafka:
    bootstrap-servers: ${KAFKA_SERVERS:localhost:9092}

app:
  supported-languages:
    - en
    - sw
```

## Command System

### Available Commands
```
/status <order_id>    - Check order status
/orders               - View recent orders
/track <order_id>     - Track order delivery
/cancel <order_id>    - Request cancellation
/help                 - Show available commands
/support              - Start support chat
/lang <en|sw>         - Change language
```

## Event System

### Incoming Events
| Event | Description |
|-------|-------------|
| `CATALOG_UPDATED` | Product catalog updates |
| `PAYMENT_STATUS_UPDATED` | Payment confirmations |
| `DELIVERY_STATUS_UPDATED` | Delivery updates |

### Outgoing Events
| Event | Description |
|-------|-------------|
| `ORDER_CREATED` | New order placed |
| `ORDER_CANCELLED` | Order cancellation requested |
| `SUPPORT_REQUESTED` | Customer requested support |

## Monitoring

### Available Dashboards

#### Kafka UI (http://localhost:8081)
- Topic management
- Consumer group monitoring
- Message browser
- ACL management

#### Grafana (http://localhost:3000)
- System metrics
- Business metrics
- API monitoring
- Event processing

### Key Metrics
```
# Order Metrics
orders_created_total
orders_completed_total
order_processing_time_seconds

# WhatsApp Metrics
whatsapp_messages_sent_total
whatsapp_messages_received_total
whatsapp_api_errors_total

# Event Metrics
events_published_total
events_consumed_total
```

## Testing

### Unit Tests
```bash
./mvnw test
```

### Integration Tests
```bash
./mvnw verify -P integration-tests
```

### API Tests
```bash
# Test webhook
curl -X POST http://localhost:8080/api/webhook \
  -H "Content-Type: application/json" \
  -d '{
    "entry": [{
      "changes": [{
        "value": {
          "messages": [{
            "from": "254712345678",
            "text": {
              "body": "/status ORD123"
            }
          }]
        }
      }]
    }]
  }'
```

## Deployment

### Docker Deployment
```bash
# Build image
docker build -t whatsapp-commerce .

# Run services
docker-compose up -d
```

### Environment Variables
```properties
# Required
WHATSAPP_BUSINESS_ACCOUNT_ID=
WHATSAPP_ACCESS_TOKEN=
WHATSAPP_PHONE_NUMBER_ID=

# Optional
MONGODB_URI=mongodb://localhost:27017/whatsapp_commerce
KAFKA_SERVERS=localhost:9092
```

## Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see [LICENSE](LICENSE) for details.

## Support

- 📝 [Documentation](docs/README.md)
- 🐛 [Issue Tracker](https://github.com/muniu/whatsapp-commerce/issues)
- 💬 [Discussion Forum](https://github.com/muniu/whatsapp-commerce/discussions)
- 📧 [Support Email](mailto:me@kanairo.com)

---

Built with ❤️ for E.African Commerce