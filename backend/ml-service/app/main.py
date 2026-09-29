from fastapi import FastAPI
from app.routers import prediction
from app.routers import training

app = FastAPI(
    title="CodingFactory ML Service",
    description="Prédiction d'acceptation de candidatures PFE via XGBoost",
    version="1.0.0"
)

app.include_router(prediction.router, prefix="/api/ml", tags=["Prediction"])
app.include_router(training.router,   prefix="/api/ml", tags=["Training"])


@app.get("/health")
def health():
    return {"status": "UP", "service": "ml-service"}
