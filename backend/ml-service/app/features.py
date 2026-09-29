"""
feature_engineering.py
Transforme les données brutes d'une candidature en vecteur numérique
utilisable par XGBoost.
"""
import re
import numpy as np
import pandas as pd

# Mots-clés positifs dans les messages de motivation (FR + EN)
POSITIVE_KEYWORDS = [
    "passion", "motivé", "motivée", "expérience", "compétence",
    "maîtrise", "enthousiaste", "professionnel", "sérieux", "rigoureux",
    "curieux", "apprendre", "innover", "contribuer", "développer",
    "autonome", "créatif", "dynamique", "engagé", "formation",
    "stage", "projet", "réaliser", "objectif", "ambition",
    "excellent", "fort", "solide", "avancé", "expert"
]

# Domaines populaires → score de demande élevé
DOMAIN_POPULARITY = {
    "intelligence artificielle": 5,
    "data science": 5,
    "machine learning": 5,
    "cybersécurité": 4,
    "developpement web": 4,
    "développement web": 4,
    "mobile": 4,
    "cloud": 4,
    "devops": 3,
    "iot": 3,
    "blockchain": 3,
    "erp": 2,
    "reseau": 2,
    "réseau": 2,
    "autre": 1,
}

# Technologies très demandées → plus de compétition → légèrement plus dur
TECH_DEMAND = {
    "react": 5, "angular": 5, "spring boot": 5, "python": 5,
    "docker": 4, "kubernetes": 4, "tensorflow": 4, "pytorch": 4,
    "vue": 4, "node": 3, "flutter": 3, "django": 3,
    "java": 3, "php": 2, "laravel": 2, "mysql": 2,
    "c#": 2, ".net": 2, "unity": 2,
}


def _count_positive_keywords(text: str) -> int:
    text_lower = text.lower()
    return sum(1 for kw in POSITIVE_KEYWORDS if kw in text_lower)


def _domain_score(domaine: str) -> int:
    return DOMAIN_POPULARITY.get(domaine.lower().strip(), 1)


def _tech_demand_score(technologie: str) -> int:
    tech_lower = technologie.lower().strip()
    for key, val in TECH_DEMAND.items():
        if key in tech_lower:
            return val
    return 1


def extract_features(data: dict) -> pd.DataFrame:
    """
    Extrait les features numériques à partir d'un dict de candidature.
    Renvoie un DataFrame d'une ligne compatible avec le modèle XGBoost.
    """
    message = data.get("message_motivation", "")
    words = message.split()

    features = {
        # Features sur le message de motivation
        "msg_nb_mots": len(words),
        "msg_nb_phrases": len(re.split(r'[.!?]+', message)),
        "msg_nb_mots_positifs": _count_positive_keywords(message),
        "msg_ratio_positifs": (
            _count_positive_keywords(message) / max(len(words), 1)
        ),
        "msg_longueur_bin": min(len(words) // 50, 5),  # 0-5 : tranche de 50 mots

        # Features sur le sujet PFE
        "domaine_popularite": _domain_score(data.get("domaine", "")),
        "tech_demande": _tech_demand_score(data.get("technologie", "")),
        "sujet_actif": int(data.get("sujet_actif", True)),

        # Features concurrentielles
        "nb_candidatures_sujet": min(data.get("nb_candidatures_sujet", 0), 20),
        "nb_candidatures_candidat": min(data.get("nb_candidatures_candidat", 0), 10),
    }

    return pd.DataFrame([features])


def get_feature_names() -> list[str]:
    return [
        "msg_nb_mots", "msg_nb_phrases", "msg_nb_mots_positifs",
        "msg_ratio_positifs", "msg_longueur_bin",
        "domaine_popularite", "tech_demande", "sujet_actif",
        "nb_candidatures_sujet", "nb_candidatures_candidat",
    ]
