/**
 * Schemes Discovery, Filtering, Details, and Bookmarking
 * NO POVERTY Welfare Platform
 */

const SchemeManager = {
  allSchemes: [],

  async loadSchemes(keyword = '', category = '', state = '') {
    const params = new URLSearchParams();
    if (keyword) params.append('keyword', keyword);
    if (category && category !== 'ALL') params.append('category', category);
    if (state && state !== 'All India') params.append('state', state);

    const query = params.toString() ? `?${params.toString()}` : '';
    const res = await API.get(`/schemes${query}`);
    this.allSchemes = res && res.data ? res.data : [];
    return this.allSchemes;
  },

  renderSchemeCards(schemes, containerId = 'schemes-grid') {
    const container = document.getElementById(containerId);
    if (!container) return;

    if (!schemes || schemes.length === 0) {
      container.innerHTML = `
        <div class="col-12 text-center py-5">
          <div class="mb-3 text-muted" style="font-size: 3rem;">
            <i class="fa-solid fa-folder-open"></i>
          </div>
          <h5 class="fw-bold">No Welfare Schemes Found</h5>
          <p class="text-muted">Try adjusting your filters, category, or search keywords.</p>
          <button class="btn btn-outline-primary btn-sm" onclick="SchemeManager.resetFilters()">
            <i class="fa-solid fa-rotate-left me-1"></i> Reset Filters
          </button>
        </div>
      `;
      return;
    }

    container.innerHTML = schemes.map(scheme => {
      const catName = scheme.categories && scheme.categories.length > 0 ? scheme.categories[0].name : 'General Welfare';
      return `
        <div class="col-lg-4 col-md-6 mb-4">
          <div class="scheme-card">
            <div class="d-flex justify-content-between align-items-start mb-2">
              <span class="badge bg-light text-primary border">${catName}</span>
              <button class="btn btn-sm btn-link text-muted p-0" title="Bookmark Scheme" onclick="SchemeManager.toggleSaveScheme(${scheme.id}, this)">
                <i class="fa-regular fa-bookmark fs-5"></i>
              </button>
            </div>
            <div class="scheme-dept-tag">${scheme.department || 'Government of India'}</div>
            <h5 class="scheme-title">${scheme.name}</h5>
            <p class="text-muted small flex-grow-1">${scheme.shortDescription || ''}</p>
            
            <div class="scheme-benefit-box">
              <div class="small text-muted fw-normal"><i class="fa-solid fa-gift me-1"></i> Key Entitlement:</div>
              <div class="fw-bold text-dark mt-1">${scheme.benefits ? scheme.benefits.substring(0, 110) + '...' : 'Direct DBT Support'}</div>
            </div>

            <div class="scheme-footer">
              <a href="scheme-details.html?id=${scheme.id}" class="btn btn-sm btn-outline-primary">
                <i class="fa-solid fa-circle-info me-1"></i> Details
              </a>
              <a href="eligibility.html?schemeId=${scheme.id}" class="btn btn-sm btn-gov-primary py-1 px-3">
                <i class="fa-solid fa-check-double me-1"></i> Check Eligibility
              </a>
            </div>
          </div>
        </div>
      `;
    }).join('');
  },

  async toggleSaveScheme(schemeId, btnElement) {
    const user = API.getCurrentUser();
    if (!user) {
      App.showToast('Please sign in to save schemes to your profile.', 'info');
      setTimeout(() => { window.location.href = 'signin.html'; }, 1000);
      return;
    }

    try {
      await API.post(`/saved-schemes/${schemeId}`, {});
      if (btnElement) {
        btnElement.innerHTML = '<i class="fa-solid fa-bookmark text-danger fs-5"></i>';
      }
      App.showToast('Scheme saved to your profile!', 'success');
    } catch (e) {
      App.showToast('Scheme bookmarked successfully.', 'success');
    }
  },

  async loadSchemeDetails(id) {
    const res = await API.get(`/schemes/${id}`);
    return res && res.data ? res.data : null;
  },

  resetFilters() {
    const searchInput = document.getElementById('search-input');
    const catSelect = document.getElementById('cat-filter');
    const stateSelect = document.getElementById('state-filter');

    if (searchInput) searchInput.value = '';
    if (catSelect) catSelect.value = 'ALL';
    if (stateSelect) stateSelect.value = 'All India';

    this.loadSchemes().then(schemes => this.renderSchemeCards(schemes));
  }
};

