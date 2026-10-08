package com.nopoverty.platform.controller;

import com.nopoverty.platform.dto.ApiResponse;
import com.nopoverty.platform.model.SavedScheme;
import com.nopoverty.platform.model.Scheme;
import com.nopoverty.platform.model.User;
import com.nopoverty.platform.repository.SavedSchemeRepository;
import com.nopoverty.platform.repository.SchemeRepository;
import com.nopoverty.platform.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/saved-schemes")
@CrossOrigin(origins = "*")
public class SavedSchemeController {

    @Autowired
    private AuthService authService;

    @Autowired
    private SavedSchemeRepository savedSchemeRepository;

    @Autowired
    private SchemeRepository schemeRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Scheme>>> getSavedSchemes(Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        List<Scheme> schemes = savedSchemeRepository.findByUser(user).stream()
                .map(SavedScheme::getScheme)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Saved schemes", schemes));
    }

    @PostMapping("/{schemeId}")
    public ResponseEntity<ApiResponse<String>> saveScheme(@PathVariable Long schemeId, Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        Scheme scheme = schemeRepository.findById(schemeId)
                .orElseThrow(() -> new IllegalArgumentException("Scheme not found with ID: " + schemeId));

        if (!savedSchemeRepository.existsByUserIdAndSchemeId(user.getId(), schemeId)) {
            savedSchemeRepository.save(new SavedScheme(user, scheme));
            scheme.setSavedCount(scheme.getSavedCount() != null ? scheme.getSavedCount() + 1 : 1);
            schemeRepository.save(scheme);
        }
        return ResponseEntity.ok(ApiResponse.success("Scheme saved successfully", "Saved"));
    }

    @DeleteMapping("/{schemeId}")
    @Transactional
    public ResponseEntity<ApiResponse<String>> unsaveScheme(@PathVariable Long schemeId, Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        savedSchemeRepository.deleteByUserIdAndSchemeId(user.getId(), schemeId);
        return ResponseEntity.ok(ApiResponse.success("Scheme removed from saved list", "Removed"));
    }
}

