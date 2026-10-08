# 🇮🇳 Project Setup & Execution Guide — NO POVERTY Welfare Platform

This guide provides step-by-step instructions for running the complete application on Windows, Linux, or macOS.

---

## Prerequisites
- **Java**: JDK 17 or higher (tested with Java 27)
- **Maven**: 3.8+ (tested with Maven 3.10)
- **MySQL**: 8.0+ (Optional; built-in H2 runs automatically out of the box!)
- Modern web browser (Chrome, Edge, Firefox)

---

## Quick Start (1-Minute Run with Auto-Seeded Database)

The backend includes an automatic fallback and in-memory database configuration that auto-populates all demo schemes, users, categories, documents, and notifications.

### Step 1: Open Terminal in Project Directory
```powershell
cd C:\Users\Sainath\.gemini\antigravity\scratch\no-poverty\backend
```

### Step 2: Compile & Launch Spring Boot
```powershell
mvn spring-boot:run
```

Once started, open your browser:
- **Web Application Portal:** [http://localhost:8085/](http://localhost:8085/)
- **REST APIs Base:** [http://localhost:8085/api](http://localhost:8085/api)
- **Health Check:** [http://localhost:8085/api/health](http://localhost:8085/api/health)
- **H2 Console:** [http://localhost:8085/h2-console](http://localhost:8085/h2-console) (JDBC URL: `jdbc:h2:mem:no_poverty_db`, User: `sa`, Password: empty)

---

## Running with MySQL 8.0+

### Step 1: Initialize Database in MySQL
1. Make sure your local MySQL 8.0 service is running.
2. In **MySQL Workbench** or command-line:
   ```bash
   mysql -u root -p
   source ../database/schema.sql;
   source ../database/sample_data.sql;
   ```

### Step 2: Configure Environment Variables / Properties
Edit `.env` or set environment variables:
```bash
set DB_USERNAME=root
set DB_PASSWORD=your_mysql_password
```

### Step 3: Run Spring Boot with MySQL Profile
```powershell
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

---

## Running Frontend Standalone (Optional)

If you prefer to serve the frontend via a separate lightweight static server (e.g. Python, VS Code Live Server, or Nginx):

```powershell
cd C:\Users\Sainath\.gemini\antigravity\scratch\no-poverty\frontend
python -m http.server 3000
```
Open [http://localhost:3000/index.html](http://localhost:3000/index.html). The frontend's `api.js` client has CORS enabled and connects to `http://localhost:8080/api`.

---

## Pre-Configured Demo Credentials

| Role | Username / Identifier | Password | Access Rights |
|---|---|---|---|
| **Citizen (Farmer/Rural)** | `citizen@example.com` or `9876543210` | `password123` | Dashboard, Profile, PM-KISAN, PMAY-G |
| **Citizen (Student/Youth)** | `pooja.student@example.com` or `9811223344` | `password123` | Dashboard, Profile, NSP Scholarship |
| **Administrator** | `admin@nopoverty.gov.in` | `admin123` | Admin Portal, Scheme CRUD, Analytics |
| **Developer Admin** | `dev@nopoverty.gov.in` | `devadmin123` | Full system audit, Developer access |

---

## Running Automated Backend Tests

```powershell
cd C:\Users\Sainath\.gemini\antigravity\scratch\no-poverty\backend
mvn test
```
All unit tests in `EligibilityServiceTest` and `SchemeServiceTest` will execute and report test counts.
