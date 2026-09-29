from pydantic import BaseModel


class CandidaturePredictRequest(BaseModel):
    """
    Données envoyées par pfe-service pour prédire le score d'une candidature.
    """
    candidature_id: int
    message_motivation: str          # texte du message de motivation
    domaine: str                     # domaine du sujet PFE (ex: "WEB", "CYBER")
    technologie: str                 # techno demandée (ex: "React", "Spring Boot")
    entreprise: str                  # nom de l'entreprise proposant le sujet
    sujet_actif: bool                # le sujet est-il encore actif ?
    nb_candidatures_candidat: int    # nb total de candidatures déjà soumises par ce candidat
    nb_candidatures_sujet: int       # nb de candidatures pour ce même sujet (compétition)


class CandidaturePredictResponse(BaseModel):
    """
    Réponse renvoyée au pfe-service.
    """
    candidature_id: int
    probabilite_acceptation: float   # entre 0.0 et 1.0
    score_pourcentage: int           # arrondi ex: 78
    decision_suggeree: str           # "ACCEPTE" | "REFUSE" | "A_EXAMINER"
    explication: str                 # phrase lisible pour l'admin


class TrainingResponse(BaseModel):
    message: str
    nb_samples: int
    accuracy: float
    model_saved: bool
