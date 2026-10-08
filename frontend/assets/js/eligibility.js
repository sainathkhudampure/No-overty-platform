/**
 * Multi-Step Eligibility Wizard & Evaluation
 * Aligned with UN SDG 1: NO POVERTY
 */

const EligibilityWizard = {
  currentStep: 1,
  totalSteps: 7,
  formData: {
    age: null,
    gender: 'ALL',
    annualIncome: 120000,
    occupation: '',
    educationLevel: '',
    state: 'Karnataka',
    district: '',
    isStudent: false,
    isFarmer: false,
    hasDisability: false,
    isSeniorCitizen: false,
    schemeId: null
  },

  init() {
    const urlParams = new URLSearchParams(window.location.search);
    const schemeId = urlParams.get('schemeId');
    if (schemeId) {
      this.formData.schemeId = parseInt(schemeId);
    }

    // Prefill from user profile if signed in
    const user = API.getCurrentUser();
    if (user) {
      if (user.state) this.formData.state = user.state;
      if (user.district) this.formData.district = user.district;
    }

    this.renderStep(this.currentStep);
    this.updateStepperIndicator();
  },

  updateStepperIndicator() {
    for (let i = 1; i <= this.totalSteps; i++) {
      const stepEl = document.getElementById(`step-indicator-${i}`);
      if (stepEl) {
        if (i < this.currentStep) {
          stepEl.className = 'wizard-step-item completed';
        } else if (i === this.currentStep) {
          stepEl.className = 'wizard-step-item active';
        } else {
          stepEl.className = 'wizard-step-item';
        }
      }
    }
  },

  renderStep(step) {
    for (let i = 1; i <= this.totalSteps; i++) {
      const panel = document.getElementById(`step-panel-${i}`);
      if (panel) panel.classList.add('d-none');
    }

    const currentPanel = document.getElementById(`step-panel-${step}`);
    if (currentPanel) currentPanel.classList.remove('d-none');

    this.updateStepperIndicator();
  },

  nextStep() {
    if (!this.validateCurrentStep()) return;

    if (this.currentStep < this.totalSteps - 1) {
      this.currentStep++;
      this.renderStep(this.currentStep);
    } else if (this.currentStep === this.totalSteps - 1) {
      this.submitEvaluation();
    }
  },

  prevStep() {
    if (this.currentStep > 1) {
      this.currentStep--;
      this.renderStep(this.currentStep);
    }
  },

  validateCurrentStep() {
    if (this.currentStep === 1) {
      const ageInput = document.getElementById('wizard-age');
      if (ageInput && ageInput.value) {
        this.formData.age = parseInt(ageInput.value);
      }
      const genderSelect = document.getElementById('wizard-gender');
      if (genderSelect) this.formData.gender = genderSelect.value;
    } else if (this.currentStep === 2) {
      const incomeInput = document.getElementById('wizard-income');
      if (incomeInput && incomeInput.value) {
        this.formData.annualIncome = parseFloat(incomeInput.value);
      }
    } else if (this.currentStep === 3) {
      const occSelect = document.getElementById('wizard-occupation');
      if (occSelect) this.formData.occupation = occSelect.value;
    } else if (this.currentStep === 4) {
      const eduSelect = document.getElementById('wizard-education');
      if (eduSelect) this.formData.educationLevel = eduSelect.value;
    } else if (this.currentStep === 5) {
      const stateSelect = document.getElementById('wizard-state');
      if (stateSelect) this.formData.state = stateSelect.value;
      const distInput = document.getElementById('wizard-district');
      if (distInput) this.formData.district = distInput.value;
    } else if (this.currentStep === 6) {
      this.formData.isStudent = document.getElementById('wizard-student')?.checked || false;
      this.formData.isFarmer = document.getElementById('wizard-farmer')?.checked || false;
      this.formData.hasDisability = document.getElementById('wizard-disability')?.checked || false;
      this.formData.isSeniorCitizen = document.getElementById('wizard-senior')?.checked || (this.formData.age >= 60);
    }
    return true;
  },

  async submitEvaluation() {
    this.currentStep = 7;
    this.renderStep(7);

    const resultBox = document.getElementById('wizard-result-box');
    if (resultBox) {
      resultBox.innerHTML = `
        <div class="text-center py-5">
          <div class="spinner-border text-primary mb-3" role="status"></div>
          <h5>Evaluating Official Eligibility Criteria...</h5>
          <p class="text-muted small">Checking age parameters, income ceilings, and statutory category restrictions</p>
        </div>
      `;
    }

    try {
      let result;
      if (this.formData.schemeId) {
        const res = await API.post(`/schemes/${this.formData.schemeId}/eligibility`, this.formData);
        result = res && res.data ? res.data : null;
      } else {
        const res = await API.post('/schemes/check-eligibility', this.formData);
        const list = res && res.data ? res.data : [];
        result = list.length > 0 ? list[0] : null;
      }

      this.displayResult(result);
    } catch (e) {
      console.warn('Evaluation error:', e);
      this.displayResult(null);
    }
  },

  displayResult(result) {
    const resultBox = document.getElementById('wizard-result-box');
    if (!resultBox) return;

    if (!result) {
      resultBox.innerHTML = `
        <div class="alert alert-warning">
          <h5>Could not calculate eligibility</h5>
          <p>Please try again or browse schemes directly from the scheme finder.</p>
        </div>
      `;
      return;
    }

    const badgeClass = result.status === 'GREEN' 
      ? 'badge-green' 
      : result.status === 'YELLOW' 
      ? 'badge-yellow' 
      : 'badge-red';

    const iconClass = result.status === 'GREEN' 
      ? 'fa-circle-check text-success' 
      : result.status === 'YELLOW' 
      ? 'fa-triangle-exclamation text-warning' 
      : 'fa-circle-xmark text-danger';

    resultBox.innerHTML = `
      <div class="card border-0 shadow-sm p-4 text-center mb-4">
        <div class="mb-3" style="font-size: 3.5rem;">
          <i class="fa-solid ${iconClass}"></i>
        </div>
        <div class="d-flex justify-content-center mb-2">
          <span class="badge-gov ${badgeClass} fs-6 px-3 py-1">
            ${result.statusLabel} (${result.matchPercentage}% Match)
          </span>
        </div>
        <h3 class="fw-bold text-dark mt-2">${result.schemeName || 'Evaluated Scheme'}</h3>
        <p class="text-muted max-w-600 mx-auto mt-2">${result.guidanceNotes}</p>

        <div class="row text-start mt-4 g-3">
          <div class="col-md-6">
            <div class="p-3 bg-light rounded-3 h-100">
              <h6 class="fw-bold text-success mb-2"><i class="fa-solid fa-check me-1"></i> Criteria Matched</h6>
              <ul class="list-unstyled small mb-0">
                ${result.matchedCriteria && result.matchedCriteria.length > 0
                  ? result.matchedCriteria.map(m => `<li class="mb-1 text-dark"><i class="fa-solid fa-check text-success me-2"></i>${m}</li>`).join('')
                  : '<li class="text-muted">No specific matches documented.</li>'}
              </ul>
            </div>
          </div>
          <div class="col-md-6">
            <div class="p-3 bg-light rounded-3 h-100">
              <h6 class="fw-bold text-danger mb-2"><i class="fa-solid fa-xmark me-1"></i> Disqualifying / Missing Points</h6>
              <ul class="list-unstyled small mb-0">
                ${result.unmatchedCriteria && result.unmatchedCriteria.length > 0
                  ? result.unmatchedCriteria.map(u => `<li class="mb-1 text-danger"><i class="fa-solid fa-xmark me-2"></i>${u}</li>`).join('')
                  : result.pendingCriteria && result.pendingCriteria.length > 0
                  ? result.pendingCriteria.map(p => `<li class="mb-1 text-warning"><i class="fa-solid fa-circle-notch me-2"></i>${p}</li>`).join('')
                  : '<li class="text-success"><i class="fa-solid fa-shield-check me-2"></i>No disqualifying criteria found!</li>'}
              </ul>
            </div>
          </div>
        </div>

        <div class="alert alert-secondary text-start small mt-4 border-0">
          <strong><i class="fa-solid fa-scale-balanced me-1"></i> Official Portal Notice:</strong>
          ${result.disclaimer}
        </div>

        <div class="d-flex justify-content-center gap-3 mt-3 flex-wrap">
          <a href="${result.officialPortalUrl || '#'}" target="_blank" rel="noopener noreferrer" class="btn btn-gov-primary">
            <i class="fa-solid fa-arrow-up-right-from-square me-1"></i> Apply on Official Portal
          </a>
          <a href="documents.html" class="btn btn-outline-primary">
            <i class="fa-solid fa-folder-open me-1"></i> Prepare Documents
          </a>
          <button class="btn btn-outline-secondary" onclick="EligibilityWizard.resetWizard()">
            <i class="fa-solid fa-rotate-left me-1"></i> Check Another
          </button>
        </div>
      </div>
    `;
  },

  resetWizard() {
    this.currentStep = 1;
    this.init();
  }
};

