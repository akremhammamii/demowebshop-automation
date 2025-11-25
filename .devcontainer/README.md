# 🚀 Dev Container Setup - Architecture Standalone

Configuration pour développer et tester dans un **conteneur Docker unique** contenant Java, Maven, Chrome et ChromeDriver.

## 📋 Prérequis

1. **Docker Desktop** installé et en cours d'exécution
2. **VS Code** avec l'extension **Dev Containers** (`ms-vscode-remote.remote-containers`)

## 🏗️ Architecture

```
┌─────────────────────────────────────┐
│   Conteneur Docker Unique           │
│                                      │
│  ✅ Java 17 (Eclipse Temurin)       │
│  ✅ Maven 3.9                        │
│  ✅ Google Chrome (latest stable)    │
│  ✅ ChromeDriver (auto-matched)      │
│  ✅ Git + outils dev                 │
│                                      │
│  📁 Votre code monté depuis l'hôte  │
└─────────────────────────────────────┘
```

## 🔧 Utilisation avec VS Code

### 1. Ouvrir le projet dans le conteneur

1. Ouvrez VS Code dans le dossier du projet
2. Une notification apparaîtra en bas à droite: **"Reopen in Container"**
3. Cliquez dessus (ou `F1` → `Dev Containers: Reopen in Container`)
4. VS Code va:
   - Construire l'image Docker depuis `.devcontainer/Dockerfile.dev`
   - Démarrer le conteneur
   - Installer les extensions Java/Cucumber
   - Télécharger les dépendances Maven

**⏱️ Premier démarrage:** ~5-10 minutes (construction de l'image)  
**⚡ Redémarrages suivants:** ~10-30 secondes

### 2. Développer dans le conteneur

Une fois ouvert, vous êtes **à l'intérieur du conteneur** :

```bash
# Compiler le projet
mvn clean compile

# Lancer les tests
mvn test

# Lancer les tests sans les bugs connus
mvn test -Dcucumber.filter.tags="not @bug"

# Générer le rapport Allure
mvn allure:report
mvn allure:serve
```

### 3. Vérifier l'installation

```bash
# Versions installées
java -version          # → Java 17
mvn -version           # → Maven 3.9
google-chrome --version  # → Chrome 131.x
chromedriver --version   # → ChromeDriver 131.x
```

## 🤖 Utilisation avec Jenkins

Le fichier `Jenkinsfile` utilise le même Dockerfile pour exécuter les tests en CI/CD.

### Configuration Jenkins

1. **Installer le plugin Docker Pipeline** dans Jenkins
2. **Créer un Pipeline** pointant vers votre repository Git
3. **Jenkins va automatiquement** :
   - Construire l'image Docker depuis `.devcontainer/Dockerfile.dev`
   - Lancer les tests à l'intérieur du conteneur
   - Publier les rapports (JUnit, Cucumber, Allure)

### Pipeline Stages

```groovy
Checkout → Build → Test → Generate Reports
```

### Rapports générés

- **JUnit XML** → `target/surefire-reports/*.xml`
- **Cucumber JSON** → `target/cucumber-reports/*.json`
- **Allure Results** → `target/allure-results/`

## ⚙️ Configuration Chrome en Headless

Le code Java est déjà configuré pour Chrome headless via `config.properties`:

```properties
headless=true
```

Cette configuration est **obligatoire** pour Docker/Jenkins car il n'y a pas d'interface graphique.

### Options Chrome importantes

Dans `WebDriverFactory.java`, les options suivantes sont actives :

```java
options.addArguments("--headless=new");      // ✅ Mode headless
options.addArguments("--no-sandbox");        // ✅ Requis pour Docker
options.addArguments("--disable-dev-shm-usage"); // ✅ Évite les erreurs mémoire
options.addArguments("--disable-gpu");
options.addArguments("--window-size=1920,1080");
```

## 📂 Structure des fichiers

```
demo/
├── .devcontainer/
│   ├── devcontainer.json      # Configuration VS Code Dev Container
│   ├── Dockerfile.dev         # Image Docker pour développement
│   └── README.md              # Ce fichier
├── src/
│   ├── main/java/             # Code source
│   └── test/                  # Tests Cucumber
├── pom.xml                    # Configuration Maven
├── Jenkinsfile                # Pipeline Jenkins
└── Dockerfile                 # Image pour build/CI (optionnel)
```

## 🐛 Troubleshooting

### Le conteneur ne démarre pas

```bash
# Reconstruire l'image depuis zéro
F1 → "Dev Containers: Rebuild Container"
```

### Chrome ne s'exécute pas

Vérifiez que `headless=true` dans `config.properties` et que les options `--no-sandbox` et `--disable-dev-shm-usage` sont présentes.

### Maven ne trouve pas les dépendances

```bash
# Dans le conteneur, forcer le téléchargement
mvn dependency:purge-local-repository
mvn dependency:go-offline
```

### Jenkins ne peut pas construire l'image

Assurez-vous que:
1. Docker est installé sur l'agent Jenkins
2. L'agent Jenkins a les permissions pour exécuter Docker
3. Le plugin "Docker Pipeline" est installé

## ✅ Avantages de cette approche

| Avantage | Description |
|----------|-------------|
| 🔒 **Isolation** | Environnement reproductible, pas d'installation locale |
| 🚀 **Simplicité** | Un seul conteneur, pas de Docker Compose complexe |
| 🤝 **Jenkins-ready** | Même Dockerfile pour dev et CI/CD |
| 🧹 **Clean host** | Votre machine reste propre, tout est dans Docker |
| 🔄 **Portable** | Fonctionne sur Windows, Mac, Linux |

## 📚 Ressources

- [VS Code Dev Containers](https://code.visualstudio.com/docs/devcontainers/containers)
- [Docker Pipeline Plugin](https://plugins.jenkins.io/docker-workflow/)
- [Selenium Manager](https://www.selenium.dev/documentation/selenium_manager/)
