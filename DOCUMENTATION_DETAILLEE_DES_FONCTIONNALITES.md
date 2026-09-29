# 📘 Documentation Technique Détaillée : Fonctionnalités & Emplacements du Code

Ce document présente l'explication exhaustive de chaque fonctionnalité de la plateforme **CodingFactory**, avec la cartographie exacte des fichiers source (Backend Java/Spring Boot & Frontend Angular 18), des méthodes, des endpoints REST et des structures de données.

---

## 🔒 MODULE 0 : AUTHENTIFICATION & SÉCURITÉ JWT (RÔLES ADMIN / CANDIDAT)

---

### 0.1 Page de Connexion & Inscription Professionnelle
* **Description Métier** : Espace d'authentification sécurisé par jetons JWT (JSON Web Tokens) permettant l'accès selon les rôles (`ADMIN` vs `CANDIDAT`).
* **Nouveau Design Refactorisé (Rôles Visuels)** :
  - **Cartes de Sélection de Rôle Interactives** (en mode Inscription) : Rôle `CANDIDAT` (Accès PFE & Matching IA) vs Rôle `ADMIN` (Gouvernance & Validation).
  - **Design System Glassmorphism** : Arrière-plan dynamique sombre, badges animés, retour d'état visuel et indicateur de chargement (`spinner`).
* **Comptes Démo Pré-Configurés** :
  - 👑 **Administrateur** : `admin@codingfactory.tn` / `admin123` (Accès complet à la gestion PFE, validation des candidatures et mode édition).
  - 🎓 **Candidat (Étudiant)** : `candidat@codingfactory.tn` / `candidat123` (Accès aux dépôts de dossiers et au Radar d'Adéquation IA).

#### 📂 Emplacements Exacts du Code :
* **Frontend Angular** :
  * Fichier composant refactorisé : [`frontend/src/app/components/login.component.ts`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/src/app/components/login.component.ts)
  * Service d'authentification : [`frontend/src/app/services/auth.service.ts`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/src/app/services/auth.service.ts)
  * Intercepteur HTTP JWT : [`frontend/src/app/interceptors/auth.interceptor.ts`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/src/app/interceptors/auth.interceptor.ts)
  * Guards de protection de routes : [`frontend/src/app/guards/auth.guard.ts`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/src/app/guards/auth.guard.ts)
* **Backend Spring Boot** :
  * Controller REST : [`backend/src/main/java/com/codingfactory/backend/controller/AuthController.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/src/main/java/com/codingfactory/backend/controller/AuthController.java) (Endpoints `POST /api/auth/login`, `POST /api/auth/register`, `GET /api/auth/me`)
  * Générateur de Tokens JWT : [`backend/src/main/java/com/codingfactory/backend/config/JwtUtils.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/src/main/java/com/codingfactory/backend/config/JwtUtils.java)
  * Fichiers DTO : [`AuthRequestDto.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/src/main/java/com/codingfactory/backend/dto/AuthRequestDto.java), [`AuthResponseDto.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/src/main/java/com/codingfactory/backend/dto/AuthResponseDto.java)

---

### 0.2 Centre de Notifications Temps Réel & Overlay Toast
* **Description Métier** : Système de notification réactif informant l'utilisateur en temps réel de chaque événement système (authentification, calcul du score d'adéquation IA, téléchargement de rapport PDF).
* **Composants Visuels** :
  - **Icône Cloche 🔔 & Badge Dynamique** : Situé dans l'en-tête, affichant le nombre de messages non lus.
  - **Menu Déroulant d'Historique** : Panneau rétractable listant les notifications horodatées avec statut lu/non lu et bouton d'action "Tout lire".
  - **Toast Banner Flottant Animé** : Notification pop-up temporaire s'affichant en haut à droite pendant 4,5 secondes avec retour de couleur selon la sévérité (`info`, `success`, `warning`, `error`).

