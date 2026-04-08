$ports = @(8761, 9001, 8083, 8082, 8084, 8086)

Write-Host "Scanning for running microservices by their ports..." -ForegroundColor Cyan

foreach ($port in $ports) {
    # Find the connection object listening on the specific port
    $connection = Get-NetTCPConnection -LocalPort $port -State Listen -ErrorAction SilentlyContinue
    
    if ($connection) {
        $pidToKill = $connection.OwningProcess
        Write-Host "Found process $pidToKill listening on port $port. Stopping it..." -ForegroundColor Yellow
        Stop-Process -Id $pidToKill -Force -ErrorAction SilentlyContinue
        Write-Host "✅ Stopped service on port $port" -ForegroundColor Green
    } else {
        Write-Host "No active service found on port $port" -ForegroundColor DarkGray
    }
}

Write-Host "All microservices have been successfully stopped!" -ForegroundColor Cyan
