# Script pour construire et tester dans Docker
# Usage: .\run-tests-in-docker.ps1

Write-Host "🚀 Construction de l'image Docker..." -ForegroundColor Cyan
docker build -f .devcontainer/Dockerfile.dev -t demowebshop-dev .

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Erreur lors de la construction de l'image" -ForegroundColor Red
    exit 1
}

Write-Host "✅ Image construite avec succès!" -ForegroundColor Green
Write-Host ""
Write-Host "🧪 Exécution des tests dans Docker..." -ForegroundColor Cyan

docker run --rm -it `
  -v "${PWD}:/workspace" `
  -w /workspace `
  --user root `
  --shm-size=2g `
  demowebshop-dev `
  mvn test -Dcucumber.filter.tags="not @bug"

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "✅ Tests réussis!" -ForegroundColor Green
} else {
    Write-Host ""
    Write-Host "❌ Tests échoués" -ForegroundColor Red
    exit 1
}
