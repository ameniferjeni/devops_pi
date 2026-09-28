import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ChatbotService } from '../services/chatbot.service';
import { ChatbotServiceInfo } from '../models/chatbot-response.model';

interface ChatMessage {
  role: 'user' | 'bot';
  text: string;
  meta?: string;
  pfeLink?: boolean;
}

@Component({
  selector: 'app-chatbot',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  template: `
    <section class="cf-hero">
      <h1>Chatbot CodingFactory</h1>
      <p>
        Analyse d'intention, orientation vers un service de consulting et proposition d'un consultant référent.
      </p>
    </section>

    <div class="cf-chat-layout">
      <aside class="cf-panel" style="margin-bottom: 0">
        <div class="cf-panel-head">
          <div>
            <h2>Services</h2>
            <p>Catalogue synchronisé avec MySQL.</p>
          </div>
        </div>
        <ul class="cf-service-list" *ngIf="services.length; else noServices">
          <li *ngFor="let s of services">
            <strong>{{ s.nom }}</strong>
            {{ s.description }}
            <div style="margin-top: 6px; font-size: 0.78rem; color: var(--cf-text-muted)">
              {{ s.consultantsCount }} consultant(s)
            </div>
          </li>
        </ul>
        <ng-template #noServices>
          <p style="color: var(--cf-text-muted); font-size: 0.9rem">Démarrez le backend pour charger les services.</p>
        </ng-template>
      </aside>

      <section class="cf-panel" style="margin-bottom: 0">
        <div class="cf-panel-head">
          <div>
            <h2>CodingBot</h2>
            <p>Assistant personnalisé — conversation en direct.</p>
          </div>
        </div>

        <div class="cf-field">
          <label for="prenom">Votre prénom</label>
          <input
            id="prenom"
            class="cf-input"
            type="text"
            [(ngModel)]="prenom"
            (blur)="savePrenom()"
            placeholder="Exemple : Salma"
            maxlength="50"
          />
        </div>

        <div class="cf-suggestions">
          <span>Suggestions :</span>
          <button type="button" class="cf-chip" *ngFor="let s of suggestions" (click)="useSuggestion(s)">{{ s }}</button>
        </div>

        <div class="cf-messages" #scrollArea>
          <div
            *ngFor="let msg of messages"
            class="cf-message"
            [class.user]="msg.role === 'user'"
            [class.bot]="msg.role === 'bot'"
          >
            <p>{{ msg.text }}</p>
            <small *ngIf="msg.meta">{{ msg.meta }}</small>
            <a *ngIf="msg.pfeLink" routerLink="/pfe" class="cf-inline-link">Voir le module PFE →</a>
          </div>
          <div class="cf-message bot cf-typing" *ngIf="loading">CodingBot analyse votre demande…</div>
        </div>

        <div class="cf-chat-input-row">
          <textarea
            class="cf-textarea"
            [(ngModel)]="question"
            (keydown.enter)="onEnter($event)"
            placeholder="Décrivez votre besoin (développement, sécurité, formation, conseil…)"
          ></textarea>
          <button type="button" class="cf-btn cf-btn-primary" (click)="sendQuestion()" [disabled]="loading || !question.trim()">
            {{ loading ? 'Analyse en cours…' : 'Envoyer' }}
          </button>
        </div>
      </section>
    </div>
  `
})
export class ChatbotComponent implements OnInit {
  private static readonly PRENOM_KEY = 'codingfactory.chatbot.prenom';

  @ViewChild('scrollArea') scrollArea?: ElementRef<HTMLElement>;

  prenom = '';
  question = '';
  loading = false;
  messages: ChatMessage[] = [];
  services: ChatbotServiceInfo[] = [];

  suggestions = [
    'Bonjour',
    'Quels sujets PFE proposez-vous ?',
    'Audit cybersécurité',
    'Formation Spring Boot',
    'Application web sur mesure'
  ];

  constructor(private chatbotService: ChatbotService) {}

  ngOnInit(): void {
    const saved = localStorage.getItem(ChatbotComponent.PRENOM_KEY);
    if (saved) {
      this.prenom = saved;
      this.messages.push({
        role: 'bot',
        text: `Bonjour ${saved}, je suis CodingBot. Décrivez votre projet ou choisissez une suggestion.`
      });
    } else {
      this.messages.push({
        role: 'bot',
        text: 'Bonjour, je suis CodingBot. Indiquez votre prénom puis votre besoin — je vous orienterai vers le bon service.'
      });
    }

    this.chatbotService.getServices().subscribe({
      next: data => (this.services = data),
      error: () => {}
    });
  }

  savePrenom(): void {
    const trimmed = this.prenom.trim();
    if (trimmed) {
      localStorage.setItem(ChatbotComponent.PRENOM_KEY, trimmed);
    } else {
      localStorage.removeItem(ChatbotComponent.PRENOM_KEY);
    }
  }

  useSuggestion(text: string): void {
    this.question = text;
    this.sendQuestion();
  }

  onEnter(event: Event): void {
    const ke = event as KeyboardEvent;
    if (ke.shiftKey) {
      return;
    }
    ke.preventDefault();
    this.sendQuestion();
  }

  sendQuestion(): void {
    const text = this.question.trim();
    if (!text || this.loading) {
      return;
    }

    this.savePrenom();
    this.messages.push({ role: 'user', text });
    this.question = '';
    this.loading = true;
    this.scrollToBottom();

    this.chatbotService.ask(text, this.prenom || undefined).subscribe({
      next: data => {
        let text = data.reponse;
        if (data.serviceDescription && data.intention !== 'SALUTATION' && data.intention !== 'CONTACT') {
          text += '\n\n' + data.serviceDescription;
        }
        const metaParts = [
          data.intentionLabel || data.intention,
          data.service ? `Service : ${data.service}` : null,
          data.consultant && data.consultant !== 'Équipe CodingFactory' ? `Consultant : ${data.consultant}` : null,
          data.consultantEmail ? `Email : ${data.consultantEmail}` : null
        ].filter(Boolean);
        this.messages.push({
          role: 'bot',
          text,
          meta: metaParts.join(' · '),
          pfeLink: data.intention === 'PFE'
        });
        this.loading = false;
        this.scrollToBottom();
      },
      error: () => {
        this.messages.push({
          role: 'bot',
          text: 'Connexion au serveur impossible. Lancez le backend (port 8081) et MySQL, puis réessayez.'
        });
        this.loading = false;
        this.scrollToBottom();
      }
    });
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
