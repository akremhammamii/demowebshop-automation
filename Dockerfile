# Étape 1 : Build image
FROM maven:3.9.2-eclipse-temurin-17 AS build

# Install Chrome dependencies and tools
RUN apt-get update && apt-get install -y --no-install-recommends \
    wget \
    gnupg \
    unzip \
    ca-certificates \
    curl \
    fonts-liberation \
    libnss3 \
    libx11-xcb1 \
    libxcomposite1 \
    libxcursor1 \
    libxdamage1 \
    libxext6 \
    libxi6 \
    libxrandr2 \
    libxrender1 \
    libxtst6 \
    xdg-utils \
    && rm -rf /var/lib/apt/lists/*

# Install Google Chrome stable
RUN wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | gpg --dearmor -o /usr/share/keyrings/google-chrome-keyring.gpg \
    && echo "deb [arch=amd64 signed-by=/usr/share/keyrings/google-chrome-keyring.gpg] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update \
    && apt-get install -y google-chrome-stable \
    && rm -rf /var/lib/apt/lists/*

# Install ChromeDriver using Chrome for Testing API (for Chrome 115+)
RUN CHROME_VERSION=$(google-chrome --version | awk '{print $3}') \
    && CHROME_MAJOR="${CHROME_VERSION%%.*}" \
    && echo "Chrome version: ${CHROME_VERSION} (major: ${CHROME_MAJOR})" \
    && CHROMEDRIVER_VERSION=$(wget -qO- "https://googlechromelabs.github.io/chrome-for-testing/LATEST_RELEASE_${CHROME_MAJOR}") \
    && echo "ChromeDriver version: ${CHROMEDRIVER_VERSION}" \
    && wget -q "https://storage.googleapis.com/chrome-for-testing-public/${CHROMEDRIVER_VERSION}/linux64/chromedriver-linux64.zip" -O /tmp/chromedriver.zip \
    && unzip /tmp/chromedriver.zip -d /tmp \
    && mv /tmp/chromedriver-linux64/chromedriver /usr/local/bin/chromedriver \
    && chmod +x /usr/local/bin/chromedriver \
    && rm -rf /tmp/* \
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
