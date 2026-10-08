# 🇮🇳 Database Architecture & Schema Specification

**Database Name:** `no_poverty_db`  
**Engine:** MySQL 8.0+ / InnoDB  
**Collation:** `utf8mb4_unicode_ci`  
**Alignment:** UN SDG 1 — No Poverty  

---

## 1. Table Catalog & Relationships

| Table Name | Primary Purpose | Key Foreign Keys |
|---|---|---|
| `roles` | Security authorities (`ROLE_USER`, `ROLE_ADMIN`, `ROLE_DEVELOPER_ADMIN`) | None |
| `users` | Citizen and administrative user accounts | None |
| `user_roles` | Many-to-many role assignments | `users(id)`, `roles(id)` |
| `profiles` | Extended socio-economic, agricultural, and demographic data | `users(id)` |
| `family_members` | Household dependents (students, elders, disability status) | `users(id)` |
| `beneficiary_categories` | 10 welfare target groups (Farmer, Student, Senior, etc.) | None |
| `schemes` | Core registry of state & central welfare programs | None |
| `scheme_categories` | Many-to-many scheme classification | `schemes(id)`, `beneficiary_categories(id)` |
| `scheme_eligibility` | Age, income ceilings, gender, and occupation requirements | `schemes(id)` |
| `scheme_documents` | Master prerequisite checklist per scheme | `schemes(id)` |
| `documents` | Citizen document readiness vault | `users(id)` |
| `saved_schemes` | User bookmarks | `users(id)`, `schemes(id)` |
| `applications` | Internal milestone tracker for citizen applications | `users(id)`, `schemes(id)` |
| `notifications` | System and application milestone alerts | `users(id)` (null for global) |
| `feedback` | Usability rating and citizen comments | `users(id)`, `schemes(id)` |
| `search_history` | Anonymous search queries | `users(id)` |
| `view_history` | Scheme view history | `users(id)`, `schemes(id)` |
| `admin_logs` | Audit trail for administrative actions | `users(id)` |

---

## 2. Key Indexes
- `idx_user_email` on `users(email)`
- `idx_user_mobile` on `users(mobile)`
- `idx_profile_income` on `profiles(annual_family_income)`
- `idx_scheme_name` on `schemes(name)`
- `idx_scheme_state` on `schemes(state_availability)`
- `idx_app_status` on `applications(status)`

---

## 3. SQL Execution Files
- `database/schema.sql`: Full DDL script creating database, 18 tables, indexes, constraints.
- `database/sample_data.sql`: DML script with authentic Indian welfare schemes and pre-configured test accounts.