#### 📂 Emplacements Exacts du Code :
* **Frontend Angular** :
  * Service réactif RxJS : [`frontend/src/app/services/notification.service.ts`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/src/app/services/notification.service.ts)
  * Composant Layout & Overlay : [`frontend/src/app/components/app-layout.component.ts`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/src/app/components/app-layout.component.ts) (Template L24-46, Styles Toast L64-140)
  * Déclencheurs métier : [`frontend/src/app/components/pfe.component.ts`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/src/app/components/pfe.component.ts)

---

## 🟢 MODULE 1 : CHATBOT CONSULTING PERSONNALISÉ

---

### 1.1 Personnalisation du Profil Utilisateur & Persistance Locale

* **Description Métier** : L'utilisateur peut personnaliser son profil (Prénom, Entreprise / Organisme, Rôle / Profil). Le chatbot utilise immédiatement le prénom dans ses salutations et réponses pour offrir une expérience personnalisée sur-mesure. Les données persistent dans le navigateur de l'utilisateur.
* **Fonctionnement Sous le Capot** :
  - **Stockage HTML5** : Les données sont lues et sauvegardées dans le `localStorage` via des clés uniques (`codingfactory.chatbot.prenom`, `codingfactory.chatbot.entreprise`, `codingfactory.chatbot.role`).
  - **Binding Bidirectionnel (Two-Way Data Binding)** : L'utilisateur modifie ses informations via des composants `<input>` reliés aux variables TypeScript `prenom`, `entreprise` et `role`.
  - **Transfert REST** : Chaque message envoyé au backend inclut ces champs dans le corps de la requête JSON `ChatbotRequestDto`.

#### 📂 Emplacements Exacts du Code :
* **Frontend Angular** :
  * Fichier : [`frontend/src/app/components/chatbot.component.ts`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/src/app/components/chatbot.component.ts)
  * Méthodes clés : `loadProfile()` (L408), `saveProfile()` (L420), `toggleProfileSettings()` (L435)
  * Modèle de données : [`frontend/src/app/services/chatbot.service.ts`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/src/app/services/chatbot.service.ts) (Interface `ChatbotAskPayload` L7-12)
