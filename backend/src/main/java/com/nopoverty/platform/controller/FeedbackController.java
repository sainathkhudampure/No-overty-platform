package com.nopoverty.platform.controller;

import com.nopoverty.platform.dto.ApiResponse;
import com.nopoverty.platform.model.Feedback;
import com.nopoverty.platform.model.Scheme;
import com.nopoverty.platform.model.User;
import com.nopoverty.platform.repository.FeedbackRepository;
import com.nopoverty.platform.repository.SchemeRepository;
import com.nopoverty.platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "*")
public class FeedbackController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private SchemeRepository schemeRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/scheme/{schemeId}")
    public ResponseEntity<ApiResponse<List<Feedback>>> getSchemeFeedback(@PathVariable Long schemeId) {
        List<Feedback> feedbackList = feedbackRepository.findBySchemeId(schemeId);
        return ResponseEntity.ok(ApiResponse.success("Scheme feedback", feedbackList));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Feedback>> submitFeedback(
            @RequestBody Feedback feedback,
            Authentication authentication) {

        if (authentication != null && authentication.isAuthenticated()) {
            userRepository.findByEmail(authentication.getName()).ifPresent(feedback::setUser);
        }

        if (feedback.getScheme() != null && feedback.getScheme().getId() != null) {
            schemeRepository.findById(feedback.getScheme().getId()).ifPresent(feedback::setScheme);
        }

        Feedback saved = feedbackRepository.save(feedback);
        return ResponseEntity.ok(ApiResponse.success("Feedback submitted successfully", saved));
    }
}

