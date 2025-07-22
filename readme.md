# Spring Boot Demo for Hostim.dev

This is a simple Spring Boot app that shows how a modern Java web app works. It handles user data, caching, file uploads, and runs inside Docker containers.

## What it does

- Lets you create and list users
- Handles avatar image uploads
- Stores data in PostgreSQL
- Caches user info using Redis
- Runs everything in Docker

## Tech used

- Java + Spring Boot 3.x
- JPA/Hibernate for database mapping
- PostgreSQL 14
- Redis 7
- Docker + Docker Compose
- Thymeleaf for HTML templates
- Maven for managing dependencies

## How it’s set up

- `model/User.java`: defines the user structure
- `service/UserService.java`: contains business logic and caching
- `controller/UserController.java`: handles HTTP routes
- `service/FileStorageService.java`: deals with file uploads
- `config/RedisCacheConfig.java`: sets up Redis
- `Dockerfile`: builds the Java app image
- `docker-compose.yml`: runs everything together

## Key features

- Uses AOP (Aspect-Oriented Programming) to track caching
- Generates unique filenames using UUIDs
- Stores all data in volumes so nothing gets lost

## How to run it

To start everything locally:

```bash
docker-compose up --build
```

This spins up:

- The Spring Boot web app
- A PostgreSQL database
- A Redis cache

It uses volumes to keep the data:

- `postgres-data`: for database
- `redis-data`: for Redis cache
- `upload-data`: for uploaded avatars

Once it's running, open [http://localhost:8080](http://localhost:8080) in your browser.

This app is also set up for easy deployment on Hostim.dev. It shows how to spin up a full stack app with a database and cache inside containers. For detailed steps on hosting this on Hostim.dev, [check out the Hostim.dev deployment guide](https://hostim.dev/docs/guides/frameworks/springboot).
