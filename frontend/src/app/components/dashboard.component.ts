import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

import { PfeService, PfeStats } from '../services/pfe.service';

import { ChatbotService } from '../services/chatbot.service';



@Component({

  selector: 'app-dashboard',

  standalone: true,

  imports: [CommonModule, RouterLink],

  template: `

    <section class="cf-hero">

      <h1>Plateforme CodingFactory</h1>

      <p>

        Consulting IT et projets de fin d'études : une seule plateforme pour orienter les clients,

        publier les sujets PFE et suivre les candidatures.

      </p>

    </section>



    <div class="cf-stat-row" *ngIf="stats">

      <div class="cf-stat">

        <span class="cf-stat-value">{{ stats.sujetsActifs }}</span>

        <span class="cf-stat-label">Sujets PFE actifs</span>

      </div>

      <div class="cf-stat">

        <span class="cf-stat-value">{{ stats.projetsRealises }}</span>

        <span class="cf-stat-label">Projets réalisés</span>

      </div>

      <div class="cf-stat">

        <span class="cf-stat-value">{{ stats.candidaturesEnAttente }}</span>

        <span class="cf-stat-label">Candidatures en attente</span>

      </div>

      <div class="cf-stat">

        <span class="cf-stat-value">{{ servicesCount }}</span>

        <span class="cf-stat-label">Services consulting</span>

      </div>

    </div>



    <div class="cf-grid-3">

      <article class="cf-card cf-card-feature">

        <span class="cf-card-icon" aria-hidden="true">🎓</span>

        <h3>Module PFE</h3>

        <p>Gestion du cycle de vie des projets de fin d'études proposés par CodingFactory.</p>

        <ul>

          <li>Liste des sujets (domaine, technologie, entreprise)</li>

          <li>Études de cas : méthodologie et résultats</li>

          <li>Candidature en ligne avec suivi du statut</li>

          <li>Mode administration (CRUD, validation)</li>

        </ul>

        <a routerLink="/pfe" class="cf-btn cf-btn-primary">Explorer les sujets PFE</a>

      </article>



      <article class="cf-card cf-card-feature">

        <span class="cf-card-icon" aria-hidden="true">💬</span>

        <h3>Chatbot Consulting</h3>

        <p>Assistant personnalisé pour les questions générales et l'orientation vers les experts.</p>

        <ul>

          <li>Salutations et FAQ (contact, présentation)</li>

          <li>Détection d'intention (dev, cyber, formation, conseil, PFE)</li>

          <li>Liaison services & consultants MySQL</li>

          <li>Conversation avec suggestions rapides</li>

        </ul>

        <a routerLink="/chatbot" class="cf-btn cf-btn-accent">Parler à CodingBot</a>

      </article>



      <article class="cf-card cf-card-feature">

        <span class="cf-card-icon" aria-hidden="true">⚙️</span>

        <h3>Stack technique</h3>

        <p>Architecture prête pour un déploiement professionnel et l'intégration DevOps.</p>

        <ul>

          <li>API REST Spring Boot 3 — port 8081</li>

          <li>Angular 18 — port 4200</li>

          <li>MySQL + JPA/Hibernate</li>

        </ul>

        <span class="cf-badge cf-badge-muted">GitHub Actions · Jenkins · Docker Compose</span>

      </article>

    </div>

  `

})

export class DashboardComponent implements OnInit {

  stats?: PfeStats;

  servicesCount = 0;



  constructor(

    private pfeService: PfeService,

    private chatbotService: ChatbotService

  ) {}



  ngOnInit(): void {

    this.pfeService.getStats().subscribe({

      next: data => (this.stats = data),

      error: () => {}

    });

    this.chatbotService.getServices().subscribe({

      next: data => (this.servicesCount = data.length),

      error: () => {}

    });

  }

}


