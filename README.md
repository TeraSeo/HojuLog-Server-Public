# Hojulog 📝  
A secure and scalable technical blog platform built with Spring Boot and React.

## 🔗 Frontend Github Link
[https://github.com/TeraSeo/Hojulog-Frontend](https://github.com/TeraSeo/Hojulog-Frontend)

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
- Frontend runs on EC2 with nginx
- Database is managed on **AWS RDS (MySQL)**  
- Domain and SSL via **Route 53 + Certbot (Let's Encrypt)**

## 📊 ER Diagram
<img width="948" height="463" alt="Screenshot 2025-07-26 at 11 54 03 PM" src="https://github.com/user-attachments/assets/bf4ae560-cb7d-4036-972c-14393150450f" />

## 📸 Screenshots & Key Features

### 🏠 Home Page – Category Filtered Posts
> Browse posts by category such as society, travel, study, and more.

<details>
  <summary>📷 View Screenshot</summary>
  
  <img width="1512" height="759" alt="Home Screenshot" src="https://github.com/user-attachments/assets/281c7f75-0434-4f5c-8a5b-bf660105fa9e" />
</details>

---

### 🗳 World Cup Voting Feature
> Users can vote between candidates and see live results in a World Cup-style format.

<details>
  <summary>📷 View Screenshots</summary>
  
  <img width="1512" height="757" alt="Voting 1" src="https://github.com/user-attachments/assets/b110fdbe-9e71-42a8-a2f1-9316dfe6d039" />
  <img width="1512" height="750" alt="Voting 2" src="https://github.com/user-attachments/assets/82f809df-d5c1-4c67-9a4e-c7643a7a5604" />
</details>

---

### 🧩 Customizable Blog Format
> Offers a flexible post creation experience with:
> - 🗺 Google Maps API for selecting post locations  
> - 🖋 Custom font selection and styling options  
> - 🖼 Media embedding (images, thumbnails)  
> - 🧱 Predefined layout blocks for structured blog formatting  

<details>
  <summary>📷 View Screenshots</summary>
  
  <img width="1505" height="756" alt="Editor 1" src="https://github.com/user-attachments/assets/3b889a02-7d8d-4a05-b32d-4df67757f11c" />
  <img width="1506" height="758" alt="Editor 2" src="https://github.com/user-attachments/assets/ab527b31-8da9-445d-8fda-47a5e7b121f4" />
  <img width="1412" height="746" alt="Editor 3" src="https://github.com/user-attachments/assets/7e52257c-c288-4d7b-818a-6e25bb1b5684" />
  <img width="1238" height="738" alt="Editor 4" src="https://github.com/user-attachments/assets/f5048081-dd8c-4dd9-9dcb-375b6a718912" />
</details>

---

### 🔐 Role-based Access (Admin/User)
> Provides role-based login and access control using Spring Security.

<details>
  <summary>📷 View Screenshot</summary>

  <img width="1509" height="754" alt="Login Screenshot" src="https://github.com/user-attachments/assets/4bc15cb4-d52f-4edf-918c-22a03d7924ce" />
</details>
