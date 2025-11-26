# 🚀 Jenkins CI/CD - Guide Rapide

Guide pour intégrer votre framework de tests avec Jenkins.

## Option 1: Jenkins avec Docker (Recommandé pour débuter)

### Démarrer Jenkins

```bash
docker run -d --name jenkins -p 8080:8080 -p 50000:50000 -v jenkins_home:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock jenkins/jenkins:lts
```

### Accéder à Jenkins

1. Ouvrir: http://localhost:8080
2. Mot de passe initial:
   ```bash
   docker logs jenkins
   ```
   Cherchez le mot de passe admin dans les logs

3. Installer les plugins recommandés
4. Créer votre premier utilisateur admin

## Plugins Requis

Allez dans **Manage Jenkins** → **Plugins** → **Available**:

- [x] **Docker Pipeline** (pour utiliser Docker dans Jenkinsfile)
- [x] **Allure** (pour les rapports de tests)
- [x] Git (déjà installé)
- [x] JUnit (déjà installé)

## Configuration des Outils (Obligatoire)

### Installer Allure Commandline
1. Allez dans **Manage Jenkins** → **Tools** (ou Global Tool Configuration).
2. Cherchez la section **Allure Commandline**.
3. Cliquez sur **Add Allure Commandline**.
4. Nom: `allure` (ou laissez par défaut).
5. Cochez **Install automatically**.
6. Cliquez sur **Save**.

## Créer le Pipeline

### 1. Nouveau Job

1. Cliquez **"New Item"**
2. Nom: `Demowebshop-Tests`
3. Type: **Pipeline**
4. Cliquez **OK**

### 2. Configuration

**Configuration Requise:**

Vous **DEVEZ** utiliser "Pipeline script from SCM". L'option "Pipeline script" (copier-coller) ne fonctionnera pas car Jenkins a besoin du code pour construire l'image Docker.

```
Pipeline → Definition: "Pipeline script from SCM"
SCM: Git
Repository URL: https://github.com/akremhammamii/demowebshop-automation.git
Branch: */master
Script Path: Jenkinsfile
```

### 3. Lancer le Build

1. Cliquez **"Build Now"**
2. Cliquez sur le build #1
3. **"Console Output"** pour voir les logs

## Ce que fait le Pipeline

```
Checkout → Build (mvn compile) → Test (mvn test) → Reports (Allure)
```

**Résultat:**
- ✅ Tests exécutés dans Docker
- ✅ Rapports JUnit générés
- ✅ Rapports Cucumber archivés
- ✅ Dashboard Allure interactif

## Automatisation GitHub/GitLab

### GitHub Webhook

1. **Dans votre repo GitHub:**
   - Settings → Webhooks → Add webhook
   - URL: `http://votre-jenkins:8080/github-webhook/`
   - Content type: `application/json`

2. **Dans Jenkins:**
   - Job → Configure → Build Triggers
   - ☑️ "GitHub hook trigger for GITScm polling"

**Résultat:** Build automatique à chaque `git push`! 🚀

### GitLab Webhook

1. **Dans votre repo GitLab:**
   - Settings → Webhooks
   - URL: `http://votre-jenkins:8080/project/Demowebshop-Tests`
   - Push events: ☑️

2. **Dans Jenkins:**
   - Job → Configure → Build Triggers
   - ☑️ "Build when a change is pushed to GitLab"

## Voir les Rapports

### JUnit
Build → **Test Results** → Voir pass/fail

### Allure
Build → **Allure Report** → Dashboard interactif

### Cucumber
Build → **Artifacts** → Télécharger `target/cucumber-reports/`

## Troubleshooting

### "Docker not found"
```bash
# Ajouter Jenkins au groupe docker
sudo usermod -aG docker jenkins
sudo systemctl restart jenkins
```

### "Chrome crashed"
Déjà résolu avec `--shm-size=2g` dans le Jenkinsfile ✅

### Pas de rapport Allure
1. Manage Jenkins → Tools → Allure Commandline
2. Add Allure → Installer automatiquement

## Commandes Utiles

```bash
# Logs Jenkins
docker logs jenkins -f

# Redémarrer Jenkins
docker restart jenkins

# Arrêter Jenkins
docker stop jenkins

# Supprimer Jenkins (garde les données)
docker rm jenkins
```

## Architecture Finale

```
Git Push → GitHub Webhook → Jenkins
            ↓
         Build Docker Image (.devcontainer/Dockerfile.dev)
            ↓
         Run Tests (mvn test)
            ↓
         Generate Reports (Allure, JUnit)
            ↓
         Archive & Publish ✅
```

## Prochaines Étapes

1. ✅ Jenkins installé et configuré
2. ✅ Pipeline créé
3. ⏭️ Push code vers GitHub/GitLab
4. ⏭️ Configurer webhook
5. ⏭️ Chaque commit = tests automatiques!

---

**Votre CI/CD est opérationnel!** 🎊
