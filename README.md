# 🇮🇳 NO POVERTY — Smart Government Schemes Finder & Welfare Assistance Platform

> **Aligned with United Nations Sustainable Development Goal 1: NO POVERTY**  
> *"End poverty in all its forms everywhere by implementing nationally appropriate social protection systems and measures for all."*

---

## 🌟 Executive Summary

**NO POVERTY** is a full-stack digital public-service platform that bridges the gap between marginalized Indian citizens and life-changing government welfare entitlements.

The platform provides an intelligent, accessible, and transparent bridge to discover verified welfare programs, check eligibility with transparent criteria, receive personalized recommendations, manage document preparedness, and navigate securely to official Government of India portals (`.gov.in` / `.nic.in`).

### Target Beneficiaries:
- 🎓 **Students & Youth** (Post-Matric Scholarships, tuition fee waivers)
- 🌾 **Farmers & Agricultural Laborers** (PM-KISAN, crop insurance, equipment credit)
- 👴 **Senior Citizens** (Old age pensions, social security, elder care)
- 💼 **Job Seekers & Street Vendors** (PM SVANidhi, MGNREGA, skill training)
- 🏠 **Homeless & Low-Income Families** (PMAY-G rural housing grants)
- 🏥 **Vulnerable Households** (Ayushman Bharat ₹5 Lakh cashless health cover)
- 👩‍👧 **Women & Children** (Maternity DBT, Beti Bachao Beti Padhao, Sukanya Samriddhi)
- ♿ **Persons with Benchmark Disabilities** (ADIP free assistive aids, Divyangjan pensions)

---

## 🏗️ Architecture & Technology Stack

```
NO POVERTY PLATFORM
├── Frontend (HTML5 / Bootstrap 5 / Vanilla JS / Chart.js / Font Awesome)
│   ├── Responsive SaaS Design System (Deep Navy, White, Saffron Accents)
│   ├── i18n Multilingual Engine (English, Hindi, Kannada, Marathi)
│   ├── Web Speech Voice Recognition & SpeechSynthesis Text-to-Speech (TTS)
│   └── Offline-Capable REST API Client with JWT Bearer Auth
│
├── Backend (Java 17+ / Spring Boot 3.3.4 / Maven)
│   ├── Spring Web & RESTful JSON Controllers
│   ├── Spring Security 6 & JJWT Stateless Bearer Authentication
│   ├── BCrypt Password Hashing & Role-Based Access Control (RBAC)
│   ├── Spring Data JPA & Hibernate ORM
│   ├── Intelligent 3-Tier Eligibility & Recommendation Engines
│   └── Embedded H2 & Production MySQL 8.0 Dual-Dialect Support
│
└── Database (MySQL 8.0+ / MySQL Workbench Compatible)
    ├── 18 Relational Tables with Primary/Foreign Keys and Indexes
    ├── Verified Authentic Indian Government Schemes & Portals Seed Data
    └── User Profiles, Dependents, Documents Vault, and Milestones
```

---

## 🚀 Key Features

| Feature | Description |
|---|---|
| **Smart Scheme Finder** | Multifaceted search across keyword, beneficiary category, and state with sorting. |
| **Eligibility Wizard** | 7-step interactive wizard delivering **GREEN** (Likely eligible), **YELLOW** (Verification needed), and **RED** (Ineligible) determinations with exact criteria matches and disclaimers. |
| **Recommendation Engine** | Relevance scoring algorithm factoring in family size, dependents, income ceilings, and agricultural landholding. |
| **Verified Portal Redirection** | Strict zero-fraud architecture: applications are processed solely on verified government portals. |
| **Internal Application Tracker** | Interactive visual milestone timeline (Draft &rarr; Submitted &rarr; Verifying &rarr; Approved &rarr; Benefit Received). |
| **Document Vault & Checklist** | Automatic checklist identifying missing standard documents (Aadhaar, Income Certificate, Khata/RoR). |
| **AI Welfare Assistant** | Conversational chatbot trained on scheme rules, benefits, and required documents with prompt chips. |
| **Voice Search & TTS** | Web Speech API voice input and browser SpeechSynthesis read-aloud functionality. |
| **Multilingual i18n** | Real dynamic translation for English, Hindi (हिन्दी), Kannada (ಕನ್ನಡ), and Marathi (मराठी). |
| **Admin Operations & Analytics** | Chart.js interactive dashboards, scheme CRUD, citizen management, and system broadcasts. |

