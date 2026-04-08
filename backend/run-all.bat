@echo off
echo Starting Eureka Server...
start "Eureka Server" cmd /k "cd eureka-server && mvn spring-boot:run"

echo Waiting 15 seconds for Eureka Server to initialize...
timeout /t 15

echo Starting API Gateway...
start "API Gateway" cmd /k "cd api-gateway && mvn spring-boot:run"

echo Starting remaining microservices...
start "User Service" cmd /k "cd user-service && mvn spring-boot:run"
start "Restaurant Service" cmd /k "cd restaurant-service && mvn spring-boot:run"
start "Payment Service" cmd /k "cd payment-service && mvn spring-boot:run"
start "Order Cart Service" cmd /k "cd order-cart-service && mvn spring-boot:run"

echo All microservices are starting up in separate windows!
pause
