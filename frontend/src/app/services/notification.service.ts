import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export interface AppNotification {
  id: string;
  titre: string;
  message: string;
  type: 'info' | 'success' | 'warning' | 'error';
  timestamp: Date;
  lu: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private notificationsSubject = new BehaviorSubject<AppNotification[]>([]);
  public notifications$ = this.notificationsSubject.asObservable();

  private toastSubject = new BehaviorSubject<AppNotification | null>(null);
  public toast$ = this.toastSubject.asObservable();

  constructor() {
    this.initDefaultNotifications();
  }

  private initDefaultNotifications(): void {
    const initial: AppNotification[] = [
      {
        id: '1',
        titre: '🔐 Authentification JWT',
        message: 'Bienvenue sur la plateforme CodingFactory en mode sécurisé.',
        type: 'info',
        timestamp: new Date(),
        lu: false
      },
      {
        id: '2',
        titre: '🎓 Service PFE Matcher',
        message: 'Le moteur d’analyse IA est prêt pour l’évaluation des compétences.',
        type: 'success',
        timestamp: new Date(Date.now() - 300000),
        lu: false
      }
    ];
    this.notificationsSubject.next(initial);
  }

  addNotification(titre: string, message: string, type: 'info' | 'success' | 'warning' | 'error' = 'info'): void {
    const notif: AppNotification = {
      id: Date.now().toString(),
      titre,
      message,
      type,
      timestamp: new Date(),
      lu: false
    };

    const current = this.notificationsSubject.value;
    this.notificationsSubject.next([notif, ...current]);

    // Trigger toast overlay
    this.toastSubject.next(notif);
    setTimeout(() => {
      if (this.toastSubject.value?.id === notif.id) {
        this.toastSubject.next(null);
      }
    }, 4500);
  }

  markAllAsRead(): void {
    const updated = this.notificationsSubject.value.map(n => ({ ...n, lu: true }));
    this.notificationsSubject.next(updated);
  }

  clearAll(): void {
    this.notificationsSubject.next([]);
  }

  getUnreadCount(): number {
    return this.notificationsSubject.value.filter(n => !n.lu).length;
  }
}
