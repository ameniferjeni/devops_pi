import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ChatbotService } from '../services/chatbot.service';
import { ChatbotServiceInfo, ChatbotResponse } from '../models/chatbot-response.model';

interface ChatMessage {
  role: 'user' | 'bot';
  text: string;
  meta?: string;
  pfeLink?: boolean;
  actionType?: string;
  consultantNom?: string;
  consultantRole?: string;
  consultantEmail?: string;
  consultantPhone?: string;
  consultantAvatar?: string;
  serviceNom?: string;
  suggestions?: string[];
  time?: string;
}

@Component({
  selector: 'app-chatbot',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  template: `
    <!-- Hero Banner -->
    <section class="cf-hero cf-hero-compact">
      <div class="cf-hero-row">
        <div>
          <div style="display: flex; align-items: center; gap: 10px; margin-bottom: 8px;">
            <span class="cf-badge cf-badge-success" style="background: rgba(255,255,255,0.2); color: #fff; font-size: 0.75rem;">
              🤖 Chatbot Personnalisé v2.0
            </span>
          </div>
          <h1>Assistant Consulting & Orientation</h1>
          <p>
            Posez vos questions générales sur CodingFactory, découvrez nos services de consulting et entrez en contact direct avec nos experts référents.
          </p>
        </div>
        <button type="button" class="cf-btn cf-btn-ghost" (click)="resetChat()" title="Réinitialiser la discussion">
          🔄 Effacer la conversation
        </button>
      </div>
    </section>

    <!-- Main Layout: Services Sidebar + Chat Section -->
    <div class="cf-chat-layout">
      
      <!-- Sidebar: Catalogue des Services Consulting -->
      <aside class="cf-panel" style="margin-bottom: 0">
        <div class="cf-panel-head">
          <div>
            <h2>Services Consulting</h2>
            <p>Cliquez sur un service pour consulter un expert</p>
          </div>
        </div>

        <div *ngIf="services.length; else noServices" class="cf-service-list-container">
          <ul class="cf-service-list">
            <li 
              *ngFor="let s of services" 
              class="cf-service-item"
              (click)="askAboutService(s)"
              style="cursor: pointer; transition: transform 0.15s ease, border-color 0.15s ease;"
            >
              <div style="display: flex; justify-content: space-between; align-items: flex-start;">
                <strong>{{ s.nom }}</strong>
                <span class="cf-badge cf-badge-muted" style="font-size: 0.7rem;">{{ s.consultantsCount }} expert(s)</span>
              </div>
              <p style="margin: 4px 0 8px; font-size: 0.8rem; color: var(--cf-text-muted); line-height: 1.4;">
                {{ s.description }}
              </p>
              
              <!-- Expert info badge -->
              <div *ngIf="s.expertNom" style="margin-top: 6px; padding: 6px 8px; background: rgba(47, 128, 237, 0.06); border-radius: 6px; font-size: 0.78rem;">
                <div style="font-weight: 600; color: var(--cf-primary);">👤 Expert : {{ s.expertNom }}</div>
                <div style="font-size: 0.72rem; color: var(--cf-text-muted);">{{ s.expertRole }}</div>
              </div>

              <!-- Tech tags -->
              <div *ngIf="s.technologies && s.technologies.length" style="display: flex; flex-wrap: wrap; gap: 4px; margin-top: 8px;">
                <span *ngFor="let tech of s.technologies" class="cf-tag" style="font-size: 0.7rem; padding: 2px 6px;">
                  {{ tech }}
                </span>
              </div>
            </li>
          </ul>
        </div>

        <ng-template #noServices>
          <div style="text-align: center; padding: 20px 10px; color: var(--cf-text-muted);">
            <span style="font-size: 2rem;">⏳</span>
            <p style="font-size: 0.88rem; margin-top: 8px;">Chargement des services de consulting…</p>
          </div>
        </ng-template>
      </aside>

      <!-- Central Section: Personalized Chatbot -->
      <section class="cf-panel" style="margin-bottom: 0; display: flex; flex-direction: column; min-height: 580px;">
        
        <!-- Header & Profile Customization Bar -->
        <div class="cf-panel-head" style="border-bottom: 1px solid var(--cf-border); padding-bottom: 14px; margin-bottom: 14px;">
          <div>
            <div style="display: flex; align-items: center; gap: 8px;">
              <span style="display: inline-block; width: 10px; height: 10px; border-radius: 50%; background: #27ae60;"></span>
              <h2 style="font-size: 1.15rem;">CodingBot Assistant</h2>
            </div>
            <p style="font-size: 0.85rem; margin-top: 2px;">
              Personnalisé pour <strong style="color: var(--cf-primary);">{{ prenom || 'Cher visiteur' }}</strong> 
              <span *ngIf="entreprise"> ({{ entreprise }})</span>
            </p>
          </div>

          <!-- Quick Toggle for Profile Settings -->
          <button type="button" class="cf-btn cf-btn-sm cf-btn-ghost" (click)="toggleProfileSettings()">
            ⚙️ {{ showProfileSettings ? 'Masquer profil' : 'Personnaliser mon profil' }}
          </button>
        </div>

        <!-- User Profile Settings Panel (Collapsible) -->
        <div *ngIf="showProfileSettings" style="background: #f0f4f9; border: 1px solid var(--cf-border); border-radius: 10px; padding: 14px; margin-bottom: 14px;">
          <h4 style="margin: 0 0 10px; font-size: 0.9rem; color: var(--cf-primary);">👤 Personnalisation du profil</h4>
          <div class="cf-field-grid">
            <div class="cf-field">
              <label for="prenomInput">Votre Prénom</label>
              <input
                id="prenomInput"
                class="cf-input"
                type="text"
                [(ngModel)]="prenom"
                (change)="saveProfile()"
                placeholder="Ex: Salma, Mohamed..."
                maxlength="50"
              />
            </div>
            <div class="cf-field">
              <label for="entrepriseInput">Entreprise / Organisme</label>
              <input
                id="entrepriseInput"
                class="cf-input"
                type="text"
                [(ngModel)]="entreprise"
                (change)="saveProfile()"
                placeholder="Ex: Tech Corp, Université..."
                maxlength="50"
              />
            </div>
            <div class="cf-field">
              <label for="roleInput">Votre Rôle</label>
              <select id="roleInput" class="cf-select" [(ngModel)]="role" (change)="saveProfile()">
                <option value="Client">Client / Entreprise</option>
                <option value="Etudiant">Étudiant / Candidat PFE</option>
                <option value="Partenaire">Partenaire Technique</option>
                <option value="Autre">Autre / Curieux</option>
              </select>
            </div>
          </div>
        </div>

        <!-- Chat Messages Container -->
        <div class="cf-messages" #scrollArea style="flex: 1; min-height: 320px; max-height: 480px;">
          
          <div
            *ngFor="let msg of messages"
            class="cf-message"
            [class.user]="msg.role === 'user'"
            [class.bot]="msg.role === 'bot'"
          >
            <!-- Message Header / Role -->
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 4px; font-size: 0.78rem; opacity: 0.85;">
              <strong>{{ msg.role === 'user' ? (prenom || 'Vous') : 'CodingBot' }}</strong>
              <span>{{ msg.time }}</span>
            </div>

            <!-- Main Response Content -->
            <div style="white-space: pre-wrap; word-break: break-word;">{{ msg.text }}</div>

            <!-- Embedded Consultant Business Card -->
            <div 
              *ngIf="msg.consultantNom && msg.consultantNom !== 'Équipe CodingFactory Consulting'" 
              style="margin-top: 12px; padding: 12px; background: rgba(255, 255, 255, 0.95); border: 1px solid var(--cf-border); border-radius: 10px; color: var(--cf-text);"
            >
              <div style="display: flex; align-items: center; gap: 10px;">
                <span style="font-size: 1.8rem; background: var(--cf-accent-soft); padding: 6px; border-radius: 50%;">
                  {{ msg.consultantAvatar || '👨‍💼' }}
                </span>
                <div style="flex: 1;">
                  <h4 style="margin: 0; font-size: 0.95rem; color: var(--cf-primary);">{{ msg.consultantNom }}</h4>
                  <p style="margin: 2px 0 0; font-size: 0.8rem; color: var(--cf-text-muted);">{{ msg.consultantRole }}</p>
                  <div style="font-size: 0.78rem; color: var(--cf-accent); margin-top: 2px;">
                    📧 {{ msg.consultantEmail }} <span *ngIf="msg.consultantPhone"> | 📞 {{ msg.consultantPhone }}</span>
                  </div>
                </div>
              </div>

              <!-- Consultant Action Buttons -->
              <div style="display: flex; gap: 8px; margin-top: 10px;">
                <a 
                  [href]="'mailto:' + msg.consultantEmail" 
                  class="cf-btn cf-btn-sm cf-btn-ghost" 
                  style="text-decoration: none; font-size: 0.78rem;"
                >
                  ✉️ Envoyer un e-mail
                </a>
                <button 
                  type="button" 
                  class="cf-btn cf-btn-sm cf-btn-primary" 
                  (click)="openRdvModal(msg.consultantNom, msg.consultantEmail, msg.serviceNom)"
                  style="font-size: 0.78rem;"
                >
                  📅 Programmer un RDV
                </button>
              </div>
            </div>

            <!-- Meta details tag -->
            <small *ngIf="msg.meta" style="margin-top: 8px;">{{ msg.meta }}</small>

            <!-- Special Action Links -->
            <div *ngIf="msg.pfeLink" style="margin-top: 10px;">
              <a routerLink="/pfe" class="cf-btn cf-btn-sm cf-btn-primary" style="text-decoration: none;">
                🎓 Explorer les sujets PFE 2026 →
              </a>
            </div>
          </div>

          <!-- Typing Indicator -->
          <div class="cf-message bot cf-typing" *ngIf="loading">
            <span style="display: inline-block; margin-right: 6px;">🤖</span> CodingBot analyse votre demande consulting...
          </div>
        </div>

        <!-- Contextual Suggestions Bar -->
        <div class="cf-suggestions" *ngIf="currentSuggestions.length">
          <span style="font-size: 0.8rem; font-weight: 600;">Suggestions rapides :</span>
          <button 
            type="button" 
            class="cf-chip" 
            *ngFor="let s of currentSuggestions" 
            (click)="useSuggestion(s)"
          >
            {{ s }}
          </button>
        </div>

        <!-- Chat Input Row -->
        <div class="cf-chat-input-row" style="margin-top: 10px;">
          <div style="display: flex; gap: 8px;">
            <textarea
              class="cf-textarea"
              style="min-height: 60px; height: 60px; flex: 1;"
              [(ngModel)]="question"
              (keydown.enter)="onEnter($event)"
              placeholder="Posez votre question (ex: Quels sont vos tarifs ? Besoin d'un audit de sécurité...)"
            ></textarea>
            <button 
              type="button" 
              class="cf-btn cf-btn-primary" 
              style="padding: 0 24px;"
              (click)="sendQuestion()" 
              [disabled]="loading || !question.trim()"
            >
              {{ loading ? 'Analyse…' : 'Envoyer' }}
            </button>
          </div>
        </div>

      </section>

    </div>

    <!-- RDV Modal Simulation -->
    <div *ngIf="rdvModalOpen" class="cf-modal-backdrop" style="position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; background: rgba(0,0,0,0.5); display: grid; place-items: center; z-index: 100;">
      <div class="cf-panel" style="max-width: 480px; width: 90%; background: #fff; border-radius: 14px; padding: 24px; box-shadow: 0 20px 50px rgba(0,0,0,0.3);">
        <h3 style="margin-top: 0; color: var(--cf-primary);">📅 Prise de RDV avec {{ rdvConsultantNom }}</h3>
        <p style="font-size: 0.88rem; color: var(--cf-text-muted);">
          Un créneau d'échange personnalisé pour discuter de votre besoin en <strong>{{ rdvServiceNom || 'Consulting' }}</strong>.
        </p>

        <form (ngSubmit)="confirmRdv()">
          <div class="cf-field">
            <label>Votre nom complet</label>
            <input class="cf-input" type="text" [(ngModel)]="rdvUserName" name="rdvUserName" required placeholder="Votre nom" />
          </div>
          <div class="cf-field">
            <label>Votre adresse email</label>
            <input class="cf-input" type="email" [(ngModel)]="rdvUserEmail" name="rdvUserEmail" required placeholder="exemple@domaine.com" />
          </div>
          <div class="cf-field">
            <label>Date souhaitée</label>
            <input class="cf-input" type="date" [(ngModel)]="rdvDate" name="rdvDate" required />
          </div>

          <div style="display: flex; justify-content: flex-end; gap: 10px; margin-top: 18px;">
            <button type="button" class="cf-btn cf-btn-ghost" (click)="closeRdvModal()">Annuler</button>
            <button type="submit" class="cf-btn cf-btn-primary">Confirmer la demande de RDV</button>
          </div>
        </form>
      </div>
    </div>
  `
})
export class ChatbotComponent implements OnInit {
  private static readonly PRENOM_KEY = 'codingfactory.chatbot.prenom';
  private static readonly ENTREPRISE_KEY = 'codingfactory.chatbot.entreprise';
  private static readonly ROLE_KEY = 'codingfactory.chatbot.role';

