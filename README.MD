# Totos Backends

## Overview

Totos Backends is a microservices-based backend showcase built with Spring Boot. It demonstrates a secure, containerized architecture with an API Gateway, custom JWT authentication (RSA keys), user management, and product management services. The project is suitable for portfolio demos and small-scale deployments.

Key goals:
- Service separation and modularity
- JWT-based authentication without OAuth2
- Containerized services with Docker Compose
- Clear examples of routing, role-based authorization, and persistence

## Tech Stack

- Language: Java 21 (Eclipse Temurin)
- Framework: Spring Boot 3.x
- Build: Maven (mvnw included)
- Database: MySQL 8.x
- Containerization: Docker, Docker Compose
- Security: Spring Security + JWT (RSA keys)
- Service communication: Spring Cloud Gateway, OpenFeign (where used)
- Persistence: Spring Data JPA / Hibernate
- Rate limiting: Bucket4j (in Auth API)
- Utility: Lombok (models/reducers), Spring Actuator (recommended)
- Testing: JUnit / Spring Boot Test

## Repository Layout (high level)

- totos-apigateway/apigateway — API Gateway (routing, auth filter)
- totos-authAPI/auth-api — Auth service (register/login, JWT signing)
- totos-usersAPI/usersAPI — Users service (client/examples)
- totos-productsAPI/productsAPI — Products service (CRUD, duplicates protection)
- docker-compose.yaml — orchestration for local demo
- .env — environment variables (not committed)
- init.sql — DB initialization (databases/schema)

## Architecture (summary)

Client -> API Gateway -> { Auth API, Users API, Products API }  
All services connect to MySQL. Gateway enforces JWT filter and routes requests to downstream services. Auth API issues JWT signed with RSA private key; other services validate with public key.

Key properties:
- Stateless authentication with JWT
- Role-based access control embedded in token claims
- Keys persisted via environment variables (base64-encoded PEM) for containerized runs

## Generate RSA keys (recommended)

Generate keys and convert to base64 (one-liners):

Linux / macOS:
```bash
# generate 2048-bit private key (PKCS#8 recommended)
openssl genpkey -algorithm RSA -out private.pem -pkeyopt rsa_keygen_bits:2048
# extract public key (X.509)
openssl rsa -pubout -in private.pem -out public.pem
# base64 single-line values (no headers/footers)
grep -v "-----" private.pem | tr -d '\n' > private_b64.txt
grep -v "-----" public.pem  | tr -d '\n' > public_b64.txt
```

Important: remove PEM headers/footers and newlines; store the pure base64 text in env.

## Run locally with Docker Compose

From repository root:

```bash
docker-compose up --build
```

Services and ports (defaults):
- API Gateway — http://localhost:8080
- Auth API — http://localhost:8081
- Users API — http://localhost:8082
- Products API — http://localhost:8083
- MySQL — 3306

## Key Endpoints (examples)

Auth API (8081)
- POST /auth/register — register user (first registered user becomes ADMIN)
- POST /auth/login — login; returns access token (and optionally refresh token)
- GET /auth/public-key — returns public key for other services (if implemented)

Products API (8083)
- GET /products — list products (public or secured depending on config)
- POST /products — create (checks duplicates by name+description)
- GET /products/{id} — get by id (role-restricted in some endpoints)

Include header for secured endpoints:
```
Authorization: Bearer <access-token>
```

## Troubleshooting

- JWT errors → verify base64 encoding and that service uses same persistent keys.
- Lombok/IDE errors → install Lombok plugin and use compatible JDK.
- DB errors → check `init.sql`, docker volumes, and env DB credentials.

## Notes

- This repository is intended as a learning / portfolio project demonstrating microservices concepts and custom JWT handling without OAuth2.
- For production readiness, replace local secret handling with a secure secret manager and include additional hardening and observability features.