#!/bin/bash

# Démarrer Ollama en arrière-plan
nohup ollama serve &

# Attendre qu'Ollama soit prêt
sleep 5

# Exécuter le script principal
python3 /app/app/main.py
