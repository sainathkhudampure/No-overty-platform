/**
 * Application Status Tracker with Interactive Status Timeline
 * Internal citizen tracking system (No false government claim)
 */

const ApplicationTracker = {
  applications: [],

  async loadApplications() {
    const res = await API.get('/applications');
    this.applications = res && res.data ? res.data : [];
    this.renderApplications();
  },

  renderApplications() {
    const container = document.getElementById('applications-list');
    if (!container) return;

    if (this.applications.length === 0) {
      container.innerHTML = `
        <div class="card border-0 shadow-sm p-5 text-center">
          <i class="fa-solid fa-file-signature fs-1 text-muted mb-3"></i>
          <h5 class="fw-bold">No Tracked Applications Found</h5>
          <p class="text-muted small">After submitting your application on an official government portal, record your application acknowledgment number here to track your milestone progress.</p>
          <button class="btn btn-primary btn-sm mx-auto" data-bs-toggle="modal" data-bs-target="#newAppModal">
            <i class="fa-solid fa-plus me-1"></i> Track New Application
          </button>
        </div>
      `;
      return;
    }

    container.innerHTML = this.applications.map(app => {
      const steps = ['Draft', 'Submitted', 'Under Verification', 'Approved', 'Benefit Received'];
      const currentIndex = steps.indexOf(app.status);

      return `
        <div class="card border-0 shadow-sm p-4 mb-4">
          <div class="d-flex justify-content-between align-items-start flex-wrap gap-2 mb-3">
            <div>
              <span class="badge bg-primary-subtle text-primary mb-1">${app.scheme ? app.scheme.code : 'SCHEME'}</span>
              <h5 class="fw-bold text-dark mb-0">${app.scheme ? app.scheme.name : 'Government Scheme'}</h5>
              <div class="text-muted small mt-1">
                <strong>Application No:</strong> <span class="badge bg-light text-dark border font-monospace">${app.applicationNumber}</span>
                ${app.appliedDate ? ` • <strong>Applied Date:</strong> ${app.appliedDate}` : ''}
              </div>
            </div>
            <div class="text-end">
              <span class="badge-gov ${app.status === 'Approved' ? 'badge-green' : app.status === 'Rejected' ? 'badge-red' : 'badge-yellow'} px-3 py-1 fs-6">
                ${app.status}
              </span>
              <div class="mt-2">
                <button class="btn btn-sm btn-outline-danger" onclick="ApplicationTracker.deleteApp(${app.id})">
                  <i class="fa-solid fa-trash me-1"></i> Delete
                </button>
              </div>
            </div>
          </div>

          <!-- Visual Progress Timeline -->
          <div class="py-3 px-2">
            <div class="d-flex justify-content-between position-relative mb-2">
              <div class="progress position-absolute w-100 top-50 translate-middle-y" style="height: 4px; z-index: 1;">
                <div class="progress-bar bg-success" style="width: ${Math.max(0, (currentIndex / (steps.length - 1)) * 100)}%;"></div>
              </div>

              ${steps.map((st, idx) => {
                const isPassed = idx <= currentIndex;
                const isCurrent = idx === currentIndex;
                const circleBg = isPassed ? 'bg-success text-white' : 'bg-light text-muted border';
                return `
                  <div class="text-center position-relative" style="z-index: 2;">
                    <div class="rounded-circle d-flex align-items-center justify-content-center mx-auto mb-1 ${circleBg}" style="width: 28px; height: 28px; font-size: 0.75rem;">
                      ${isPassed ? '<i class="fa-solid fa-check"></i>' : idx + 1}
                    </div>
                    <div class="small ${isCurrent ? 'fw-bold text-dark' : 'text-muted'}" style="font-size: 0.72rem;">${st}</div>
                  </div>
                `;
              }).join('')}
            </div>
          </div>

          ${app.notes ? `
            <div class="bg-light p-3 rounded-2 small text-secondary mt-2">
              <strong><i class="fa-solid fa-note-sticky text-warning me-1"></i> Notes & Remarks:</strong> ${app.notes}
            </div>
          ` : ''}

          <div class="text-muted small mt-3 pt-2 border-top d-flex justify-content-between flex-wrap gap-2">
            <span><i class="fa-solid fa-info-circle text-primary me-1"></i> Self-managed milestone tracking for citizen records.</span>
            ${app.scheme && app.scheme.officialPortalUrl ? `
              <a href="${app.scheme.officialPortalUrl}" target="_blank" class="text-decoration-none fw-semibold">
                Verify on Official Portal <i class="fa-solid fa-arrow-up-right-from-square small ms-1"></i>
              </a>
            ` : ''}
          </div>
        </div>
      `;
    }).join('');
  },

  async deleteApp(id) {
    if (!confirm('Remove this application tracker record?')) return;
    await API.delete(`/applications/${id}`);
    App.showToast('Application record removed.', 'info');
    this.loadApplications();
  }
};
