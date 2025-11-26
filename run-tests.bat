@echo off
REM Script pour lancer les tests dans Docker
echo 🚀 Lancement des tests dans Docker...

docker run --rm ^
  -v "%cd%:/workspace" ^
  -w /workspace ^
  --user root ^
  --shm-size=2g ^
  demowebshop-dev ^
  mvn test -Dcucumber.filter.tags="not @bug"

if %ERRORLEVEL% EQU 0 (
  echo.
  echo ✅ Tests réussis!
) else (
  echo.
  echo ❌ Tests échoués
  exit /b 1
)
