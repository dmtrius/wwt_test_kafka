# Spring Microservices Demo - WWT

## Build

mvn -f auth-api/pom.xml clean package -DskipTests
mvn -f data-api/pom.xml clean package -DskipTests

## Run

docker compose up -d --build

## Test

Register:
curl -X POST http://localhost:8080/api/auth/register \
-H "Content-Type: application/json" \
-d '{"email":"a@a.com","password":"pass"}'

Login:
curl -X POST http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{"email":"a@a.com","password":"pass"}'

Copy token from response.

Process:
curl -X POST http://localhost:8080/api/process \
-H "Authorization: Bearer <token>" \
-H "Content-Type: application/json" \
-d '{"text":"hello"}'
