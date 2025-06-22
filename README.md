# Secure-casino-protocol
A secure online dice game using cryptographic commitment, authentication, and encryption.

## How to Download and Run Secure-casino-protocol

### Prerequisites
- Git installed (`git --version` to check)
- Java JDK installed (compatible with the Spring Boot app)
- Maven installed (if using Maven wrapper or global Maven)
- Nginx installed and configured (optional, for https redirection and SSL certificates)
- OpenSSL installed (optional, for SSL testing)

---

### Step 1: Clone the repository

```bash
git clone https://github.com/eGkritsis/Secure-casino-protocol.git
cd Secure-casino-protocol
```

### Step 2: Build the project
Using maven wrapper:
```bash
./mvnw clean package
```

### Step 3: Database Setup (PostgreSQL)
To set up the database required for the secure casino protocol:

### 1. Install PostgreSQL

On Ubuntu:

```bash
sudo apt update
sudo apt install postgresql postgresql-contrib
```

Start PostgreSQL Service
```bash
sudo systemctl start postgresql
sudo systemctl enable postgresql
```

Login to PostgreSQL
```bash
sudo -u postgres psql
```

Create the Database and Table
```bash
-- Create database
CREATE DATABASE GDPR;

-- Connect to GDPR database
\c GDPR;

-- Create users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);
```
Insert Default Users

```bash
-- Insert admin user
INSERT INTO users (first_name, last_name, username, password)
VALUES ('Admin', 'Admin', 'admin', '$2a$12$4uWoFJokQGVs8sEhxY3GQ.FEtg/PuU1jKnk6.bcLfRPekEnk66atK');

-- Insert second user
INSERT INTO users (first_name, last_name, username, password)
VALUES ('F3312306', 'F3312409', 'F3312306F3312409',
'$2a$12$Bc7kocAYSOfzMK.86SSV1u4SnDrJZcH509cfkqWYFrlzCUYIfkrbS');
```

### Step 4: Run the application
Replace the jar file with the actual jar filename.
```bash
java -jar target/secure-casino-protocol-0.0.1-SNAPSHOT.jar
```

### NGINX Configuration with HTTPS & SSL
This project uses **NGINX** as a reverse proxy to serve the Spring Boot application securely over **HTTPS** using **OpenSSL-generated certificates**.

---
Install NGINX

```bash
sudo apt update
sudo apt install nginx
``` 

Move Certificates to NGINX Directory:
```bash
sudo mkdir -p /etc/nginx/ssl
sudo cp domain.crt domain.key rootCA.crt /etc/nginx/ssl/
```

Create a full-chain certificate (for client compatibility):
```bash
cat /etc/nginx/ssl/domain.crt /etc/nginx/ssl/rootCA.crt > /etc/nginx/ssl/full_chain.crt
sudo chmod 644 /etc/nginx/ssl/*
```
Configure NGINX to Proxy Spring Boot with HTTPS

```bash
sudo nano /etc/nginx/sites-available/default
```

Replace its content with:
```bash
server {
    listen 80;
    server_name localhost;

    return 301 https://$host$request_uri;
}

server {
    listen 443 ssl;
    server_name localhost;

    ssl_certificate /etc/nginx/ssl/full_chain.crt;
    ssl_certificate_key /etc/nginx/ssl/domain.key;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```
Configure NGINX to serve the proper SSL Certficates:
```bash
# Server HTTPS with SSL
server {
    listen 443 ssl http2;
    listen [::]:443 ssl http2;
    server_name localhost;

    # SSL Certificates
    ssl_certificate /etc/nginx/ssl/full_chain.crt;
    ssl_certificate_key /etc/nginx/ssl/domain.key;
    ssl_trusted_certificate /etc/nginx/ssl/rootCA.crt;

    # Secure SSL settings
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
    ssl_prefer_server_ciphers on;

    # Root directory and default index file
    root /var/www/html;
    index index.html index.htm index.nginx-debian.html;

    location / {
        try_files $uri $uri/ =404;
    }
}
```


Restart NGINX:
```bash
sudo nginx -t && sudo systemctl reload nginx
```

Verify SSL & Redirect:
```bash
openssl s_client -connect localhost:443 -CAfile /etc/nginx/ssl/rootCA.crt
```


