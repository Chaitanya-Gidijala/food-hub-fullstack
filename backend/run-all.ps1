# Start Eureka Server first and wait for it to boot up
Write-Host "Starting Eureka Server..." -ForegroundColor Cyan
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd eureka-server; mvn spring-boot:run"

Write-Host "Waiting 15 seconds for Eureka Server to initialize..." -ForegroundColor Yellow
Start-Sleep -Seconds 15

# Start the rest of the services
Write-Host "Starting other background microservices..." -ForegroundColor Cyan

Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd user-service; mvn spring-boot:run"
Start-Sleep -Seconds 2

Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd restaurant-service; mvn spring-boot:run"
Start-Sleep -Seconds 2

Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd payment-service; mvn spring-boot:run"
Start-Sleep -Seconds 2

Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd order-cart-service; mvn spring-boot:run"
Start-Sleep -Seconds 2

Write-Host "Starting API Gateway..." -ForegroundColor Cyan
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd api-gateway; mvn spring-boot:run"

Write-Host "All services are booting up in separate terminal windows!" -ForegroundColor Green
