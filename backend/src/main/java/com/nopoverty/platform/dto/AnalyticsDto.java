package com.nopoverty.platform.dto;

import java.util.List;
import java.util.Map;

public class AnalyticsDto {
    private long totalUsers;
    private long totalSchemes;
    private long activeSchemes;
    private long savedSchemesCount;
    private long applicationsTracked;
    private double averageFeedbackRating;

    private Map<String, Long> usersByCategory;
    private Map<String, Long> schemesByCategory;
    private Map<String, Long> applicationsByStatus;
    private Map<String, Long> usersByState;
    private Map<String, Long> monthlyRegistrations;
    private List<Map<String, Object>> mostViewedSchemes;

    public AnalyticsDto() {}

    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }

    public long getTotalSchemes() { return totalSchemes; }
    public void setTotalSchemes(long totalSchemes) { this.totalSchemes = totalSchemes; }

    public long getActiveSchemes() { return activeSchemes; }
    public void setActiveSchemes(long activeSchemes) { this.activeSchemes = activeSchemes; }

    public long getSavedSchemesCount() { return savedSchemesCount; }
    public void setSavedSchemesCount(long savedSchemesCount) { this.savedSchemesCount = savedSchemesCount; }

    public long getApplicationsTracked() { return applicationsTracked; }
    public void setApplicationsTracked(long applicationsTracked) { this.applicationsTracked = applicationsTracked; }

    public double getAverageFeedbackRating() { return averageFeedbackRating; }
    public void setAverageFeedbackRating(double averageFeedbackRating) { this.averageFeedbackRating = averageFeedbackRating; }

    public Map<String, Long> getUsersByCategory() { return usersByCategory; }
    public void setUsersByCategory(Map<String, Long> usersByCategory) { this.usersByCategory = usersByCategory; }

    public Map<String, Long> getSchemesByCategory() { return schemesByCategory; }
    public void setSchemesByCategory(Map<String, Long> schemesByCategory) { this.schemesByCategory = schemesByCategory; }

    public Map<String, Long> getApplicationsByStatus() { return applicationsByStatus; }
    public void setApplicationsByStatus(Map<String, Long> applicationsByStatus) { this.applicationsByStatus = applicationsByStatus; }

    public Map<String, Long> getUsersByState() { return usersByState; }
    public void setUsersByState(Map<String, Long> usersByState) { this.usersByState = usersByState; }

    public Map<String, Long> getMonthlyRegistrations() { return monthlyRegistrations; }
    public void setMonthlyRegistrations(Map<String, Long> monthlyRegistrations) { this.monthlyRegistrations = monthlyRegistrations; }

    public List<Map<String, Object>> getMostViewedSchemes() { return mostViewedSchemes; }
    public void setMostViewedSchemes(List<Map<String, Object>> mostViewedSchemes) { this.mostViewedSchemes = mostViewedSchemes; }
}

