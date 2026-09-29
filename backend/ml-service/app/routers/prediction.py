from fastapi import APIRouter, HTTPException
from app.schemas import CandidaturePredictRequest, CandidaturePredictResponse
from app.features import extract_features
from app.model import load_model, predict_proba, decision_label, explication

router = APIRouter()


@router.post("/predict", response_model=CandidaturePredictResponse)
def predict(req: CandidaturePredictRequest):
    """
    Prédit la probabilité d'acceptation d'une candidature PFE.
    Appelé par pfe-service via Feign.
    """
    model = load_model()
    if model is None:
        # Le premier dépôt doit fonctionner même si personne n'a encore appelé /train.
        from app.routers.training import train
        train()
        model = load_model()

    if model is None:
        raise HTTPException(status_code=503, detail="Le modèle ML n'a pas pu être chargé.")

    X = extract_features(req.model_dump())
    proba = predict_proba(model, X)
    pct = int(round(proba * 100))

    return CandidaturePredictResponse(
        candidature_id=req.candidature_id,
        probabilite_acceptation=round(proba, 4),
        score_pourcentage=pct,
        decision_suggeree=decision_label(proba),
        explication=explication(proba)
    )
