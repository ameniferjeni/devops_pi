import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive, RouterOutlet, Router } from '@angular/router';
import { AuthService, UserAuth } from '../services/auth.service';
import { NotificationService, AppNotification } from '../services/notification.service';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <div class="cf-shell">
      
      <!-- Real-time Floating Toast Banner Overlay -->
      <div *ngIf="activeToast" class="toast-overlay" [class]="'toast-' + activeToast.type">
        <div class="toast-header">
          <span class="toast-title">{{ activeToast.titre }}</span>
          <button (click)="activeToast = null" class="toast-close">×</button>
        </div>
        <div class="toast-message">{{ activeToast.message }}</div>
      </div>

      <header class="cf-header">
        <a routerLink="/" class="cf-brand">
          <span class="cf-brand-mark">CF</span>
          <span class="cf-brand-text">
            <strong>CodingFactory</strong>
            <small>PFE & Consulting</small>
          </span>
        </a>

        <nav class="cf-nav" style="align-items: center;">
          <a routerLink="/" routerLinkActive="active" [routerLinkActiveOptions]="{ exact: true }">Accueil</a>
          <a routerLink="/pfe" routerLinkActive="active">Module PFE</a>
          <a routerLink="/chatbot" routerLinkActive="active">Chatbot</a>

          <!-- REAL-TIME NOTIFICATION CENTER BELL -->
          <div class="notif-dropdown-wrapper">
            <button type="button" class="notif-bell-btn" (click)="toggleNotifDropdown()">
              🔔
              <span class="notif-badge" *ngIf="unreadCount > 0">{{ unreadCount }}</span>
            </button>

            <!-- Dropdown Panel -->
            <div class="notif-dropdown" *ngIf="notifOpen">
              <div class="notif-header">
                <strong>Notifications Temps Réel</strong>
                <button (click)="markAllAsRead()" class="btn-read-all">Tout lire</button>
              </div>
              <div class="notif-list">
                <div *ngFor="let n of notifications" class="notif-item" [class.unread]="!n.lu">
                  <div class="notif-item-title">{{ n.titre }}</div>
                  <div class="notif-item-msg">{{ n.message }}</div>
                  <div class="notif-item-time">{{ n.timestamp | date:'HH:mm:ss' }}</div>
                </div>
                <div *ngIf="!notifications.length" class="notif-empty">Aucune notification</div>
              </div>
            </div>
          </div>

          <!-- Logged in state -->
          <div *ngIf="user; else loginBtn" style="display: flex; align-items: center; gap: 10px; margin-left: 12px; background: rgba(255,255,255,0.12); padding: 4px 12px; border-radius: 999px;">
            <span style="font-size: 0.85rem; font-weight: 600;">
              👤 {{ user.prenom || user.email }}
            </span>
            <span 
              class="cf-badge" 
              [class.cf-badge-success]="user.role === 'ADMIN'"
              [class.cf-badge-warning]="user.role === 'CANDIDAT'"
              style="font-size: 0.7rem; padding: 2px 8px;"
            >
              {{ user.role }}
            </span>
            <button 
              type="button" 
              (click)="logout()" 
              style="background: transparent; border: none; color: rgba(255,255,255,0.8); cursor: pointer; font-size: 0.8rem; font-weight: 600; padding: 2px 6px;"
              title="Se déconnecter"
            >
              🚪 Quitter
            </button>
          </div>

          <!-- Unauthenticated state -->
          <ng-template #loginBtn>
            <a routerLink="/login" routerLinkActive="active" style="background: var(--cf-accent); color: #fff; border-radius: 999px; padding: 6px 16px; margin-left: 8px;">
              🔑 Connexion
            </a>
          </ng-template>
        </nav>
      </header>

      <main class="cf-main">
        <router-outlet />
      </main>

      <footer class="cf-footer">
        Projet intégré CodingFactory — Spring Boot · Angular · JWT Security · MySQL
      </footer>
    </div>
  `,
  styles: [`
    .toast-overlay {
      position: fixed;
      top: 20px;
      right: 20px;
      z-index: 9999;
      background: #ffffff;
      border-radius: 12px;
      padding: 14px 18px;
      box-shadow: 0 10px 30px rgba(0,0,0,0.25);
      min-width: 280px;
      max-width: 360px;
      animation: slideIn 0.3s ease-out;
      border-left: 5px solid #2f80ed;
    }
    .toast-info { border-left-color: #2f80ed; }
    .toast-success { border-left-color: #27ae60; }
    .toast-warning { border-left-color: #f2994a; }
    .toast-error { border-left-color: #eb5757; }
    
    .toast-header { display: flex; justify-content: space-between; align-items: center; }
    .toast-title { font-weight: 700; font-size: 0.9rem; color: #0b2d4a; }
    .toast-close { background: none; border: none; font-size: 1.2rem; cursor: pointer; color: #828282; }
    .toast-message { font-size: 0.82rem; color: #4f4f4f; margin-top: 4px; }

    .notif-dropdown-wrapper { position: relative; margin-left: 8px; }
    .notif-bell-btn {
      background: rgba(255,255,255,0.12);
      border: none;
      color: #fff;
      padding: 6px 10px;
      border-radius: 50%;
      cursor: pointer;
      position: relative;
      font-size: 1.1rem;
    }
    .notif-badge {
      position: absolute;
      top: -4px;
      right: -4px;
      background: #eb5757;
      color: #fff;
      font-size: 0.65rem;
      font-weight: 800;
      padding: 2px 5px;
      border-radius: 10px;
    }
    .notif-dropdown {
      position: absolute;
      top: 40px;
      right: 0;
      width: 310px;
      background: #ffffff;
      border-radius: 14px;
      box-shadow: 0 15px 35px rgba(0,0,0,0.2);
      z-index: 1000;
      overflow: hidden;
      color: #333;
    }
    .notif-header {
      background: #0b2d4a;
      color: #fff;
      padding: 10px 14px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 0.82rem;
    }
    .btn-read-all { background: none; border: none; color: #70b5ff; font-size: 0.75rem; cursor: pointer; text-decoration: underline; }
    .notif-list { max-height: 260px; overflow-y: auto; }
    .notif-item { padding: 10px 14px; border-bottom: 1px solid #f0f0f0; }
    .notif-item.unread { background: #eef6ff; }
    .notif-item-title { font-weight: 700; font-size: 0.82rem; color: #0b2d4a; }
    .notif-item-msg { font-size: 0.78rem; color: #5a6e85; margin-top: 2px; }
    .notif-item-time { font-size: 0.68rem; color: #999; margin-top: 4px; text-align: right; }
    .notif-empty { padding: 20px; text-align: center; color: #888; font-size: 0.8rem; }

    @keyframes slideIn {
      from { transform: translateX(100px); opacity: 0; }
      to { transform: translateX(0); opacity: 1; }
    }
  `]
})
export class AppLayoutComponent implements OnInit {
  user: UserAuth | null = null;
  notifications: AppNotification[] = [];
  unreadCount = 0;
  notifOpen = false;
  activeToast: AppNotification | null = null;

  constructor(
    private authService: AuthService, 
    private notifService: NotificationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(u => {
      this.user = u;
    });

    this.notifService.notifications$.subscribe(list => {
      this.notifications = list;
      this.unreadCount = this.notifService.getUnreadCount();
    });

    this.notifService.toast$.subscribe(t => {
      this.activeToast = t;
    });
  }

  toggleNotifDropdown(): void {
    this.notifOpen = !this.notifOpen;
  }

  markAllAsRead(): void {
    this.notifService.markAllAsRead();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}

