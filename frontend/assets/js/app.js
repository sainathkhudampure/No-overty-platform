/**
 * Core Application Framework - NO POVERTY Welfare Platform
 * Manages Auth State, Navigation, Theme Switching, i18n Translations, and Notifications
 */

const App = {
  currentLang: localStorage.getItem('np_lang') || 'en',
  currentTheme: localStorage.getItem('np_theme') || 'light',
  translations: {},

  async init() {
    this.applyTheme(this.currentTheme);
    await this.loadTranslations(this.currentLang);
    this.renderHeader();
    this.renderFooter();
    this.bindGlobalEvents();
    this.checkRouteAccess();
  },

  applyTheme(theme) {
    this.currentTheme = theme;
    document.documentElement.setAttribute('data-theme', theme);
    localStorage.setItem('np_theme', theme);

    const toggleBtn = document.getElementById('theme-toggle-btn');
    if (toggleBtn) {
      toggleBtn.innerHTML = theme === 'dark' 
        ? '<i class="fa-solid fa-sun text-warning"></i>' 
        : '<i class="fa-solid fa-moon"></i>';
    }
  },

  toggleTheme() {
    this.applyTheme(this.currentTheme === 'dark' ? 'light' : 'dark');
  },

  async loadTranslations(lang) {
    this.currentLang = lang;
    localStorage.setItem('np_lang', lang);
    try {
      // Determine relative path depending on page location
      const i18nPath = window.location.pathname.includes('/pages/') 
        ? `../i18n/${lang}.json` 
        : `i18n/${lang}.json`;

      const res = await fetch(i18nPath);
      if (res.ok) {
        this.translations = await res.json();
        this.translateDOM();
      }
    } catch (e) {
      console.warn('Could not load translation file for:', lang, e);
    }
  },

  translateDOM() {
    document.querySelectorAll('[data-i18n]').forEach(el => {
      const key = el.getAttribute('data-i18n');
      if (this.translations[key]) {
        if (el.tagName === 'INPUT' && el.getAttribute('placeholder')) {
          el.placeholder = this.translations[key];
        } else {
          el.innerText = this.translations[key];
        }
      }
    });
  },

  t(key, defaultText = '') {
    return this.translations[key] || defaultText || key;
  },

  renderHeader() {
    const user = API.getCurrentUser();
    const token = API.getToken();
    const isAuthenticated = Boolean(user && token);
    const isAdmin = isAuthenticated && user.roles && (user.roles.includes('ROLE_ADMIN') || user.roles.includes('ROLE_DEVELOPER_ADMIN') || user.roles.includes('ADMIN'));

    const path = window.location.pathname;
    const isRoot = !path.includes('/pages/');
    const prefix = isRoot ? '' : ''; // all pages will sit at root or relative path

    const navContainer = document.getElementById('app-navbar');
    if (!navContainer) return;

    navContainer.innerHTML = `
      <!-- Top National Banner -->
      <div class="top-gov-ribbon">
        <div class="container-custom">
          <div>
            <i class="fa-solid fa-flag text-danger me-1"></i>
            <span>SDG 1 — NO POVERTY Welfare Assistance Initiative</span>
          </div>
          <div class="d-flex align-items-center gap-3">
            <span class="d-none d-md-inline text-light">Official Portal Guidance System</span>
            <div class="dropdown">
              <select id="lang-selector" class="form-select form-select-sm bg-dark text-white border-secondary py-0" style="font-size: 0.75rem;">
                <option value="en" ${this.currentLang === 'en' ? 'selected' : ''}>English</option>
                <option value="hi" ${this.currentLang === 'hi' ? 'selected' : ''}>हिन्दी (Hindi)</option>
                <option value="kn" ${this.currentLang === 'kn' ? 'selected' : ''}>ಕನ್ನಡ (Kannada)</option>
                <option value="mr" ${this.currentLang === 'mr' ? 'selected' : ''}>मराठी (Marathi)</option>
              </select>
            </div>
            <button id="theme-toggle-btn" class="btn btn-sm btn-link text-white p-0" title="Toggle Theme">
              ${this.currentTheme === 'dark' ? '<i class="fa-solid fa-sun text-warning"></i>' : '<i class="fa-solid fa-moon"></i>'}
            </button>
          </div>
        </div>
      </div>

      <!-- Main Navbar -->
      <nav class="navbar navbar-expand-lg main-navbar py-2">
        <div class="container-custom d-flex justify-content-between align-items-center w-100">
          <a class="navbar-brand navbar-brand-group" href="index.html">
            <div class="brand-emblem">
              <i class="fa-solid fa-hands-holding-child"></i>
            </div>
            <div>
              <div class="brand-text-title">
                NO POVERTY <span class="badge bg-danger rounded-pill" style="font-size: 0.65rem;">SDG 1</span>
              </div>
              <div class="brand-text-subtitle">Smart Welfare & Schemes Finder</div>
            </div>
          </a>

          <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#navContent">
            <i class="fa-solid fa-bars fs-4"></i>
          </button>

          <div class="collapse navbar-collapse" id="navContent">
            <ul class="navbar-nav ms-auto align-items-lg-center gap-1">
              <li class="nav-item">
                <a class="nav-link-custom ${path.includes('scheme-finder') ? 'active' : ''}" href="scheme-finder.html">
                  <i class="fa-solid fa-magnifying-glass text-primary"></i> <span data-i18n="nav_find_schemes">Find Schemes</span>
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link-custom ${path.includes('eligibility') ? 'active' : ''}" href="eligibility.html">
                  <i class="fa-solid fa-clipboard-check text-success"></i> <span data-i18n="nav_check_eligibility">Check Eligibility</span>
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link-custom ${path.includes('ai-assistant') ? 'active' : ''}" href="ai-assistant.html">
                  <i class="fa-solid fa-robot text-info"></i> <span data-i18n="nav_ai_assistant">AI Assistant</span>
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link-custom ${path.includes('voice-search') ? 'active' : ''}" href="voice-search.html">
                  <i class="fa-solid fa-microphone text-danger"></i> <span>Voice Search</span>
                </a>
              </li>

              ${isAuthenticated ? `
                <li class="nav-item dropdown">
                  <a class="nav-link-custom dropdown-toggle" href="#" id="servicesDropdown" role="button" data-bs-toggle="dropdown">
                    <i class="fa-solid fa-grid-2"></i> Services
                  </a>
                  <ul class="dropdown-menu dropdown-menu-end shadow-sm border-0">
                    <li><a class="dropdown-item" href="dashboard.html"><i class="fa-solid fa-gauge me-2 text-primary"></i> Dashboard</a></li>
                    <li><a class="dropdown-item" href="recommendations.html"><i class="fa-solid fa-star me-2 text-warning"></i> Recommendations</a></li>
                    <li><a class="dropdown-item" href="saved-schemes.html"><i class="fa-solid fa-bookmark me-2 text-danger"></i> Saved Schemes</a></li>
                    <li><a class="dropdown-item" href="documents.html"><i class="fa-solid fa-folder-open me-2 text-info"></i> Document Checklist</a></li>
                    <li><a class="dropdown-item" href="application-status.html"><i class="fa-solid fa-clock-rotate-left me-2 text-success"></i> Application Tracker</a></li>
                    <li><a class="dropdown-item" href="family.html"><i class="fa-solid fa-people-roof me-2 text-secondary"></i> Family Welfare</a></li>
                    <li><hr class="dropdown-divider"></li>
                    <li><a class="dropdown-item" href="profile.html"><i class="fa-solid fa-user-pen me-2"></i> Edit Profile</a></li>
                  </ul>
                </li>
                ${isAdmin ? `
                  <li class="nav-item">
                    <a class="btn btn-sm btn-outline-danger ms-lg-2 px-3 fw-bold" href="admin-dashboard.html">
                      <i class="fa-solid fa-shield-halved me-1"></i> Admin Portal
                    </a>
                  </li>
                ` : ''}
                <li class="nav-item ms-lg-2">
                  <a href="dashboard.html" class="btn btn-sm btn-light border fw-semibold d-flex align-items-center gap-1">
                    <i class="fa-solid fa-user-circle text-primary"></i> ${user.fullName ? user.fullName.split(' ')[0] : 'Profile'}
                  </a>
                </li>
                <li class="nav-item">
                  <button class="btn btn-sm btn-outline-secondary ms-1" onclick="App.logout()">
                    <i class="fa-solid fa-arrow-right-from-bracket"></i>
                  </button>
                </li>
              ` : `
                <li class="nav-item ms-lg-2">
                  <a class="btn btn-sm btn-outline-primary px-3" href="signin.html">
                    <i class="fa-solid fa-arrow-right-to-bracket me-1"></i> <span data-i18n="nav_signin">Sign In</span>
                  </a>
                </li>
                <li class="nav-item ms-1">
                  <a class="btn-gov-primary py-1 px-3" style="font-size: 0.85rem;" href="signup.html">
                    <i class="fa-solid fa-user-plus me-1"></i> <span data-i18n="nav_signup">Register</span>
                  </a>
                </li>
              `}
            </ul>
          </div>
        </div>
      </nav>
    `;

    // Bind dropdown change
    const langSelect = document.getElementById('lang-selector');
    if (langSelect) {
      langSelect.addEventListener('change', (e) => {
        this.loadTranslations(e.target.value);
      });
    }

    const themeBtn = document.getElementById('theme-toggle-btn');
    if (themeBtn) {
      themeBtn.addEventListener('click', () => this.toggleTheme());
    }
  },

  renderFooter() {
    const footerContainer = document.getElementById('app-footer');
    if (!footerContainer) return;

    footerContainer.innerHTML = `
      <footer class="main-footer">
        <div class="container-custom">
          <div class="row g-4">
            <div class="col-lg-4 col-md-6">
              <div class="d-flex align-items-center gap-2 mb-3">
                <div class="brand-emblem" style="width: 36px; height: 36px; font-size: 1rem;">
                  <i class="fa-solid fa-hands-holding-child"></i>
                </div>
                <h5 class="mb-0 text-white fw-bold">NO POVERTY PLATFORM</h5>
              </div>
              <p class="text-secondary small mb-3">
                A modern digital social-impact public initiative aligned with <strong>United Nations SDG 1: No Poverty</strong>.
                Dedicated to bridging citizens with legitimate welfare entitlements, documents preparation, and official application portals.
              </p>
              <div class="d-flex gap-2">
                <span class="badge bg-danger">UN SDG 1</span>
                <span class="badge bg-primary">Gov Welfare Guidance</span>
                <span class="badge bg-success">DBT Ready</span>
              </div>
            </div>

            <div class="col-lg-2 col-md-3 col-6">
              <h6 class="footer-heading">Beneficiaries</h6>
              <ul class="footer-links">
                <li><a href="scheme-finder.html?cat=student">Students & Youth</a></li>
                <li><a href="scheme-finder.html?cat=farmer">Farmers & Agriculture</a></li>
                <li><a href="scheme-finder.html?cat=senior_citizen">Senior Citizens</a></li>
                <li><a href="scheme-finder.html?cat=employment">Job Seekers</a></li>
                <li><a href="scheme-finder.html?cat=disability">Persons with Disabilities</a></li>
              </ul>
            </div>

            <div class="col-lg-2 col-md-3 col-6">
              <h6 class="footer-heading">Platform Tools</h6>
              <ul class="footer-links">
                <li><a href="scheme-finder.html">Smart Scheme Finder</a></li>
                <li><a href="eligibility.html">Eligibility Wizard</a></li>
                <li><a href="voice-search.html">Voice Scheme Search</a></li>
                <li><a href="ai-assistant.html">AI Welfare Assistant</a></li>
                <li><a href="scheme-compare.html">Scheme Comparison</a></li>
              </ul>
            </div>

            <div class="col-lg-2 col-md-3 col-6">
              <h6 class="footer-heading">Quick Links</h6>
              <ul class="footer-links">
                <li><a href="about.html">About SDG 1 Mission</a></li>
                <li><a href="application-guidance.html">Application Guidance</a></li>
                <li><a href="feedback.html">Citizen Feedback</a></li>
                <li><a href="contact.html">Help Desk & Helplines</a></li>
                <li><a href="admin-dashboard.html">Admin Login</a></li>
              </ul>
            </div>

            <div class="col-lg-2 col-md-3 col-6">
              <h6 class="footer-heading">Legal & Privacy</h6>
              <ul class="footer-links">
                <li><a href="privacy.html">Privacy Policy</a></li>
                <li><a href="terms.html">Terms of Service</a></li>
                <li><a href="terms.html#disclaimer">Source Disclaimer</a></li>
              </ul>
            </div>
          </div>

          <div class="footer-bottom">
            <div>
              &copy; 2026 NO POVERTY Welfare Platform. Informational Guidance Portal.
            </div>
            <div class="text-secondary small">
              <i class="fa-solid fa-shield-check text-success me-1"></i> All external application links redirect exclusively to verified official government portals.
            </div>
          </div>
        </div>
      </footer>
      <div id="toast-container"></div>
    `;
  },

  bindGlobalEvents() {
    // Listen for search input shortcuts or alerts
  },

  checkRouteAccess() {
    const path = window.location.pathname;
    const user = API.getCurrentUser();
    const token = API.getToken();

    const requiresAuth = [
      'dashboard.html', 'profile.html', 'family.html', 
      'saved-schemes.html', 'documents.html', 'application-status.html'
    ];

    const requiresAdmin = [
      'admin-dashboard.html', 'admin-schemes.html', 'admin-users.html',
      'admin-categories.html', 'admin-applications.html', 'admin-documents.html',
      'admin-notifications.html', 'admin-analytics.html'
    ];

    const isAuthRequired = requiresAuth.some(p => path.endsWith(p));
    const isAdminRequired = requiresAdmin.some(p => path.endsWith(p));

    if (isAuthRequired && (!user || !token)) {
      window.location.href = 'signin.html?redirect=' + encodeURIComponent(path);
      return;
    }

    if (isAdminRequired) {
      if (!user || !token) {
        window.location.href = 'signin.html?redirect=' + encodeURIComponent(path);
        return;
      }
      const hasAdminRole = user.roles && (user.roles.includes('ROLE_ADMIN') || user.roles.includes('ROLE_DEVELOPER_ADMIN') || user.roles.includes('ADMIN'));
      if (!hasAdminRole) {
        window.location.href = '403.html';
        return;
      }
    }
  },

  logout() {
    API.setToken(null);
    API.setCurrentUser(null);
    this.showToast('You have been logged out safely.', 'info');
    setTimeout(() => {
      window.location.href = 'index.html';
    }, 600);
  },

  showToast(message, type = 'info') {
    let container = document.getElementById('toast-container');
    if (!container) {
      container = document.createElement('div');
      container.id = 'toast-container';
      document.body.appendChild(container);
    }

    const toast = document.createElement('div');
    toast.className = `gov-toast ${type}`;
    const icon = type === 'success' 
      ? 'fa-circle-check text-success' 
      : type === 'error' 
      ? 'fa-triangle-exclamation text-danger' 
      : 'fa-circle-info text-primary';

    toast.innerHTML = `
      <i class="fa-solid ${icon} fs-5"></i>
      <div class="flex-grow-1">${message}</div>
      <button class="btn-close btn-close-sm" style="font-size: 0.7rem;" onclick="this.parentElement.remove()"></button>
    `;

    container.appendChild(toast);
    setTimeout(() => {
      if (toast.parentElement) toast.remove();
    }, 4000);
  }
};

document.addEventListener('DOMContentLoaded', () => {
  App.init();
});

