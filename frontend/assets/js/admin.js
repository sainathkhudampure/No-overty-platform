/**
 * Admin Dashboard & Analytics Controller
 * UN SDG 1 - NO POVERTY Administrative Operations
 */

const AdminPortal = {
  analyticsData: null,

  async initDashboard() {
    await this.loadAnalytics();
  },

  async loadAnalytics() {
    const res = await API.get('/admin/analytics');
    if (res && res.data) {
      this.analyticsData = res.data;
      this.renderSummaryCards(res.data);
      this.renderCharts(res.data);
    }
  },

  renderSummaryCards(data) {
    const elUsers = document.getElementById('admin-stat-users');
    const elSchemes = document.getElementById('admin-stat-schemes');
    const elApps = document.getElementById('admin-stat-apps');
    const elRating = document.getElementById('admin-stat-rating');

    if (elUsers) elUsers.innerText = data.totalUsers || 0;
    if (elSchemes) elSchemes.innerText = data.totalSchemes || 0;
    if (elApps) elApps.innerText = data.applicationsTracked || 0;
    if (elRating) elRating.innerText = (data.averageFeedbackRating || 4.8) + ' ★';
  },

  renderCharts(data) {
    if (typeof Chart === 'undefined') return;

    // 1. Schemes by Category Chart
    const ctxSchemes = document.getElementById('chart-schemes-category');
    if (ctxSchemes && data.schemesByCategory) {
      new Chart(ctxSchemes, {
        type: 'doughnut',
        data: {
          labels: Object.keys(data.schemesByCategory),
          datasets: [{
            data: Object.values(data.schemesByCategory),
            backgroundColor: [
              '#3b82f6', '#10b981', '#f59e0b', '#ef4444', 
              '#8b5cf6', '#06b6d4', '#ec4899', '#64748b'
            ]
          }]
        },
        options: {
          responsive: true,
          plugins: { legend: { position: 'bottom' } }
        }
      });
    }

    // 2. Applications by Status Chart
    const ctxApps = document.getElementById('chart-applications-status');
    if (ctxApps && data.applicationsByStatus) {
      new Chart(ctxApps, {
        type: 'pie',
        data: {
          labels: Object.keys(data.applicationsByStatus),
          datasets: [{
            data: Object.values(data.applicationsByStatus),
            backgroundColor: ['#94a3b8', '#3b82f6', '#f59e0b', '#10b981', '#ef4444']
          }]
        },
        options: {
          responsive: true,
          plugins: { legend: { position: 'bottom' } }
        }
      });
    }

    // 3. Monthly Registrations Line Chart
    const ctxMonthly = document.getElementById('chart-monthly-registrations');
    if (ctxMonthly && data.monthlyRegistrations) {
      new Chart(ctxMonthly, {
        type: 'line',
        data: {
          labels: Object.keys(data.monthlyRegistrations),
          datasets: [{
            label: 'Citizen Registrations',
            data: Object.values(data.monthlyRegistrations),
            borderColor: '#ea580c',
            backgroundColor: 'rgba(234, 88, 12, 0.1)',
            fill: true,
            tension: 0.3
          }]
        },
        options: {
          responsive: true,
          scales: { y: { beginAtZero: true } }
        }
      });
    }

    // 4. Users by State Bar Chart
    const ctxState = document.getElementById('chart-users-state');
    if (ctxState && data.usersByState) {
      new Chart(ctxState, {
        type: 'bar',
        data: {
          labels: Object.keys(data.usersByState),
          datasets: [{
            label: 'Beneficiaries by State',
            data: Object.values(data.usersByState),
            backgroundColor: '#0e2749'
          }]
        },
        options: {
          responsive: true,
          scales: { y: { beginAtZero: true } }
        }
      });
    }
  },

  // Schemes CRUD
  async loadSchemesTable() {
    const res = await API.get('/admin/schemes');
    const tableBody = document.getElementById('admin-schemes-tbody');
    if (!tableBody) return;

    const list = res && res.data ? res.data : [];
    tableBody.innerHTML = list.map(s => `
      <tr>
        <td class="fw-bold font-monospace">${s.code}</td>
        <td>
          <div class="fw-semibold text-dark">${s.name}</div>
          <div class="small text-muted">${s.department}</div>
        </td>
        <td>${s.stateAvailability || 'All India'}</td>
        <td><span class="badge ${s.isActive ? 'bg-success' : 'bg-secondary'}">${s.isActive ? 'Active' : 'Archived'}</span></td>
        <td><span class="badge bg-light text-dark border">${s.viewsCount || 0}</span></td>
        <td class="text-end">
          <a href="scheme-details.html?id=${s.id}" class="btn btn-sm btn-outline-info me-1"><i class="fa-solid fa-eye"></i></a>
          <button class="btn btn-sm btn-outline-danger" onclick="AdminPortal.deleteScheme(${s.id})"><i class="fa-solid fa-trash"></i></button>
        </td>
      </tr>
    `).join('');
  },

  async deleteScheme(id) {
    if (!confirm('Are you sure you want to delete / archive this scheme record?')) return;
    await API.delete(`/admin/schemes/${id}`);
    App.showToast('Scheme deleted successfully.', 'info');
    this.loadSchemesTable();
  },

  // Users Management
  async loadUsersTable() {
    const res = await API.get('/admin/users');
    const tableBody = document.getElementById('admin-users-tbody');
    if (!tableBody) return;

    const users = res && res.data ? res.data : [];
    tableBody.innerHTML = users.map(u => `
      <tr>
        <td class="fw-bold">${u.fullName}</td>
        <td>${u.email}<br><small class="text-muted">${u.mobile}</small></td>
        <td>${u.state || 'N/A'}, ${u.district || ''}</td>
        <td>${u.roles ? u.roles.map(r => `<span class="badge bg-secondary-subtle text-dark me-1">${r.name.replace('ROLE_', '')}</span>`).join('') : ''}</td>
        <td><span class="badge ${u.status === 'ACTIVE' ? 'bg-success' : 'bg-danger'}">${u.status}</span></td>
        <td class="text-end">
          <button class="btn btn-sm btn-outline-secondary" onclick="AdminPortal.toggleUserStatus(${u.id}, '${u.status}')">
            ${u.status === 'ACTIVE' ? 'Suspend' : 'Activate'}
          </button>
        </td>
      </tr>
    `).join('');
  },

  async toggleUserStatus(userId, currentStatus) {
    const nextStatus = currentStatus === 'ACTIVE' ? 'SUSPENDED' : 'ACTIVE';
    await API.put(`/admin/users/${userId}/status`, { status: nextStatus });
    App.showToast(`User status updated to ${nextStatus}`, 'success');
    this.loadUsersTable();
  }
};

