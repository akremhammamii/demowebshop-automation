# Étape 1 : Build image
FROM maven:3.9.2-eclipse-temurin-17 AS build
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
