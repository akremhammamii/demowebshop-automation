# Étape 1 : Build image
FROM maven:3.9.2-eclipse-temurin-17 AS build

# Installer dépendances
RUN apt-get update && apt-get install -y \
    wget gnupg2 ca-certificates unzip curl && \
    rm -rf /var/lib/apt/lists/*

# Ajouter dépôt officiel Google Chrome
RUN wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | gpg --dearmor -o /usr/share/keyrings/google-chrome-keyring.gpg && \
    echo "deb [arch=amd64 signed-by=/usr/share/keyrings/google-chrome-keyring.gpg] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list && \
    apt-get update && apt-get install -y google-chrome-stable && \
    rm -rf /var/lib/apt/lists/*

# Installer ChromeDriver automatiquement (Chrome for Testing)
RUN CHROME_VERSION=$(google-chrome --version | grep -oP '\d+\.\d+\.\d+\.\d+') && \
    echo "Chrome version: $CHROME_VERSION" && \
    # Télécharger la version correspondante depuis Chrome for Testing
    CHROMEDRIVER_URL="https://storage.googleapis.com/chrome-for-testing-public/${CHROME_VERSION}/linux64/chromedriver-linux64.zip" && \
    echo "Trying ChromeDriver URL: $CHROMEDRIVER_URL" && \
    if wget --spider "$CHROMEDRIVER_URL" 2>/dev/null; then \
    wget -O /tmp/chromedriver.zip "$CHROMEDRIVER_URL"; \
    else \
    echo "Exact version not found, using latest stable from Chrome for Testing" && \
    LATEST_VERSION=$(curl -s "https://googlechromelabs.github.io/chrome-for-testing/last-known-good-versions.json" | grep -oP '"Stable":\{"channel":"Stable","version":"\K[^"]+') && \
    CHROMEDRIVER_URL="https://storage.googleapis.com/chrome-for-testing-public/${LATEST_VERSION}/linux64/chromedriver-linux64.zip" && \
    wget -O /tmp/chromedriver.zip "$CHROMEDRIVER_URL"; \
    fi && \
    unzip /tmp/chromedriver.zip -d /tmp && \
    mv /tmp/chromedriver-linux64/chromedriver /usr/local/bin/ && \
    chmod +x /usr/local/bin/chromedriver && \
    rm -rf /tmp/* && \
    # Vérifier l'installation
    echo "Versions installées:" && \
    google-chrome --version && \
    chromedriver --version

# Copier projet Maven
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Run tests during build (this is a test framework, not an application)
# Tests will execute and the build will fail if tests fail
RUN mvn -B clean test -Dcucumber.filter.tags="not @bug"

# Optional: Generate Allure report
RUN mvn allure:report || true

# -------------------------------------------------
# Stage 2: Archive test results (optional)
# -------------------------------------------------
FROM alpine:latest
WORKDIR /reports

# Copy test reports from build stage
COPY --from=build /app/target/cucumber-reports /reports/cucumber/
COPY --from=build /app/target/surefire-reports /reports/surefire/
COPY --from=build /app/target/site/allure-maven-plugin /reports/allure/ || true

# This image just holds the test results
# You can extract them with: docker cp <container>:/reports ./local-reports
CMD ["echo", "Test execution completed. Reports available in /reports"]
