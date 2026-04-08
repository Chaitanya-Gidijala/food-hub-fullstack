@echo off
echo Scanning for running microservices by their ports...

:: Ports: Eureka(8761), API Gateway(9001), Order(8083), Payment(8082), Restaurant(8084), User(8086)
FOR %%p IN (8761 9001 8083 8082 8084 8086) DO (
    FOR /F "tokens=5" %%a IN ('netstat -aon ^| find "LISTENING" ^| findstr ":%%p"') DO (
        if "%%a" NEQ "0" (
            echo Found process %%a listening on port %%p. Stopping it...
            taskkill /F /PID %%a
        )
    )
)

echo.
echo All microservices have been successfully stopped!
pause
