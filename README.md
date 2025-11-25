# 🛍️ DemoWebShop Test Automation Framework

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-blue.svg)](https://maven.apache.org/)
[![Selenium](https://img.shields.io/badge/Selenium-4.38.0-green.svg)](https://www.selenium.dev/)
[![Cucumber](https://img.shields.io/badge/Cucumber-7.20.1-brightgreen.svg)](https://cucumber.io/)

Comprehensive test automation framework for [DemoWebShop](http://demowebshop.tricentis.com/) using Selenium WebDriver, Cucumber BDD, and JUnit 5.

---

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Running Tests](#-running-tests)
- [Test Reports](#-test-reports)
- [Known Issues](#-known-issues)
- [Best Practices](#-best-practices)
- [Contributing](#-contributing)

---

## ✨ Features

- ✅ **BDD with Cucumber** - Gherkin syntax for readable test scenarios
- ✅ **Page Object Model** - Clean separation of page logic and tests
- ✅ **Data-Driven Testing** - Scenario Outlines with Examples tables
- ✅ **Dynamic Test Data** - Random email generation to avoid duplicates
- ✅ **Comprehensive Coverage**:
  - User Registration (1 positive + 8 negative scenarios)
  - User Login (1 positive + 5 negative scenarios)
- ✅ **Bug Tracking** - Tagged scenarios for known issues
- ✅ **Allure Reports** - Beautiful test execution reports
- ✅ **CI/CD Ready** - Maven-based execution

---

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Programming language |
| Maven | 3.8+ | Build & dependency management |
| Selenium WebDriver | 4.38.0 | Browser automation |
| Cucumber JVM | 7.20.1 | BDD framework |
| JUnit 5 | 5.11.4 | Test runner |
| Allure | 2.31.0 | Test reporting |
| WebDriverManager | 5.9.2 | Automatic driver management |

---

## 📁 Project Structure

```
demo/
├── src/
│   ├── main/java/com/demowebshop/
│   │   ├── config/          # Configuration management
│   │   ├── pages/           # Page Object Model classes
│   │   │   ├── BasePage.java
│   │   │   ├── HomePage.java
│   │   │   ├── RegisterPage.java
│   │   │   └── LoginPage.java
│   │   ├── context/         # Test context & page manager
│   │   └── utils/           # Helper utilities
│   │       ├── ElementHelper.java
│   │       └── RandomUtil.java
│   │
│   └── test/
│       ├── java/com/demowebshop/
│       │   ├── runners/     # Cucumber test runners
│       │   ├── steps/       # Step definitions
│       │   │   ├── RegisterSteps.java
│       │   │   └── LoginSteps.java
│       │   └── hooks/       # Cucumber hooks (setup/teardown)
│       │
│       └── resources/
│           ├── features/    # Cucumber feature files
│           │   ├── registre.feature
│           │   └── login.feature
│           └── config.properties
│
├── target/                  # Build output (ignored by Git)
├── .gitignore
├── pom.xml
└── README.md
```

---

## 📦 Prerequisites

- **Java JDK 17** or higher
- **Maven 3.8+**
- **Chrome Browser** (latest version)
- **Git**

---

## 🚀 Installation

### 1. Clone the repository

```bash
git clone https://github.com/akremhammamii/demowebshop-automation.git
cd demowebshop-automation/demo
```

### 2. Install dependencies

```bash
mvn clean install -DskipTests
```

---

## ▶️ Running Tests

### Run All Tests

```bash
mvn clean test
```

### Run Specific Tags

**Registration tests only:**
```bash
mvn test -D"cucumber.filter.tags=@registration"
```

**Login tests only:**
```bash
mvn test -D"cucumber.filter.tags=@login"
```

**Positive tests only:**
```bash
mvn test -D"cucumber.filter.tags=@positive"
```

**Negative tests only:**
```bash
mvn test -D"cucumber.filter.tags=@negative"
```

**Exclude known bugs:**
```bash
mvn test -D"cucumber.filter.tags=not @bug"
```

---

## 📊 Test Reports

### Surefire Reports

After running tests, view HTML reports:
```
target/surefire-reports/index.html
```

### Allure Reports

Generate and view Allure reports:

```bash
# Generate report
mvn allure:report

# Serve report (opens in browser)
mvn allure:serve
```

---

## 🐛 Known Issues

### BUG-LOGIN-001: Invalid Email Format Validation Missing

**Status:** Open  
**Priority:** Medium  
**Description:** Login form does not validate email format, unlike registration form.

**Test Case:**
```gherkin
@bug @BUG-LOGIN-001
Scenario: Login with invalid email format shows no error
```

**Details:** See detailed bug report in [BUG-LOGIN-001.md](.gemini/antigravity/brain/.../BUG-LOGIN-001.md)

**Workaround:** Test is tagged with `@bug` and excluded from CI runs using:
```bash
mvn test -D"cucumber.filter.tags=not @bug"
```

---

## 📚 Test Coverage

### Registration Tests

**Feature:** `registre.feature`

| Scenario Type | Count | Description |
|--------------|-------|-------------|
| ✅ Positive | 1 | Successful registration with dynamic email |
| ❌ Negative | 8 | Various validation failures |

**Negative Test Cases:**
- Missing first name
- Missing last name
- Missing email
- Missing password
- Invalid email format (2 cases)
- Password mismatch
- Weak password

### Login Tests

**Feature:** `login.feature`

| Scenario Type | Count | Description |
|--------------|-------|-------------|
| ✅ Positive | 1 | Successful login with registered credentials |
| ❌ Negative | 4 | Invalid credentials attempts |
| 🐛 Known Bug | 1 | Invalid email format (BUG-LOGIN-001) |

**Background:** Each scenario creates a fresh user account

---

## 🎯 Best Practices Implemented

### 1. **Page Object Model (POM)**
- Separation of concerns
- Reusable page methods
- Easy maintenance

### 2. **Dynamic Test Data**
- Random email generation using UUID
- Avoids duplicate registration errors
- Enables parallel test execution

### 3. **Bug Tracking in Tests**
- Known bugs tagged with `@bug` + bug ID
- Can be excluded from CI: `not @bug`
- Bug documentation linked to test cases

### 4. **Comprehensive Validation**
- Positive scenarios verify happy paths
- Negative scenarios test boundaries
- Error messages validated

### 5. **Scenario Context**
- Data sharing between steps
- Background for setup
- Clean test isolation

---

## 🤝 Contributing

### Reporting Bugs

1. Check [existing issues](https://github.com/akremhammamii/demowebshop-automation/issues)
2. Create detailed bug report with:
   - Steps to reproduce
   - Expected vs. actual behavior
   - Screenshots if applicable
   - Test case reference

### Adding Tests

1. Create feature file in `src/test/resources/features/`
2. Implement step definitions in `src/test/java/com/demowebshop/steps/`
3. Add page objects if needed in `src/main/java/com/demowebshop/pages/`
4. Follow naming conventions and coding standards
5. Run tests locally before committing

### Commit Message Convention

```
<type>: <subject>

<body>

<footer>
```

**Types:**
- `feat:` New feature
- `fix:` Bug fix
- `test:` Adding/updating tests
- `docs:` Documentation changes
- `refactor:` Code refactoring
- `chore:` Maintenance tasks

**Example:**
```
feat: Add checkout flow test scenarios

- Added positive checkout scenario
- Added 5 negative validation tests
- Updated CartPage with new methods

Closes #15
```

---

## 📝 License

This project is for educational purposes and testing practice.

---

## 👤 Author

**Akrem Hammami**
- GitHub: [@akremhammamii](https://github.com/akremhammamii)

---

## 🙏 Acknowledgments

- [DemoWebShop](http://demowebshop.tricentis.com/) by Tricentis
- [Selenium](https://www.selenium.dev/) community
- [Cucumber](https://cucumber.io/) team

---

**Last Updated:** November 2025
