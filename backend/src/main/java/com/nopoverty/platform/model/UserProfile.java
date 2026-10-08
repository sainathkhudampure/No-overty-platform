package com.nopoverty.platform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "profiles")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonIgnore
    private User user;

    @Column(length = 20)
    private String gender; // MALE, FEMALE, OTHER

    @Column(length = 255)
    private String address;

    @Column(length = 10)
    private String pincode;

    @Column(precision = 12, scale = 2)
    private BigDecimal annualFamilyIncome = BigDecimal.ZERO;

    @Column(length = 50)
    private String employmentStatus; // EMPLOYED, UNEMPLOYED, SELF_EMPLOYED, STUDENT

    private Integer familySize = 1;

    @Column(length = 50)
    private String educationLevel; // PRIMARY, SECONDARY, HIGHER_SECONDARY, UNDERGRADUATE, POSTGRADUATE, DIPLOMA

    private Boolean studentStatus = false;

    @Column(length = 100)
    private String institutionType;

    @Column(length = 150)
    private String courseName;

    private Boolean farmerStatus = false;

    @Column(precision = 6, scale = 2)
    private BigDecimal landSizeAcres = BigDecimal.ZERO;

    @Column(length = 100)
    private String cropType;

    private Boolean disabilityStatus = false;
    private Boolean seniorCitizenStatus = false;

    @Column(length = 50)
    private String houseOwnership; // OWNED_PUCCA, OWNED_KUTCHA, RENTED, HOMELESS

    @Column(length = 20)
    private String areaType = "RURAL"; // RURAL, URBAN

    @Column(columnDefinition = "TEXT")
    private String categorySpecificData;

    private Integer completionPercentage = 20;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public UserProfile() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public BigDecimal getAnnualFamilyIncome() { return annualFamilyIncome; }
    public void setAnnualFamilyIncome(BigDecimal annualFamilyIncome) { this.annualFamilyIncome = annualFamilyIncome; }

    public String getEmploymentStatus() { return employmentStatus; }
    public void setEmploymentStatus(String employmentStatus) { this.employmentStatus = employmentStatus; }

    public Integer getFamilySize() { return familySize; }
    public void setFamilySize(Integer familySize) { this.familySize = familySize; }

    public String getEducationLevel() { return educationLevel; }
    public void setEducationLevel(String educationLevel) { this.educationLevel = educationLevel; }

    public Boolean getStudentStatus() { return studentStatus; }
    public void setStudentStatus(Boolean studentStatus) { this.studentStatus = studentStatus; }

    public String getInstitutionType() { return institutionType; }
    public void setInstitutionType(String institutionType) { this.institutionType = institutionType; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public Boolean getFarmerStatus() { return farmerStatus; }
    public void setFarmerStatus(Boolean farmerStatus) { this.farmerStatus = farmerStatus; }

    public BigDecimal getLandSizeAcres() { return landSizeAcres; }
    public void setLandSizeAcres(BigDecimal landSizeAcres) { this.landSizeAcres = landSizeAcres; }

    public String getCropType() { return cropType; }
    public void setCropType(String cropType) { this.cropType = cropType; }

    public Boolean getDisabilityStatus() { return disabilityStatus; }
    public void setDisabilityStatus(Boolean disabilityStatus) { this.disabilityStatus = disabilityStatus; }

    public Boolean getSeniorCitizenStatus() { return seniorCitizenStatus; }
    public void setSeniorCitizenStatus(Boolean seniorCitizenStatus) { this.seniorCitizenStatus = seniorCitizenStatus; }

    public String getHouseOwnership() { return houseOwnership; }
    public void setHouseOwnership(String houseOwnership) { this.houseOwnership = houseOwnership; }

    public String getAreaType() { return areaType; }
    public void setAreaType(String areaType) { this.areaType = areaType; }

    public String getCategorySpecificData() { return categorySpecificData; }
    public void setCategorySpecificData(String categorySpecificData) { this.categorySpecificData = categorySpecificData; }

    public Integer getCompletionPercentage() { return completionPercentage; }
    public void setCompletionPercentage(Integer completionPercentage) { this.completionPercentage = completionPercentage; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PreUpdate
    public void onUpdate() { this.updatedAt = LocalDateTime.now(); }
}

