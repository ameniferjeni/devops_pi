export interface ChatbotResponse {
  intention: string;
  intentionLabel: string;
  reponse: string;
  service: string;
  serviceDescription?: string;
  consultant: string;
  consultantEmail?: string;
}

export interface ChatbotServiceInfo {
  id: number;
  nom: string;
  description: string;
  consultantsCount: number;
}
