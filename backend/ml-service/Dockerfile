FROM python:3.11-slim

WORKDIR /app

# Dépendances système pour pymysql / cryptography
RUN apt-get update && apt-get install -y --no-install-recommends \
    gcc \
    default-libmysqlclient-dev \
    && rm -rf /var/lib/apt/lists/*

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY app/ ./app/

# Dossier de stockage du modèle (monté en volume dans docker-compose)
RUN mkdir -p /app/model_store

EXPOSE 8084

CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8084"]
