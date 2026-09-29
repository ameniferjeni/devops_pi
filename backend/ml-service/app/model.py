"""
model.py
Chargement / sauvegarde du modèle XGBoost.
Le modèle est entraîné via /api/ml/train et sauvegardé dans /app/model_store/.
"""
import os
import joblib
import numpy as np
import xgboost as xgb
from pathlib import Path

MODEL_DIR  = Path("/app/model_store")
MODEL_PATH = MODEL_DIR / "xgb_candidature.pkl"


def load_model() -> xgb.XGBClassifier | None:
    """Charge le modèle depuis le disque. Retourne None s'il n'existe pas encore."""
    if MODEL_PATH.exists():
        return joblib.load(MODEL_PATH)
    return None


def save_model(model: xgb.XGBClassifier) -> None:
    MODEL_DIR.mkdir(parents=True, exist_ok=True)
    joblib.dump(model, MODEL_PATH)


def build_model() -> xgb.XGBClassifier:
    """Crée un nouveau classifieur XGBoost avec des hyperparamètres raisonnables."""
    return xgb.XGBClassifier(
        n_estimators=200,
        max_depth=4,
        learning_rate=0.05,
        subsample=0.8,
        colsample_bytree=0.8,
        use_label_encoder=False,
        eval_metric="logloss",
        random_state=42,
    )


def predict_proba(model: xgb.XGBClassifier, X) -> float:
    """Retourne la probabilité d'acceptation (classe 1)."""
    proba = model.predict_proba(X)
    return float(proba[0][1])


def decision_label(proba: float) -> str:
    if proba >= 0.70:
        return "ACCEPTE"
    elif proba <= 0.35:
        return "REFUSE"
    else:
        return "A_EXAMINER"


def explication(proba: float) -> str:
    pct = int(round(proba * 100))
    if proba >= 0.70:
        return f"Le profil est très bien aligné avec les critères du sujet ({pct}%). Candidature recommandée."
    elif proba >= 0.50:
        return f"Le profil présente des points positifs ({pct}%). Une revue manuelle est conseillée."
    elif proba >= 0.35:
        return f"Le dossier manque de détails suffisants ({pct}%). Candidature à examiner attentivement."
    else:
        return f"Le profil ne correspond pas bien aux critères ({pct}%). Candidature peu susceptible d'être retenue."
