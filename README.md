# Secure-casino-protocol

A secure online dice game using cryptographic commitment, authentication, and encryption.

---

## How to Download and Run Secure-casino-protocol

### Prerequisites

- Git installed (`git --version` to check)
- Java JDK installed (compatible with Spring Boot app)
- Maven installed (if using Maven wrapper or global Maven)
- Nginx installed and configured (optional, for HTTPS redirection and SSL certificates)
- OpenSSL installed (optional, for SSL testing and certificate creation)

---

## OpenSSL: Creating CA, CSR, and SSL Certificate

Follow these steps to create your SSL certificates using OpenSSL:

1. **Create Private Key**

   ```bash
   openssl genrsa -out domain.key
   ```

2. **Create Certificate Signing Request (CSR)**

   ```bash
   openssl req -key domain.key -new -out domain.csr
   ```

   During the prompt, use the following settings:

   ```
   Country Name (2 letter code) [AU]: GR
   State or Province Name (full name) [Some-State]: Attica
   Locality Name (eg, city) []: Athens
   Organization Name (eg, company) [Internet Widgits Pty Ltd]: aueb-casino
   Organizational Unit Name (eg, section) []: cybersecurity
   Common Name (e.g. server FQDN or YOUR name) []: localhost
   Email Address []: example@aueb.gr
   ```

3. **Create Root CA (Certificate Authority)**

   ```bash
   openssl req -x509 -sha256 -days 1825 -newkey rsa:2048 -keyout rootCA.key -out rootCA.crt
   ```

   Use pass phrase: `aueb123`

4. **Add CA Certificate to Trusted Certificates**

   ```bash
   sudo apt install -y ca-certificates
   sudo cp rootCA.crt /usr/local/share/ca-certificates
   sudo update-ca-certificates
   ```

5. **Create Configuration File (`domain.ext`)**

   ```text
   authorityKeyIdentifier=keyid,issuer
   basicConstraints=CA:FALSE
   subjectAltName = @alt_names
   [alt_names]
   DNS.1 = localhost
   ```

6. **Sign CSR with Root CA**

   ```bash
   openssl x509 -req -CA rootCA.crt -CAkey rootCA.key -in domain.csr -out domain.crt -days 365 -CAcreateserial -extfile domain.ext
   ```

7. **Verify Certificate**

   ```bash
   openssl x509 -text -noout -in domain.crt
   ```

8. **Install Nginx**

   ```bash
   sudo apt install nginx
   ```

9. **Move Certificates to Nginx Directory**

   ```bash
   sudo mkdir -p /etc/nginx/ssl
   sudo cp domain.crt domain.key rootCA.crt /etc/nginx/ssl/
   ```

10. **Create Full Chain Certificate**

    ```bash
    sudo sh -c 'cat /etc/nginx/ssl/domain.crt /etc/nginx/ssl/rootCA.crt > /etc/nginx/ssl/full_chain.crt'
    ```

11. **Change Permissions**

    ```bash
    sudo chmod 644 /etc/nginx/ssl/*
    ```

12. **Test and Reload Nginx**

    ```bash
    sudo nginx -t && sudo systemctl reload nginx
    ```

13. **Configure HTTPS and HTTP to HTTPS Redirection**

    Add the following to `/etc/nginx/sites-available/default`:

    ```nginx
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

    server {
        listen 443 ssl http2;
        listen [::]:443 ssl http2;
        server_name localhost;

        ssl_certificate /etc/nginx/ssl/full_chain.crt;
        ssl_certificate_key /etc/nginx/ssl/domain.key;
        ssl_trusted_certificate /etc/nginx/ssl/rootCA.crt;

        ssl_protocols TLSv1.2 TLSv1.3;
        ssl_ciphers HIGH:!aNULL:!MD5;
        ssl_prefer_server_ciphers on;

        root /var/www/html;
        index index.html index.htm index.nginx-debian.html;

        location / {
            try_files $uri $uri/ =404;
        }
    }
    ```

14. **Test and Reload Nginx Again**

    ```bash
    sudo nginx -t && sudo systemctl reload nginx
    ```

15. **Import `rootCA.crt` into Your Browser**

16. **Verify Certificate Chain**

    ```bash
    openssl s_client -connect localhost:443 -CAfile /etc/nginx/ssl/rootCA.crt
    ```

    Access `http://localhost` to confirm it redirects to HTTPS with a valid SSL chain.

---

## Authentication and Database Setup

1. Install PostgreSQL

   ```bash
   sudo apt install postgresql postgresql-contrib
   ```

2. Start and enable PostgreSQL service

   ```bash
   sudo systemctl start postgresql
   sudo systemctl enable postgresql
   ```

3. Access PostgreSQL shell as `postgres` user

   ```bash
   sudo -u postgres psql
   ```

4. Change password for `postgres`

   ```sql
   ALTER USER postgres WITH PASSWORD 'password';
   ```

5. Create `GDPR` database

   ```sql
   CREATE DATABASE "GDPR";
   ```

6. Connect to `GDPR` database

   ```sql
   \c "GDPR";
   ```

7. Create `users` table

   ```sql
   CREATE TABLE users (
       id SERIAL PRIMARY KEY,
       first_name VARCHAR(100) NOT NULL,
       last_name VARCHAR(100) NOT NULL,
       username VARCHAR(100) UNIQUE NOT NULL,
       password VARCHAR(255) NOT NULL
   );
   ```

8. Insert admin user

   ```sql
   INSERT INTO users (first_name, last_name, username, password) VALUES
   ('Admin', 'Admin', 'admin', '$2a$12$4uWoFJokQGVs8sEhxY3GQ.Fetg/PuU1jKnk6.bcLfRPekEnk66atK');
   ```

9. Insert another user

   ```sql
   INSERT INTO users (first_name, last_name, username, password) VALUES
   ('F3312306', 'F3312409', 'F3312306F3312409', '$2a$12$Bc7kocAYSOfzMK.86SSV1u4SnDrJZcH509cfkqWYFrlzCUYIfkrbS');
   ```

10. Exit psql

    ```sql
    \q
    ```

11. Restart PostgreSQL and test connection

    ```bash
    sudo systemctl restart postgresql
    psql -U postgres -d GDPR -h localhost -W
    ```

    Password: `password`

---

## Running the Project

1. Clone the repository

   ```bash
   git clone https://github.com/eGkritsis/Secure-casino-protocol.git
   ```

2. Enter the project directory

   ```bash
   cd Secure-casino-protocol
   ```

3. Build the project

   ```bash
   ./mvnw clean package
   ```

4. Run the jar (replace with actual jar filename)

   ```bash
   java -jar target/demo-0.0.1-SNAPSHOT.jar
   ```

5. Access API at:

   ```
   https://localhost/api/auth/login
   ```

---