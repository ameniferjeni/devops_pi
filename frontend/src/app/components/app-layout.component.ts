import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <div class="cf-shell">
      <header class="cf-header">
        <a routerLink="/" class="cf-brand">
          <span class="cf-brand-mark">CF</span>
          <span class="cf-brand-text">
            <strong>CodingFactory</strong>
            <small>PFE & Consulting</small>
          </span>
        </a>
        <nav class="cf-nav">
          <a routerLink="/" routerLinkActive="active" [routerLinkActiveOptions]="{ exact: true }">Accueil</a>
          <a routerLink="/pfe" routerLinkActive="active">Module PFE</a>
          <a routerLink="/chatbot" routerLinkActive="active">Chatbot</a>
        </nav>
      </header>

      <main class="cf-main">
        <router-outlet />
      </main>

      <footer class="cf-footer">
        Projet intégré CodingFactory — Spring Boot · Angular · MySQL
      </footer>
    </div>
  `
})
export class AppLayoutComponent {}
