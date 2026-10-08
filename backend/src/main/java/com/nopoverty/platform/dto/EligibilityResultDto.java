package com.nopoverty.platform.dto;

import java.util.ArrayList;
import java.util.List;

public class EligibilityResultDto {
    private Long schemeId;
    private String schemeCode;
    private String schemeName;
    private String department;
    private String status; // GREEN, YELLOW, RED
    private String statusLabel; // Likely eligible / Potentially eligible / Currently appears ineligible
    private int matchPercentage;
    private List<String> matchedCriteria = new ArrayList<>();
    private List<String> unmatchedCriteria = new ArrayList<>();
    private List<String> pendingCriteria = new ArrayList<>();
    private String guidanceNotes;
    private String disclaimer = "Based on the information provided. Always verify official eligibility guidelines on the official government portal before applying. This result does not constitute an official government determination.";
    private String officialPortalUrl;
    private String officialApplicationUrl;

    public EligibilityResultDto() {}

    public Long getSchemeId() { return schemeId; }
    public void setSchemeId(Long schemeId) { this.schemeId = schemeId; }

    public String getSchemeCode() { return schemeCode; }
    public void setSchemeCode(String schemeCode) { this.schemeCode = schemeCode; }

    public String getSchemeName() { return schemeName; }
    public void setSchemeName(String schemeName) { this.schemeName = schemeName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getStatusLabel() { return statusLabel; }
    public void setStatusLabel(String statusLabel) { this.statusLabel = statusLabel; }

    public int getMatchPercentage() { return matchPercentage; }
    public void setMatchPercentage(int matchPercentage) { this.matchPercentage = matchPercentage; }

    public List<String> getMatchedCriteria() { return matchedCriteria; }
    public void setMatchedCriteria(List<String> matchedCriteria) { this.matchedCriteria = matchedCriteria; }

    public List<String> getUnmatchedCriteria() { return unmatchedCriteria; }
    public void setUnmatchedCriteria(List<String> unmatchedCriteria) { this.unmatchedCriteria = unmatchedCriteria; }

    public List<String> getPendingCriteria() { return pendingCriteria; }
    public void setPendingCriteria(List<String> pendingCriteria) { this.pendingCriteria = pendingCriteria; }

    public String getGuidanceNotes() { return guidanceNotes; }
    public void setGuidanceNotes(String guidanceNotes) { this.guidanceNotes = guidanceNotes; }

    public String getDisclaimer() { return disclaimer; }
    public void setDisclaimer(String disclaimer) { this.disclaimer = disclaimer; }

    public String getOfficialPortalUrl() { return officialPortalUrl; }
    public void setOfficialPortalUrl(String officialPortalUrl) { this.officialPortalUrl = officialPortalUrl; }

    public String getOfficialApplicationUrl() { return officialApplicationUrl; }
    public void setOfficialApplicationUrl(String officialApplicationUrl) { this.officialApplicationUrl = officialApplicationUrl; }
}

