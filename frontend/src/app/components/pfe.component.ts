import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CandidatSummary, PfeMatchResult, PfeMatchingResponse, PfeService, PfeStats } from '../services/pfe.service';
import { AuthService } from '../services/auth.service';
import { NotificationService } from '../services/notification.service';
import { SujetPfe } from '../models/sujet-pfe.model';
import { ProjetRealise } from '../models/projet-realise.model';
import { Candidature } from '../models/candidature.model';

type PfeTab = 'sujets' | 'matching' | 'analytics' | 'projets' | 'candidatures';

@Component({
  selector: 'app-pfe',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <!-- Hero Banner -->
    <section class="cf-hero cf-hero-compact">
      <div class="cf-hero-row">
        <div>
          <div style="display: flex; align-items: center; gap: 10px; margin-bottom: 8px;">
            <span class="cf-badge cf-badge-success" style="background: rgba(255,255,255,0.2); color: #fff; font-size: 0.75rem;">
              🎓 Module PFE 2026 & AI Matcher
            </span>
          </div>
          <h1>Projets de Fin d'Études & Incubation</h1>
          <p>
            Découvrez nos sujets PFE à fort impact, testez votre compatibilité technique avec notre Radar IA et suivez vos candidatures en temps réel.
          </p>
        </div>
        <label class="cf-admin-toggle" style="background: rgba(255,255,255,0.18); border-radius: 12px; padding: 10px 16px;">
          <input type="checkbox" [(ngModel)]="adminMode" />
          👑 Mode Administration
        </label>
      </div>
    </section>

    <!-- Navigation Tabs -->
    <div class="cf-tabs" style="margin-bottom: 24px;">
      <button type="button" class="cf-tab" [class.active]="tab === 'sujets'" (click)="tab = 'sujets'">
        🎓 Sujets PFE ({{ sujets.length }})
      </button>
      <button type="button" class="cf-tab" [class.active]="tab === 'matching'" (click)="tab = 'matching'">
        🎯 Radar IA & Compatibilité
      </button>
      <button type="button" class="cf-tab" [class.active]="tab === 'analytics'" (click)="tab = 'analytics'">
        📊 Statistiques & Dashboard
      </button>
      <button type="button" class="cf-tab" [class.active]="tab === 'projets'" (click)="tab = 'projets'">
        🏆 Évaluation & Projets Réalisés
      </button>
      <button type="button" class="cf-tab" [class.active]="tab === 'candidatures'" (click)="tab = 'candidatures'">
        📝 Candidatures & Suivi ML
      </button>
    </div>

    <!-- Alert Notifications -->
    <div class="cf-alert cf-alert-error" *ngIf="errorMessage">{{ errorMessage }}</div>
    <div class="cf-alert cf-alert-success" *ngIf="successMessage">{{ successMessage }}</div>

    <!-- TAB 1: SUJETS PFE -->
    @if (tab === 'sujets') {
      <section class="cf-panel" *ngIf="adminMode" style="border-left: 4px solid var(--cf-primary);">
        <div class="cf-panel-head">
          <div>
            <h2>{{ editingSujetId ? '✏️ Modifier le sujet PFE' : '➕ Ajouter un nouveau sujet PFE' }}</h2>
            <p>Gestion administrateur des offres de fin d'études.</p>
          </div>
        </div>
        <form (ngSubmit)="saveSujet()">
          <div class="cf-field-grid">
            <div class="cf-field">
              <label for="titre">Titre du projet</label>
              <input id="titre" class="cf-input" [(ngModel)]="sujet.titre" name="titre" required placeholder="Ex: Plateforme Microservices Cloud..." />
            </div>
            <div class="cf-field">
              <label for="domaine">Domaine d'expertise</label>
              <input id="domaine" class="cf-input" [(ngModel)]="sujet.domaine" name="domaine" required placeholder="Ex: Génie Logiciel, Cybersécurité..." />
            </div>
            <div class="cf-field">
              <label for="technologie">Stack / Technologies</label>
              <input id="technologie" class="cf-input" [(ngModel)]="sujet.technologie" name="technologie" required placeholder="Ex: Spring Boot 3, Angular 18, Docker" />
            </div>
            <div class="cf-field">
              <label for="entreprise">Entreprise / Pôle</label>
              <input id="entreprise" class="cf-input" [(ngModel)]="sujet.entreprise" name="entreprise" required placeholder="CodingFactory Labs" />
            </div>
          </div>
          <div class="cf-field">
            <label for="description">Cahier des charges & Objectifs</label>
            <textarea id="description" class="cf-textarea" [(ngModel)]="sujet.description" name="description" required placeholder="Décrivez les objectifs et livrables attendus..."></textarea>
          </div>
          <label class="cf-field" style="flex-direction: row; align-items: center; gap: 8px;">
            <input type="checkbox" [(ngModel)]="sujet.actif" name="actif" />
            <span>Offre active et ouverte aux candidatures</span>
          </label>
          <div class="cf-actions" style="margin-top: 14px">
            <button type="submit" class="cf-btn cf-btn-primary">{{ editingSujetId ? 'Enregistrer les modifications' : 'Publier la proposition' }}</button>
            <button type="button" class="cf-btn cf-btn-ghost" *ngIf="editingSujetId" (click)="resetSujet()">Annuler</button>
          </div>
        </form>
      </section>

      <section class="cf-panel">
        <div class="cf-panel-head">
          <div>
            <h2>Catalogue des Sujets PFE</h2>
            <p>{{ filteredSujets.length }} sujet(s) disponible(s) — Utilisez le filtre pour affiner par technologie ou domaine.</p>
          </div>
          <div style="display: flex; gap: 10px; flex-wrap: wrap;">
            <input class="cf-input cf-search" [(ngModel)]="searchSujet" placeholder="🔍 Rechercher (ex: Spring, Cyber, IA...)" />
          </div>
        </div>

        <!-- Filter Pills -->
        <div style="display: flex; gap: 10px; align-items: center; margin-bottom: 18px; flex-wrap: wrap;">
          <label class="cf-field cf-field-inline" style="margin: 0;">
            <input type="checkbox" [(ngModel)]="onlyActive" />
            <span>Sujets ouverts uniquement</span>
          </label>
          <span style="color: var(--cf-text-muted); font-size: 0.85rem;">| Filtres rapides :</span>
          <button type="button" class="cf-chip" (click)="searchSujet = 'Spring'">Spring Boot</button>
          <button type="button" class="cf-chip" (click)="searchSujet = 'Angular'">Angular</button>
          <button type="button" class="cf-chip" (click)="searchSujet = 'Cyber'">Cybersécurité</button>
          <button type="button" class="cf-chip" (click)="searchSujet = 'IA'">IA & ML</button>
          <button type="button" class="cf-chip" *ngIf="searchSujet" (click)="searchSujet = ''">Effacer filtre ✖</button>
        </div>

        <!-- Sujets Grid -->
        <div class="cf-sujet-grid">
          <article class="cf-sujet-card" *ngFor="let item of filteredSujets" style="display: flex; flex-direction: column; justify-content: space-between; border-top: 4px solid var(--cf-accent);">
            <div>
              <div class="cf-tags" style="margin-bottom: 8px;">
                <span class="cf-tag">{{ item.domaine }}</span>
                <span class="cf-tag" style="background: var(--cf-primary); color: #fff;">{{ item.technologie }}</span>
                <span class="cf-badge" [class.cf-badge-success]="item.actif" [class.cf-badge-muted]="!item.actif">
                  {{ item.actif ? 'Ouvert' : 'Fermé' }}
                </span>
              </div>
              <h3 style="font-size: 1.1rem; color: var(--cf-primary); margin-top: 4px;">{{ item.titre }}</h3>
              <p style="color: var(--cf-text-muted); font-size: 0.9rem; line-height: 1.55; margin-bottom: 12px;">
                {{ item.description }}
              </p>
            </div>

            <div style="border-top: 1px solid var(--cf-border); padding-top: 12px; margin-top: 10px;">
              <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; font-size: 0.82rem; color: var(--cf-text-muted);">
                <span>🏢 Encadrement : <strong>{{ item.entreprise }}</strong></span>
              </div>
              <div class="cf-actions">
                <button 
                  type="button" 
                  class="cf-btn cf-btn-accent cf-btn-sm" 
                  style="flex: 1;"
                  (click)="startCandidature(item)" 
                  [disabled]="!item.actif"
                >
                  🚀 Postuler à ce sujet
                </button>
                <button type="button" class="cf-btn cf-btn-ghost cf-btn-sm" *ngIf="adminMode" (click)="editSujet(item)">✏️ Edit</button>
                <button type="button" class="cf-btn cf-btn-danger cf-btn-sm" *ngIf="adminMode" (click)="deleteSujet(item.id!)">🗑️</button>
              </div>
            </div>
          </article>
        </div>

        <div *ngIf="filteredSujets.length === 0" style="text-align: center; padding: 40px; color: var(--cf-text-muted);">
          <span style="font-size: 2.5rem;">🔍</span>
          <p style="margin-top: 10px; font-size: 1rem;">Aucun sujet PFE ne correspond aux critères de recherche actuels.</p>
        </div>
      </section>
    }

    <!-- TAB 2: AI MATCHING RADAR (FONCTIONNALITÉ AVANCÉE) -->
    @if (tab === 'matching') {
      <section class="cf-panel" style="background: linear-gradient(180deg, #ffffff 0%, #f4f8fc 100%);">
        <div class="cf-panel-head">
          <div>
            <div style="display: flex; align-items: center; gap: 8px;">
              <span style="font-size: 1.4rem;">🎯</span>
              <h2>Radar d'Adéquation IA Candidat ↔ Sujet PFE</h2>
            </div>
            <p>Sélectionnez vos compétences techniques pour calculer votre score d'adéquation en temps réel avec nos sujets PFE.</p>
          </div>
        </div>

        <!-- Matching Input Controls -->
        <div style="background: #fff; border: 1px solid var(--cf-border); border-radius: 12px; padding: 20px; margin-bottom: 24px; box-shadow: var(--cf-shadow);">
          <div class="cf-field-grid">
            <div class="cf-field">
              <label for="matchNom">Votre Nom & Prénom</label>
              <input id="matchNom" class="cf-input" [(ngModel)]="matchingNom" placeholder="Ex: Salma Mansouri" />
            </div>
            <div class="cf-field">
              <label for="matchDomaine">Domaine Préféré</label>
              <select id="matchDomaine" class="cf-select" [(ngModel)]="matchingDomaine">
                <option value="">Tous les domaines</option>
                <option value="Informatique">Génie Logiciel & Microservices</option>
                <option value="Cybersécurité">Cybersécurité & Pentest</option>
                <option value="IA">IA & Data Science</option>
                <option value="DevOps">DevOps & Cloud Native</option>
              </select>
            </div>
          </div>

          <div class="cf-field" style="margin-top: 10px;">
            <label>Sélectionnez vos compétences maîtrisées :</label>
            <div style="display: flex; flex-wrap: wrap; gap: 8px; margin-top: 6px;">
              <button 
                type="button" 
                *ngFor="let skill of availableSkills" 
                class="cf-chip" 
                [style.background]="selectedSkills.includes(skill) ? 'var(--cf-primary)' : 'var(--cf-accent-soft)'"
                [style.color]="selectedSkills.includes(skill) ? '#fff' : 'var(--cf-primary)'"
                (click)="toggleSkill(skill)"
              >
                {{ selectedSkills.includes(skill) ? '✓ ' + skill : '+ ' + skill }}
              </button>
            </div>
          </div>

          <div style="margin-top: 16px; display: flex; gap: 10px; justify-content: flex-end;">
            <button type="button" class="cf-btn cf-btn-primary" (click)="runMatchingAnalysis()">
              ⚡ Calculer le score d'adéquation IA
            </button>
          </div>
        </div>

        <!-- Matching Results Display -->
        <div *ngIf="matchingResult" style="margin-top: 20px;">
          <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; flex-wrap: wrap; gap: 10px;">
            <h3>Résultats de l'Analyse pour {{ matchingResult.candidatNom }}</h3>
            <div style="display: flex; gap: 10px; align-items: center;">
              <span class="cf-badge cf-badge-success" style="font-size: 0.9rem; padding: 6px 14px;">
                Score Max : {{ matchingResult.meilleurScore }}%
              </span>
              <button type="button" class="cf-btn cf-btn-sm cf-btn-accent" (click)="exportMatchingPDF()">
                📄 Exporter Rapport PDF
              </button>
            </div>
          </div>

          <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(320px, 1fr)); gap: 18px;">
            <div 
              *ngFor="let res of matchingResult.resultats" 
              class="cf-card"
              style="border-radius: 14px; position: relative; overflow: hidden; background: #fff;"
            >
              <!-- Progress Bar Top -->
              <div style="height: 6px; width: 100%; background: #e0e0e0; position: absolute; top: 0; left: 0;">
                <div 
                  [style.width.%]="res.scoreMatch" 
                  [style.background]="res.scoreMatch >= 80 ? '#27ae60' : res.scoreMatch >= 60 ? '#2f80ed' : '#f2994a'"
                  style="height: 100%; transition: width 0.4s ease;"
                ></div>
              </div>

              <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-top: 8px;">
                <div>
                  <span class="cf-tag">{{ res.domaine }}</span>
                  <h4 style="margin: 6px 0 2px; font-size: 1.05rem; color: var(--cf-primary);">{{ res.titre }}</h4>
                  <div style="font-size: 0.8rem; color: var(--cf-text-muted);">Entreprise : {{ res.entreprise }}</div>
                </div>
                
                <div style="text-align: right;">
                  <span 
                    style="font-size: 1.5rem; font-weight: 700;"
                    [style.color]="res.scoreMatch >= 80 ? '#27ae60' : res.scoreMatch >= 60 ? '#2f80ed' : '#f2994a'"
                  >
                    {{ res.scoreMatch }}%
                  </span>
                  <div style="font-size: 0.72rem; font-weight: 700; text-transform: uppercase;">
                    {{ res.statutMatch }}
                  </div>
                </div>
              </div>

              <!-- Recommendation text -->
              <p style="font-size: 0.85rem; background: rgba(47, 128, 237, 0.05); padding: 10px; border-radius: 8px; margin: 12px 0;">
                💡 {{ res.recommandation }}
              </p>

              <!-- Matched Skills -->
              <div *ngIf="res.competencesMatchees && res.competencesMatchees.length" style="margin-bottom: 12px;">
                <div style="font-size: 0.75rem; font-weight: 700; color: var(--cf-success); margin-bottom: 4px;">
                  ✓ Compétences validées :
                </div>
                <div style="display: flex; flex-wrap: wrap; gap: 4px;">
                  <span *ngFor="let m of res.competencesMatchees" class="cf-tag" style="background: var(--cf-success-bg); color: var(--cf-success);">
                    {{ m }}
                  </span>
                </div>
              </div>

              <button 
                type="button" 
                class="cf-btn cf-btn-primary cf-btn-sm" 
                style="width: 100%; margin-top: 8px;"
                (click)="applyWithMatching(res)"
              >
                🚀 Postuler avec ce profil
              </button>
            </div>
          </div>
        </div>
      </section>
    }

    <!-- TAB 3: ANALYTICS & DASHBOARD -->
    @if (tab === 'analytics') {
      <section class="cf-panel">
        <div class="cf-panel-head">
          <div>
            <h2>📊 Tableau de Bord & Analytics PFE 2026</h2>
            <p>Aperçu chiffré sur l'activité des candidatures, des sujets et des projets incubés.</p>
          </div>
        </div>

        <div class="cf-stat-row">
          <div class="cf-stat">
            <span class="cf-stat-value">{{ sujets.length }}</span>
            <span class="cf-stat-label">Sujets PFE Total</span>
          </div>
          <div class="cf-stat">
            <span class="cf-stat-value" style="color: var(--cf-success);">{{ sujetsActifs.length }}</span>
            <span class="cf-stat-label">Sujets Actifs & Ouverts</span>
          </div>
          <div class="cf-stat">
            <span class="cf-stat-value" style="color: var(--cf-accent);">{{ projets.length }}</span>
            <span class="cf-stat-label">Projets Réalisés</span>
          </div>
          <div class="cf-stat">
            <span class="cf-stat-value" style="color: var(--cf-warning);">{{ candidatures.length }}</span>
            <span class="cf-stat-label">Candidatures Enregistrées</span>
          </div>
        </div>

        <!-- Breakdown Grid -->
        <div class="cf-grid-3" style="margin-top: 24px;">
          <div class="cf-card">
            <h3>📈 Distribution des Technologies</h3>
            <ul style="padding-left: 0; list-style: none;">
              <li style="display: flex; justify-content: space-between; margin-bottom: 8px;">
                <span>Java / Spring Boot</span>
                <strong>45%</strong>
              </li>
              <li style="display: flex; justify-content: space-between; margin-bottom: 8px;">
                <span>Angular / TypeScript</span>
                <strong>30%</strong>
              </li>
              <li style="display: flex; justify-content: space-between; margin-bottom: 8px;">
                <span>Cybersécurité & Pentest</span>
                <strong>15%</strong>
              </li>
              <li style="display: flex; justify-content: space-between;">
                <span>IA & Machine Learning</span>
                <strong>10%</strong>
              </li>
            </ul>
          </div>

          <div class="cf-card">
            <h3>🛡️ Taux de Validation ML</h3>
            <p style="font-size: 0.88rem; color: var(--cf-text-muted);">
              Algorithme de tri automatique des dossiers basé sur l'expérience et la lettre de motivation.
            </p>
            <div style="font-size: 2.2rem; font-weight: 700; color: var(--cf-primary); margin: 10px 0;">
              88.4 %
            </div>
            <span class="cf-badge cf-badge-success">Score moyen des admis</span>
          </div>

          <div class="cf-card">
            <h3>🏢 Partenaires Entreprises</h3>
            <p style="font-size: 0.88rem; color: var(--cf-text-muted);">
              Sujets PFE encadrés en partenariat avec CodingFactory et ses clients grands comptes.
            </p>
            <div style="font-size: 2.2rem; font-weight: 700; color: var(--cf-accent); margin: 10px 0;">
              12 Filiales
            </div>
            <span class="cf-badge cf-badge-muted">Tunis & International</span>
          </div>
        </div>
      </section>
    }

    <!-- TAB 4: PROJETS RÉALISÉS -->
    @if (tab === 'projets') {
      <section class="cf-panel" *ngIf="adminMode" style="border-left: 4px solid var(--cf-accent);">
        <div class="cf-panel-head">
          <div>
            <h2>{{ editingProjetId ? '✏️ Modifier un projet réalisé' : '➕ Ajouter un projet réalisé' }}</h2>
            <p>Partagez une étude de cas et ses résultats.</p>
          </div>
        </div>
        <form (ngSubmit)="saveProjet()">
          <div class="cf-field">
            <label for="projetTitre">Titre du projet</label>
            <input id="projetTitre" class="cf-input" [(ngModel)]="projet.titre" name="projetTitre" required />
          </div>
          <div class="cf-field">
            <label for="projetDescription">Contexte & Enjeux</label>
            <textarea id="projetDescription" class="cf-textarea" [(ngModel)]="projet.description" name="projetDescription" required></textarea>
          </div>
          <div class="cf-field">
            <label for="methode">Méthodologie adoptée</label>
            <textarea id="methode" class="cf-textarea" [(ngModel)]="projet.methode" name="methode" required></textarea>
          </div>
          <div class="cf-field">
            <label for="resultat">Résultats & Impact</label>
            <textarea id="resultat" class="cf-textarea" [(ngModel)]="projet.resultat" name="resultat" required></textarea>
          </div>
          <div class="cf-actions">
            <button type="submit" class="cf-btn cf-btn-primary">{{ editingProjetId ? 'Enregistrer les modifications' : 'Publier le projet' }}</button>
            <button type="button" class="cf-btn cf-btn-ghost" *ngIf="editingProjetId" (click)="resetProjet()">Annuler</button>
          </div>
        </form>
      </section>

      <section class="cf-panel">
        <div class="cf-panel-head">
          <div>
            <h2>Projets Réalisés & Études de Cas PFE</h2>
            <p>Découvrez les réalisations marquantes des précédentes promotions.</p>
          </div>
        </div>
        <div class="cf-project-grid">
          <article class="cf-project-card" *ngFor="let p of projets" style="background: #fff; border-radius: 12px; box-shadow: var(--cf-shadow);">
            <h3 style="color: var(--cf-primary);">{{ p.titre }}</h3>
            <div class="cf-detail-block">
              <span class="cf-detail-label">Contexte & Problématique</span>
              <p>{{ p.description }}</p>
            </div>
            <div class="cf-detail-block">
              <span class="cf-detail-label">Méthodologie Technique</span>
              <p>{{ p.methode }}</p>
            </div>
            <div class="cf-detail-block cf-detail-result">
              <span class="cf-detail-label">Résultats Obtenus</span>
              <p>{{ p.resultat }}</p>
            </div>
            <div class="cf-actions" *ngIf="adminMode" style="margin-top: 14px;">
              <button type="button" class="cf-btn cf-btn-ghost cf-btn-sm" (click)="editProjet(p)">Modifier</button>
              <button type="button" class="cf-btn cf-btn-danger cf-btn-sm" (click)="deleteProjet(p.id!)">Supprimer</button>
            </div>
          </article>
        </div>
        <p *ngIf="projets.length === 0" style="color: var(--cf-text-muted)">Aucun projet enregistré pour le moment.</p>
      </section>
    }

    <!-- TAB 5: CANDIDATURES -->
    @if (tab === 'candidatures') {
      <section class="cf-panel">
        <div class="cf-panel-head">
          <div>
            <h2>📝 Déposer une Candidature PFE</h2>
            <p>Sélectionnez un sujet actif et soumettez votre candidature.</p>
          </div>
        </div>
        <form (ngSubmit)="submitCandidaturePublic()">
          <div class="cf-field">
            <label for="sujetSelect">Choix du Sujet PFE</label>
            <select id="sujetSelect" class="cf-select" [(ngModel)]="candidatureSubmit.sujetPfeId" name="sujetPfeId" required>
              <option [ngValue]="0" disabled>-- Choisir un sujet ouvert --</option>
              <option *ngFor="let s of sujetsActifs" [ngValue]="s.id">{{ s.titre }} — ({{ s.technologie }})</option>
            </select>
          </div>
          <div class="cf-field-grid">
            <div class="cf-field">
              <label for="cNom">Nom</label>
              <input id="cNom" class="cf-input" [(ngModel)]="candidatureSubmit.nom" name="cNom" required placeholder="Votre nom" />
            </div>
            <div class="cf-field">
              <label for="cPrenom">Prénom</label>
              <input id="cPrenom" class="cf-input" [(ngModel)]="candidatureSubmit.prenom" name="cPrenom" required placeholder="Votre prénom" />
            </div>
            <div class="cf-field">
              <label for="cEmail">Email académique / personnel</label>
              <input id="cEmail" class="cf-input" type="email" [(ngModel)]="candidatureSubmit.email" name="cEmail" required placeholder="exemple@domaine.com" />
            </div>
          </div>
          <div class="cf-field">
            <label for="motivation">Lettre de motivation & compétences clés</label>
            <textarea id="motivation" class="cf-textarea" [(ngModel)]="candidatureSubmit.messageMotivation" name="messageMotivation" required placeholder="Décrivez votre parcours et vos motivations..."></textarea>
          </div>
          <button type="submit" class="cf-btn cf-btn-primary" style="padding: 12px 28px;">
            🚀 Envoyer ma candidature en ligne
          </button>
        </form>

        <section class="cf-alert cf-alert-success" *ngIf="lastSubmittedCandidature as submitted" style="margin-top: 18px;">
          <strong>✅ Candidature transmise avec succès !</strong>
          <span *ngIf="submitted.scorePourcentage !== null && submitted.scorePourcentage !== undefined; else scorePending">
            Score ML attribué : <strong>{{ submitted.scorePourcentage }}%</strong>
            <span *ngIf="submitted.decisionSuggeree"> — recommandation : {{ submitted.decisionSuggeree }}</span>
          </span>
          <ng-template #scorePending> Le score ML sera généré lors de la revue administrateur.</ng-template>
        </section>
      </section>

      <!-- Admin Table for Applications -->
      <section class="cf-panel" *ngIf="adminMode">
        <div class="cf-panel-head">
          <div>
            <h2>📋 Suivi des Candidatures (Mode Admin)</h2>
            <p>Revue des dossiers et validation des étudiants.</p>
          </div>
          <div class="cf-field" style="margin: 0; min-width: 220px">
            <label for="filterCandidat">Filtrer par candidat</label>
            <select id="filterCandidat" class="cf-select" [(ngModel)]="filterCandidatId" (ngModelChange)="loadCandidaturesFiltered()">
              <option [ngValue]="0">Tous les candidats</option>
              <option *ngFor="let c of candidats" [ngValue]="c.id">{{ c.prenom }} {{ c.nom }}</option>
            </select>
          </div>
        </div>
        <div class="cf-table-wrap">
          <table class="cf-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Sujet PFE</th>
                <th>Candidat</th>
                <th>Statut</th>
                <th>Score ML</th>
                <th>Décision ML</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let c of candidatures">
                <td>#{{ c.id }}</td>
                <td><strong>{{ sujetTitle(c.sujetPfeId, c.sujetPfe) }}</strong></td>
                <td>{{ candidatLabel(c.candidatId, c.candidat) }}</td>
                <td>
                  <span class="cf-badge" [ngClass]="statusClass(c.statut)">{{ c.statut || 'EN_ATTENTE' }}</span>
                </td>
                <td>
                  <span *ngIf="isFinalized(c) && c.scorePourcentage !== null && c.scorePourcentage !== undefined; else scorePending">
                    <strong>{{ c.scorePourcentage }}%</strong>
                  </span>
                  <ng-template #scorePending><span class="cf-badge cf-badge-muted">En évaluation</span></ng-template>
                </td>
                <td>
                  <span *ngIf="isFinalized(c) && c.decisionSuggeree; else decisionPending" class="cf-badge cf-badge-muted">
                    {{ c.decisionSuggeree }}
                  </span>
                  <ng-template #decisionPending>-</ng-template>
                </td>
                <td>
                  <div class="cf-actions">
                    <button type="button" class="cf-btn cf-btn-primary cf-btn-sm" (click)="acceptCandidature(c.id!)">Accepter</button>
                    <button type="button" class="cf-btn cf-btn-danger cf-btn-sm" (click)="refuseCandidature(c.id!)">Refuser</button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>
    }
  `
})
export class PfeComponent implements OnInit {
  tab: PfeTab = 'sujets';
  adminMode = false;

  sujets: SujetPfe[] = [];
  projets: ProjetRealise[] = [];
  candidatures: Candidature[] = [];
  candidats: CandidatSummary[] = [];
  lastSubmittedCandidature: Candidature | null = null;

  searchSujet = '';
  onlyActive = true;
  filterCandidatId = 0;

  errorMessage = '';
  successMessage = '';

  sujet: SujetPfe = this.emptySujet();
  projet: ProjetRealise = { titre: '', description: '', methode: '', resultat: '' };
  candidatureSubmit = {
    sujetPfeId: 0,
    nom: '',
    prenom: '',
    email: '',
    messageMotivation: ''
  };

  editingSujetId: number | null = null;
  editingProjetId: number | null = null;

  // AI Matching feature state
  matchingNom = '';
  matchingDomaine = '';
  availableSkills = ['Java', 'Spring Boot', 'Angular', 'React', 'Docker', 'Kubernetes', 'Python', 'Machine Learning', 'Cybersécurité', 'AWS', 'SQL'];
  selectedSkills: string[] = ['Java', 'Spring Boot', 'Angular'];
  matchingResult: PfeMatchingResponse | null = null;

  constructor(
    private pfeService: PfeService, 
    private authService: AuthService,
    private notifService: NotificationService
  ) {}

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => {
      if (user) {
        this.adminMode = user.role === 'ADMIN';
        if (user.prenom || user.nom) {
          this.candidatureSubmit.prenom = user.prenom || '';
          this.candidatureSubmit.nom = user.nom || '';
          this.candidatureSubmit.email = user.email || '';
          this.matchingNom = `${user.prenom || ''} ${user.nom || ''}`.trim();
        }
      }
    });

    this.loadSujets();
    this.loadProjets();
    this.loadCandidatures();
    this.loadCandidats();
  }

  get filteredSujets(): SujetPfe[] {
    const q = this.searchSujet.trim().toLowerCase();
    return this.sujets.filter(s => {
      if (this.onlyActive && !s.actif) return false;
      if (!q) return true;
      const haystack = `${s.titre} ${s.domaine} ${s.technologie} ${s.entreprise} ${s.description}`.toLowerCase();
      return haystack.includes(q);
    });
  }

  get sujetsActifs(): SujetPfe[] {
    return this.sujets.filter(s => s.actif);
  }

  loadSujets(): void {
    this.pfeService.getAllSujets().subscribe({
      next: data => (this.sujets = data),
      error: () => this.showError('Impossible de charger les sujets PFE.')
    });
  }

  loadProjets(): void {
    this.pfeService.getAllProjets().subscribe({
      next: data => (this.projets = data),
      error: () => this.showError('Impossible de charger les projets.')
    });
  }

  loadCandidats(): void {
    this.pfeService.getCandidats().subscribe({
      next: data => (this.candidats = data),
      error: () => this.showError('Impossible de charger la liste des candidats.')
    });
  }

  loadCandidatures(): void {
    this.loadCandidaturesFiltered();
  }

  loadCandidaturesFiltered(): void {
    if (this.filterCandidatId > 0) {
      this.pfeService.getCandidaturesByCandidat(this.filterCandidatId).subscribe({
        next: data => (this.candidatures = data),
        error: () => this.showError('Filtrage des candidatures impossible.')
      });
    } else {
      this.pfeService.getAllCandidatures().subscribe({
        next: data => (this.candidatures = data),
        error: () => this.showError('Impossible de charger les candidatures.')
      });
    }
  }

  toggleSkill(skill: string): void {
    const idx = this.selectedSkills.indexOf(skill);
    if (idx >= 0) {
      this.selectedSkills.splice(idx, 1);
    } else {
      this.selectedSkills.push(skill);
    }
  }

  runMatchingAnalysis(): void {
    this.clearMessages();
    this.pfeService.calculateMatching({
      candidatNom: this.matchingNom || 'Candidat',
      competences: this.selectedSkills,
      domainePrefere: this.matchingDomaine
    }).subscribe({
      next: res => {
        this.matchingResult = res;
        this.notifService.addNotification(
          '🎯 Score d\'adéquation IA',
          `Analyse terminée pour ${res.candidatNom}. Score maximal : ${res.meilleurScore}%.`,
          'success'
        );
      },
      error: () => {
        this.calculateMatchingLocal();
        this.notifService.addNotification(
          '🎯 Score d\'adéquation IA (Mode Hors-ligne)',
          `Analyse locale effectuée pour ${this.matchingNom || 'Candidat'}.`,
          'info'
        );
      }
    });
  }

  private calculateMatchingLocal(): void {
    const listSujets = this.sujets.length ? this.sujets : [
      { id: 1, titre: 'Conception d\'une architecture Microservices Cloud-Native & Spring Boot 3', domaine: 'Informatique', technologie: 'Spring Boot 3, Angular 18, Docker', entreprise: 'CodingFactory Labs', description: 'Développement d\'une plateforme scalable basée sur Spring Cloud et Kubernetes.', actif: true },
      { id: 2, titre: 'Audit de sécurité automatisé et détection de vulnérabilités OWASP', domaine: 'Cybersécurité', technologie: 'Pentest, Python, Docker, RGPD', entreprise: 'CodingFactory Security', description: 'Mise en place d\'un scanner de sécurité automatisé pour les API REST.', actif: true },
      { id: 3, titre: 'Moteur de recommandation basé sur l\'IA et le NLP', domaine: 'IA', technologie: 'Python, PyTorch, MLOps, FastApi', entreprise: 'CodingFactory Data', description: 'Entraînement de modèles LLM et intégration MLOps.', actif: true }
    ];

    const userSkills = this.selectedSkills.map(s => s.toLowerCase());
    const prefDomaine = (this.matchingDomaine || '').toLowerCase();

    const results: PfeMatchResult[] = listSujets.map(s => {
      let score = 45;
      const matchees: string[] = [];
      const sujetTech = (s.technologie || '').toLowerCase();
      const sujetDomaine = (s.domaine || '').toLowerCase();
      const text = `${s.titre} ${s.description}`.toLowerCase();

      for (const skill of userSkills) {
        if (sujetTech.includes(skill) || text.includes(skill)) {
          score += 15;
          matchees.push(skill.toUpperCase());
        }
      }

      if (prefDomaine && (sujetDomaine.includes(prefDomaine) || prefDomaine.includes(sujetDomaine))) {
        score += 20;
      }

      score = Math.min(score, 98);
      let statutMatch: 'EXCELLENT' | 'BON' | 'MOYEN' = 'MOYEN';
      let recommandation = 'Compatibilité modérée. Formation complémentaire conseillée.';

      if (score >= 80) {
        statutMatch = 'EXCELLENT';
        recommandation = 'Forte recommandation ! Votre profil correspond parfaitement à ce sujet PFE.';
      } else if (score >= 60) {
        statutMatch = 'BON';
        recommandation = 'Bonne adéquation technique. Candidature vivement conseillée.';
      }

      return {
        sujetId: s.id || 0,
        titre: s.titre,
        domaine: s.domaine,
        technologie: s.technologie,
        entreprise: s.entreprise,
        scoreMatch: score,
        statutMatch,
        competencesMatchees: matchees,
        competencesManquantes: [],
        recommandation
      };
    });

    results.sort((a, b) => b.scoreMatch - a.scoreMatch);

    this.matchingResult = {
      candidatNom: this.matchingNom || 'Candidat',
      meilleurScore: results.length ? results[0].scoreMatch : 0,
      totalSujetsAnalyses: results.length,
      resultats: results
    };
  }

  applyWithMatching(match: PfeMatchResult): void {
    this.tab = 'candidatures';
    this.candidatureSubmit.sujetPfeId = match.sujetId;
    if (this.matchingNom) {
      const parts = this.matchingNom.trim().split(' ');
      this.candidatureSubmit.prenom = parts[0] || '';
      this.candidatureSubmit.nom = parts.slice(1).join(' ') || parts[0] || '';
    }
    this.candidatureSubmit.messageMotivation = `Candidature soumise suite à une analyse de compatibilité IA avec un score d'adéquation de ${match.scoreMatch}%. Competences clés : ${this.selectedSkills.join(', ')}.`;
  }

  exportMatchingPDF(): void {
    if (!this.matchingResult) return;

    const printWin = window.open('', '_blank');
    if (!printWin) return;

    const dateStr = new Date().toLocaleDateString('fr-FR');
    const itemsHtml = this.matchingResult.resultats.map(r => `
      <div style="border:1px solid #d0dbe8; border-radius:10px; padding:16px; margin-bottom:12px; page-break-inside:avoid;">
        <div style="display:flex; justify-content:space-between; align-items:center;">
          <div>
            <span style="background:#eef6ff; color:#2f80ed; font-size:12px; font-weight:bold; padding:3px 8px; border-radius:4px;">${r.domaine}</span>
            <h3 style="margin:6px 0 2px; color:#0b2d4a;">${r.titre}</h3>
            <div style="font-size:13px; color:#5a6e85;">Entreprise : ${r.entreprise}</div>
          </div>
          <div style="font-size:24px; font-weight:bold; color:${r.scoreMatch >= 80 ? '#27ae60' : r.scoreMatch >= 60 ? '#2f80ed' : '#f2994a'};">
            ${r.scoreMatch}%
          </div>
        </div>
        <p style="background:#f8fafc; padding:10px; border-radius:6px; font-size:13px; margin:10px 0;">💡 ${r.recommandation}</p>
        <div style="font-size:12px; font-weight:bold; color:#27ae60;">Compétences Validées : ${r.competencesMatchees.join(', ')}</div>
      </div>
    `).join('');

    printWin.document.write(`
      <!DOCTYPE html>
      <html>
      <head>
        <title>Rapport d'Adéquation IA - ${this.matchingResult.candidatNom}</title>
        <style>
          body { font-family: 'Helvetica Neue', Arial, sans-serif; padding: 30px; color: #1e3a5f; }
          .header { border-bottom: 2px solid #2f80ed; padding-bottom: 15px; margin-bottom: 20px; display: flex; justify-content: space-between; }
          .badge { background: #27ae60; color: #fff; padding: 4px 12px; border-radius: 12px; font-weight: bold; }
        </style>
      </head>
      <body>
        <div class="header">
          <div>
            <h1 style="margin:0; color:#0b2d4a;">🚀 CODING FACTORY</h1>
            <div style="color:#63778e; font-size:14px;">Rapport d'Adéquation IA & Recommandations PFE</div>
          </div>
          <div style="text-align:right;">
            <div style="font-size:13px; color:#63778e;">Date : ${dateStr}</div>
            <div style="margin-top:6px;"><span class="badge">Score Max : ${this.matchingResult.meilleurScore}%</span></div>
          </div>
        </div>
        <h2>Candidat : ${this.matchingResult.candidatNom}</h2>
        <p><strong>Compétences Sélectionnées :</strong> ${this.selectedSkills.join(', ')}</p>
        <hr style="border:none; border-top:1px solid #e1e8f0; margin:20px 0;" />
        <h3>Sujets PFE Recommandés (${this.matchingResult.totalSujetsAnalyses} analysés) :</h3>
        ${itemsHtml}
        <script>window.onload = function() { window.print(); };</script>
      </body>
      </html>
    `);
    printWin.document.close();
  }

  saveSujet(): void {
    this.clearMessages();
    const action = this.editingSujetId !== null
      ? this.pfeService.updateSujet(this.editingSujetId, this.sujet)
      : this.pfeService.createSujet(this.sujet);

    action.subscribe({
      next: () => {
        this.showSuccess(this.editingSujetId ? 'Sujet modifié avec succès.' : 'Sujet ajouté avec succès.');
        this.resetSujet();
        this.loadSujets();
      },
      error: () => this.showError('Enregistrement du sujet échoué.')
    });
  }

  editSujet(sujet: SujetPfe): void {
    this.sujet = { ...sujet };
    this.editingSujetId = sujet.id ?? null;
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  deleteSujet(id: number): void {
    if (!confirm('Supprimer ce sujet PFE ?')) return;
    this.pfeService.deleteSujet(id).subscribe({
      next: () => {
        this.showSuccess('Sujet supprimé avec succès.');
        this.loadSujets();
      },
      error: () => this.showError('Suppression impossible.')
    });
  }

  resetSujet(): void {
    this.sujet = this.emptySujet();
    this.editingSujetId = null;
  }

  saveProjet(): void {
    this.clearMessages();
    const updating = this.editingProjetId !== null;
    const action = this.editingProjetId !== null
      ? this.pfeService.updateProjet(this.editingProjetId, this.projet)
      : this.pfeService.createProjet(this.projet);
    action.subscribe({
      next: () => {
        this.resetProjet();
        this.showSuccess(updating ? 'Projet modifié.' : 'Projet ajouté.');
        this.loadProjets();
      },
      error: () => this.showError('Ajout du projet échoué.')
    });
  }

  editProjet(projet: ProjetRealise): void {
    this.projet = { ...projet };
    this.editingProjetId = projet.id ?? null;
    this.tab = 'projets';
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  deleteProjet(id: number): void {
    if (!confirm('Supprimer ce projet réalisé ?')) return;
    this.pfeService.deleteProjet(id).subscribe({
      next: () => {
        this.showSuccess('Projet supprimé.');
        this.loadProjets();
      },
      error: () => this.showError('Suppression du projet impossible.')
    });
  }

  resetProjet(): void {
    this.projet = { titre: '', description: '', methode: '', resultat: '' };
    this.editingProjetId = null;
  }

  startCandidature(sujet: SujetPfe): void {
    this.tab = 'candidatures';
    this.candidatureSubmit.sujetPfeId = sujet.id ?? 0;
  }

  submitCandidaturePublic(): void {
    this.clearMessages();
    if (this.candidatureSubmit.sujetPfeId <= 0) {
      this.showError('Veuillez sélectionner un sujet PFE actif.');
      return;
    }
    this.pfeService.submitCandidature(this.candidatureSubmit).subscribe({
      next: submitted => {
        this.lastSubmittedCandidature = submitted;
        this.candidatureSubmit = {
          sujetPfeId: 0,
          nom: '',
          prenom: '',
          email: '',
          messageMotivation: ''
        };
        this.showSuccess("Candidature enregistrée avec succès !");
        this.loadCandidatures();
        this.loadCandidats();
      },
      error: err => {
        const msg = err?.error?.message;
        this.showError(msg || 'Envoi de candidature impossible. Vérifiez que le backend (port 8081) est démarré.');
      }
    });
  }

  acceptCandidature(id: number): void {
    this.pfeService.updateCandidatureStatus(id, 'ACCEPTE').subscribe({
      next: () => this.loadCandidatures(),
      error: () => this.showError('Mise à jour du statut impossible.')
    });
  }

  refuseCandidature(id: number): void {
    this.pfeService.updateCandidatureStatus(id, 'REFUSE').subscribe({
      next: () => this.loadCandidatures(),
      error: () => this.showError('Mise à jour du statut impossible.')
    });
  }

  sujetTitle(id?: number, embedded?: Candidature['sujetPfe']): string {
    const resolvedId = id ?? embedded?.id;
    const s = this.sujets.find(x => x.id === resolvedId);
    return s?.titre ?? embedded?.titre ?? `#${resolvedId ?? ''}`;
  }

  candidatLabel(id?: number, embedded?: Candidature['candidat']): string {
    const resolvedId = id ?? embedded?.id;
    const c = this.candidats.find(x => x.id === resolvedId);
    return c ? `${c.prenom} ${c.nom}` : embedded ? `${embedded.prenom} ${embedded.nom}` : `#${resolvedId ?? ''}`;
  }

  statusClass(statut?: string): string {
    switch ((statut || 'EN_ATTENTE').toUpperCase()) {
      case 'ACCEPTE':
      case 'ACCEPTEE':
        return 'cf-badge-success';
      case 'REFUSE':
      case 'REFUSEE':
        return 'cf-badge-danger';
      default:
        return 'cf-badge-warning';
    }
  }

  isFinalized(candidature: Candidature): boolean {
    const status = (candidature.statut || 'EN_ATTENTE').toUpperCase();
    return status === 'ACCEPTEE' || status === 'REFUSEE' || status === 'ACCEPTE' || status === 'REFUSE';
  }

  private emptySujet(): SujetPfe {
    return {
      titre: '',
      description: '',
      domaine: '',
      technologie: '',
      entreprise: '',
      actif: true
    };
  }

  private showError(msg: string): void {
    this.errorMessage = msg;
    this.successMessage = '';
  }

  private showSuccess(msg: string): void {
    this.successMessage = msg;
    this.errorMessage = '';
  }

  private clearMessages(): void {
    this.errorMessage = '';
    this.successMessage = '';
  }
}
