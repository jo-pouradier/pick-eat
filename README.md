---
Date: 2025-01-23
---
![Pick'EAT banner](./eating_banner.png)
# PICK EAT

Pick EAT est une application web que nous avons développée dans le cadre de notre projet de fin d'études en école 
d'ingénieur. Elle permet aux utilisateurs de trouver facilement un restaurant correspondant à leurs préférences et à
celles de leurs amis, simplifiant ainsi l'organisation de sorties.

## Sommaire

- [A propos](#-a-propos)
- [Technologies](#-technologies)
-  [Fonctionnalités](#-fonctionnalités)
    - [Authentification](#authentification)
    - [Création d'évènements](#création-dévènements)
    - [Chat de l'évènement](#chat-de-lévènement)
    - [Vote](#vote)
    - [Ticket de caisse](#ticket-de-caisse)
- [Architecture](#-architecture)
- [Contributeurs](#-contributeurs)


## 💡 A propos 

Lors de vos sorties entre amis, il est difficile de vous accorder sur un restaurant ? Pick EAT est là pour vous aider !
Cette solution permet de créer un évènement entre amis pour vous permettre de voter en swipant à droite ou à gauche sur 
une sélection de restaurants générée selon vos préférences. Le restaurant obtenant le plus de votes sera alors choisi.
## 🛠️ Technologies

### Frontend stack
![vue js](https://img.shields.io/badge/-Vue-4fc08d?style=flat&logo=vue-dot-js&logoColor=fff)
![Typescript](https://img.shields.io/badge/-Typescript-3178c6?style=flat&logo=typescript&logoColor=fff)

### Backend stack
![SpringBoot](https://img.shields.io/badge/-SpringBoot-6db33f?style=flat&logo=spring&logoColor=fff)
![Java](https://img.shields.io/badge/-Java-007396?style=flat&logo=java&logoColor=fff)
![Python](https://img.shields.io/badge/-Python-3776ab?style=flat&logo=python&logoColor=fff)
![FastAPI](https://img.shields.io/badge/-FastAPI-009688?style=flat&logo=fastapi&logoColor=fff)
![AciveMQ](https://img.shields.io/badge/-ActiveMQ-000?style=flat&logo=apache-activemq&logoColor=fff)

### Infrastructure stack
![Docker](https://img.shields.io/badge/-Docker-2496ed?style=flat&logo=docker&logoColor=fff)
![Aws](https://img.shields.io/badge/-AWS-232f3e?style=flat&logo=amazon-aws&logoColor=fff)
![Ansible](https://img.shields.io/badge/-Ansible-ee0000?style=flat&logo=ansible&logoColor=fff)
![OAuth Github](https://img.shields.io/badge/-OAuth-000?style=flat&logo=github&logoColor=fff)
![Nginx](https://img.shields.io/badge/-Nginx-269539?style=flat&logo=nginx&logoColor=fff)


### Data/ IA stack
![Selenium](https://img.shields.io/badge/-Selenium-43b02a?style=flat&logo=selenium&logoColor=fff)
![Google Places API](https://img.shields.io/badge/-Google%20Places%20API-4285f4?style=flat&logo=google&logoColor=fff)
![Tesseract](https://img.shields.io/badge/-Tesseract-000?style=flat&logo=python&logoColor=fff)
![Transformers](https://img.shields.io/badge/-Transformers-000?style=flat&logo=huggingface&logoColor=fff)
![OpenCV](https://img.shields.io/badge/-OpenCV-5c3ee8?style=flat&logo=opencv&logoColor=fff)
![Ollama](https://img.shields.io/badge/-Ollama-000?style=flat&logo=ollama&logoColor=fff)

## 🚀 Fonctionnalités

### Authentification

Pour utiliser l'application, il est nécessaire de créer un compte. Vous pouvez vous inscrire soit avec nom, prénom, mail,
mot de passe, soit en utilisant votre compte Github. Une fois connecté l'authentification est gérée par un token JWT. Il 
est stocké sous forme de cookie sécurisé. Grâce à cela, il est ajouté automatiquement à chaque requête pour vérifier au 
niveau du serveur si l'utilisateur est bien connecté.

### Création d'évènements

Une fois authentifié, vous pouvez créer un évènement. Pour cela, il vous suffit de renseigner le nom de l'évènement, la
date, l'heure, le type de restaurant recherché (pizza, burger, asiatique, etc.) et l'endroit avec un rayon de recherche 
ou vous souhaitez chercher les restaurants. 

Une fois dans l'évènement, vous pouvez inviter des amis en leurs partageant un lien. Ils devront alors se connecter pour
pouvoir voter.
### Chat de l'évènement

Pour organiser au mieux votre sortie, un chat est disponible dans l'évènement. Vous pouvez discuter avec vos amis pour 
organiser les derniers détails techniques, mais aussi envoyer les photos de la sortie pour les partager et garder un 
souvenir de cette sortie.

### Vote 

Pour générer une liste de restaurants, nous avons créé une base de données de restaurants sur Lyon. Nous avons utilisé
l'API Google Places pour récupérer les informations des restaurants. Ajouté à cela, nous avons récupéré les avis des
restaurants sur les 2 derniers mois pour créer grâce à de l'analyse de sentiment une note de popularité. Une fois le vote
lancé les restaurants sont selection selon le type, les horaires d'ouverture et la note de popularité. Les utilisateurs 
vont alors voter en swipant à droite ou à gauche sur les restaurants. Une fois le vote terminé, le restaurant ayant le plus
de votes sera choisi.

### Ticket de caisse

Une fois le restaurant terminé, une fonctionalité permettant d'analyser le ticket de caisse est disponible. En utilisant
de l'OCR et un LLM, nous analysons le ticket de caisse pour en extraire les informations. Les utilisateurs peuvent alors
choisir ce qu'ils ont mangé. Cela permet de s'assurer que tout le monde paie bien sa part.


## 🏛️ Architecture

![Architecture](./Pick'EAT-archi.png)

##  🤝 Contributeurs

- [Pierre-louis Telep](https://github.com/Pierrelouis2)
- [Joseph Pouradier-Duteil](https://github.com/jo-pouradier)
- [Hugues Farthouat](https://github.com/HuguesFARTH)
- [Adrien Dalbeigue](https://github.com/Adri1D)
- [Hugo Marta](https://github.com/HugoMarta)