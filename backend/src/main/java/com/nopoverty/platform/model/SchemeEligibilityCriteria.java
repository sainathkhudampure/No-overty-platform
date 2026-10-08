package com.nopoverty.platform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "scheme_eligibility")
public class SchemeEligibilityCriteria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheme_id", nullable = false)
    @JsonIgnore
    private Scheme scheme;

    private Integer minAge = 0;
    private Integer maxAge = 120;

    @Column(precision = 12, scale = 2)
    private BigDecimal maxIncome = new BigDecimal("99999999.00");

    @Column(precision = 12, scale = 2)
    private BigDecimal minIncome = BigDecimal.ZERO;

    @Column(length = 20)
    private String requiredGender = "ALL"; // ALL, MALE, FEMALE

    private Boolean studentOnly = false;
    private Boolean farmerOnly = false;
    private Boolean disabilityOnly = false;
    private Boolean seniorOnly = false;

    @Column(length = 255)
    private String allowedStates = "All India";

    @Column(columnDefinition = "TEXT")
    private String criteriaDescription;

    private LocalDateTime createdAt = LocalDateTime.now();

    public SchemeEligibilityCriteria() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Scheme getScheme() { return scheme; }
    public void setScheme(Scheme scheme) { this.scheme = scheme; }

    public Integer getMinAge() { return minAge; }
    public void setMinAge(Integer minAge) { this.minAge = minAge; }

    public Integer getMaxAge() { return maxAge; }
    public void setMaxAge(Integer maxAge) { this.maxAge = maxAge; }

    public BigDecimal getMaxIncome() { return maxIncome; }
    public void setMaxIncome(BigDecimal maxIncome) { this.maxIncome = maxIncome; }

    public BigDecimal getMinIncome() { return minIncome; }
    public void setMinIncome(BigDecimal minIncome) { this.minIncome = minIncome; }

    public String getRequiredGender() { return requiredGender; }
    public void setRequiredGender(String requiredGender) { this.requiredGender = requiredGender; }

    public Boolean getStudentOnly() { return studentOnly; }
    public void setStudentOnly(Boolean studentOnly) { this.studentOnly = studentOnly; }

    public Boolean getFarmerOnly() { return farmerOnly; }
    public void setFarmerOnly(Boolean farmerOnly) { this.farmerOnly = farmerOnly; }

    public Boolean getDisabilityOnly() { return disabilityOnly; }
    public void setDisabilityOnly(Boolean disabilityOnly) { this.disabilityOnly = disabilityOnly; }

    public Boolean getSeniorOnly() { return seniorOnly; }
    public void setSeniorOnly(Boolean seniorOnly) { this.seniorOnly = seniorOnly; }

    public String getAllowedStates() { return allowedStates; }
    public void setAllowedStates(String allowedStates) { this.allowedStates = allowedStates; }

    public String getCriteriaDescription() { return criteriaDescription; }
    public void setCriteriaDescription(String criteriaDescription) { this.criteriaDescription = criteriaDescription; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

