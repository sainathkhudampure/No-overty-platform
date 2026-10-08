package com.nopoverty.platform.controller;

import com.nopoverty.platform.dto.ApiResponse;
import com.nopoverty.platform.dto.EligibilityRequest;
import com.nopoverty.platform.dto.RecommendationDto;
import com.nopoverty.platform.model.User;
import com.nopoverty.platform.service.AuthService;
import com.nopoverty.platform.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins = "*")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private AuthService authService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RecommendationDto>>> getPersonalizedRecommendations(
            Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            User user = authService.getUserByEmail(authentication.getName());
            List<RecommendationDto> recommendations = recommendationService.getRecommendationsForUser(user);
            return ResponseEntity.ok(ApiResponse.success("Personalized recommendations", recommendations));
        }
        // Fallback for public demo
        List<RecommendationDto> publicRecs = recommendationService.getRecommendationsForRequest(new EligibilityRequest());
        return ResponseEntity.ok(ApiResponse.success("General recommendations", publicRecs));
    }

    @PostMapping("/custom")
    public ResponseEntity<ApiResponse<List<RecommendationDto>>> getRecommendationsForCriteria(
            @RequestBody EligibilityRequest request) {
        List<RecommendationDto> recommendations = recommendationService.getRecommendationsForRequest(request);
        return ResponseEntity.ok(ApiResponse.success("Matching recommendations", recommendations));
    }
}