  @ViewChild('scrollArea') scrollArea?: ElementRef<HTMLElement>;

  prenom = '';
  entreprise = '';
  role = 'Client';
  showProfileSettings = false;

  question = '';
  loading = false;
  messages: ChatMessage[] = [];
  services: ChatbotServiceInfo[] = [];

  currentSuggestions: string[] = [
    'Quels sont vos services de consulting ?',
    'Besoin d\'un audit de cybersécurité',
    'Développement d\'une application web',
    'Quels sont vos tarifs / TJM ?',
    'Consulter les sujets PFE 2026'
  ];

  // RDV Modal state
  rdvModalOpen = false;
  rdvConsultantNom = '';
  rdvConsultantEmail = '';
  rdvServiceNom = '';
  rdvUserName = '';
  rdvUserEmail = '';
  rdvDate = '';

  constructor(private chatbotService: ChatbotService) {}

  ngOnInit(): void {
    // Load persistent profile
    this.loadProfile();

    const greetingName = this.prenom ? this.prenom : 'Cher visiteur';
    const enterpriseText = this.entreprise ? ` (${this.entreprise})` : '';

    this.messages.push({
      role: 'bot',
      text: `Bonjour ${greetingName}${enterpriseText} ! 👋 Je suis CodingBot, l'assistant virtuel de CodingFactory Consulting.\n\nPosez-moi vos questions ou sélectionnez un service ci-contre pour être orienté vers nos consultants experts.`,
      time: this.getCurrentTime()
    });

    // Load available consulting services
    this.chatbotService.getServices().subscribe({
      next: data => {
        this.services = data;
      },
      error: () => {
        // Fallback demo services if backend service list fails
        this.services = [
          {
            id: 1,
            nom: 'Développement Software & Cloud Native',
            description: 'Applications web, mobile & architectures Microservices Spring Boot & Angular.',
            consultantsCount: 5,
            expertNom: 'Sami Mansour',
            expertRole: 'Architecte Lead Software & Cloud',
            expertEmail: 'sami.mansour@codingfactory.tn',
            technologies: ['Spring Boot', 'Angular', 'AWS', 'Docker']
          },
          {
            id: 2,
            nom: 'Cybersécurité & Audit SI',
            description: 'Audits de sécurité, tests d\'intrusion Pentest et conformité ISO 27001.',
            consultantsCount: 4,
            expertNom: 'Mehdi Gharbi',
            expertRole: 'Lead Expert Cybersécurité & Pentest',
            expertEmail: 'mehdi.gharbi@codingfactory.tn',
            technologies: ['Pentest', 'RGPD', 'ISO 27001', 'SOC']
          },
          {
            id: 3,
            nom: 'Platform Engineering & DevSecOps Infrastructure',
            description: 'Internal Developer Platforms (IDP Backstage), GitOps ArgoCD et coffre-fort Vault.',
            consultantsCount: 4,
            expertNom: 'Inès Chebbi',
            expertRole: 'Lead Platform Engineer & DevSecOps Specialist',
            expertEmail: 'ines.chebbi@codingfactory.tn',
            technologies: ['Kubernetes', 'ArgoCD', 'Vault', 'Backstage']
          },
          {
            id: 4,
            nom: 'Blockchain, Web3 & FinTech Security',
            description: 'Audit de Smart Contracts Solidity, protocoles décentralisés et sécurité Web3.',
            consultantsCount: 3,
            expertNom: 'Tarek Ben Ammar',
            expertRole: 'Lead Architect Blockchain & Smart Contracts',
            expertEmail: 'tarek.benammar@codingfactory.tn',
            technologies: ['Solidity', 'Ethereum', 'Web3.js', 'ZK-Proof']
          },
          {
            id: 5,
            nom: 'Formation Informatique & Coaching',
            description: 'Formations certifiantes Java, Spring Boot, Angular, DevOps & IA.',
            consultantsCount: 6,
            expertNom: 'Amina Triki',
            expertRole: 'Directrice Formations & Tech Coach',
            expertEmail: 'amina.triki@codingfactory.tn',
            technologies: ['Java 21', 'Spring Boot 3', 'Angular 18']
          },
          {
            id: 6,
            nom: 'Conseil Stratégique IT & Gouvernance',
            description: 'Schémas directeurs, gouvernance SI et transformation digitale.',
            consultantsCount: 3,
            expertNom: 'Karim Trabelsi',
            expertRole: 'Senior Consultant IT Strategy',
            expertEmail: 'karim.trabelsi@codingfactory.tn',
            technologies: ['Gouvernance SI', 'Audit Arch.', 'Agile']
          }
        ];
      }
    });
  }

