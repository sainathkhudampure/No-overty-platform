# 🇮🇳 REST API Documentation — NO POVERTY Welfare Platform

All endpoints use standard JSON communication and adhere to the standardized response format.

---

## Standard Response Schemas

### Success Response (`200 OK`, `201 Created`)
```json
{
  "success": true,
  "message": "Human readable status message",
  "timestamp": "2026-10-07T23:30:00",
  "path": "/api/...",
  "data": { ... }
}
```

### Error Response (`400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `500 Internal Error`)
```json
{
  "success": false,
  "message": "Input validation failed / Error description",
  "timestamp": "2026-10-07T23:30:00",
  "path": "/api/...",
  "errors": [ "Error details" ]
}
```

---

## 1. Authentication APIs

### `POST /api/auth/signup`
Creates a citizen beneficiary account. Admin accounts require `adminSecretKey`.
- **Request Body:**
  ```json
  {
    "fullName": "Ramesh Kumar",
    "email": "citizen@example.com",
    "mobile": "9876543210",
    "password": "password123",
    "confirmPassword": "password123",
    "state": "Karnataka",
    "district": "Bengaluru Urban",
    "dateOfBirth": "1988-06-15",
    "preferredLanguage": "en",
    "requestedRole": "USER"
  }
  ```
- **Response:**
  Returns `AuthResponse` containing JWT Bearer Token, user details, and granted authorities.

### `POST /api/auth/login`
Authenticates citizen or administrator via email/mobile and password.
- **Request Body:**
  ```json
  {
    "identifier": "citizen@example.com",
    "password": "password123"
  }
  ```

### `GET /api/auth/me`
Returns current authenticated user details. Requires `Authorization: Bearer <token>`.

---

## 2. Schemes & Discovery APIs

### `GET /api/schemes`
Discovers and filters welfare schemes.
- **Query Parameters:**
  - `keyword` (e.g. `scholarship`, `kisan`, `pension`)
  - `category` (e.g. `student`, `farmer`, `senior_citizen`, `housing`, `employment`)
  - `state` (e.g. `Karnataka`, `Maharashtra`, `All India`)

### `GET /api/schemes/{id}`
Retrieves complete details of a scheme, including criteria, documents, official URLs, and increments the view counter.

### `POST /api/schemes/{id}/eligibility`
Evaluates eligibility for a specific scheme against submitted parameters.
- **Request Body:**
  ```json
  {
    "age": 22,
    "annualIncome": 120000,
    "gender": "MALE",
    "isStudent": true,
    "state": "Karnataka"
  }
  ```
- **Response:** Returns `status` (`GREEN`, `YELLOW`, `RED`), `matchPercentage`, `matchedCriteria`, `unmatchedCriteria`, and official disclaimer.

---

## 3. Personalized Recommendations API

### `GET /api/recommendations`
Returns scored scheme recommendations customized for the logged-in user profile and household dependents.

### `POST /api/recommendations/custom`
Returns recommendations for ad-hoc criteria without requiring profile persistence.

---

## 4. User Profile & Family Welfare APIs

### `GET /api/profile` / `PUT /api/profile`
Reads and updates personal, economic, agricultural, and educational profile fields. Computes profile completeness percentage.

### `GET /api/family` / `POST /api/family` / `DELETE /api/family/{id}`
Manages family members and household demographic dependents (Students, Seniors, Divyangjan).

---

## 5. Document Checklist & Vault APIs

### `GET /api/documents`
Lists uploaded and marked documents, along with a list of missing standard documents.

### `POST /api/documents`
Marks a document as available in the citizen's checklist.

### `POST /api/documents/upload`
Securely uploads a document file (`multipart/form-data`) with size validation.

---

## 6. Internal Application Tracker APIs

### `GET /api/applications`
Returns all tracked applications for the citizen.

### `POST /api/applications`
Adds a new application tracking record:
```
POST /api/applications?schemeId=1&applicationNumber=PMKISAN-KA-2026-99124&status=Submitted&appliedDate=2026-08-10&notes=Verified
```

---

## 7. AI Welfare Assistant API

### `POST /api/ai/ask`
Submits a citizen inquiry to the welfare assistant.
- **Request Body:**
  ```json
  {
    "question": "What support is available for small farmers?"
  }
  ```
- **Response:**
  Returns structured markdown guidance with verified official portal links.

---

## 8. Administrative Operations APIs (`ROLE_ADMIN`, `ROLE_DEVELOPER_ADMIN`)

- `GET /api/admin/analytics`: Aggregated chart data for Chart.js.
- `GET /api/admin/schemes`: All schemes (active & archived).
- `POST /api/admin/schemes`: Create new welfare scheme.
- `PUT /api/admin/schemes/{id}`: Edit scheme fields.
- `DELETE /api/admin/schemes/{id}`: Delete or archive scheme.
- `GET /api/admin/users`: All registered user accounts.
- `PUT /api/admin/users/{id}/status`: Toggle user status (`ACTIVE` / `SUSPENDED`).
- `GET /api/admin/applications`: All tracked citizen applications.
- `PUT /api/admin/applications/{id}/status`: Update milestone status.
- `POST /api/admin/notifications/broadcast`: Broadcast announcement to all users.

