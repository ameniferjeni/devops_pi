export interface Candidature {
  id?: number;
  sujetPfeId?: number;
  candidatId?: number;
  sujetPfe?: { id?: number; titre?: string };
  candidat?: { id?: number; nom?: string; prenom?: string };
  messageMotivation: string;
  statut?: string;
  probabiliteAcceptation?: number;
  scorePourcentage?: number;
  decisionSuggeree?: string;
  explicationMl?: string;
}