  loadProfile(): void {
    const savedPrenom = localStorage.getItem(ChatbotComponent.PRENOM_KEY);
    if (savedPrenom) this.prenom = savedPrenom;

    const savedEnt = localStorage.getItem(ChatbotComponent.ENTREPRISE_KEY);
    if (savedEnt) this.entreprise = savedEnt;

    const savedRole = localStorage.getItem(ChatbotComponent.ROLE_KEY);
    if (savedRole) this.role = savedRole;
  }

  saveProfile(): void {
    if (this.prenom.trim()) {
      localStorage.setItem(ChatbotComponent.PRENOM_KEY, this.prenom.trim());
    } else {
      localStorage.removeItem(ChatbotComponent.PRENOM_KEY);
    }

    if (this.entreprise.trim()) {
      localStorage.setItem(ChatbotComponent.ENTREPRISE_KEY, this.entreprise.trim());
    } else {
      localStorage.removeItem(ChatbotComponent.ENTREPRISE_KEY);
    }

    if (this.role) {
      localStorage.setItem(ChatbotComponent.ROLE_KEY, this.role);
    }
  }

  toggleProfileSettings(): void {
    this.showProfileSettings = !this.showProfileSettings;
  }

  useSuggestion(text: string): void {
    this.question = text;
    this.sendQuestion();
  }

