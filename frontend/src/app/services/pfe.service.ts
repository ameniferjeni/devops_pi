import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SujetPfe } from '../models/sujet-pfe.model';
import { ProjetRealise } from '../models/projet-realise.model';
import { Candidature } from '../models/candidature.model';
import { environment } from '../../environments/environment';

export interface CandidatSummary {
  id: number;
  nom: string;
  prenom: string;
  email: string;
}

export interface CandidatureSubmit {
  sujetPfeId: number;
  nom: string;
  prenom: string;
  email: string;
  messageMotivation: string;
}

export interface PfeStats {
  sujetsTotal: number;
  sujetsActifs: number;
  projetsRealises: number;
  candidaturesEnAttente: number;
}

export interface PfeMatchingRequest {
  candidatNom?: string;
  competences: string[];
  domainePrefere?: string;
  niveauEtudes?: string;
}

export interface PfeMatchResult {
  sujetId: number;
  titre: string;
  domaine: string;
  technologie: string;
  entreprise: string;
  scoreMatch: number;
  statutMatch: 'EXCELLENT' | 'BON' | 'MOYEN';
  competencesMatchees: string[];
  competencesManquantes: string[];
  recommandation: string;
}

export interface PfeMatchingResponse {
  candidatNom: string;
  meilleurScore: number;
  totalSujetsAnalyses: number;
  resultats: PfeMatchResult[];
}

@Injectable({
  providedIn: 'root'
})
export class PfeService {
  private baseUrl = `${environment.apiUrl}/pfe`;

  constructor(private http: HttpClient) {}

  getAllSujets(): Observable<SujetPfe[]> {
    return this.http.get<SujetPfe[]>(`${this.baseUrl}/sujets`);
  }

  createSujet(sujet: SujetPfe): Observable<SujetPfe> {
    return this.http.post<SujetPfe>(`${this.baseUrl}/sujets`, sujet);
  }

  updateSujet(id: number, sujet: SujetPfe): Observable<SujetPfe> {
    return this.http.put<SujetPfe>(`${this.baseUrl}/sujets/${id}`, sujet);
  }

  deleteSujet(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/sujets/${id}`);
  }

  calculateMatching(payload: PfeMatchingRequest): Observable<PfeMatchingResponse> {
    return this.http.post<PfeMatchingResponse>(`${this.baseUrl}/sujets/match`, payload);
  }

  getAllProjets(): Observable<ProjetRealise[]> {
    return this.http.get<ProjetRealise[]>(`${this.baseUrl}/projets`);
  }

  createProjet(projet: ProjetRealise): Observable<ProjetRealise> {
    return this.http.post<ProjetRealise>(`${this.baseUrl}/projets`, projet);
  }

  updateProjet(id: number, projet: ProjetRealise): Observable<ProjetRealise> {
    return this.http.put<ProjetRealise>(`${this.baseUrl}/projets/${id}`, projet);
  }

  deleteProjet(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/projets/${id}`);
  }

  getAllCandidatures(): Observable<Candidature[]> {
    return this.http.get<Candidature[]>(`${this.baseUrl}/candidatures`);
  }

  getCandidaturesByCandidat(candidatId: number): Observable<Candidature[]> {
    return this.http.get<Candidature[]>(`${this.baseUrl}/candidatures/candidat/${candidatId}`);
  }

  getCandidats(): Observable<CandidatSummary[]> {
    return this.http.get<CandidatSummary[]>(`${this.baseUrl}/candidats`);
  }

  createCandidature(candidature: Candidature): Observable<Candidature> {
    return this.http.post<Candidature>(`${this.baseUrl}/candidatures`, candidature);
  }

  submitCandidature(payload: CandidatureSubmit): Observable<Candidature> {
    return this.http.post<Candidature>(`${this.baseUrl}/candidatures/submit`, payload);
  }

  getStats(): Observable<PfeStats> {
    return this.http.get<PfeStats>(`${this.baseUrl}/stats`);
  }

  updateCandidatureStatus(id: number, status: string): Observable<Candidature> {
    return this.http.patch<Candidature>(`${this.baseUrl}/candidatures/${id}/status?status=${status}`, {});
  }
}
