import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ChatbotResponse, ChatbotServiceInfo } from '../models/chatbot-response.model';
import { environment } from '../../environments/environment';

export interface ChatbotAskPayload {
  question: string;
  prenom?: string;
}

@Injectable({
  providedIn: 'root'
})
export class ChatbotService {
  private baseUrl = `${environment.apiUrl}/chatbot`;

  constructor(private http: HttpClient) {}

  ask(question: string, prenom?: string): Observable<ChatbotResponse> {
    const body: ChatbotAskPayload = { question };
    const trimmedPrenom = prenom?.trim();
    if (trimmedPrenom) {
      body.prenom = trimmedPrenom;
    }
    return this.http.post<ChatbotResponse>(`${this.baseUrl}/ask`, body);
  }

  getServices(): Observable<ChatbotServiceInfo[]> {
    return this.http.get<ChatbotServiceInfo[]>(`${this.baseUrl}/services`);
  }
}
