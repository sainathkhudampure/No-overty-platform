package com.nopoverty.platform.service;

import com.nopoverty.platform.dto.EligibilityRequest;
import com.nopoverty.platform.dto.RecommendationDto;
import com.nopoverty.platform.model.*;
import com.nopoverty.platform.repository.FamilyMemberRepository;
import com.nopoverty.platform.repository.SavedSchemeRepository;
import com.nopoverty.platform.repository.SchemeRepository;
import com.nopoverty.platform.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private SchemeRepository schemeRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private FamilyMemberRepository familyMemberRepository;

    @Autowired
    private SavedSchemeRepository savedSchemeRepository;

    public List<RecommendationDto> getRecommendationsForUser(User user) {
        UserProfile profile = userProfileRepository.findByUser(user).orElse(null);
        List<FamilyMember> family = familyMemberRepository.findByUser(user);
        Set<Long> savedSchemeIds = savedSchemeRepository.findByUser(user).stream()
                .map(s -> s.getScheme().getId())
                .collect(Collectors.toSet());

        EligibilityRequest req = new EligibilityRequest();
        if (user.getDateOfBirth() != null) {
            req.setAge(Period.between(user.getDateOfBirth(), LocalDate.now()).getYears());
        }
        req.setState(user.getState());

        if (profile != null) {
            req.setGender(profile.getGender());
            req.setAnnualIncome(profile.getAnnualFamilyIncome());
            req.setOccupation(profile.getEmploymentStatus());
            req.setEducationLevel(profile.getEducationLevel());
            req.setIsStudent(profile.getStudentStatus());
            req.setIsFarmer(profile.getFarmerStatus());
            req.setHasDisability(profile.getDisabilityStatus());
            req.setIsSeniorCitizen(profile.getSeniorCitizenStatus());
            req.setLandSizeAcres(profile.getLandSizeAcres());
        }

        // Incorporate family insights
        boolean familyHasStudent = family.stream().anyMatch(f -> Boolean.TRUE.equals(f.getStudentStatus()) || (f.getAge() != null && f.getAge() >= 6 && f.getAge() <= 23));
        boolean familyHasSenior = family.stream().anyMatch(f -> Boolean.TRUE.equals(f.getSeniorCitizenStatus()) || (f.getAge() != null && f.getAge() >= 60));
        boolean familyHasDisability = family.stream().anyMatch(f -> Boolean.TRUE.equals(f.getDisabilityStatus()));

        List<Scheme> allSchemes = schemeRepository.findByIsActiveTrue();
        List<RecommendationDto> dtos = new ArrayList<>();

        for (Scheme s : allSchemes) {
            RecommendationDto dto = computeScore(s, req, familyHasStudent, familyHasSenior, familyHasDisability);
            dto.setSaved(savedSchemeIds.contains(s.getId()));
            dtos.add(dto);
        }

        dtos.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
        return dtos;
    }

    public List<RecommendationDto> getRecommendationsForRequest(EligibilityRequest req) {
        List<Scheme> allSchemes = schemeRepository.findByIsActiveTrue();
        List<RecommendationDto> dtos = new ArrayList<>();

        for (Scheme s : allSchemes) {
            RecommendationDto dto = computeScore(s, req, false, false, false);
            dtos.add(dto);
        }

        dtos.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
        return dtos;
    }

    private RecommendationDto computeScore(Scheme scheme, EligibilityRequest req,
                                           boolean familyHasStudent, boolean familyHasSenior, boolean familyHasDisability) {
        RecommendationDto dto = new RecommendationDto();
        dto.setSchemeId(scheme.getId());
        dto.setSchemeCode(scheme.getCode());
        dto.setSchemeName(scheme.getName());
        dto.setDepartment(scheme.getDepartment());
        dto.setShortDescription(scheme.getShortDescription());
        dto.setBenefits(scheme.getBenefits());
        dto.setStateAvailability(scheme.getStateAvailability());
        dto.setOfficialPortalUrl(scheme.getOfficialPortalUrl());
        dto.setOfficialApplicationUrl(scheme.getOfficialApplicationUrl());

        if (!scheme.getCategories().isEmpty()) {
            dto.setCategoryName(scheme.getCategories().iterator().next().getName());
        }

        int score = 40; // baseline interest
        List<String> factors = new ArrayList<>();
        SchemeEligibilityCriteria crit = scheme.getEligibilityCriteria();

        if (crit != null) {
            // Income Match
            if (req.getAnnualIncome() != null && crit.getMaxIncome() != null) {
                if (req.getAnnualIncome().compareTo(crit.getMaxIncome()) <= 0) {
                    score += 20;
                    factors.add("Income meets eligible bracket (<= ₹" + crit.getMaxIncome().intValue() + ")");
                } else if (crit.getMaxIncome().compareTo(new BigDecimal("9000000")) < 0) {
                    score -= 35;
                }
            }

            // Farmer Match
            if (Boolean.TRUE.equals(crit.getFarmerOnly())) {
                if (Boolean.TRUE.equals(req.getIsFarmer())) {
                    score += 30;
                    factors.add("Agricultural landowner / farmer profile match");
                } else {
                    score -= 40;
                }
            }

            // Student Match
            if (Boolean.TRUE.equals(crit.getStudentOnly())) {
                if (Boolean.TRUE.equals(req.getIsStudent())) {
                    score += 30;
                    factors.add("Student enrollment & educational stage match");
                } else if (familyHasStudent) {
                    score += 15;
                    factors.add("Dependent student identified in your family profile");
                } else {
                    score -= 40;
                }
            }

            // Senior Citizen Match
            if (Boolean.TRUE.equals(crit.getSeniorOnly())) {
                boolean userIsSenior = Boolean.TRUE.equals(req.getIsSeniorCitizen()) || (req.getAge() != null && req.getAge() >= 60);
                if (userIsSenior) {
                    score += 30;
                    factors.add("Senior citizen qualification (60+ years)");
                } else if (familyHasSenior) {
                    score += 15;
                    factors.add("Elderly dependent present in family");
                } else {
                    score -= 40;
                }
            }

            // Disability Match
            if (Boolean.TRUE.equals(crit.getDisabilityOnly())) {
                if (Boolean.TRUE.equals(req.getHasDisability())) {
                    score += 35;
                    factors.add("Divyangjan / benchmark disability criteria match");
                } else if (familyHasDisability) {
                    score += 15;
                    factors.add("Family member with disability identified");
                } else {
                    score -= 40;
                }
            }

            // Gender Match
            if (crit.getRequiredGender() != null && !"ALL".equalsIgnoreCase(crit.getRequiredGender())) {
                if (crit.getRequiredGender().equalsIgnoreCase(req.getGender())) {
                    score += 15;
                    factors.add("Gender-specific target assistance (" + crit.getRequiredGender() + ")");
                } else if (req.getGender() != null) {
                    score -= 30;
                }
            }

            // State Match
            if (crit.getAllowedStates() != null && !"All India".equalsIgnoreCase(crit.getAllowedStates())) {
                if (req.getState() != null && crit.getAllowedStates().toLowerCase().contains(req.getState().toLowerCase())) {
                    score += 10;
                    factors.add("State residency requirement matched (" + req.getState() + ")");
                }
            } else {
                score += 5;
            }
        }

        // Clamp score between 10% and 98%
        int finalScore = Math.max(15, Math.min(98, score));
        dto.setScore(finalScore);
        dto.setMatchingFactors(factors);

        if (!factors.isEmpty()) {
            dto.setMatchExplanation("Recommended because your profile matches: " + String.join(", ", factors) + ".");
        } else {
            dto.setMatchExplanation("Recommended based on public welfare assistance guidelines and general demographic suitability.");
        }

        return dto;
    }
}

