/**
 * Document Checklist & Vault Management
 * Securely handles citizen document preparedness
 */

const DocumentManager = {
  documents: [],
  missingDocuments: [],

  async loadDocuments() {
    const res = await API.get('/documents');
    if (res && res.data) {
      this.documents = res.data.documents || [];
      this.missingDocuments = res.data.missingDocuments || [];
    }
    this.renderDocumentList();
    this.renderMissingAlert();
  },

  renderDocumentList() {
    const container = document.getElementById('documents-list');
    if (!container) return;

    if (this.documents.length === 0) {
      container.innerHTML = `
        <div class="text-center py-4 text-muted">
          <i class="fa-solid fa-folder-open fs-2 mb-2"></i>
          <p>No documents uploaded or marked as ready yet.</p>
        </div>
      `;
      return;
    }

    container.innerHTML = this.documents.map(doc => `
      <div class="d-flex align-items-center justify-content-between p-3 border rounded-3 mb-2 bg-white">
        <div class="d-flex align-items-center gap-3">
          <div class="p-2 bg-light text-primary rounded-circle" style="width: 40px; height: 40px; display: flex; align-items: center; justify-content: center;">
            <i class="fa-solid fa-file-lines fs-5"></i>
          </div>
          <div>
            <div class="fw-bold text-dark">${doc.documentName}</div>
            <div class="small text-muted">${doc.documentType || 'Identity Document'} • <span class="badge bg-success-subtle text-success">${doc.status}</span></div>
          </div>
        </div>
        <div class="d-flex align-items-center gap-2">
          ${doc.filePath ? `<a href="${doc.filePath}" target="_blank" class="btn btn-sm btn-outline-primary" title="View Document"><i class="fa-solid fa-eye"></i></a>` : ''}
          <button class="btn btn-sm btn-outline-danger" title="Delete" onclick="DocumentManager.deleteDoc(${doc.id})">
            <i class="fa-solid fa-trash-can"></i>
          </button>
        </div>
      </div>
    `).join('');
  },

  renderMissingAlert() {
    const alertBox = document.getElementById('missing-docs-alert');
    if (!alertBox) return;

    if (this.missingDocuments.length === 0) {
      alertBox.innerHTML = `
        <div class="alert alert-success d-flex align-items-center gap-2 mb-4">
          <i class="fa-solid fa-shield-check fs-4"></i>
          <div>All primary standard documents are ready and marked as verified in your profile!</div>
        </div>
      `;
      return;
    }

    alertBox.innerHTML = `
      <div class="alert alert-warning mb-4">
        <h6 class="fw-bold mb-2"><i class="fa-solid fa-triangle-exclamation me-1"></i> Missing Key Documents</h6>
        <p class="small mb-2">The following standard welfare documents have not yet been marked as ready in your profile:</p>
        <div class="d-flex flex-wrap gap-2">
          ${this.missingDocuments.map(doc => `
            <span class="badge bg-warning-subtle text-dark border border-warning px-2 py-1">
              <i class="fa-regular fa-square me-1"></i> ${doc}
            </span>
          `).join('')}
        </div>
      </div>
    `;
  },

  async markAsAvailable(docName) {
    await API.post('/documents', {
      documentName: docName,
      status: 'AVAILABLE',
      isVerified: true
    });
    App.showToast(`${docName} marked as available!`, 'success');
    this.loadDocuments();
  },

  async deleteDoc(id) {
    if (!confirm('Are you sure you want to remove this document?')) return;
    await API.delete(`/documents/${id}`);
    App.showToast('Document removed.', 'info');
    this.loadDocuments();
  }
};
