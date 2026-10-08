/**
 * Family Welfare Profile Manager
 * Models household dependents to compute composite welfare eligibility
 */

const FamilyManager = {
  members: [],

  async loadFamily() {
    const res = await API.get('/family');
    if (res && res.data) {
      this.members = res.data.members || [];
      this.renderOverview(res.data);
      this.renderMembersList(this.members);
    }
  },

  renderOverview(data) {
    const overviewBox = document.getElementById('family-overview-box');
    if (!overviewBox) return;

    overviewBox.innerHTML = `
      <div class="row g-3 text-center">
        <div class="col-6 col-md-3">
          <div class="p-3 bg-light rounded-3">
            <div class="fs-3 fw-bold text-primary">${data.totalMembers || 1}</div>
            <div class="small text-muted fw-semibold">Household Size</div>
          </div>
        </div>
        <div class="col-6 col-md-3">
          <div class="p-3 bg-light rounded-3">
            <div class="fs-3 fw-bold text-success">${data.studentsCount || 0}</div>
            <div class="small text-muted fw-semibold">Enrolled Students</div>
          </div>
        </div>
        <div class="col-6 col-md-3">
          <div class="p-3 bg-light rounded-3">
            <div class="fs-3 fw-bold text-warning">${data.seniorsCount || 0}</div>
            <div class="small text-muted fw-semibold">Senior Citizens (60+)</div>
          </div>
        </div>
        <div class="col-6 col-md-3">
          <div class="p-3 bg-light rounded-3">
            <div class="fs-3 fw-bold text-info">${data.disabilityCount || 0}</div>
            <div class="small text-muted fw-semibold">Divyangjan Members</div>
          </div>
        </div>
      </div>
    `;
  },

  renderMembersList(members) {
    const container = document.getElementById('family-members-list');
    if (!container) return;

    if (members.length === 0) {
      container.innerHTML = `
        <div class="text-center py-4 text-muted">
          <i class="fa-solid fa-users-viewfinder fs-2 mb-2"></i>
          <p>No family members added yet. Add family members to discover schemes matching children, students, or elderly relatives!</p>
        </div>
      `;
      return;
    }

    container.innerHTML = members.map(m => `
      <div class="d-flex align-items-center justify-content-between p-3 border rounded-3 mb-2 bg-white flex-wrap gap-2">
        <div class="d-flex align-items-center gap-3">
          <div class="rounded-circle bg-primary-subtle text-primary d-flex align-items-center justify-content-center" style="width: 44px; height: 44px;">
            <i class="fa-solid fa-user"></i>
          </div>
          <div>
            <h6 class="fw-bold mb-0 text-dark">${m.name}</h6>
            <div class="small text-muted">
              ${m.relationship} • Age: ${m.age} • ${m.occupation || 'Dependent'}
            </div>
          </div>
        </div>
        <div class="d-flex align-items-center gap-2">
          ${m.studentStatus ? '<span class="badge bg-success-subtle text-success">Student</span>' : ''}
          ${m.seniorCitizenStatus ? '<span class="badge bg-warning-subtle text-warning">Senior Citizen</span>' : ''}
          ${m.disabilityStatus ? '<span class="badge bg-info-subtle text-info">Divyangjan</span>' : ''}
          <button class="btn btn-sm btn-outline-danger ms-2" onclick="FamilyManager.deleteMember(${m.id})">
            <i class="fa-solid fa-trash-can"></i>
          </button>
        </div>
      </div>
    `).join('');
  },

  async deleteMember(id) {
    if (!confirm('Remove family member from profile?')) return;
    await API.delete(`/family/${id}`);
    App.showToast('Family member removed.', 'info');
    this.loadFamily();
  }
};

