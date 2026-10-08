package com.nopoverty.platform.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "schemes")
public class Scheme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 255)
    private String department;

    @Column(nullable = false, length = 500)
    private String shortDescription;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String fullDescription;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String objective;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String benefits;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String applicationProcess;

    @Column(length = 100)
    private String stateAvailability = "All India";

    @Column(nullable = false, length = 500)
    private String officialPortalUrl;

    @Column(nullable = false, length = 500)
    private String officialApplicationUrl;

    private Boolean isActive = true;
    private Long viewsCount = 0L;
    private Long savedCount = 0L;

    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "scheme_categories",
        joinColumns = @JoinColumn(name = "scheme_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @OneToOne(mappedBy = "scheme", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private SchemeEligibilityCriteria eligibilityCriteria;

    @OneToMany(mappedBy = "scheme", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<SchemeDocument> documents = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Scheme() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public String getFullDescription() { return fullDescription; }
    public void setFullDescription(String fullDescription) { this.fullDescription = fullDescription; }

    public String getObjective() { return objective; }
    public void setObjective(String objective) { this.objective = objective; }

    public String getBenefits() { return benefits; }
    public void setBenefits(String benefits) { this.benefits = benefits; }

    public String getApplicationProcess() { return applicationProcess; }
    public void setApplicationProcess(String applicationProcess) { this.applicationProcess = applicationProcess; }

    public String getStateAvailability() { return stateAvailability; }
    public void setStateAvailability(String stateAvailability) { this.stateAvailability = stateAvailability; }

    public String getOfficialPortalUrl() { return officialPortalUrl; }
    public void setOfficialPortalUrl(String officialPortalUrl) { this.officialPortalUrl = officialPortalUrl; }

    public String getOfficialApplicationUrl() { return officialApplicationUrl; }
    public void setOfficialApplicationUrl(String officialApplicationUrl) { this.officialApplicationUrl = officialApplicationUrl; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean active) { isActive = active; }

    public Long getViewsCount() { return viewsCount; }
    public void setViewsCount(Long viewsCount) { this.viewsCount = viewsCount; }

    public Long getSavedCount() { return savedCount; }
    public void setSavedCount(Long savedCount) { this.savedCount = savedCount; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Set<Category> getCategories() { return categories; }
    public void setCategories(Set<Category> categories) { this.categories = categories; }

    public SchemeEligibilityCriteria getEligibilityCriteria() { return eligibilityCriteria; }
    public void setEligibilityCriteria(SchemeEligibilityCriteria eligibilityCriteria) {
        this.eligibilityCriteria = eligibilityCriteria;
        if (eligibilityCriteria != null) {
            eligibilityCriteria.setScheme(this);
        }
    }

    public List<SchemeDocument> getDocuments() { return documents; }
    public void setDocuments(List<SchemeDocument> documents) {
        this.documents = documents;
        if (documents != null) {
            for (SchemeDocument doc : documents) {
                doc.setScheme(this);
            }
        }
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PreUpdate
    public void onUpdate() { this.updatedAt = LocalDateTime.now(); }
}

