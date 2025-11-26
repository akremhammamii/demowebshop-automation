# Dockerfile for Test Execution
FROM maven:3.9.2-eclipse-temurin-17

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

# Pre-download Maven dependencies (optional, speeds up test execution)
RUN mvn dependency:go-offline || true

# Execute tests when container runs (not during build)
CMD ["mvn", "clean", "test", "allure:report"]