  askAboutService(service: ChatbotServiceInfo): void {
    this.question = `Parlez-moi du service ${service.nom} et du consultant ${service.expertNom || ''}`;
    this.sendQuestion();
  }

  onEnter(event: Event): void {
    const ke = event as KeyboardEvent;
    if (ke.shiftKey) return;
    ke.preventDefault();
    this.sendQuestion();
  }

  sendQuestion(): void {
    const text = this.question.trim();
    if (!text || this.loading) return;

    this.saveProfile();
    this.messages.push({
      role: 'user',
      text,
      time: this.getCurrentTime()
    });

    this.question = '';
    this.loading = true;
    this.scrollToBottom();

    this.chatbotService.ask(text, this.prenom || undefined, this.entreprise || undefined, this.role || undefined).subscribe({
      next: (data: ChatbotResponse) => {
        const metaParts = [
          data.intentionLabel || data.intention,
          data.serviceNom || data.service ? `Service : ${data.serviceNom || data.service}` : null
        ].filter(Boolean);

        const botMsg: ChatMessage = {
          role: 'bot',
          text: data.reponse,
          meta: metaParts.join(' · '),
          pfeLink: data.intention === 'PFE',
          actionType: data.actionType,
          consultantNom: data.consultantNom || data.consultant,
          consultantRole: data.consultantRole,
          consultantEmail: data.consultantEmail,
          consultantPhone: data.consultantPhone,
          consultantAvatar: data.consultantAvatar,
          serviceNom: data.serviceNom || data.service,
          time: this.getCurrentTime()
        };

        if (data.suggestions && data.suggestions.length > 0) {
          this.currentSuggestions = data.suggestions;
        }

        this.messages.push(botMsg);
        this.loading = false;
        this.scrollToBottom();
      },
      error: () => {
        this.messages.push({
          role: 'bot',
          text: 'Connexion au service chatbot momentanément indisponible. Assurez-vous que le backend (port 8082 ou via gateway) est démarré.',
          time: this.getCurrentTime()
        });
        this.loading = false;
        this.scrollToBottom();
      }
    });
  }

