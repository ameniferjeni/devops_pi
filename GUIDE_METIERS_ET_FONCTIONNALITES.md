# 📖 Guide Complet des Métiers, Fonctionnalités et Cartographie du Code

---

## 🏢 PARTIE 1 : Présentation Détaillée des 8 Métiers de Consulting

La plateforme **CodingFactory** réunit **8 métiers et pôles d'expertise avancés**. Chaque pôle est associé à un consultant senior référent, un catalogue de technologies et une stratégie d'accompagnement dédiée.

### 1. 💻 Développement Software & Cloud Native
* **Description** : Conception et réalisation d'applications web, mobile et architectures microservices scalables et haute performance basées sur les meilleures pratiques du Cloud.
* **Consultant Référent** : **Sami Mansour** (*Architecte Lead Software & Cloud Native*)
* **Contact** : 📧 `sami.mansour@codingfactory.tn` | 📞 `+216 20 111 222`
* **Technologies** : `Java 21 / Spring Boot 3`, `Angular 18`, `React`, `Microservices REST`, `Docker & Kubernetes`, `AWS / Azure`
* **Mots-clés déclencheurs Chatbot** : `web`, `dev`, `application`, `mobile`, `cloud`, `spring`, `angular`, `react`, `microservices`, `architecture`

---

### 2. 🛡️ Cybersécurité & Audit SI
* **Description** : Audits de sécurité automatisés et manuels, tests d'intrusion applicatifs et réseaux (Pentest), et accompagnement à la conformité réglementaire ISO 27001 & RGPD.
* **Consultant Référent** : **Mehdi Gharbi** (*Lead Expert Cybersécurité & Pentester Certifié*)
* **Contact** : 📧 `mehdi.gharbi@codingfactory.tn` | 📞 `+216 20 333 444`
* **Technologies** : `Pentest Web & API`, `Audit Code Source OWASP`, `Conformité RGPD / ISO 27001`, `SOC & SIEM Monitoring`
* **Mots-clés déclencheurs Chatbot** : `cyber`, `securite`, `audit`, `pentest`, `vulnerabilite`, `iso27001`, `rgpd`, `hack`

---

### 3. 🚀 Platform Engineering & DevSecOps Infrastructure
* **Description** : Conception d'Internal Developer Platforms (IDP), automatisation des déploiements GitOps et gestion sécurisée des secrets d'infrastructures.
* **Consultant Référent** : **Inès Chebbi** (*Lead Platform Engineer & DevSecOps Specialist*)
* **Contact** : 📧 `ines.chebbi@codingfactory.tn` | 📞 `+216 20 888 999`
* **Technologies** : `Kubernetes`, `ArgoCD`, `Terraform`, `HashiCorp Vault`, `Backstage IDP`
* **Mots-clés déclencheurs Chatbot** : `platform`, `devsecops`, `argocd`, `vault`, `backstage`, `idp`, `gitops`

---

### 4. 🔗 Blockchain, Web3 & FinTech Security
* **Description** : Déploiement de protocoles décentralisés, vérification formelle de Smart Contracts et architectures distribuées pour la finance numérique (FinTech).
* **Consultant Référent** : **Tarek Ben Ammar** (*Lead Architect Blockchain & Smart Contracts*)
* **Contact** : 📧 `tarek.benammar@codingfactory.tn` | 📞 `+216 20 777 999`
* **Technologies** : `Solidity`, `Ethereum`, `Hyperledger Fabric`, `Web3.js`, `Zero-Knowledge Proofs (ZKP)`
* **Mots-clés déclencheurs Chatbot** : `blockchain`, `web3`, `solidity`, `smart contract`, `ethereum`, `crypto`, `fintech`, `nft`

---

### 5. 🎓 Formation Informatique & Coaching Technique
* **Description** : Bootcamps et formations certifiantes sur-mesure dispensés par des experts seniors pour la montée en compétences des équipes techniques d'entreprises.
* **Consultant Référent** : **Amina Triki** (*Directrice des Formations & Tech Coach*)
* **Contact** : 📧 `amina.triki@codingfactory.tn` | 📞 `+216 20 555 666`
* **Technologies** : `Java 21 / Spring Boot 3`, `Angular 18`, `DevOps & CI/CD`, `Prompt Engineering & IA`
* **Mots-clés déclencheurs Chatbot** : `formation`, `cours`, `bootcamp`, `coaching`, `java`, `spring`, `angular`

---

### 6. 📊 Conseil Stratégique IT & Gouvernance
* **Description** : Alignement stratégique du SI, élaboration de schémas directeurs, audit d'architecture et pilotage de la transformation digitale et agile.
* **Consultant Référent** : **Karim Trabelsi** (*Senior Consultant IT Strategy & Gouvernance*)
* **Contact** : 📧 `karim.trabelsi@codingfactory.tn` | 📞 `+216 20 777 888`
* **Technologies** : `Schémas Directeurs SI`, `Audit d'Architecture`, `Agile Transformation`, `Gouvernance IT`
* **Mots-clés déclencheurs Chatbot** : `conseil`, `strategie`, `gouvernance`, `transformation`, `schema`, `consulting`

