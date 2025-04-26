# FinalProject-20-NinjaTurtles
Bazario Marketplace


This project is a scalable E-commerce system built with Spring Boot, MongoDB, Redis, and designed using Microservices architecture.

Each service handles a specific domain and communicates using OpenFeign (synchronous) and RabbitMQ (asynchronous).
The system is designed to be deployed on Kubernetes with load balancing and scaling capabilities.

📦 Tech Stack
Java 17

Spring Boot 3

MongoDB

Redis

OpenFeign (for service-to-service communication)

RabbitMQ (for asynchronous messaging)

Kubernetes (for deployment & load balancing)

Docker (for containerization)

Spring Security (for User & Auth Service)

PostgreSQL (for User, Payment, and Order services)

🧩 Microservices Overview

Microservice	Responsibilities
User Service	User management, authentication (login/logout/reset password), address management
Product & Cart Service	Product listing, filtering by category, sorting by price, cart management, save for later, checkout
Payment Service	Payment processing, applying discounts, handling refunds as points
Order Service	Order placement, cancellation, viewing and filtering order history
API Gateway	Centralized routing for all microservices
