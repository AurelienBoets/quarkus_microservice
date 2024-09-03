# E-commerce Microservices Project

Ce projet est une application de commerce électronique construite en utilisant une architecture en microservices. Le backend est développé avec [Quarkus](https://quarkus.io/), un framework Java rapide et léger, tandis que le frontend est réalisé avec [React](https://reactjs.org/).

## Table des matières

- [Architecture](#architecture)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Démarrage](#démarrage)
- [Microservices](#microservices)
- [Déploiement](#déploiement)

  ## Architecture

L'application est composée de plusieurs microservices, chacun responsable d'une partie spécifique de l'application e-commerce :

- **Service d'authentification** : Gère l'inscription, la connexion, et la gestion des utilisateurs.
- **Service de catalogue** : Gère les produits et leurs catégories.
- **Service de commande** : Gère la création, le paiement et l'historique des commandes.
- **Frontend** : Interface utilisateur construite avec React, qui consomme les API des différents microservices.

Chaque microservice communique avec les autres via des protocole GRPC.

## Installation

### Cloner le dépôt

```bash
git clone https://github.com/AurelienBoets/quarkus_microservice.git
cd quarkus_microservice
```
### Conteneuriser le projet

```bash
docker compose up
```

## Microservices

Une api gateway expose des points d'API qui peuvent être utilisés par le frontend ou d'autres services :

- **Connexion** : `http://localhost:9000/api/user/login`
- **Register** : `http://localhost:9000/api/user/register`
- **Catalogue** : `http://localhost:8080/api/product`
- **Payement** : `http://localhost:8080/api/order/payment`
