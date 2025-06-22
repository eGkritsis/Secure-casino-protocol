# Secure-casino-protocol
A secure online dice game using cryptographic commitment, authentication, and encryption.

# Secure-casino-protocol

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