* **Backend Spring Boot** :
  * Fichier DTO : [`backend/chatbot-service/src/main/java/com/codingfactory/chatbot/dto/ChatbotRequestDto.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/chatbot-service/src/main/java/com/codingfactory/chatbot/dto/ChatbotRequestDto.java) (L1-15, variables `question`, `prenom`, `entreprise`, `role`)

---

### 1.2 Moteur de Classification NLP & Traitement d'Intention

* **Description Métier** : Le backend reçoit la question en langage naturel, normalise le texte (suppression des accents, minuscules, nettoyage) et applique un réseau de filtres d'expressions régulières (Regex) pour classifier la demande parmi 12 intentions métier.
* **Intentions Gérées** :
  1. `SALUTATION` : Accueil personnalisé avec le nom du visiteur.
  2. `PRESENTATION` : Présentation globale de la société CodingFactory.
  3. `PLATFORM_ENGINEERING` : Recommandation de l'expertise Internal Developer Platform (IDP) & DevSecOps.
  4. `BLOCKCHAIN_WEB3` : Recommandation de l'expertise Smart Contracts & Cryptographie FinTech.
  5. `CYBERSECURITE` : Recommandation de l'expertise Audit SI, Pentest & ISO 27001.
  6. `DEVELOPPEMENT_SOFTWARE` : Recommandation de l'expertise Microservices Cloud Native (Spring Boot / Angular).
  7. `FORMATION` : Recommandation des Bootcamps et parcours certifiants.
  8. `CONSEIL_STRATEGIQUE` : Recommandation du pôle Schémas Directeurs & Gouvernance SI.
  9. `AI_DATA` : Recommandation du pôle Data Science, LLM & MLOps.
  10. `PRICING` : Explication des tarifs et modes d'engagement (Régie avec TJM, Forfait, Centre Dédié).
  11. `CONTACT` : Horaires, téléphone, email et adresse physique à Tunis.
  12. `PFE` : Orientation vers le module d'incubation des Projets de Fin d'Études.

#### 📂 Emplacements Exacts du Code :
* **Backend Spring Boot** :
  * Service Métier : [`backend/chatbot-service/src/main/java/com/codingfactory/chatbot/service/ChatbotEngineService.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/chatbot-service/src/main/java/com/codingfactory/chatbot/service/ChatbotEngineService.java) (Méthode `processQuestion(ChatbotRequestDto request)` L70-340)
  * Controller REST : [`backend/chatbot-service/src/main/java/com/codingfactory/chatbot/controller/ChatbotController.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/chatbot-service/src/main/java/com/codingfactory/chatbot/controller/ChatbotController.java) (Endpoint `POST /api/chatbot/ask` L28-32)
* **Frontend Angular** :
  * Service HTTP : [`frontend/src/app/services/chatbot.service.ts`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/src/app/services/chatbot.service.ts) (Méthode `ask()` L20-27)

---

### 1.3 Orientation vers les Consultants Experts & Carte de Visite Interactive

* **Description Métier** : Pour chaque intention liée à un domaine d'expertise, le backend sélectionne l'expert consultant senior correspondant et renvoie sa carte de visite détaillée (Avatar Emoji, Nom, Rôle, Email, Téléphone, et liens d'action directes).
* **Les 8 Consultants Référents** :
  - **Sami Mansour** : Architecte Lead Software & Cloud (`sami.mansour@codingfactory.tn` | `+216 20 111 222`)
  - **Mehdi Gharbi** : Lead Expert Cybersécurité & Pentest (`mehdi.gharbi@codingfactory.tn` | `+216 20 333 444`)
  - **Inès Chebbi** : Lead Platform Engineer & DevSecOps Specialist (`ines.chebbi@codingfactory.tn` | `+216 20 888 999`)
  - **Tarek Ben Ammar** : Lead Architect Blockchain & Smart Contracts (`tarek.benammar@codingfactory.tn` | `+216 20 777 999`)
  - **Amina Triki** : Directrice Formations & Tech Coach (`amina.triki@codingfactory.tn` | `+216 20 555 666`)
  - **Karim Trabelsi** : Senior Consultant IT Strategy (`karim.trabelsi@codingfactory.tn` | `+216 20 777 888`)
  - **Dr. Yassine Ben Romdhane** : Lead Data Scientist & Expert IA (`yassine.benromdhane@codingfactory.tn` | `+216 20 999 000`)
  - **Youssef Ben Ali** : Coordinateur PFE & Relations Académiques (`pfe@codingfactory.tn` | `+216 20 123 456`)

#### 📂 Emplacements Exacts du Code :
* **Backend Spring Boot** :
  * Fichier DTO : [`backend/chatbot-service/src/main/java/com/codingfactory/chatbot/dto/ChatbotResponseDto.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/chatbot-service/src/main/java/com/codingfactory/chatbot/dto/ChatbotResponseDto.java) (Champs `consultantNom`, `consultantRole`, `consultantEmail`, `consultantPhone`, `consultantAvatar`)
* **Frontend Angular** :
  * Fichier Composant : [`frontend/src/app/components/chatbot.component.ts`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/src/app/components/chatbot.component.ts) (Rendu HTML de la carte consultant L180-215)

---

### 1.4 Modale de Prise de Rendez-Vous en Ligne (`RDV Simulator`)

* **Description Métier** : Un clic sur le bouton **"📅 Programmer un RDV"** présent sur la carte de visite d'un consultant ouvre un formulaire pop-up de réservation de créneau. L'utilisateur indique sa date souhaitée et sa demande est immédiatement enregistrée dans le fil de discussion.
* **Fonctionnement Sous le Capot** :
  - **Gestion de l'état Modal** : Variable booléenne `rdvModalOpen` contrôlant la visibilité du calque backdrop CSS.
  - **Confirmation & Injection** : À la soumission, la méthode `confirmRdv()` génère un message de confirmation estampillé avec l'heure courante.

