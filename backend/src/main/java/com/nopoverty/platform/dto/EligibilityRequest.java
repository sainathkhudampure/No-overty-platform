package com.nopoverty.platform.dto;

import java.math.BigDecimal;

public class EligibilityRequest {
    private Integer age;
    private String gender; // MALE, FEMALE, OTHER, ALL
    private BigDecimal annualIncome;
    private String occupation;
    private String educationLevel;
    private String state;
    private Boolean isStudent = false;
    private Boolean isFarmer = false;
    private Boolean hasDisability = false;
    private Boolean isSeniorCitizen = false;
    private BigDecimal landSizeAcres = BigDecimal.ZERO;
    private String categoryCode;

    public EligibilityRequest() {}

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public BigDecimal getAnnualIncome() { return annualIncome; }
    public void setAnnualIncome(BigDecimal annualIncome) { this.annualIncome = annualIncome; }

    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }

    public String getEducationLevel() { return educationLevel; }
    public void setEducationLevel(String educationLevel) { this.educationLevel = educationLevel; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public Boolean getIsStudent() { return isStudent != null && isStudent; }
    public void setIsStudent(Boolean student) { isStudent = student; }

    public Boolean getIsFarmer() { return isFarmer != null && isFarmer; }
    public void setIsFarmer(Boolean farmer) { isFarmer = farmer; }

    public Boolean getHasDisability() { return hasDisability != null && hasDisability; }
    public void setHasDisability(Boolean hasDisability) { this.hasDisability = hasDisability; }

    public Boolean getIsSeniorCitizen() { return isSeniorCitizen != null && isSeniorCitizen; }
    public void setIsSeniorCitizen(Boolean seniorCitizen) { isSeniorCitizen = seniorCitizen; }

    public BigDecimal getLandSizeAcres() { return landSizeAcres; }
    public void setLandSizeAcres(BigDecimal landSizeAcres) { this.landSizeAcres = landSizeAcres; }

    public String getCategoryCode() { return categoryCode; }
    public void setCategoryCode(String categoryCode) { this.categoryCode = categoryCode; }
}

