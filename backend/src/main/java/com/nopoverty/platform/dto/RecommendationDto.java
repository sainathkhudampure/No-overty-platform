package com.nopoverty.platform.dto;

import java.util.ArrayList;
import java.util.List;

public class RecommendationDto {
    private Long schemeId;
    private String schemeCode;
    private String schemeName;
    private String department;
    private String categoryName;
    private String shortDescription;
    private String benefits;
    private int score; // 0 to 100
    private String matchExplanation;
    private List<String> matchingFactors = new ArrayList<>();
    private String stateAvailability;
    private String officialPortalUrl;
    private String officialApplicationUrl;
    private boolean isSaved;

    public RecommendationDto() {}

    public Long getSchemeId() { return schemeId; }
    public void setSchemeId(Long schemeId) { this.schemeId = schemeId; }

    public String getSchemeCode() { return schemeCode; }
    public void setSchemeCode(String schemeCode) { this.schemeCode = schemeCode; }

    public String getSchemeName() { return schemeName; }
    public void setSchemeName(String schemeName) { this.schemeName = schemeName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public String getBenefits() { return benefits; }
    public void setBenefits(String benefits) { this.benefits = benefits; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public String getMatchExplanation() { return matchExplanation; }
    public void setMatchExplanation(String matchExplanation) { this.matchExplanation = matchExplanation; }

    public List<String> getMatchingFactors() { return matchingFactors; }
    public void setMatchingFactors(List<String> matchingFactors) { this.matchingFactors = matchingFactors; }

    public String getStateAvailability() { return stateAvailability; }
    public void setStateAvailability(String stateAvailability) { this.stateAvailability = stateAvailability; }

    public String getOfficialPortalUrl() { return officialPortalUrl; }
    public void setOfficialPortalUrl(String officialPortalUrl) { this.officialPortalUrl = officialPortalUrl; }

    public String getOfficialApplicationUrl() { return officialApplicationUrl; }
    public void setOfficialApplicationUrl(String officialApplicationUrl) { this.officialApplicationUrl = officialApplicationUrl; }

    public boolean isSaved() { return isSaved; }
    public void setSaved(boolean saved) { isSaved = saved; }
}

