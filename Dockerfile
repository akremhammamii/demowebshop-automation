# Étape 1 : Build image
FROM maven:3.9.2-eclipse-temurin-17 AS build

# Install Google Chrome stable and ChromeDriver
RUN apt-get update && apt-get install -y \
    wget \
    gnupg2 \
    unzip \
    ca-certificates \
    && wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | gpg --dearmor -o /usr/share/keyrings/google-chrome-keyring.gpg \
    && echo "deb [arch=amd64 signed-by=/usr/share/keyrings/google-chrome-keyring.gpg] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update \
    && apt-get install -y google-chrome-stable \
    && CHROME_MAJOR=$(google-chrome --version | awk '{print $3}' | cut -d '.' -f 1) \
    && CHROMEDRIVER_VERSION=$(wget -qO- "https://chromedriver.storage.googleapis.com/LATEST_RELEASE_${CHROME_MAJOR}") \
    && wget -q "https://chromedriver.storage.googleapis.com/${CHROMEDRIVER_VERSION}/chromedriver_linux64.zip" -O /tmp/chromedriver.zip \
    && unzip /tmp/chromedriver.zip -d /tmp \
    && mv /tmp/chromedriver /usr/local/bin/chromedriver \
    && chmod +x /usr/local/bin/chromedriver \
    && rm -rf /tmp/* /var/lib/apt/lists/* \
    && echo "Versions installées:" \
    && google-chrome --version \
    && chromedriver --version

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
