import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ChatbotResponse, ChatbotServiceInfo } from '../models/chatbot-response.model';
import { environment } from '../../environments/environment';

export interface ChatbotAskPayload {
  question: string;
  prenom?: string;
  entreprise?: string;
  role?: string;
}

@Injectable({
  providedIn: 'root'
})
export class ChatbotService {
  private baseUrl = `${environment.apiUrl}/chatbot`;

  constructor(private http: HttpClient) {}

  ask(question: string, prenom?: string, entreprise?: string, role?: string): Observable<ChatbotResponse> {
    const body: ChatbotAskPayload = { question };
    if (prenom?.trim()) body.prenom = prenom.trim();
    if (entreprise?.trim()) body.entreprise = entreprise.trim();
    if (role?.trim()) body.role = role.trim();

    return this.http.post<ChatbotResponse>(`${this.baseUrl}/ask`, body);
  }

  getServices(): Observable<ChatbotServiceInfo[]> {
    return this.http.get<ChatbotServiceInfo[]>(`${this.baseUrl}/services`);
  }
}
