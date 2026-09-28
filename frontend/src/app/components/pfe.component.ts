import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CandidatSummary, PfeService } from '../services/pfe.service';
import { SujetPfe } from '../models/sujet-pfe.model';
import { ProjetRealise } from '../models/projet-realise.model';
import { Candidature } from '../models/candidature.model';

type PfeTab = 'sujets' | 'projets' | 'candidatures';

@Component({
  selector: 'app-pfe',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <section class="cf-hero cf-hero-compact">
      <div class="cf-hero-row">
        <div>
          <h1>Projets de fin d'études</h1>
          <p>Sujets proposés par CodingFactory, retours d'expérience sur les projets livrés et candidatures en ligne.</p>
        </div>
        <label class="cf-admin-toggle">
          <input type="checkbox" [(ngModel)]="adminMode" />
          Mode administration
        </label>
      </div>
    </section>

    <div class="cf-tabs">
      <button type="button" class="cf-tab" [class.active]="tab === 'sujets'" (click)="tab = 'sujets'">Sujets PFE</button>
      <button type="button" class="cf-tab" [class.active]="tab === 'projets'" (click)="tab = 'projets'">Projets réalisés</button>
      <button type="button" class="cf-tab" [class.active]="tab === 'candidatures'" (click)="tab = 'candidatures'">Candidatures</button>
    </div>

    <div class="cf-alert cf-alert-error" *ngIf="errorMessage">{{ errorMessage }}</div>
    <div class="cf-alert cf-alert-success" *ngIf="successMessage">{{ successMessage }}</div>

    @if (tab === 'sujets') {
      <section class="cf-panel" *ngIf="adminMode">
        <div class="cf-panel-head">
          <div>
            <h2>{{ editingSujetId ? 'Modifier un sujet' : 'Ajouter un sujet PFE' }}</h2>
            <p>CRUD complet avec statut actif/inactif.</p>
          </div>
        </div>
        <form (ngSubmit)="saveSujet()">
          <div class="cf-field-grid">
            <div class="cf-field">
              <label for="titre">Titre</label>
              <input id="titre" class="cf-input" [(ngModel)]="sujet.titre" name="titre" required />
            </div>
            <div class="cf-field">
              <label for="domaine">Domaine</label>
              <input id="domaine" class="cf-input" [(ngModel)]="sujet.domaine" name="domaine" required />
            </div>
            <div class="cf-field">
              <label for="technologie">Technologie</label>
              <input id="technologie" class="cf-input" [(ngModel)]="sujet.technologie" name="technologie" required />
            </div>
            <div class="cf-field">
              <label for="entreprise">Entreprise</label>
              <input id="entreprise" class="cf-input" [(ngModel)]="sujet.entreprise" name="entreprise" required />
            </div>
          </div>
          <div class="cf-field">
            <label for="description">Description</label>
            <textarea id="description" class="cf-textarea" [(ngModel)]="sujet.description" name="description" required></textarea>
          </div>
          <label class="cf-field">
            <input type="checkbox" [(ngModel)]="sujet.actif" name="actif" />
            Sujet actif (visible pour candidature)
          </label>
          <div class="cf-actions" style="margin-top: 12px">
            <button type="submit" class="cf-btn cf-btn-primary">{{ editingSujetId ? 'Enregistrer' : 'Ajouter le sujet' }}</button>
            <button type="button" class="cf-btn cf-btn-ghost" *ngIf="editingSujetId" (click)="resetSujet()">Annuler</button>
          </div>
        </form>
      </section>

      <section class="cf-panel">
        <div class="cf-panel-head">
          <div>
            <h2>Sujets proposés par la société</h2>
            <p>{{ filteredSujets.length }} sujet(s) — filtrez par mot-clé ou consultez uniquement les offres actives.</p>
          </div>
          <input class="cf-input cf-search" [(ngModel)]="searchSujet" placeholder="Rechercher un sujet…" />
        </div>
        <label class="cf-field cf-field-inline">
          <input type="checkbox" [(ngModel)]="onlyActive" />
          Uniquement les sujets ouverts aux candidatures
        </label>
        <div class="cf-sujet-grid">
          <article class="cf-sujet-card" *ngFor="let item of filteredSujets">
            <div class="cf-tags">
              <span class="cf-tag">{{ item.domaine }}</span>
              <span class="cf-tag">{{ item.technologie }}</span>
              <span class="cf-badge" [class.cf-badge-success]="item.actif" [class.cf-badge-muted]="!item.actif">
                {{ item.actif ? 'Actif' : 'Inactif' }}
              </span>
            </div>
            <h3>{{ item.titre }}</h3>
            <p style="color: var(--cf-text-muted); font-size: 0.9rem; line-height: 1.5">{{ item.description }}</p>
            <p><strong>Entreprise :</strong> {{ item.entreprise }}</p>
            <div class="cf-actions">
              <button type="button" class="cf-btn cf-btn-accent cf-btn-sm" (click)="startCandidature(item)" [disabled]="!item.actif">Postuler</button>
              <button type="button" class="cf-btn cf-btn-ghost cf-btn-sm" *ngIf="adminMode" (click)="editSujet(item)">Modifier</button>
              <button type="button" class="cf-btn cf-btn-danger cf-btn-sm" *ngIf="adminMode" (click)="deleteSujet(item.id!)">Supprimer</button>
            </div>
          </article>
        </div>
        <p *ngIf="filteredSujets.length === 0" style="color: var(--cf-text-muted)">Aucun sujet ne correspond à votre recherche.</p>
      </section>
    }

    @if (tab === 'projets') {
      <section class="cf-panel" *ngIf="adminMode">
        <div class="cf-panel-head">
          <div>
            <h2>Ajouter un projet réalisé</h2>
            <p>Archive des livrables PFE (méthode, résultats).</p>
          </div>
        </div>
        <form (ngSubmit)="saveProjet()">
          <div class="cf-field">
            <label for="projetTitre">Titre</label>
            <input id="projetTitre" class="cf-input" [(ngModel)]="projet.titre" name="projetTitre" required />
          </div>
          <div class="cf-field">
            <label for="projetDescription">Description</label>
            <textarea id="projetDescription" class="cf-textarea" [(ngModel)]="projet.description" name="projetDescription" required></textarea>
          </div>
          <div class="cf-field">
            <label for="methode">Méthodologie</label>
            <textarea id="methode" class="cf-textarea" [(ngModel)]="projet.methode" name="methode" required></textarea>
          </div>
          <div class="cf-field">
            <label for="resultat">Résultats</label>
            <textarea id="resultat" class="cf-textarea" [(ngModel)]="projet.resultat" name="resultat" required></textarea>
          </div>
          <button type="submit" class="cf-btn cf-btn-primary">Publier le projet</button>
        </form>
      </section>

      <section class="cf-panel">
        <div class="cf-panel-head">
          <div>
            <h2>Études de cas & projets livrés</h2>
            <p>Retours d'expérience : contexte, méthodologie adoptée et résultats obtenus.</p>
          </div>
        </div>
        <div class="cf-project-grid">
          <article class="cf-project-card" *ngFor="let p of projets">
            <h3>{{ p.titre }}</h3>
            <div class="cf-detail-block">
              <span class="cf-detail-label">Contexte</span>
              <p>{{ p.description }}</p>
            </div>
            <div class="cf-detail-block">
              <span class="cf-detail-label">Méthodologie</span>
              <p>{{ p.methode }}</p>
            </div>
            <div class="cf-detail-block cf-detail-result">
              <span class="cf-detail-label">Résultats</span>
              <p>{{ p.resultat }}</p>
            </div>
          </article>
        </div>
        <p *ngIf="projets.length === 0" style="color: var(--cf-text-muted)">Aucun projet enregistré pour le moment.</p>
      </section>
    }

    @if (tab === 'candidatures') {
      <section class="cf-panel">
        <div class="cf-panel-head">
          <div>
            <h2>Soumettre une candidature</h2>
            <p>Choisissez un sujet actif et déposez votre dossier (création automatique du profil candidat).</p>
          </div>
        </div>
        <form (ngSubmit)="submitCandidaturePublic()">
          <div class="cf-field">
            <label for="sujetSelect">Sujet PFE visé</label>
            <select id="sujetSelect" class="cf-select" [(ngModel)]="candidatureSubmit.sujetPfeId" name="sujetPfeId" required>
              <option [ngValue]="0" disabled>Choisir un sujet</option>
              <option *ngFor="let s of sujetsActifs" [ngValue]="s.id">{{ s.titre }} — {{ s.entreprise }}</option>
            </select>
          </div>
          <div class="cf-field-grid">
            <div class="cf-field">
              <label for="cNom">Nom</label>
              <input id="cNom" class="cf-input" [(ngModel)]="candidatureSubmit.nom" name="cNom" required />
            </div>
            <div class="cf-field">
              <label for="cPrenom">Prénom</label>
              <input id="cPrenom" class="cf-input" [(ngModel)]="candidatureSubmit.prenom" name="cPrenom" required />
            </div>
            <div class="cf-field">
              <label for="cEmail">Email</label>
              <input id="cEmail" class="cf-input" type="email" [(ngModel)]="candidatureSubmit.email" name="cEmail" required />
            </div>
          </div>
          <div class="cf-field">
            <label for="motivation">Lettre de motivation</label>
            <textarea id="motivation" class="cf-textarea" [(ngModel)]="candidatureSubmit.messageMotivation" name="messageMotivation" required></textarea>
          </div>
          <button type="submit" class="cf-btn cf-btn-primary">Envoyer ma candidature</button>
        </form>
      </section>

      <section class="cf-panel" *ngIf="adminMode">
        <div class="cf-panel-head">
          <div>
            <h2>Suivi des candidatures</h2>
            <p>Validation administrateur : accepter ou refuser.</p>
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
                <th>Sujet</th>
                <th>Candidat</th>
                <th>Statut</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let c of candidatures">
                <td>{{ c.id }}</td>
                <td>{{ sujetTitle(c.sujetPfeId) }}</td>
                <td>{{ candidatLabel(c.candidatId) }}</td>
                <td>
                  <span class="cf-badge" [ngClass]="statusClass(c.statut)">{{ c.statut || 'EN_ATTENTE' }}</span>
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
        <p *ngIf="candidatures.length === 0" style="color: var(--cf-text-muted); margin-top: 12px">Aucune candidature.</p>
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

  searchSujet = '';
  onlyActive = true;
  filterCandidatId = 0;

  errorMessage = '';
  successMessage = '';

  sujet: SujetPfe = this.emptySujet();
  projet: ProjetRealise = { titre: '', description: '', methode: '', resultat: '' };
  candidature: Candidature = { sujetPfeId: 0, candidatId: 0, messageMotivation: '' };
  candidatureSubmit = {
    sujetPfeId: 0,
    nom: '',
    prenom: '',
    email: '',
    messageMotivation: ''
  };

  editingSujetId: number | null = null;

  constructor(private pfeService: PfeService) {}

  ngOnInit(): void {
    this.loadSujets();
    this.loadProjets();
    this.loadCandidatures();
    this.loadCandidats();
  }

  get filteredSujets(): SujetPfe[] {
    const q = this.searchSujet.trim().toLowerCase();
    return this.sujets.filter(s => {
      if (this.onlyActive && !s.actif) {
        return false;
      }
      if (!q) {
        return true;
      }
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

  saveSujet(): void {
    this.clearMessages();
    const action =
      this.editingSujetId !== null
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
    if (!confirm('Supprimer ce sujet ?')) {
      return;
    }
    this.pfeService.deleteSujet(id).subscribe({
      next: () => {
        this.showSuccess('Sujet supprimé.');
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
    this.pfeService.createProjet(this.projet).subscribe({
      next: () => {
        this.projet = { titre: '', description: '', methode: '', resultat: '' };
        this.showSuccess('Projet ajouté.');
        this.loadProjets();
      },
      error: () => this.showError('Ajout du projet échoué.')
    });
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
      next: () => {
        this.candidatureSubmit = {
          sujetPfeId: 0,
          nom: '',
          prenom: '',
          email: '',
          messageMotivation: ''
        };
        this.showSuccess("Candidature enregistrée — statut EN_ATTENTE. L'équipe CodingFactory vous recontactera.");
        this.loadCandidatures();
        this.loadCandidats();
      },
      error: err => {
        const msg = err?.error?.message;
        this.showError(msg || 'Envoi impossible. Vérifiez MySQL et le backend (8081).');
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

  sujetTitle(id: number): string {
    const s = this.sujets.find(x => x.id === id);
    return s ? s.titre : `#${id}`;
  }

  candidatLabel(id: number): string {
    const c = this.candidats.find(x => x.id === id);
    return c ? `${c.prenom} ${c.nom}` : `#${id}`;
  }

  statusClass(statut?: string): string {
    switch ((statut || 'EN_ATTENTE').toUpperCase()) {
      case 'ACCEPTE':
        return 'cf-badge-success';
      case 'REFUSE':
        return 'cf-badge-danger';
      default:
        return 'cf-badge-warning';
    }
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
