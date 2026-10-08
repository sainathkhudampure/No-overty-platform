/**
 * Personalized Scheme Recommendations Engine
 * Computes scores and renders matched entitlements
 */

const RecommendationManager = {
  recommendations: [],

  async loadRecommendations() {
    const res = await API.get('/recommendations');
    this.recommendations = res && res.data ? res.data : [];
    this.renderRecommendations(this.recommendations);
  },

  renderRecommendations(list) {
    const container = document.getElementById('recommendations-grid');
    if (!container) return;

    if (!list || list.length === 0) {
      container.innerHTML = `
        <div class="col-12 text-center py-5">
          <div class="mb-3 text-muted" style="font-size: 3rem;">
            <i class="fa-solid fa-wand-magic-sparkles"></i>
          </div>
          <h5 class="fw-bold">No Custom Recommendations Yet</h5>
          <p class="text-muted">Complete your profile to receive tailored schemes with match scores!</p>
          <a href="profile.html" class="btn btn-primary btn-sm">Complete Profile</a>
        </div>
      `;
      return;
    }

    container.innerHTML = list.map(item => {
      const scoreColor = item.score >= 85 ? 'text-success' : item.score >= 70 ? 'text-primary' : 'text-warning';
      const progressBg = item.score >= 85 ? 'bg-success' : item.score >= 70 ? 'bg-primary' : 'bg-warning';

      return `
        <div class="col-lg-6 mb-4">
          <div class="card h-100 border-0 shadow-sm p-4">
            <div class="d-flex justify-content-between align-items-center mb-3">
              <span class="badge bg-light text-primary border">${item.categoryName || 'Welfare Scheme'}</span>
              <div class="text-end">
                <span class="fs-4 fw-bold ${scoreColor}">${item.score}%</span>
                <span class="small text-muted ms-1">Match</span>
              </div>
            </div>

            <div class="progress mb-3" style="height: 6px;">
              <div class="progress-bar ${progressBg}" style="width: ${item.score}%;"></div>
            </div>

            <h5 class="fw-bold text-dark mb-1">${item.schemeName}</h5>
            <div class="text-muted small mb-2">${item.department}</div>
            <p class="text-muted small flex-grow-1">${item.shortDescription}</p>

            <div class="p-2 px-3 rounded-2 bg-light border-start border-3 border-primary mb-3 small">
              <strong><i class="fa-solid fa-lightbulb text-warning me-1"></i> Why recommended:</strong>
              <div class="text-muted mt-1">${item.matchExplanation}</div>
            </div>

            <div class="d-flex justify-content-between align-items-center pt-2 border-top">
              <div class="d-flex gap-2">
                <a href="scheme-details.html?id=${item.schemeId}" class="btn btn-sm btn-outline-primary">
                  <i class="fa-solid fa-circle-info me-1"></i> Details
                </a>
                <a href="scheme-compare.html?id1=${item.schemeId}" class="btn btn-sm btn-outline-secondary">
                  <i class="fa-solid fa-code-compare me-1"></i> Compare
                </a>
              </div>
              <button class="btn btn-sm btn-gov-primary py-1 px-3" onclick="SchemeManager.toggleSaveScheme(${item.schemeId}, this)">
                <i class="fa-regular fa-bookmark me-1"></i> Save
              </button>
            </div>
          </div>
        </div>
      `;
    }).join('');
  }
};
