# UNO-Spring-API
This project consists in a REST API for the popular card game "UNO", enabling players to engage in real-time multiplayer matches.

## Features
* User registration and login with JWT authentication
* Role-based authorization with Spring Security
* Create new games with a specific number of players
* Search games with a specific number of players
* Play cards
* Draw cards from main deck
* Effect of special cards
* Current game status
* Player's games history

## Technologies
* Spring Boot 3.0
* Spring Security
* JSON Web Tokens (JWT)
* Maven
* Hibernate
* PostgreSQL
* OpenAPI
* Docker

## Getting Started
To get started with this project, you will need to have the following installed on your local machine:

* Docker


To build and run the project, follow these steps:

* Clone the repository: `git clone https://github.com/SebastianMorici/UNO-Spring-API.git`
* Navigate to the project directory: `cd UNO-Spring-API`
* Build and start docker containers: `docker-compose up`

-> The application will be available at http://localhost:8080.


-> The API documentation will be available at http://localhost:8080/swagger-ui/index.html#.
