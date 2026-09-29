import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="auth-wrapper">
      
      <!-- Ambient Glow & Background Elements -->
      <div class="ambient-glow glow-1"></div>
      <div class="ambient-glow glow-2"></div>

      <!-- Top Branding Badge -->
      <div class="brand-badge">
        <span class="brand-icon">🚀</span>
        <span class="brand-title">CODING FACTORY</span>
        <span class="brand-chip">DevOps & AI Platform</span>
      </div>

      <!-- Glassmorphism Main Auth Box -->
      <div class="auth-card">
        
        <!-- Tab Navigation Switcher -->
        <div class="auth-tabs">
          <button 
            type="button" 
            class="tab-btn" 
            [class.active]="mode === 'login'" 
            (click)="switchMode('login')">
            <span>🔑</span> Connexion
          </button>
          <button 
            type="button" 
            class="tab-btn" 
            [class.active]="mode === 'register'" 
            (click)="switchMode('register')">
            <span>📝</span> Inscription
          </button>
        </div>

        <!-- Header Info -->
        <div class="auth-header">
          <h2 class="auth-title">
            {{ mode === 'login' ? 'Espace Authentification' : 'Créer un Compte' }}
          </h2>
          <p class="auth-subtitle">
            {{ mode === 'login' 
              ? 'Accédez à votre espace avec vos identifiants JWT sécurisés' 
              : 'Choisissez votre rôle et rejoignez la plateforme' }}
          </p>
        </div>

        <!-- Quick 1-Click Demo Accounts (Only in Login Mode) -->
        <div class="demo-toolbar" *ngIf="mode === 'login'">
          <div class="demo-title">⚡ Accès Démo Rapide</div>
          <div class="demo-buttons">
            <button type="button" class="btn-demo admin-demo" (click)="loginAsAdmin()">
              👑 Admin (Gouvernance)
            </button>
            <button type="button" class="btn-demo candidat-demo" (click)="loginAsCandidat()">
              🎓 Candidat (Étudiant)
            </button>
          </div>
        </div>

        <!-- Feedback Banners -->
        <div class="alert-banner alert-error" *ngIf="errorMessage">
          <span>⚠️</span> {{ errorMessage }}
        </div>
        <div class="alert-banner alert-success" *ngIf="successMessage">
          <span>✅</span> {{ successMessage }}
        </div>

        <!-- ==================== LOGIN FORM ==================== -->
        <form *ngIf="mode === 'login'" (ngSubmit)="onLogin()" class="auth-form">
          <div class="form-group">
            <label for="email">Adresse E-mail</label>
            <div class="input-icon-wrapper">
              <span class="input-icon">✉️</span>
              <input 
                id="email" 
                class="form-input" 
                type="email" 
                [(ngModel)]="loginData.email" 
                name="email" 
                required 
                placeholder="ex: admin@codingfactory.tn" 
              />
            </div>
          </div>

          <div class="form-group">
            <label for="password">Mot de Passe</label>
            <div class="input-icon-wrapper">
              <span class="input-icon">🔒</span>
              <input 
                id="password" 
                class="form-input" 
                type="password" 
                [(ngModel)]="loginData.password" 
                name="password" 
                required 
                placeholder="••••••••" 
              />
            </div>
          </div>

          <button 
            type="submit" 
            class="submit-btn btn-primary" 
            [disabled]="loading || !loginData.email || !loginData.password">
            <span class="spinner" *ngIf="loading"></span>
            <span>{{ loading ? 'Vérification JWT...' : 'Se Connecter' }}</span>
          </button>
        </form>

        <!-- ==================== REGISTER FORM ==================== -->
        <form *ngIf="mode === 'register'" (ngSubmit)="onRegister()" class="auth-form">
          
          <!-- Interactive Role Picker Cards -->
          <div class="form-group">
            <label>Choisissez votre Rôle :</label>
            <div class="role-picker-grid">
              
              <div 
                class="role-card" 
                [class.selected]="registerData.role === 'CANDIDAT'"
                (click)="registerData.role = 'CANDIDAT'">
                <div class="role-card-icon">🎓</div>
                <div class="role-card-content">
                  <div class="role-card-title">Candidat / Étudiant</div>
                  <div class="role-card-desc">Accès aux sujets PFE, candidature 1-clic et matching IA</div>
                </div>
                <div class="role-card-check" *ngIf="registerData.role === 'CANDIDAT'">✓</div>
              </div>

              <div 
                class="role-card" 
                [class.selected]="registerData.role === 'ADMIN'"
                (click)="registerData.role = 'ADMIN'">
                <div class="role-card-icon">👑</div>
                <div class="role-card-content">
                  <div class="role-card-title">Administrateur</div>
                  <div class="role-card-desc">Gouvernance complète, gestion PFE et validation des candidats</div>
                </div>
                <div class="role-card-check" *ngIf="registerData.role === 'ADMIN'">✓</div>
              </div>

            </div>
          </div>

          <div class="form-row">
            <div class="form-group">
              <label for="regPrenom">Prénom</label>
              <input id="regPrenom" class="form-input" [(ngModel)]="registerData.prenom" name="prenom" required placeholder="Votre prénom" />
            </div>
            <div class="form-group">
              <label for="regNom">Nom</label>
              <input id="regNom" class="form-input" [(ngModel)]="registerData.nom" name="nom" required placeholder="Votre nom" />
            </div>
          </div>

          <div class="form-group">
            <label for="regEmail">Adresse E-mail</label>
            <input id="regEmail" class="form-input" type="email" [(ngModel)]="registerData.email" name="regEmail" required placeholder="exemple@codingfactory.tn" />
          </div>

          <div class="form-group">
            <label for="regPassword">Mot de Passe</label>
            <input id="regPassword" class="form-input" type="password" [(ngModel)]="registerData.password" name="regPassword" required placeholder="Mot de passe sécurisé" />
          </div>

          <button 
            type="submit" 
            class="submit-btn btn-accent" 
            [disabled]="loading || !registerData.email || !registerData.password">
            <span class="spinner" *ngIf="loading"></span>
            <span>{{ loading ? 'Création de compte...' : 'Créer mon compte ' + (registerData.role === 'ADMIN' ? 'Admin' : 'Candidat') }}</span>
          </button>
        </form>

      </div>

      <!-- Footer -->
      <div class="auth-footer">
        © 2026 CodingFactory · Architecture Microservices & JWT Security
      </div>

    </div>
  `,
  styles: [`
    .auth-wrapper {
      min-height: 100vh;
      background: linear-gradient(135deg, #071927 0%, #0d2a45 40%, #133b60 100%);
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      padding: 32px 16px;
      position: relative;
      overflow: hidden;
      font-family: 'Inter', system-ui, -apple-system, sans-serif;
    }

    .ambient-glow {
      position: absolute;
      border-radius: 50%;
      filter: blur(100px);
      pointer-events: none;
    }
    .glow-1 {
      width: 400px;
      height: 400px;
      background: rgba(47, 128, 237, 0.15);
      top: -100px;
      left: -100px;
    }
    .glow-2 {
      width: 500px;
      height: 500px;
      background: rgba(0, 198, 255, 0.12);
      bottom: -150px;
      right: -150px;
    }

    .brand-badge {
      display: inline-flex;
      align-items: center;
      gap: 10px;
      background: rgba(255, 255, 255, 0.08);
      border: 1px solid rgba(255, 255, 255, 0.15);
      padding: 8px 18px;
      border-radius: 40px;
      backdrop-filter: blur(12px);
      margin-bottom: 24px;
      color: #ffffff;
      box-shadow: 0 8px 32px rgba(0,0,0,0.2);
    }
    .brand-title {
      font-weight: 800;
      letter-spacing: 0.06em;
      font-size: 1.1rem;
    }
    .brand-chip {
      background: rgba(47, 128, 237, 0.3);
      color: #70b5ff;
      font-size: 0.72rem;
      font-weight: 700;
      padding: 3px 10px;
      border-radius: 20px;
      text-transform: uppercase;
    }

    .auth-card {
      width: 100%;
      max-width: 480px;
      background: rgba(255, 255, 255, 0.98);
      backdrop-filter: blur(24px);
      border-radius: 24px;
      padding: 36px 32px;
      box-shadow: 0 30px 70px rgba(0, 0, 0, 0.4);
      position: relative;
      z-index: 10;
      transition: all 0.3s ease;
    }

    .auth-tabs {
      display: flex;
      background: #f0f4f9;
      border-radius: 14px;
      padding: 4px;
      margin-bottom: 24px;
    }
    .tab-btn {
      flex: 1;
      padding: 10px;
      border: none;
      background: transparent;
      border-radius: 10px;
      font-weight: 700;
      font-size: 0.9rem;
      color: #5a6e85;
      cursor: pointer;
      transition: all 0.25s ease;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 6px;
    }
    .tab-btn.active {
      background: #ffffff;
      color: #0b2d4a;
      box-shadow: 0 4px 12px rgba(11, 45, 74, 0.12);
    }

    .auth-header {
      text-align: center;
      margin-bottom: 24px;
    }
    .auth-title {
      margin: 0 0 6px;
      font-size: 1.5rem;
      font-weight: 800;
      color: #0b2d4a;
    }
    .auth-subtitle {
      margin: 0;
      font-size: 0.86rem;
      color: #63778e;
    }

    .demo-toolbar {
      background: #f0f7ff;
      border: 1px dashed #2f80ed;
      border-radius: 14px;
      padding: 12px;
      margin-bottom: 20px;
      text-align: center;
    }
    .demo-title {
      font-size: 0.74rem;
      font-weight: 800;
      color: #2f80ed;
      text-transform: uppercase;
      letter-spacing: 0.05em;
      margin-bottom: 8px;
    }
    .demo-buttons {
      display: flex;
      gap: 8px;
      justify-content: center;
    }
    .btn-demo {
      border: none;
      padding: 8px 14px;
      border-radius: 8px;
      font-size: 0.8rem;
      font-weight: 700;
      cursor: pointer;
      transition: transform 0.15s ease;
    }
    .btn-demo:hover {
      transform: translateY(-2px);
    }
    .admin-demo {
      background: #0b2d4a;
      color: #ffffff;
    }
    .candidat-demo {
      background: #2f80ed;
      color: #ffffff;
    }

    .alert-banner {
      padding: 12px 16px;
      border-radius: 12px;
      font-size: 0.88rem;
      font-weight: 600;
      margin-bottom: 20px;
      display: flex;
      align-items: center;
      gap: 8px;
    }
    .alert-error {
      background: #fff0f0;
      color: #d32f2f;
      border: 1px solid #ffcdd2;
    }
    .alert-success {
      background: #e8f5e9;
      color: #2e7d32;
      border: 1px solid #c8e6c9;
    }

    .auth-form {
      display: flex;
      flex-direction: column;
      gap: 16px;
    }
    .form-group {
      display: flex;
      flex-direction: column;
      gap: 6px;
    }
    .form-group label {
      font-size: 0.85rem;
      font-weight: 700;
      color: #1e3a5f;
    }
    .form-row {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 12px;
    }

    .input-icon-wrapper {
      position: relative;
      display: flex;
      align-items: center;
    }
    .input-icon {
      position: absolute;
      left: 12px;
      font-size: 1rem;
    }
    .form-input {
      width: 100%;
      padding: 12px 14px 12px 38px;
      border: 1.5px solid #d0dbe8;
      border-radius: 12px;
      font-size: 0.92rem;
      transition: border-color 0.2s ease, box-shadow 0.2s ease;
      outline: none;
    }
    .form-input:focus {
      border-color: #2f80ed;
      box-shadow: 0 0 0 4px rgba(47, 128, 237, 0.12);
    }

    /* Role Cards Picker */
    .role-picker-grid {
      display: flex;
      flex-direction: column;
      gap: 10px;
      margin-top: 4px;
    }
    .role-card {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 12px 14px;
      border: 2px solid #e1e8f0;
      border-radius: 14px;
      background: #ffffff;
      cursor: pointer;
      transition: all 0.2s ease;
      position: relative;
    }
    .role-card:hover {
      border-color: #b8d5fb;
      background: #f8fbff;
    }
    .role-card.selected {
      border-color: #2f80ed;
      background: #edf5ff;
      box-shadow: 0 4px 14px rgba(47, 128, 237, 0.12);
    }
    .role-card-icon {
      font-size: 1.6rem;
    }
    .role-card-content {
      flex: 1;
    }
    .role-card-title {
      font-weight: 700;
      font-size: 0.9rem;
      color: #0b2d4a;
    }
    .role-card-desc {
      font-size: 0.76rem;
      color: #63778e;
      margin-top: 2px;
    }
    .role-card-check {
      width: 22px;
      height: 22px;
      background: #2f80ed;
      color: #ffffff;
      border-radius: 50%;
      display: grid;
      place-items: center;
      font-size: 0.8rem;
      font-weight: 800;
    }

    .submit-btn {
      width: 100%;
      padding: 14px;
      border: none;
      border-radius: 12px;
      font-size: 0.95rem;
      font-weight: 700;
      cursor: pointer;
      transition: all 0.2s ease;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 8px;
      margin-top: 6px;
    }
    .btn-primary {
      background: linear-gradient(135deg, #0b2d4a 0%, #1e517b 100%);
      color: #ffffff;
      box-shadow: 0 8px 20px rgba(11, 45, 74, 0.25);
    }
    .btn-primary:hover:not(:disabled) {
      transform: translateY(-1px);
      box-shadow: 0 10px 24px rgba(11, 45, 74, 0.35);
    }
    .btn-accent {
      background: linear-gradient(135deg, #2f80ed 0%, #00c6ff 100%);
      color: #ffffff;
      box-shadow: 0 8px 20px rgba(47, 128, 237, 0.3);
    }
    .btn-accent:hover:not(:disabled) {
      transform: translateY(-1px);
      box-shadow: 0 10px 24px rgba(47, 128, 237, 0.4);
    }
    .submit-btn:disabled {
      opacity: 0.6;
      cursor: not-allowed;
    }

    .spinner {
      width: 16px;
      height: 16px;
      border: 2px solid rgba(255,255,255,0.3);
      border-top-color: #ffffff;
      border-radius: 50%;
      animation: spin 0.8s linear infinite;
    }
    @keyframes spin {
      to { transform: rotate(360deg); }
    }

    .auth-footer {
      margin-top: 24px;
      color: rgba(255, 255, 255, 0.5);
      font-size: 0.8rem;
      text-align: center;
      position: relative;
      z-index: 10;
    }
  `]
})
export class LoginComponent {
  mode: 'login' | 'register' = 'login';
  loading = false;
  errorMessage = '';
  successMessage = '';

  loginData = {
    email: '',
    password: ''
  };

  registerData = {
    prenom: '',
    nom: '',
    email: '',
    password: '',
    role: 'CANDIDAT' as 'CANDIDAT' | 'ADMIN'
  };

  constructor(private authService: AuthService, private router: Router) {}

  switchMode(newMode: 'login' | 'register'): void {
    this.mode = newMode;
    this.clearMessages();
  }

  onLogin(): void {
    this.clearMessages();
    this.loading = true;

    this.authService.login(this.loginData.email, this.loginData.password).subscribe({
      next: res => {
        this.loading = false;
        this.successMessage = `Connexion réussie ! Bienvenue ${res.prenom || res.email} (Rôle : ${res.role})`;
        setTimeout(() => {
          this.router.navigate(['/pfe']);
        }, 700);
      },
      error: err => {
        this.loading = false;
        const msg = err?.error?.message || err?.error || 'Échec de connexion. Vérifiez vos identifiants.';
        this.errorMessage = msg;
      }
    });
  }

  onRegister(): void {
    this.clearMessages();
    this.loading = true;

    this.authService.register(
      this.registerData.prenom,
      this.registerData.nom,
      this.registerData.email,
      this.registerData.password,
      this.registerData.role
    ).subscribe({
      next: res => {
        this.loading = false;
        this.successMessage = `Compte créé avec succès ! Connecté en tant que ${res.role}.`;
        setTimeout(() => {
          this.router.navigate(['/pfe']);
        }, 700);
      },
      error: err => {
        this.loading = false;
        const msg = err?.error?.message || 'Impossible de créer le compte.';
        this.errorMessage = msg;
      }
    });
  }

  loginAsAdmin(): void {
    this.loginData.email = 'admin@codingfactory.tn';
    this.loginData.password = 'admin123';
    this.switchMode('login');
    this.onLogin();
  }

  loginAsCandidat(): void {
    this.loginData.email = 'candidat@codingfactory.tn';
    this.loginData.password = 'candidat123';
    this.switchMode('login');
    this.onLogin();
  }

  clearMessages(): void {
    this.errorMessage = '';
    this.successMessage = '';
  }
}