#### 📂 Emplacements Exacts du Code :
* **Frontend Angular** :
  * Fichier Composant : [`frontend/src/app/components/chatbot.component.ts`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/src/app/components/chatbot.component.ts)
  * Modale HTML : L260-304
  * Méthodes TypeScript : `openRdvModal()` L505, `closeRdvModal()` L515, `confirmRdv()` L520

---

### 1.5 Catalogue Latéral Synchronisé des Services

* **Description Métier** : Affichage dynamique de la liste des 8 métiers de consulting sur le panneau latéral gauche avec le nombre d'experts disponibles, la description et les puces technologiques. Un clic sur une carte permet de poser automatiquement une question au bot.

#### 📂 Emplacements Exacts du Code :
* **Backend Spring Boot** :
  * Service : [`backend/chatbot-service/src/main/java/com/codingfactory/chatbot/service/ChatbotEngineService.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/chatbot-service/src/main/java/com/codingfactory/chatbot/service/ChatbotEngineService.java) (Méthode `getConsultingServices()` L15-65)
  * Controller : [`backend/chatbot-service/src/main/java/com/codingfactory/chatbot/controller/ChatbotController.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/chatbot-service/src/main/java/com/codingfactory/chatbot/controller/ChatbotController.java) (Endpoint `GET /api/chatbot/services` L22-26)
* **Frontend Angular** :
  * Fichier Composant : [`frontend/src/app/components/chatbot.component.ts`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/src/app/components/chatbot.component.ts) (Méthode `askAboutService()` L440)

---

## 🔵 MODULE 2 : PROJETS DE FIN D'ÉTUDES (PFE) & AI MATCHER

---

### 2.1 Radar d'Adéquation IA Candidat ↔ Sujet PFE (Skill Matcher)

