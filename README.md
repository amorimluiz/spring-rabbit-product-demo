# spring-rabbit-product-demo

Minimal POC with **two Spring Boot services** communicating via **RabbitMQ**:

- **producer-service-rabbit** (Gradle): exposes `POST /products` and publishes the message (ProductDTO) to a queue.
- **consumer-service-rabbit** (Gradle): consumes the queue and **logs** the product.
- **commons** (Maven lib): shared classes (e.g., `ProductDTO`, contracts, and queue/exchange constants).

> Repository: https://github.com/amorimluiz/spring-rabbit-product-demo

---

## Architecture (quick view)

```
Client → POST /products  ──► Producer ──► RabbitMQ (exchange/queue) ──► Consumer ──► LOG
```

- The **commons** lib defines the DTO and (optionally) exchange/queue names.
- The **producer** serializes `ProductDTO` and publishes it to RabbitMQ (AMQP).
- The **consumer** listens to the queue and simply logs the received `ProductDTO`.

---

## Requirements

- **JDK 17+** (or the version used by the project)
- **Docker + Docker Compose** (to quickly spin up RabbitMQ)
- **Gradle Wrapper** (embedded in the Gradle projects) and **Maven** for `commons`

> The project includes a `docker-compose.yml` at the root to start **RabbitMQ** with **Management UI** (port 15672) and broker (port 5672).

---

## Start RabbitMQ

From the project root:

```bash
docker compose up -d
# or, depending on your version:
docker-compose up -d
```

Access management UI: http://localhost:15672  
Default credentials: **guest / guest**

---

## Build modules

### 1) commons (Maven)
```bash
cd commons
mvn -q -DskipTests install
cd ..
```

### 2) producer-service-rabbit (Gradle)
```bash
cd producer-service-rabbit
./gradlew -q clean build
cd ..
```

### 3) consumer-service-rabbit (Gradle)
```bash
cd consumer-service-rabbit
./gradlew -q clean build
cd ..
```

> Tip: publishing the `commons` lib locally via `mvn install` is enough for Gradle services to resolve the dependency.

---

## Configuration (application.yml/properties)

Each service has its own config. Key points:

- **RabbitMQ connection**:
    - `spring.rabbitmq.host=localhost`
    - `spring.rabbitmq.port=5672`
    - `spring.rabbitmq.username=guest`
    - `spring.rabbitmq.password=guest`
- **Exchange/queue/routingKey names**: check constants (in `commons` or `application.yml`).
- **HTTP port**: run on different ports to avoid conflicts.
    - Example: `--server.port=8081` for producer and `--server.port=8082` for consumer.

---

## Running services (dev mode)

In **two separate terminals**:

### Consumer
```bash
cd consumer-service-rabbit
./gradlew bootRun --args='--server.port=8082'
```

### Producer
```bash
cd producer-service-rabbit
./gradlew bootRun --args='--server.port=8081'
```

> You can also run the JARs built under `build/libs`:
> ```bash
> java -jar consumer-service-rabbit/build/libs/*-SNAPSHOT.jar --server.port=8082
> java -jar producer-service-rabbit/build/libs/*-SNAPSHOT.jar --server.port=8081
> ```

---

## Testing the flow (cURL)

Send a `POST` to the **producer**:

```bash
curl -X POST "http://localhost:8080/product"   -H "Content-Type: application/json"   -d '{
        "id": 1,
        "name": "Coffee beans",
        "price": 49.90,
      }'
```

**Expected result**:
- **Producer** responds with 2xx (e.g., 202/201) and publishes the `ProductDTO`.
- **Consumer** logs something like: `Received ProductDTO{id=..., name=..., ...}`

> Exact payload may vary depending on `ProductDTO` definition in **commons**.

---

## Troubleshooting

- If the message does **not** reach the consumer:
    1. Check RabbitMQ Management UI (http://localhost:15672) for **exchange**, **queue**, **binding**, and pending messages.
    2. Verify **credentials** and **host/port** in configs.
    3. Ensure **producer** and **consumer** use the **same names** (exchange/queue/routingKey).
- Common errors:
    - `ACCESS_REFUSED`: wrong user/virtual host.
    - `NOT_FOUND`: queue/exchange/binding missing.
    - `Connection refused`: RabbitMQ not running or wrong port.

---

## Project structure

```
spring-rabbit-product-demo/
├─ docker-compose.yml
├─ commons/                 # Maven lib (DTOs & contracts)
├─ producer-service-rabbit/ # HTTP service (POST /products) → publish message
└─ consumer-service-rabbit/ # Queue listener → log ProductDTO
```

---

## License

MIT