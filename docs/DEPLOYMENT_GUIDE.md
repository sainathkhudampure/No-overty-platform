# 🚀 Deployment Guide — NO POVERTY Welfare Assistance Platform

This guide provides step-by-step instructions for deploying the **NO POVERTY Platform** to live cloud hosting, containers, or sharing a live public URL.

---

## 🌟 Option 1: Instant Live Public URL (Ready Right Now via Tunnel)

Your local instance is already connected to a public secure HTTPS tunnel!

* **Live Public URL:** **[https://silly-beers-smile.loca.lt](https://silly-beers-smile.loca.lt)**
* **Tunnel Verification Password (Public IP):** `136.232.15.18`

> **How it works:** When you open the URL above for the first time, localtunnel will ask for the "Tunnel Password". Enter `136.232.15.18` and click Submit. The platform is immediately live and accessible from your mobile phone, laptop, or anywhere on the internet!

---

## ☁️ Option 2: Free 1-Click Cloud Deployment on Render.com

[Render](https://render.com) provides free cloud hosting with automated HTTPS and GitHub integration.

### Steps:
1. **Push your project to GitHub:**
   ```powershell
   cd C:\Users\Sainath\.gemini\antigravity\scratch\no-poverty
   git init
   git add .
   git commit -m "Initial deploy: NO POVERTY Welfare Platform"
   git branch -M main
   git remote add origin https://github.com/<your-username>/no-poverty-platform.git
   git push -u origin main
   ```

2. **Deploy on Render:**
   * Go to [dashboard.render.com](https://dashboard.render.com/) and sign in.
   * Click **New +** &rarr; **Blueprint** (or **Web Service**).
   * Select your GitHub repository `no-poverty-platform`.
   * Render will automatically detect [`render.yaml`](file:///C:/Users/Sainath/.gemini/antigravity/scratch/no-poverty/render.yaml) and [`Dockerfile`](file:///C:/Users/Sainath/.gemini/antigravity/scratch/no-poverty/Dockerfile).
   * Click **Apply / Create Web Service**.
   * Within 3-4 minutes, your application will be live at `https://no-poverty-platform.onrender.com`!

---

## 🚂 Option 3: Free Cloud Deployment on Railway.app

1. Go to [railway.app](https://railway.app/) and sign in with GitHub.
2. Click **New Project** &rarr; **Deploy from GitHub repo**.
3. Select your `no-poverty-platform` repository.
4. Click **Add Variables**:
   * `PORT`: `8085`
   * `SPRING_PROFILES_ACTIVE`: `default`
5. Railway builds the Docker container and assigns an instant custom domain (e.g., `https://no-poverty-production.up.railway.app`).

---

## 🐳 Option 4: Production Deployment with Docker & Docker Compose

If deploying on a Linux VPS (Ubuntu/Debian, AWS EC2, DigitalOcean, or Azure VM):

1. **Install Docker and Docker Compose on your server:**
   ```bash
   sudo apt-get update && sudo apt-get install -y docker.io docker-compose
   ```

2. **Clone the repository:**
   ```bash
   git clone https://github.com/<your-username>/no-poverty-platform.git
   cd no-poverty-platform
   ```

3. **Start the multi-container stack (Spring Boot + MySQL 8.0):**
   ```bash
   docker-compose up -d --build
   ```

4. **Verify running containers:**
   ```bash
   docker-compose ps
   ```
   * Spring Boot runs on port 8085.
   * MySQL 8.0 runs on port 3306 with auto-imported schemas.
   * Health checks ensure zero-downtime startup.

---

## 🖥️ Option 5: Local Production Run (Windows / Standalone Server)

To run the application standalone as a persistent server:
```powershell
cd C:\Users\Sainath\.gemini\antigravity\scratch\no-poverty
.\START_PLATFORM.bat
```
To stop the server anytime:
```powershell
.\STOP_PLATFORM.bat
```

