export interface ChatbotResponse {
  intention: string;
  intentionLabel: string;
  reponse: string;
  serviceNom?: string;
  serviceDescription?: string;
  consultantNom?: string;
  consultantRole?: string;
  consultantEmail?: string;
  consultantPhone?: string;
  consultantAvatar?: string;
  suggestions?: string[];
  actionType?: string;
  // Backwards compatibility
  service?: string;
  consultant?: string;
}

export interface ChatbotServiceInfo {
  id: number;
  nom: string;
  description: string;
  consultantsCount: number;
  expertNom?: string;
  expertRole?: string;
  expertEmail?: string;
  expertPhone?: string;
  technologies?: string[];
}
