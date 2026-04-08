$ports = @(8084)

Write-Host "Targeting Order and Restaurant services (Ports 8083, 8084)..." -ForegroundColor Cyan

foreach ($port in $ports) {
    $connection = Get-NetTCPConnection -LocalPort $port -State Listen -ErrorAction SilentlyContinue
    if ($connection) {
        $pidToKill = $connection.OwningProcess
        Write-Host "Force stopping PID $pidToKill on port $port..." -ForegroundColor Yellow
        Stop-Process -Id $pidToKill -Force -ErrorAction SilentlyContinue
    }
}

Write-Host "Restarting services..." -ForegroundColor Cyan

Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd order-cart-service; mvn spring-boot:run"
Write-Host "Order Cart Service starting..." -ForegroundColor Green

Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd restaurant-service; mvn spring-boot:run"
Write-Host "Restaurant Service starting..." -ForegroundColor Green

Write-Host "Both services are now booting up!" -ForegroundColor Cyan