  resetChat(): void {
    this.messages = [];
    const greetingName = this.prenom ? this.prenom : 'Cher visiteur';
    this.messages.push({
      role: 'bot',
      text: `Discussion réinitialisée. Bonjour ${greetingName} ! Que souhaitez-vous savoir sur nos services de consulting ?`,
      time: this.getCurrentTime()
    });
    this.currentSuggestions = [
      'Quels sont vos services de consulting ?',
      'Besoin d\'un audit de cybersécurité',
      'Développement d\'une application web',
      'Quels sont vos tarifs / TJM ?',
      'Consulter les sujets PFE 2026'
    ];
  }

  openRdvModal(consultantNom?: string, consultantEmail?: string, serviceNom?: string): void {
    this.rdvConsultantNom = consultantNom || 'un consultant expert';
    this.rdvConsultantEmail = consultantEmail || 'contact@codingfactory.tn';
    this.rdvServiceNom = serviceNom || 'Consulting';
    this.rdvUserName = this.prenom || '';
    this.rdvUserEmail = '';
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    this.rdvDate = tomorrow.toISOString().split('T')[0];
    this.rdvModalOpen = true;
  }

  closeRdvModal(): void {
    this.rdvModalOpen = false;
  }

  confirmRdv(): void {
    this.rdvModalOpen = false;
    this.messages.push({
      role: 'bot',
      text: `✅ Demande de rendez-vous enregistrée avec succès !\n\nUn e-mail de confirmation a été envoyé à ${this.rdvConsultantNom} (${this.rdvConsultantEmail}) pour la date du ${this.rdvDate}.\nUn membre de notre équipe vous recontactera sous peu au sujet du service ${this.rdvServiceNom}.`,
      time: this.getCurrentTime()
    });
    this.scrollToBottom();
  }

  private getCurrentTime(): string {
    const now = new Date();
    return now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
  }

  private scrollToBottom(): void {
    setTimeout(() => {
      const el = this.scrollArea?.nativeElement;
      if (el) {
        el.scrollTop = el.scrollHeight;
      }
    }, 50);
  }
}