---

## 🔑 Pre-Configured Demo Credentials

| Role | Username / Identifier | Password | Access Rights |
|---|---|---|---|
| **Citizen (Farmer/Rural)** | `citizen@example.com` or `9876543210` | `password123` | Dashboard, Profile, PM-KISAN, PMAY-G |
| **Citizen (Student/Youth)** | `pooja.student@example.com` or `9811223344` | `password123` | Dashboard, Profile, NSP Scholarship |
| **Administrator** | `admin@nopoverty.gov.in` | `admin123` | Admin Portal, Scheme CRUD, Analytics |
| **Developer Admin** | `dev@nopoverty.gov.in` | `devadmin123` | Full system audit, Developer access |

---

## 🛠️ Step-by-Step Installation & Execution

### Option A: 1-Click Launch (H2 Auto-Seeded Database)
1. Open PowerShell or Command Prompt in the backend folder:
   ```powershell
   cd C:\Users\Sainath\.gemini\antigravity\scratch\no-poverty\backend
   mvn spring-boot:run
   ```
2. Open your web browser:
   - **Web Portal:** [http://localhost:8085/](http://localhost:8085/)
   - **REST APIs:** [http://localhost:8085/api](http://localhost:8085/api)
   - **Health Check:** [http://localhost:8085/api/health](http://localhost:8085/api/health)
   - **H2 Console:** [http://localhost:8085/h2-console](http://localhost:8085/h2-console)

### Option B: Run with MySQL 8.0+
1. Import database scripts into MySQL Server / MySQL Workbench:
   ```bash
   mysql -u root -p
   source database/schema.sql;
   source database/sample_data.sql;
   ```
2. Run Spring Boot with MySQL profile:
   ```powershell
   cd backend
   mvn spring-boot:run -Dspring-boot.run.profiles=mysql
   ```

---

## 🧪 Automated Testing
Backend tests validate authentication, scheme search queries, and eligibility determinations:
```powershell
cd backend
mvn test
```
All tests pass cleanly with 0 failures and 0 errors.

---

## 📄 Project Folder Hierarchy

```
no-poverty/
├── frontend/
│   ├── assets/
│   │   ├── css/ (styles.css)
│   │   └── js/ (api.js, app.js, auth.js, scheme.js, eligibility.js, ...)
│   ├── i18n/ (en.json, hi.json, kn.json, mr.json)
│   ├── index.html, signin.html, signup.html, dashboard.html, ...
│   ├── admin-dashboard.html, admin-schemes.html, admin-analytics.html, ...
│   └── 404.html, 403.html, 500.html
├── backend/
│   ├── pom.xml
│   ├── src/main/java/com/nopoverty/platform/
│   │   ├── config/ (SecurityConfig, JwtTokenProvider, DataInitializer, ...)
│   │   ├── controller/ (AuthController, SchemeController, AdminController, ...)
│   │   ├── dto/ (ApiResponse, AuthRequest, EligibilityRequest, ...)
│   │   ├── model/ (User, Scheme, UserProfile, ApplicationRecord, ...)
│   │   ├── repository/ (UserRepository, SchemeRepository, ...)
│   │   └── service/ (SchemeService, EligibilityService, AnalyticsService, ...)
│   └── src/test/ (EligibilityServiceTest, SchemeServiceTest)
├── database/
│   ├── schema.sql (18 MySQL tables, constraints, indexes)
│   ├── sample_data.sql (Authentic Indian schemes, criteria, demo users)
│   └── README.md
├── docs/
│   ├── API.md (Exhaustive REST API documentation)
│   ├── DATABASE.md (Schema specifications)
│   └── PROJECT_SETUP.md (Quick start guide)
├── .env.example
├── .gitignore
└── README.md
```

---

## ⚖️ Source Attribution & Disclaimers
* All scheme descriptions, eligibility parameters, and portal URLs are modeled after official programs of the Government of India (Ministry of Agriculture, Ministry of Social Justice, Ministry of Rural Development, Ministry of Housing & Urban Affairs, and National Health Authority).
* This platform is an informational guidance tool. It does not issue or approve government benefits. All applications are completed directly by citizens on official government portals.
