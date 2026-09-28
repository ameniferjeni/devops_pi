# CodingFactory — PFE & Chatbot

Application full-stack (Spring Boot 3 + Angular 18 + MySQL) avec **intégration continue (CI)** et **déploiement continu (CD)** via GitHub Actions, Jenkins et Docker.

## Démarrage local (Windows)

```bat
start-CODINGFACTORY.bat
```

- Frontend : http://localhost:4200 (proxy API → backend)
- Backend : http://localhost:8081

## DevOps — CI/CD

### CI (intégration continue)

À chaque **push** ou **pull request** sur `main` / `develop` :

| Job | Action |
|-----|--------|
| **backend-ci** | `mvn verify` (tests unitaires + packaging JAR) |
| **frontend-ci** | `npm ci` + `npm run build:ci` |

Fichier : [`.github/workflows/ci-cd.yml`](.github/workflows/ci-cd.yml)

### CD (déploiement continu)

Sur **main** / **master** uniquement :

1. Construction des images Docker (`backend`, `frontend`, `mysql`)
2. `docker compose up` + test de fumée (`/api/pfe/stats`, page d’accueil nginx)

### Jenkins

1. Installer les outils Jenkins : **JDK 17** (`JDK17`), **Node.js 20** (`NodeJS20`), **Docker**
2. Créer un pipeline **Pipeline script from SCM** pointant sur ce dépôt
3. Le [`Jenkinsfile`](Jenkinsfile) exécute les mêmes étapes CI/CD

### Docker (production locale)

```bash
docker compose up --build -d
```

| Service | URL |
|---------|-----|
| Application (Angular + proxy API) | http://localhost:8080 |
| API REST directe | http://localhost:8081 |
| MySQL | localhost:3306 (mot de passe : `codingfactory`) |

Variable optionnelle : `MYSQL_ROOT_PASSWORD` dans un fichier `.env` à la racine.

## Structure DevOps

```
.github/workflows/ci-cd.yml   # GitHub Actions CI/CD
Jenkinsfile                 # Pipeline Jenkins
docker-compose.yml          # Stack MySQL + backend + frontend
backend/Dockerfile
frontend/Dockerfile
frontend/nginx.conf           # Reverse proxy /api → backend
```

## Initialiser Git + GitHub Actions

```bash
git init
git add .
git commit -m "feat: plateforme CodingFactory avec pipeline CI/CD"
git branch -M main
git remote add origin <url-du-repo>
git push -u origin main
```

Les workflows se déclenchent automatiquement sur GitHub après le push.
