# Utiliser une image de base Ubuntu
FROM ubuntu:22.04

# Installer Python, Ollama, et autres dépendances
RUN apt-get update && apt-get install -y \
    curl \
    python3 \
    python3-pip \
    libgl1-mesa-glx \
    libglib2.0-0 \
    build-essential && \
    apt-get clean

# Installer Ollama
RUN curl -fsSL https://ollama.com/install.sh | sh

# Ajouter Ollama au PATH
ENV PATH="/root/.ollama/bin:$PATH"

# Démarrer Ollama en arrière-plan pour télécharger le modèle
RUN nohup ollama serve & sleep 5 && ollama pull qwen2.5:7b

# Définir le répertoire de travail
WORKDIR /app

# Copier le script et les dépendances Python
COPY requirements.txt .
COPY app/ ./app/

# Rendre le script d'entrée exécutable
COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

# Installer les bibliothèques Python nécessaires
RUN pip3 install --no-cache-dir -r requirements.txt

# Exposer le port pour Ollama (par défaut 11434)
EXPOSE 11434

# Définir l'entrée du conteneur pour utiliser le script d'entrée
ENTRYPOINT ["/app/entrypoint.sh"]