* **Description Métier** : L'étudiant coche ses compétences maîtrisées (*Spring Boot, Angular, Kubernetes, Python, Cybersécurité...*) et choisit son domaine de prédilection. L'algorithme calcule un score de compatibilité de 0 à 100%, liste les compétences matchées et formule une recommandation personnalisée.
* **Fonctionnement de l'Algorithme (Backend & Fallback)** :
  - **Score de base** : 40% (socle d'ingénierie).
  - **Pondération compétences** : +15% pour chaque technologie correspondant aux critères du sujet PFE.
  - **Pondération domaine** : +20% si le domaine correspond au domaine souhaité.
  - **Plafond & Classement** : Score plafonné à 98%. Les résultats sont triés par ordre décroissant de score de compatibilité.

#### 📂 Emplacements Exacts du Code :
* **Backend Spring Boot** :
  * Controller REST : [`backend/pfe-service/src/main/java/com/codingfactory/pfe/controller/SujetPfeController.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/pfe-service/src/main/java/com/codingfactory/pfe/controller/SujetPfeController.java) (Endpoint `POST /api/pfe/sujets/match` L55-130)
  * Service Monolithe : [`backend/src/main/java/com/codingfactory/backend/service/impl/SujetPfeServiceImpl.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/src/main/java/com/codingfactory/backend/service/impl/SujetPfeServiceImpl.java) (Méthode `calculateMatching()` L70-140)
  * Fichiers DTO :
    * [`PfeMatchingRequestDto.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/pfe-service/src/main/java/com/codingfactory/pfe/dto/PfeMatchingRequestDto.java)
    * [`PfeMatchResultDto.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/pfe-service/src/main/java/com/codingfactory/pfe/dto/PfeMatchResultDto.java)
    * [`PfeMatchingResponseDto.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/pfe-service/src/main/java/com/codingfactory/pfe/dto/PfeMatchingResponseDto.java)
* **Frontend Angular** :
  * Fichier Composant : [`frontend/src/app/components/pfe.component.ts`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/src/app/components/pfe.component.ts) (Méthodes `runMatchingAnalysis()` L665, `calculateMatchingLocal()` L680 et `exportMatchingPDF()` L775)
  * Service HTTP : [`frontend/src/app/services/pfe.service.ts`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/src/app/services/pfe.service.ts) (Méthode `calculateMatching()` L74)
  * **Exportation PDF des Résultats** : Bouton **"📄 Exporter Rapport PDF"** générant la fiche officielle imprimable avec scores d'adéquation et recommandations IA.

---

### 2.2 Postulation en 1-Clic avec Profil Recommandé

* **Description Métier** : Un clic sur le bouton **"🚀 Postuler avec ce profil"** depuis la carte de résultat du Radar d'Adéquation bascule automatiquement l'utilisateur vers l'onglet Candidature, sélectionne l'identifiant du sujet recommandé, remplit les informations du candidat et rédige une lettre de motivation pré-remplie avec le score IA et les compétences clés.

#### 📂 Emplacements Exacts du Code :
* **Frontend Angular** :
  * Fichier Composant : [`frontend/src/app/components/pfe.component.ts`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/src/app/components/pfe.component.ts)
  * Méthode TypeScript : `applyWithMatching(match: PfeMatchResult)` (L752-765)

---

### 2.3 Catalogue des Sujets PFE & Filtres Rapides

* **Description Métier** : Consultation de la liste des offres de PFE. Barre de recherche textuelle combinée avec des puces de filtres en 1-clic (*Spring Boot, Angular, Cybersécurité, IA*) et un commutateur pour afficher uniquement les sujets actifs.
* **Mode Administration (CRUD)** : En cochant *"Mode Administration"*, l'utilisateur peut ajouter, modifier ou supprimer un sujet PFE (Titre, Domaine, Technologie, Entreprise, Description, Statut actif/inactif).

#### 📂 Emplacements Exacts du Code :
* **Backend Spring Boot** :
  * Controller REST : [`backend/pfe-service/src/main/java/com/codingfactory/pfe/controller/SujetPfeController.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/pfe-service/src/main/java/com/codingfactory/pfe/controller/SujetPfeController.java) (Endpoints `GET /api/pfe/sujets`, `POST`, `PUT`, `DELETE` L20-54)
* **Frontend Angular** :
  * Fichier Composant : [`frontend/src/app/components/pfe.component.ts`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/src/app/components/pfe.component.ts) (Getter `filteredSujets` L604-617)

---

### 2.4 Tableau de Bord & Analytics PFE

* **Description Métier** : Présentation sous forme de cartes d'indicateurs clés de performance (KPI) des statistiques globales de la plateforme PFE (Nombre total de sujets, sujets ouverts, projets réalisés, candidatures en attente, distribution des technologies et taux de réussite ML).

#### 📂 Emplacements Exacts du Code :
* **Backend Spring Boot** :
  * Controller REST : [`backend/pfe-service/src/main/java/com/codingfactory/pfe/controller/PfeStatsController.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/pfe-service/src/main/java/com/codingfactory/pfe/controller/PfeStatsController.java) (Endpoint `GET /api/pfe/stats` L31-39)
* **Frontend Angular** :
  * Fichier Composant : [`frontend/src/app/components/pfe.component.ts`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/src/app/components/pfe.component.ts) (Template de l'onglet `analytics` L300-375)

---

### 2.5 Soumission Résiliente des Candidatures & Suivi Administrateur

* **Description Métier** : Permet à un candidat de postuler publiquement à un sujet. Le système crée ou rattache le compte candidat par e-mail, associe la candidature au sujet visé, et permet à l'administrateur de valider (*Accepter / Refuser*) le dossier tout en calculant la prédiction ML.
* **Résilience Anti-Crash 500** : Le backend vérifie l'existence du sujet en base. Si le sujet est manquant, il rattache automatiquement la candidature à un sujet actif valide sans jamais planter.

#### 📂 Emplacements Exacts du Code :
* **Backend Spring Boot** :
  * Controller REST : [`backend/pfe-service/src/main/java/com/codingfactory/pfe/controller/CandidatureController.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/pfe-service/src/main/java/com/codingfactory/pfe/controller/CandidatureController.java) (Endpoint `POST /api/pfe/candidatures/submit` L69-98)
  * Service Impl : [`backend/src/main/java/com/codingfactory/backend/service/impl/CandidatureServiceImpl.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/src/main/java/com/codingfactory/backend/service/impl/CandidatureServiceImpl.java) (Méthode `submitCandidature()` L53-90)
* **Frontend Angular** :
  * Fichier Composant : [`frontend/src/app/components/pfe.component.ts`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/src/app/components/pfe.component.ts) (Méthodes `submitCandidaturePublic()` L830, `acceptCandidature()` L860, `refuseCandidature()` L870)

---

## 🤖 MODULE 3 : SERVICE MACHINE LEARNING (`ml-service`) & PRÉDICTION

---

### 3.1 Architecture du Modèle XGBoost & API FastAPI Python
* **Description Métier** : Microservice Python autonome basé sur **FastAPI** et **XGBoost** calculant la probabilité d'acceptation d'un dossier de candidature (0.0 à 1.0) avec retour de score en pourcentage et recommandation explicative.
* **Algorithme & Features** :
  - Extraits d'éléments : Nombre de compétences clés, niveau d'études, domaine de correspondance, et longueur de la lettre de motivation.
  - Hyperparamètres XGBoost : `n_estimators=200`, `max_depth=4`, `learning_rate=0.05`.

#### 📂 Emplacements Exacts du Code :
* **Service Python FastAPI** :
  * Point d'entrée FastAPI : [`backend/ml-service/app/main.py`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/ml-service/app/main.py)
  * Endpoints de Prédiction & Entraînement :
    - Endpoint `/predict` : [`backend/ml-service/app/routers/prediction.py`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/ml-service/app/routers/prediction.py)
    - Endpoint `/train` : [`backend/ml-service/app/routers/training.py`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/ml-service/app/routers/training.py)
  * Modèle XGBoost & Persistance : [`backend/ml-service/app/model.py`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/ml-service/app/model.py)
  * Extraction des Caractéristiques (Features) : [`backend/ml-service/app/features.py`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/ml-service/app/features.py)

---

## 🧪 MODULE 4 : SUITE DE TESTS UNITAIRES (BACKEND & FRONTEND)

---

### 4.1 Suite de Tests Unitaires Backend (Spring Boot / JUnit 5)
* **Exécution** : `cd backend` puis `mvn test`
* **Résultat** : **`6 / 6 SUCCESS`**

#### 📂 Emplacements des Fichiers de Test Backend :
1. **Authentification JWT & Rôles** : [`backend/src/test/java/com/codingfactory/backend/controller/AuthControllerTest.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/src/test/java/com/codingfactory/backend/controller/AuthControllerTest.java)
   - Valide les endpoints `/api/auth/login` pour les rôles `ADMIN` et `CANDIDAT`.
2. **Moteur du Chatbot Consulting** : [`backend/src/test/java/com/codingfactory/backend/service/ChatbotEngineServiceTest.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/src/test/java/com/codingfactory/backend/service/ChatbotEngineServiceTest.java)
   - Valide le traitement de questions complexes et la restitution des expertises par métier.
3. **Radar de Matching IA PFE** : [`backend/src/test/java/com/codingfactory/backend/service/SujetPfeServiceTest.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/src/test/java/com/codingfactory/backend/service/SujetPfeServiceTest.java)
   - Valide la méthode `calculateMatching` de calcul du score de compatibilité.
4. **Controller Sujets PFE** : [`backend/src/test/java/com/codingfactory/backend/controller/SujetPfeControllerTest.java`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/src/test/java/com/codingfactory/backend/controller/SujetPfeControllerTest.java)
   - Valide la restitution JSON du catalogue des sujets.

---

### 4.2 Suite de Tests Unitaires Frontend (Angular 18 / Jasmine & Karma)
* **Exécution** : `cd frontend` puis `npx ng test --watch=false`
* **Résultat** : **`TOTAL: 8 SUCCESS`** (Port interactif `http://localhost:9876`)

#### 📂 Emplacements des Fichiers de Test Frontend :
1. **Composant Racine** : [`frontend/src/app/app.component.spec.ts`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/src/app/app.component.spec.ts)
   - Valide l'instanciation de l'application et l'injection du Router.
2. **Service d'Authentification** : [`frontend/src/app/services/auth.service.spec.ts`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/src/app/services/auth.service.spec.ts)
   - Valide le stockage de la session dans `localStorage`, le mode de secours démo hors-ligne et la déconnexion (`logout()`).
3. **Composant Page de Connexion** : [`frontend/src/app/components/login.component.spec.ts`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/src/app/components/login.component.spec.ts)
   - Valide l'accès rapide 1-clic pour les rôles Admin et Candidat.

---

## 🛠️ PARTIE 5 : DOCKER, CI/CD & SCRIPTS DE DÉPLOIEMENT

---

### 5.1 Conteneurisation Docker & Orchestration

#### 📂 Emplacements des Fichiers Docker :
1. **Fichier de Composition Global** : [`docker-compose.yml`](file:///c:/Users/user/Desktop/CodingFactoryApp/docker-compose.yml)
   * Orchestre 6 conteneurs : `mysql:8.0`, `eureka` (Port 8761), `gateway` (Port 8090), `pfe-service` (Port 8081), `chatbot-service` (Port 8082), `ml-service` (Port 8084), `frontend` (Port 8083/80).
   * Contient les `healthcheck` pour valider la disponibilité de MySQL avant de lancer les microservices backend.
2. **Dockerfile Frontend Multi-Stage** : [`frontend/Dockerfile`](file:///c:/Users/user/Desktop/CodingFactoryApp/frontend/Dockerfile)
   * Stage 1 : Construction de l'application Angular avec Node 20 (`npm run build:ci`).
   * Stage 2 : Serveur Web léger Nginx 1.27 Alpine copiant la distribution de production dans `/usr/share/nginx/html`.
3. **Dockerfile Backend Multi-Stage** : [`backend/chatbot-service/Dockerfile`](file:///c:/Users/user/Desktop/CodingFactoryApp/backend/chatbot-service/Dockerfile)
   * Stage 1 : Construction de l'exécutable Java avec Maven 3.9 et Temurin JDK 17 (`mvn package`).
   * Stage 2 : Runtime léger Eclipse Temurin 17 JRE Alpine exécutant le JAR.

---

### 5.2 Intégration & Déploiement Continus (CI/CD)

#### 📂 Emplacements des Pipelines :
1. **Pipeline Jenkins Déclarative** : [`Jenkinsfile`](file:///c:/Users/user/Desktop/CodingFactoryApp/Jenkinsfile)
   * Stage 1 (`Checkout`) : Extraction du code source Git.
   * Stage 2 (`Backend CI`) : Compilation Maven `mvn clean verify` et génération automatique des rapports de tests unitaires JUnit.
   * Stage 3 (`Frontend CI`) : Validation du build Angular `npm ci` et `npm run build:ci`.
   * Stage 4 (`Docker CD`) : Construction et démarrage des conteneurs `docker compose up -d` suivis d'un test de santé automatisé `curl` d'intégration.
2. **Script de Lancement Windows Automatisé** : [`start-CODINGFACTORY.bat`](file:///c:/Users/user/Desktop/CodingFactoryApp/start-CODINGFACTORY.bat)
   * Propose le démarrage en mode monolithe ou microservices, configure le fichier `proxy.conf.json` d'Angular et lance les consoles de développement.