---

### 7. 🤖 Intelligence Artificielle & Data Science
* **Description** : Valorisation des données d'entreprise, intégration de modèles de Machine Learning, agents IA générative (LLM) et industrialisation MLOps.
* **Consultant Référent** : **Dr. Yassine Ben Romdhane** (*Lead Data Scientist & Expert IA*)
* **Contact** : 📧 `yassine.benromdhane@codingfactory.tn` | 📞 `+216 20 999 000`
* **Technologies** : `Python & PyTorch`, `Generative AI / LLM`, `MLOps & CI/CD ML`, `Business Intelligence`
* **Mots-clés déclencheurs Chatbot** : `ia`, `ai`, `data`, `machine learning`, `deep learning`, `python`, `mlops`, `llm`

---

### 8. 🎓 Module PFE & Encadrement Académique
* **Description** : Incubation et encadrement technique des projets de fin d'études informatiques sur des thématiques R&D innovantes.
* **Consultant Référent** : **Youssef Ben Ali** (*Coordinateur PFE & Partenariats Académiques*)
* **Contact** : 📧 `pfe@codingfactory.tn` | 📞 `+216 20 123 456`
* **Technologies** : `Encadrement Technique`, `Sujets PFE Inédits`, `Coaching Carrière`, `Recrutement`
* **Mots-clés déclencheurs Chatbot** : `pfe`, `stage`, `sujet`, `etudiant`, `candidat`, `recrutement`

---

## ⚙️ PARTIE 2 : Explication des Fonctionnalités et Emplacement du Code

---

### 🟢 MODULE 1 : CHATBOT CONSULTING PERSONNALISÉ

#### 1. Personnalisation du Profil Utilisateur & Persistance
* **Fonctionnalité** : L'utilisateur peut personnaliser son profil (Prénom, Entreprise, Rôle). Les réponses du bot s'adaptent dynamiquement et les données sont conservées en mémoire locale (`localStorage`).
* **Emplacement Code Frontend** :
  * File: [`frontend/src/app/components/chatbot.component.ts`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/src/app/components/chatbot.component.ts)
  * Methods: `loadProfile()`, `saveProfile()`, `toggleProfileSettings()`
* **Emplacement Code Backend** :
  * File: [`backend/chatbot-service/src/main/java/com/codingfactory/chatbot/dto/ChatbotRequestDto.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/chatbot-service/src/main/java/com/codingfactory/chatbot/dto/ChatbotRequestDto.java)

#### 2. Moteur d'Analyse NLP & Classification d'Intention
* **Fonctionnalité** : Analyse la question en langage naturel, élimine les bruits de saisie, classe la demande parmi 12 intentions et sélectionne le service approprié.
* **Emplacement Code Backend** :
  * File: [`backend/chatbot-service/src/main/java/com/codingfactory/chatbot/service/ChatbotEngineService.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/chatbot-service/src/main/java/com/codingfactory/chatbot/service/ChatbotEngineService.java)
  * Method: `processQuestion(ChatbotRequestDto request)`
  * File: [`backend/chatbot-service/src/main/java/com/codingfactory/chatbot/controller/ChatbotController.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/chatbot-service/src/main/java/com/codingfactory/chatbot/controller/ChatbotController.java)
  * Endpoint: `POST /api/chatbot/ask`

#### 3. Recommandation Consultant & Prise de RDV en Ligne
* **Fonctionnalité** : Génère une carte de visite avec les coordonnées de l'expert référent et ouvre une modale interactive de réservation d'entretien (`RDV Modal`).
* **Emplacement Code Frontend** :
  * File: [`frontend/src/app/components/chatbot.component.ts`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/src/app/components/chatbot.component.ts)
  * Methods: `openRdvModal()`, `confirmRdv()`, template HTML de la carte consultant
* **Emplacement Code Backend** :
  * File: [`backend/chatbot-service/src/main/java/com/codingfactory/chatbot/dto/ChatbotResponseDto.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/chatbot-service/src/main/java/com/codingfactory/chatbot/dto/ChatbotResponseDto.java)

#### 4. Catalogue Latéral Synchronisé des Services
* **Fonctionnalité** : Récupération et affichage dynamique des 8 métiers de consulting avec leurs tags de technologies et bouton d'interrogation rapide.
* **Emplacement Code Frontend** :
  * File: [`frontend/src/app/services/chatbot.service.ts`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/src/app/services/chatbot.service.ts)
  * Method: `getServices()`
