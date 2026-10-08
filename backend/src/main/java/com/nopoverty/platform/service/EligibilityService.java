package com.nopoverty.platform.service;

import com.nopoverty.platform.dto.EligibilityRequest;
import com.nopoverty.platform.dto.EligibilityResultDto;
import com.nopoverty.platform.model.Scheme;
import com.nopoverty.platform.model.SchemeEligibilityCriteria;
import com.nopoverty.platform.repository.SchemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class EligibilityService {

    @Autowired
    private SchemeRepository schemeRepository;

    public EligibilityResultDto evaluateEligibility(Long schemeId, EligibilityRequest request) {
        Scheme scheme = schemeRepository.findById(schemeId)
                .orElseThrow(() -> new IllegalArgumentException("Scheme not found with ID: " + schemeId));

        return evaluateScheme(scheme, request);
    }

    public List<EligibilityResultDto> evaluateAllSchemes(EligibilityRequest request) {
        List<Scheme> schemes = schemeRepository.findByIsActiveTrue();
        List<EligibilityResultDto> results = new ArrayList<>();
        for (Scheme s : schemes) {
            results.add(evaluateScheme(s, request));
        }
        return results;
    }

    public EligibilityResultDto evaluateScheme(Scheme scheme, EligibilityRequest request) {
        EligibilityResultDto dto = new EligibilityResultDto();
        dto.setSchemeId(scheme.getId());
        dto.setSchemeCode(scheme.getCode());
        dto.setSchemeName(scheme.getName());
        dto.setDepartment(scheme.getDepartment());
        dto.setOfficialPortalUrl(scheme.getOfficialPortalUrl());
        dto.setOfficialApplicationUrl(scheme.getOfficialApplicationUrl());

        SchemeEligibilityCriteria criteria = scheme.getEligibilityCriteria();
        if (criteria == null) {
            dto.setStatus("YELLOW");
            dto.setStatusLabel("Potentially eligible — additional information required");
            dto.setMatchPercentage(60);
            dto.getPendingCriteria().add("Scheme eligibility details under review. Please refer to official portal guidelines.");
            return dto;
        }

        List<String> matched = new ArrayList<>();
        List<String> failed = new ArrayList<>();
        List<String> pending = new ArrayList<>();

        int totalChecks = 0;

        // 1. Age check
        if (criteria.getMinAge() != null && criteria.getMaxAge() != null &&
            (criteria.getMinAge() > 0 || criteria.getMaxAge() < 100)) {
            totalChecks++;
            if (request.getAge() != null) {
                if (request.getAge() >= criteria.getMinAge() && request.getAge() <= criteria.getMaxAge()) {
                    matched.add("Age (" + request.getAge() + " yrs) is within eligible range (" +
                            criteria.getMinAge() + " - " + criteria.getMaxAge() + " yrs)");
                } else {
                    failed.add("Age (" + request.getAge() + " yrs) is outside eligible range (" +
                            criteria.getMinAge() + " - " + criteria.getMaxAge() + " yrs)");
                }
            } else {
                pending.add("Applicant age must be between " + criteria.getMinAge() + " and " + criteria.getMaxAge() + " years");
            }
        }

        // 2. Income check
        if (criteria.getMaxIncome() != null && criteria.getMaxIncome().compareTo(new BigDecimal("9000000")) < 0) {
            totalChecks++;
            if (request.getAnnualIncome() != null) {
                if (request.getAnnualIncome().compareTo(criteria.getMaxIncome()) <= 0) {
                    matched.add("Annual family income (₹" + request.getAnnualIncome() +
                            ") satisfies the income ceiling (<= ₹" + criteria.getMaxIncome() + ")");
                } else {
                    failed.add("Annual family income (₹" + request.getAnnualIncome() +
                            ") exceeds maximum eligibility ceiling of ₹" + criteria.getMaxIncome());
                }
            } else {
                pending.add("Family income certification required (must be under ₹" + criteria.getMaxIncome() + "/yr)");
            }
        }

        // 3. Gender check
        if (criteria.getRequiredGender() != null && !"ALL".equalsIgnoreCase(criteria.getRequiredGender())) {
            totalChecks++;
            if (request.getGender() != null && !request.getGender().isEmpty()) {
                if (criteria.getRequiredGender().equalsIgnoreCase(request.getGender())) {
                    matched.add("Gender criteria matched (" + criteria.getRequiredGender() + ")");
                } else {
                    failed.add("Scheme is exclusively designed for " + criteria.getRequiredGender() + " beneficiaries");
                }
            } else {
                pending.add("Scheme has specific gender requirement: " + criteria.getRequiredGender());
            }
        }

        // 4. Farmer check
        if (Boolean.TRUE.equals(criteria.getFarmerOnly())) {
            totalChecks++;
            if (Boolean.TRUE.equals(request.getIsFarmer())) {
                matched.add("Applicant is a verified agricultural landholder / farmer");
            } else {
                failed.add("Scheme is specifically restricted to agricultural landholders / farmers");
            }
        }

        // 5. Student check
        if (Boolean.TRUE.equals(criteria.getStudentOnly())) {
            totalChecks++;
            if (Boolean.TRUE.equals(request.getIsStudent())) {
                matched.add("Applicant is currently enrolled as an active student");
            } else {
                failed.add("Scheme is specifically intended for active students");
            }
        }

        // 6. Senior Citizen check
        if (Boolean.TRUE.equals(criteria.getSeniorOnly())) {
            totalChecks++;
            boolean isSenior = Boolean.TRUE.equals(request.getIsSeniorCitizen()) ||
                    (request.getAge() != null && request.getAge() >= 60);
            if (isSenior) {
                matched.add("Applicant satisfies senior citizen qualification (60+ yrs)");
            } else {
                failed.add("Scheme is restricted to senior citizens aged 60 and above");
            }
        }

        // 7. Disability check
        if (Boolean.TRUE.equals(criteria.getDisabilityOnly())) {
            totalChecks++;
            if (Boolean.TRUE.equals(request.getHasDisability())) {
                matched.add("Applicant holds benchmark disability (Divyangjan qualification)");
            } else {
                failed.add("Scheme is reserved for persons with benchmark disabilities");
            }
        }

        // 8. State check
        if (criteria.getAllowedStates() != null && !"All India".equalsIgnoreCase(criteria.getAllowedStates())) {
            totalChecks++;
            if (request.getState() != null && !request.getState().isEmpty()) {
                if (criteria.getAllowedStates().toLowerCase().contains(request.getState().toLowerCase())) {
                    matched.add("State residency requirement matched (" + request.getState() + ")");
                } else {
                    failed.add("Scheme is restricted to residents of: " + criteria.getAllowedStates());
                }
            } else {
                pending.add("Residency verification required for: " + criteria.getAllowedStates());
            }
        }

        if (totalChecks == 0) {
            matched.add("Universal welfare program open to all eligible resident citizens");
            totalChecks = 1;
        }

        dto.setMatchedCriteria(matched);
        dto.setUnmatchedCriteria(failed);
        dto.setPendingCriteria(pending);

        int matchPercent = Math.max(10, Math.min(100, (int) Math.round(((double) matched.size() / Math.max(1, totalChecks)) * 100)));
        dto.setMatchPercentage(matchPercent);

        if (!failed.isEmpty()) {
            dto.setStatus("RED");
            dto.setStatusLabel("Currently appears ineligible");
            dto.setGuidanceNotes("Based on your answers, you do not meet one or more primary eligibility criteria. Review other schemes or verify alternative provisions on the official portal.");
        } else if (!pending.isEmpty()) {
            dto.setStatus("YELLOW");
            dto.setStatusLabel("Potentially eligible — additional information required");
            dto.setGuidanceNotes("You appear potentially eligible! Some details require verification with supporting official documents (e.g. income or age proof).");
        } else {
            dto.setStatus("GREEN");
            dto.setStatusLabel("Likely eligible");
            dto.setGuidanceNotes("Great news! You meet all preliminary criteria based on the information provided. Proceed to review required documents and apply on the official government portal.");
        }

        return dto;
    }
}

