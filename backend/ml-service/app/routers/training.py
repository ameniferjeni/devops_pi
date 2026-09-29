"""
training.py
Endpoint POST /api/ml/train
Récupère les candidatures ACCEPTE/REFUSE depuis MySQL et entraîne XGBoost.
Si pas assez de données réelles, génère des données synthétiques pour bootstrapper.
"""
import os
import numpy as np
import pandas as pd
from fastapi import APIRouter
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
from sqlalchemy import create_engine, text

from app.schemas import TrainingResponse
from app.features import extract_features, get_feature_names
from app.model import build_model, save_model

router = APIRouter()

# URL MySQL injectée via variable d'environnement (docker-compose)
DB_URL = os.getenv(
    "DATABASE_URL",
    "mysql+pymysql://root:codingfactory@mysql:3306/codingfactory"
)


def _load_from_db() -> pd.DataFrame:
    """Charge les candidatures traitées (ACCEPTE / REFUSE) depuis MySQL."""
    engine = create_engine(DB_URL)
    query = text("""
        SELECT
            c.id,
            c.message_motivation,
            c.statut,
            s.domaine,
            s.technologie,
            s.entreprise,
            s.actif,
            (SELECT COUNT(*) FROM candidature c2
             WHERE c2.candidat_id = c.candidat_id) AS nb_candidatures_candidat,
            (SELECT COUNT(*) FROM candidature c3
             WHERE c3.sujet_pfe_id = c.sujet_pfe_id) AS nb_candidatures_sujet
        FROM candidature c
        JOIN sujet_pfe s ON c.sujet_pfe_id = s.id
        WHERE c.statut IN ('ACCEPTEE', 'REFUSEE')
    """)
    with engine.connect() as conn:
        df = pd.read_sql(query, conn)
    return df


def _generate_synthetic_data(n: int = 300) -> tuple[pd.DataFrame, np.ndarray]:
    """
    Génère des données synthétiques réalistes pour bootstrapper le modèle
    quand la DB ne contient pas encore assez d'historique.
    """
    rng = np.random.default_rng(42)

    domaines = ["Développement Web", "Intelligence Artificielle",
                "Cybersécurité", "Data Science", "Mobile", "Cloud", "Réseau"]
    techs = ["React", "Angular", "Spring Boot", "Python", "Docker",
             "TensorFlow", "Flutter", "Node.js", "Django", "Java"]
    entreprises = ["TechCorp", "InnoSoft", "DataLab", "SecureNet",
                   "CloudSys", "MobileTech", "WebStudio"]

    rows = []
    labels = []

    for _ in range(n):
        nb_mots = rng.integers(20, 400)
        nb_mots_positifs = rng.integers(0, min(15, nb_mots // 10 + 1))
        domaine = rng.choice(domaines)
        tech = rng.choice(techs)
        entreprise = rng.choice(entreprises)
        actif = bool(rng.choice([True, True, True, False]))
        nb_cand_candidat = int(rng.integers(0, 8))
        nb_cand_sujet = int(rng.integers(1, 15))

        # Règle métier synthétique : accepté si message long + mots positifs
        # + domaine porteur + peu de concurrence
        score = (
            min(nb_mots / 200, 1.0) * 0.35
            + min(nb_mots_positifs / 10, 1.0) * 0.30
            + (1.0 if domaine in ["Intelligence Artificielle", "Data Science", "Cybersécurité"] else 0.4) * 0.15
            + (1.0 - min(nb_cand_sujet / 15, 1.0)) * 0.10
            + (min(nb_cand_candidat / 5, 1.0)) * 0.10
            + rng.uniform(-0.1, 0.1)  # bruit
        )
        label = 1 if score >= 0.5 else 0

        message = (
            "Je suis très motivé par ce projet. " * (nb_mots_positifs + 1)
            + "Lorem ipsum " * max(nb_mots - nb_mots_positifs * 8, 1)
        )[:500]

        rows.append({
            "message_motivation": message,
            "domaine": domaine,
            "technologie": tech,
            "entreprise": entreprise,
            "sujet_actif": actif,
            "nb_candidatures_candidat": nb_cand_candidat,
            "nb_candidatures_sujet": nb_cand_sujet,
        })
        labels.append(label)

    X = pd.concat([extract_features(r) for r in rows], ignore_index=True)
    y = np.array(labels)
    return X, y


@router.post("/train", response_model=TrainingResponse)
def train():
    """
    Entraîne le modèle XGBoost.
    Utilise les données réelles de la DB si disponibles (>=20 samples),
    sinon génère des données synthétiques pour bootstrapper.
    """
    X, y = None, None
    source = "synthétiques"

    try:
        df = _load_from_db()
        if len(df) >= 20:
            source = "base de données"
            rows = []
            for _, row in df.iterrows():
                rows.append(extract_features({
                    "message_motivation": row["message_motivation"],
                    "domaine": row["domaine"],
                    "technologie": row["technologie"],
                    "entreprise": row["entreprise"],
                    "sujet_actif": bool(row["actif"]),
                    "nb_candidatures_candidat": int(row["nb_candidatures_candidat"]),
                    "nb_candidatures_sujet": int(row["nb_candidatures_sujet"]),
                }))
            X = pd.concat(rows, ignore_index=True)
            y = np.array([1 if s == "ACCEPTEE" else 0 for s in df["statut"]])
    except Exception:
        pass  # DB inaccessible ou pas encore de données → synthétique

    if X is None or len(X) < 20:
        X, y = _generate_synthetic_data(300)

    X_train, X_test, y_train, y_test = train_test_split(
        X, y, test_size=0.2, random_state=42, stratify=y
    )

    model = build_model()
    model.fit(X_train, y_train)

    y_pred = model.predict(X_test)
    acc = float(accuracy_score(y_test, y_pred))

    save_model(model)

    return TrainingResponse(
        message=f"Modèle entraîné avec succès sur données {source}",
        nb_samples=len(X),
        accuracy=round(acc, 4),
        model_saved=True
    )
