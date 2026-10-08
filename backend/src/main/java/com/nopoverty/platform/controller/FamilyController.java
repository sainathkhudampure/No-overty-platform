package com.nopoverty.platform.controller;

import com.nopoverty.platform.dto.ApiResponse;
import com.nopoverty.platform.model.FamilyMember;
import com.nopoverty.platform.model.User;
import com.nopoverty.platform.repository.FamilyMemberRepository;
import com.nopoverty.platform.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/family")
@CrossOrigin(origins = "*")
public class FamilyController {

    @Autowired
    private AuthService authService;

    @Autowired
    private FamilyMemberRepository familyMemberRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getFamily(Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        List<FamilyMember> members = familyMemberRepository.findByUser(user);

        // Calculate family welfare overview
        long studentsCount = members.stream().filter(m -> Boolean.TRUE.equals(m.getStudentStatus()) || (m.getAge() != null && m.getAge() >= 6 && m.getAge() <= 23)).count();
        long seniorsCount = members.stream().filter(m -> Boolean.TRUE.equals(m.getSeniorCitizenStatus()) || (m.getAge() != null && m.getAge() >= 60)).count();
        long disabilityCount = members.stream().filter(m -> Boolean.TRUE.equals(m.getDisabilityStatus())).count();

        Map<String, Object> response = new HashMap<>();
        response.put("members", members);
        response.put("totalMembers", members.size() + 1); // including primary user
        response.put("studentsCount", studentsCount);
        response.put("seniorsCount", seniorsCount);
        response.put("disabilityCount", disabilityCount);

        return ResponseEntity.ok(ApiResponse.success("Family welfare profile", response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FamilyMember>> addMember(@RequestBody FamilyMember member, Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        member.setUser(user);
        if (member.getAge() != null && member.getAge() >= 60) {
            member.setSeniorCitizenStatus(true);
        }
        FamilyMember saved = familyMemberRepository.save(member);
        return ResponseEntity.ok(ApiResponse.success("Family member added", saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteMember(@PathVariable Long id, Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        familyMemberRepository.findById(id).ifPresent(m -> {
            if (m.getUser().getId().equals(user.getId())) {
                familyMemberRepository.delete(m);
            }
        });
        return ResponseEntity.ok(ApiResponse.success("Family member removed", "Deleted"));
    }
}

