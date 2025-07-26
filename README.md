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

## 📊 ER Diagram
<img width="948" height="463" alt="Screenshot 2025-07-26 at 11 54 03 PM" src="https://github.com/user-attachments/assets/bf4ae560-cb7d-4036-972c-14393150450f" />

## 4. 📸 Screenshots

### 🏠 Home Page – Category Filtered Posts
<img width="1512" height="759" alt="Screenshot 2025-07-27 at 12 05 58 AM" src="https://github.com/user-attachments/assets/281c7f75-0434-4f5c-8a5b-bf660105fa9e" />

### 🗳 World Cup Voting Feature
<img width="1512" height="757" alt="Screenshot 2025-07-27 at 12 07 47 AM" src="https://github.com/user-attachments/assets/b110fdbe-9e71-42a8-a2f1-9316dfe6d039" />
<img width="1512" height="750" alt="Screenshot 2025-07-27 at 12 07 22 AM" src="https://github.com/user-attachments/assets/82f809df-d5c1-4c67-9a4e-c7643a7a5604" />
> Users can vote between candidates and see live results.

### 🧩 Customizable Blog Format
<img width="1505" height="756" alt="Screenshot 2025-07-27 at 12 20 22 AM" src="https://github.com/user-attachments/assets/3b889a02-7d8d-4a05-b32d-4df67757f11c" />
<img width="1506" height="758" alt="Screenshot 2025-07-27 at 12 20 42 AM" src="https://github.com/user-attachments/assets/ab527b31-8da9-445d-8fda-47a5e7b121f4" />
<img width="1412" height="746" alt="Screenshot 2025-07-27 at 12 21 26 AM" src="https://github.com/user-attachments/assets/7e52257c-c288-4d7b-818a-6e25bb1b5684" />
<img width="1238" height="738" alt="Screenshot 2025-07-27 at 12 21 44 AM" src="https://github.com/user-attachments/assets/f5048081-dd8c-4dd9-9dcb-375b6a718912" />
> Offers a flexible post creation experience with support for:
> - 🗺 Google Maps API integration for selecting post locations  
> - 🖋 Custom font selection and styling options  
> - 🖼 Media embedding (images, thumbnails)  
> - 🧱 Predefined layout blocks for structured blog formatting  

### 🔐 Role-based Access (Admin/User)
<img width="1509" height="754" alt="Screenshot 2025-07-27 at 12 24 02 AM" src="https://github.com/user-attachments/assets/4bc15cb4-d52f-4edf-918c-22a03d7924ce" />
