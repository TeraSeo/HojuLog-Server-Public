# Hojulog 📝  
A secure and scalable technical blog platform built with Spring Boot and React.

## 🔗 Frontend github link
[https://github.com/TeraSeo/Hojulog-Frontend](https://github.com/TeraSeo/Hojulog-Frontend)

## 🔗 Live Site  
[https://hojulog.com](https://hojulog.com)

## 📚 Overview  
Hojulog is a community blog platform designed to help Korean residents in Australia connect and communicate. Users can log in via Google or Kakao(Korean Social Media) accounts and share blog posts across various topics such as travel, jobs, real estate, daily life, and more. The platform creates a space for sharing useful information and experiences within the Korean-Australian community.  
Built with Spring Boot and React, it ensures secure authentication via Spring Security and OAuth2, and efficient data handling with JPA. The full application is deployed on AWS using EC2 and RDS.

## 💡 Key Features  
- Social login with Google and GitHub (OAuth2)
- Full CRUD operations for blog posts
- Secure authentication and authorization (Spring Security)
- RESTful API structure
- Cloud deployment using AWS EC2 and RDS

## 🛠️ Tech Stack  

### Backend  
- Java 17  
- Spring Boot  
- Spring Security & OAuth2  
- Spring Data JPA (Hibernate)  
- MySQL (AWS RDS)  
- AWS EC2, Nginx  

### Frontend  
- React  
- Material-UI (MUI)  
- Axios  

## 🚀 Deployment  
The application is deployed on AWS:  
- Backend runs on **EC2** with **pm2**
- Database is managed on **AWS RDS (MySQL)**  
- Domain and SSL via **Route 53 + Certbot (Let's Encrypt)**
