package com.nopoverty.platform.dto;

import java.util.List;
import java.util.Map;

public class AiQuestionRequest {
    private String question;
    private Long schemeId;
    private String userCategory;

    public AiQuestionRequest() {}

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public Long getSchemeId() { return schemeId; }
    public void setSchemeId(Long schemeId) { this.schemeId = schemeId; }

    public String getUserCategory() { return userCategory; }
    public void setUserCategory(String userCategory) { this.userCategory = userCategory; }
}

