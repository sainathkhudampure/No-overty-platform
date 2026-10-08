package com.nopoverty.platform;

import com.nopoverty.platform.dto.EligibilityRequest;
import com.nopoverty.platform.dto.EligibilityResultDto;
import com.nopoverty.platform.model.Scheme;
import com.nopoverty.platform.model.SchemeEligibilityCriteria;
import com.nopoverty.platform.service.EligibilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class EligibilityServiceTest {

    private EligibilityService eligibilityService;
    private Scheme studentScheme;

    @BeforeEach
    void setUp() {
        eligibilityService = new EligibilityService();

        studentScheme = new Scheme();
        studentScheme.setId(100L);
        studentScheme.setCode("TEST-SCHOLARSHIP");
        studentScheme.setName("Higher Education Scholarship");
        studentScheme.setDepartment("Ministry of Education");

        SchemeEligibilityCriteria criteria = new SchemeEligibilityCriteria();
        criteria.setMinAge(16);
        criteria.setMaxAge(30);
        criteria.setMaxIncome(new BigDecimal("250000.00"));
        criteria.setStudentOnly(true);
        criteria.setFarmerOnly(false);
        criteria.setRequiredGender("ALL");
        criteria.setAllowedStates("All India");

        studentScheme.setEligibilityCriteria(criteria);
    }

    @Test
    void testEligibleStudentYieldsGreenStatus() {
        EligibilityRequest req = new EligibilityRequest();
        req.setAge(20);
        req.setAnnualIncome(new BigDecimal("120000.00"));
        req.setIsStudent(true);
        req.setGender("FEMALE");
        req.setState("Karnataka");

        EligibilityResultDto result = eligibilityService.evaluateScheme(studentScheme, req);

        assertEquals("GREEN", result.getStatus());
        assertEquals("Likely eligible", result.getStatusLabel());
        assertTrue(result.getMatchPercentage() >= 80);
        assertTrue(result.getUnmatchedCriteria().isEmpty());
    }

    @Test
    void testIneligibleHighIncomeYieldsRedStatus() {
        EligibilityRequest req = new EligibilityRequest();
        req.setAge(20);
        req.setAnnualIncome(new BigDecimal("500000.00")); // Above 2.5L limit
        req.setIsStudent(true);

        EligibilityResultDto result = eligibilityService.evaluateScheme(studentScheme, req);

        assertEquals("RED", result.getStatus());
        assertEquals("Currently appears ineligible", result.getStatusLabel());
        assertFalse(result.getUnmatchedCriteria().isEmpty());
    }

    @Test
    void testMissingInfoYieldsYellowStatus() {
        EligibilityRequest req = new EligibilityRequest();
        // Leaving income null, age null, student flag true
        req.setIsStudent(true);

        EligibilityResultDto result = eligibilityService.evaluateScheme(studentScheme, req);

        assertEquals("YELLOW", result.getStatus());
        assertEquals("Potentially eligible — additional information required", result.getStatusLabel());
        assertFalse(result.getPendingCriteria().isEmpty());
    }
}

