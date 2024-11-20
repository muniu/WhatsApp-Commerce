# WhatsApp Commerce
 WhatsApp powered Small Business OS

A production-ready WhatsApp commerce system leveraging WhatsApp Business API's native catalog feature, enabling businesses to manage orders through simple commands and automated workflows.

# Table of Contents

* Features
* Architecture
* Prerequisites
* Quick Start
* Development Setup
* Configuration
* Command System
* Event System
* Monitoring
* Troubleshooting
* Testing
* Deployment
* Contributing
* License

## Features
### Core Features

* 🛍️ Native WhatsApp Catalog integration
* 📦 Order management through WhatsApp
* 🌍 Multi-language support (EN/SW)
* 🔄 Event-driven architecture
* 📊 Real-time monitoring

### Catalog Management

* Automatic sync with WhatsApp Business Catalog
* Multi-currency pricing
* Multi-language product descriptions
* Stock management
* Image handling

### Order Management

* Order creation from catalog items
* Status tracking
* Order history
* Cancellation handling
* Support integration

### Supported Languages

* 🇬🇧 English
* 🇰🇪 Swahili

## Architecture 

TODO

## Prerequisites

* Java 17+
* Docker & Docker Compose
* MongoDB 5.0+
* Apache Kafka
* WhatsApp Business API access

## Quick Start

Clone the repository



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
WHATSAPP_BUSINESS_ACCOUNT_ID=your_account_id
WHATSAPP_ACCESS_TOKEN=your_access_token
WHATSAPP_PHONE_NUMBER_ID=your_phone_number_id
```

3. **Start Services**
```bash
docker-compose up -d
```

4. **Verify Installation**
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

### Available Profiles
- `dev`: Development configuration
- `prod`: Production configuration
- `test`: Testing configuration


# Command System

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

### Example Usage
```
Customer: /status ORD123
Bot: Order #ORD123
Status: In Progress
Created: Jan 15, 2024
Items: 2x Product A
Total: KES 2,500
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

#### Prometheus (http://localhost:9090)
- Raw metrics
- Query interface
- Alert rules

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

## Troubleshooting

### Common Issues

#### Webhook Verification
```bash
curl -X GET "http://localhost:8080/api/webhook\
?hub.mode=subscribe\
&hub.verify_token=your_token\
&hub.challenge=challenge_string"
```

#### Kafka Connectivity
```bash
# List topics
docker-compose exec kafka kafka-topics.sh \
  --bootstrap-server localhost:9092 \
  --list

# View consumer groups
docker-compose exec kafka kafka-consumer-groups.sh \
  --bootstrap-server localhost:9092 \
  --list
```

#### MongoDB Checks
```bash
# Access MongoDB shell
docker-compose exec mongodb mongosh

# Check indexes
use whatsapp_commerce
db.orders.getIndexes()
```

### Logging
```yaml
logging:
  level:
    com.whatsappcommerce: DEBUG
    org.apache.kafka: INFO
    org.mongodb: INFO
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

### Load Tests
```bash
k6 run load-tests/order-flow.js
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

### Development Guidelines
- Follow Java coding standards
- Write unit tests for new features
- Update documentation
- Add appropriate logging
- Include monitoring metrics

## License

This project is licensed under the MIT License - see [LICENSE](LICENSE) for details.

---

## Support

- 📝 [Documentation](README.md)
- 🐛 [Issue Tracker](https://github.com/muniu/whatsapp-commerce/issues)
- 💬 [Discussion Forum](https://github.com/muniu/whatsapp-commerce/discussions)
- 📧 [Support Email](mailto:me@kanairo.com)

---

Built with ❤️ for E.African Commerce