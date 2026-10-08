package com.nopoverty.platform.controller;

import com.nopoverty.platform.dto.ApiResponse;
import com.nopoverty.platform.model.User;
import com.nopoverty.platform.model.UserProfile;
import com.nopoverty.platform.repository.UserProfileRepository;
import com.nopoverty.platform.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "*")
public class ProfileController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<UserProfile>> getProfile(Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        UserProfile profile = userProfileRepository.findByUser(user)
                .orElseGet(() -> {
                    UserProfile p = new UserProfile();
                    p.setUser(user);
                    return userProfileRepository.save(p);
                });
        return ResponseEntity.ok(ApiResponse.success("Profile fetched", profile));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<UserProfile>> updateProfile(@RequestBody UserProfile updated, Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        UserProfile profile = userProfileRepository.findByUser(user)
                .orElseGet(() -> {
                    UserProfile p = new UserProfile();
                    p.setUser(user);
                    return p;
                });

        if (updated.getGender() != null) profile.setGender(updated.getGender());
        if (updated.getAddress() != null) profile.setAddress(updated.getAddress());
        if (updated.getPincode() != null) profile.setPincode(updated.getPincode());
        if (updated.getAnnualFamilyIncome() != null) profile.setAnnualFamilyIncome(updated.getAnnualFamilyIncome());
        if (updated.getEmploymentStatus() != null) profile.setEmploymentStatus(updated.getEmploymentStatus());
        if (updated.getFamilySize() != null) profile.setFamilySize(updated.getFamilySize());
        if (updated.getEducationLevel() != null) profile.setEducationLevel(updated.getEducationLevel());
        if (updated.getStudentStatus() != null) profile.setStudentStatus(updated.getStudentStatus());
        if (updated.getInstitutionType() != null) profile.setInstitutionType(updated.getInstitutionType());
        if (updated.getCourseName() != null) profile.setCourseName(updated.getCourseName());
        if (updated.getFarmerStatus() != null) profile.setFarmerStatus(updated.getFarmerStatus());
        if (updated.getLandSizeAcres() != null) profile.setLandSizeAcres(updated.getLandSizeAcres());
        if (updated.getCropType() != null) profile.setCropType(updated.getCropType());
        if (updated.getDisabilityStatus() != null) profile.setDisabilityStatus(updated.getDisabilityStatus());
        if (updated.getSeniorCitizenStatus() != null) profile.setSeniorCitizenStatus(updated.getSeniorCitizenStatus());
        if (updated.getHouseOwnership() != null) profile.setHouseOwnership(updated.getHouseOwnership());
        if (updated.getAreaType() != null) profile.setAreaType(updated.getAreaType());
        if (updated.getCategorySpecificData() != null) profile.setCategorySpecificData(updated.getCategorySpecificData());

        // Calculate dynamic completion %
        int filled = 0;
        int total = 10;
        if (profile.getGender() != null) filled++;
        if (profile.getAddress() != null) filled++;
        if (profile.getPincode() != null) filled++;
        if (profile.getAnnualFamilyIncome() != null) filled++;
        if (profile.getEmploymentStatus() != null) filled++;
        if (profile.getEducationLevel() != null) filled++;
        if (profile.getHouseOwnership() != null) filled++;
        if (profile.getAreaType() != null) filled++;
        if (user.getState() != null) filled++;
        if (user.getDistrict() != null) filled++;

        int percentage = Math.max(25, (int) Math.round(((double) filled / total) * 100));
        profile.setCompletionPercentage(percentage);

        UserProfile saved = userProfileRepository.save(profile);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", saved));
    }
}

