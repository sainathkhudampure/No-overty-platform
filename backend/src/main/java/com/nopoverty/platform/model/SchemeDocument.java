package com.nopoverty.platform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "scheme_documents")
public class SchemeDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheme_id", nullable = false)
    @JsonIgnore
    private Scheme scheme;

    @Column(nullable = false, length = 150)
    private String documentName;

    private Boolean isMandatory = true;

    @Column(length = 255)
    private String description;

    private LocalDateTime createdAt = LocalDateTime.now();

    public SchemeDocument() {}

    public SchemeDocument(String documentName, Boolean isMandatory, String description) {
        this.documentName = documentName;
        this.isMandatory = isMandatory;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Scheme getScheme() { return scheme; }
    public void setScheme(Scheme scheme) { this.scheme = scheme; }

    public String getDocumentName() { return documentName; }
    public void setDocumentName(String documentName) { this.documentName = documentName; }

    public Boolean getIsMandatory() { return isMandatory; }
    public void setIsMandatory(Boolean mandatory) { isMandatory = mandatory; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