* **Emplacement Code Backend** :
  * File: [`backend/chatbot-service/src/main/java/com/codingfactory/chatbot/service/ChatbotEngineService.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/chatbot-service/src/main/java/com/codingfactory/chatbot/service/ChatbotEngineService.java)
  * Method: `getConsultingServices()`

---

### 🔵 MODULE 2 : PROJETS DE FIN D'ÉTUDES (PFE) & AI MATCHER

#### 1. Radar d'Adéquation IA Candidat ↔ Sujet PFE (Skill Matcher)
* **Fonctionnalité** : L'étudiant sélectionne ses compétences et son domaine préféré ➔ L'algorithme calcule un score de compatibilité (0–100%), identifie les prérequis validés vs manquants et fournit une recommandation personnalisée.
* **Emplacement Code Backend** :
  * File: [`backend/pfe-service/src/main/java/com/codingfactory/pfe/controller/SujetPfeController.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/pfe-service/src/main/java/com/codingfactory/pfe/controller/SujetPfeController.java)
  * Endpoint: `POST /api/pfe/sujets/match`
  * Method: `calculateMatching(PfeMatchingRequestDto request)`
  * File: [`backend/src/main/java/com/codingfactory/backend/service/impl/SujetPfeServiceImpl.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/src/main/java/com/codingfactory/backend/service/impl/SujetPfeServiceImpl.java)
* **Emplacement Code Frontend** :
  * File: [`frontend/src/app/components/pfe.component.ts`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/src/app/components/pfe.component.ts)
  * Methods: `runMatchingAnalysis()`, `calculateMatchingLocal()`

#### 2. Postulation 1-Clic avec Profil Recommandé
* **Fonctionnalité** : Permet à l'étudiant de postuler directement à un sujet depuis le Radar d'Adéquation IA avec pré-remplissage automatique des compétences et de la motivation.
* **Emplacement Code Frontend** :
  * File: [`frontend/src/app/components/pfe.component.ts`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/src/app/components/pfe.component.ts)
  * Method: `applyWithMatching(PfeMatchResult match)`

#### 3. Catalogue des Sujets PFE & Filtres Rapides
* **Fonctionnalité** : Exploration des offres de PFE avec filtres en 1-clic (*Spring Boot, Angular, Cybersécurité, IA*) et recherche en temps réel.
* **Emplacement Code Frontend** :
  * File: [`frontend/src/app/components/pfe.component.ts`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/src/app/components/pfe.component.ts)
  * Property: `filteredSujets`

#### 4. Tableau de Bord Analytics PFE
* **Fonctionnalité** : Visualisation des métriques clés (Total sujets, sujets actifs, candidatures enregistrées, distribution des technologies et taux de réussite ML).
* **Emplacement Code Backend** :
  * File: [`backend/pfe-service/src/main/java/com/codingfactory/pfe/controller/PfeStatsController.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/pfe-service/src/main/java/com/codingfactory/pfe/controller/PfeStatsController.java)
  * Endpoint: `GET /api/pfe/stats`
* **Emplacement Code Frontend** :
  * File: [`frontend/src/app/components/pfe.component.ts`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/src/app/components/pfe.component.ts)
  * Tab: `analytics`

#### 5. Soumission Résiliente de Candidatures & Suivi Administrateur
* **Fonctionnalité** : Formulaire de candidature publique avec création/rattachement du profil candidat, intégration du score ML et administration des admissions (*Accepter / Refuser*).
* **Emplacement Code Backend** :
  * File: [`backend/pfe-service/src/main/java/com/codingfactory/pfe/controller/CandidatureController.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/pfe-service/src/main/java/com/codingfactory/pfe/controller/CandidatureController.java)
  * Method: `submitCandidature()`
  * File: [`backend/src/main/java/com/codingfactory/backend/service/impl/CandidatureServiceImpl.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/src/main/java/com/codingfactory/backend/service/impl/CandidatureServiceImpl.java)

---

## 🛠️ PARTIE 3 : Infrastructure, Docker & Pipelines CI/CD

### 🐳 Conteneurisation Docker
* **`docker-compose.yml`** : [`docker-compose.yml`](file:///c:/Users/user/Desktop/CodingFactoryApp/docker-compose.yml) ➔ Orchestre les 6 conteneurs (`mysql`, `eureka`, `gateway`, `pfe-service`, `chatbot-service`, `ml-service`, `frontend`).
* **Frontend `Dockerfile`** : [`frontend/Dockerfile`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/Dockerfile) ➔ Multi-stage (Node.js 20 ➔ Nginx 1.27 Alpine).
* **Backend `Dockerfile`** : [`backend/chatbot-service/Dockerfile`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/chatbot-service/Dockerfile) ➔ Multi-stage (Maven 3.9 + Java 17 ➔ JRE Alpine 17).

### 🔄 Pipelines CI/CD
* **`Jenkinsfile`** : [`Jenkinsfile`](file:///c:/Users/user/Desktop/CodingFactoryApp/Jenkinsfile) ➔ Pipeline déclarative Jenkins (Stages: *Checkout*, *Backend CI*, *Frontend CI*, *Docker CD*).
* **Scripts de Lancement Local** : [`start-CODINGFACTORY.bat`](file:///c:/Users/user/Desktop/CodingFactoryApp/start-CODINGFACTORY.bat).
