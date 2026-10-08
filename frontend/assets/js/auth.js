/**
 * Authentication & Security Management
 * Validations, Password Strength Meter, and Role-Based Redirection
 */

const AuthManager = {
  initSignup() {
    const passwordInput = document.getElementById('signup-password');
    if (passwordInput) {
      passwordInput.addEventListener('input', (e) => {
        this.updatePasswordStrength(e.target.value);
      });
    }

    const roleSelect = document.getElementById('signup-role');
    const adminKeyGroup = document.getElementById('admin-key-group');
    if (roleSelect && adminKeyGroup) {
      roleSelect.addEventListener('change', (e) => {
        if (e.target.value.includes('ADMIN')) {
          adminKeyGroup.classList.remove('d-none');
        } else {
          adminKeyGroup.classList.add('d-none');
        }
      });
    }

    const form = document.getElementById('signup-form');
    if (form) {
      form.addEventListener('submit', (e) => {
        e.preventDefault();
        this.handleSignup();
      });
    }
  },

  initSignin() {
    const form = document.getElementById('signin-form');
    if (form) {
      form.addEventListener('submit', (e) => {
        e.preventDefault();
        this.handleSignin();
      });
    }
  },

  togglePasswordVisibility(inputId, iconId) {
    const input = document.getElementById(inputId);
    const icon = document.getElementById(iconId);
    if (!input) return;

    if (input.type === 'password') {
      input.type = 'text';
      if (icon) icon.className = 'fa-solid fa-eye-slash';
    } else {
      input.type = 'password';
      if (icon) icon.className = 'fa-solid fa-eye';
    }
  },

  updatePasswordStrength(password) {
    const bar = document.getElementById('strength-bar');
    const text = document.getElementById('strength-text');
    if (!bar) return;

    let score = 0;
    if (password.length >= 6) score += 20;
    if (password.length >= 10) score += 20;
    if (/[A-Z]/.test(password)) score += 20;
    if (/[0-9]/.test(password)) score += 20;
    if (/[^A-Za-z0-9]/.test(password)) score += 20;

    bar.style.width = `${score}%`;

    if (score < 40) {
      bar.style.backgroundColor = '#ef4444';
      if (text) text.innerText = 'Weak';
    } else if (score < 80) {
      bar.style.backgroundColor = '#f59e0b';
      if (text) text.innerText = 'Moderate';
    } else {
      bar.style.backgroundColor = '#10b981';
      if (text) text.innerText = 'Strong';
    }
  },

  async handleSignin() {
    const identifier = document.getElementById('signin-identifier').value.trim();
    const password = document.getElementById('signin-password').value;

    if (!identifier || !password) {
      App.showToast('Please fill in your email/mobile and password', 'error');
      return;
    }

    const submitBtn = document.getElementById('signin-submit-btn');
    if (submitBtn) submitBtn.disabled = true;

    try {
      const res = await API.post('/auth/login', { identifier, password });
      if (res && res.data && res.data.token) {
        API.setToken(res.data.token);
        API.setCurrentUser(res.data);
        App.showToast(`Welcome back, ${res.data.fullName}!`, 'success');

        const roles = res.data.roles || [];
        const isAdmin = roles.includes('ROLE_ADMIN') || roles.includes('ROLE_DEVELOPER_ADMIN') || roles.includes('ADMIN');

        setTimeout(() => {
          if (isAdmin) {
            window.location.href = 'admin-dashboard.html';
          } else {
            window.location.href = 'dashboard.html';
          }
        }, 800);
      } else {
        App.showToast('Invalid login response', 'error');
      }
    } catch (err) {
      App.showToast(err.message || 'Login failed', 'error');
    } finally {
      if (submitBtn) submitBtn.disabled = false;
    }
  },

  async handleSignup() {
    const fullName = document.getElementById('signup-name').value.trim();
    const email = document.getElementById('signup-email').value.trim();
    const mobile = document.getElementById('signup-mobile').value.trim();
    const password = document.getElementById('signup-password').value;
    const confirmPassword = document.getElementById('signup-confirm-password').value;
    const state = document.getElementById('signup-state').value;
    const district = document.getElementById('signup-district').value.trim();
    const dob = document.getElementById('signup-dob').value;
    const requestedRole = document.getElementById('signup-role')?.value || 'USER';
    const adminKey = document.getElementById('signup-admin-key')?.value || '';
    const terms = document.getElementById('signup-terms')?.checked;

    if (!terms) {
      App.showToast('You must accept the terms of service and disclaimer.', 'error');
      return;
    }

    if (password !== confirmPassword) {
      App.showToast('Passwords do not match.', 'error');
      return;
    }

    let cleanMobile = mobile.replace(/[^0-9]/g, '');
    if (cleanMobile.length === 11 && cleanMobile.startsWith('0')) {
      cleanMobile = cleanMobile.substring(1);
    } else if (cleanMobile.length === 12 && cleanMobile.startsWith('91')) {
      cleanMobile = cleanMobile.substring(2);
    }

    if (cleanMobile.length !== 10) {
      App.showToast('Please enter a valid 10-digit mobile number.', 'error');
      return;
    }

    const payload = {
      fullName,
      email,
      mobile: cleanMobile,
      password,
      confirmPassword,
      state,
      district,
      dateOfBirth: dob || null,
      requestedRole,
      adminSecretKey: adminKey
    };

    const submitBtn = document.getElementById('signup-submit-btn');
    if (submitBtn) submitBtn.disabled = true;

    try {
      const res = await API.post('/auth/signup', payload);
      if (res && res.data && res.data.token) {
        API.setToken(res.data.token);
        API.setCurrentUser(res.data);
        App.showToast('Account registered successfully! Welcome.', 'success');
        setTimeout(() => {
          window.location.href = 'dashboard.html';
        }, 800);
      } else {
        App.showToast(res.message || 'Sign up failed.', 'error');
      }
    } catch (err) {
      App.showToast(err.message || 'Sign up failed. Please try again.', 'error');
    } finally {
      if (submitBtn) submitBtn.disabled = false;
    }
  }
};

