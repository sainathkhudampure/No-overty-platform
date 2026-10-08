package com.nopoverty.platform.controller;

import com.nopoverty.platform.dto.ApiResponse;
import com.nopoverty.platform.model.ApplicationRecord;
import com.nopoverty.platform.model.Scheme;
import com.nopoverty.platform.model.User;
import com.nopoverty.platform.repository.ApplicationRecordRepository;
import com.nopoverty.platform.repository.SchemeRepository;
import com.nopoverty.platform.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
public class ApplicationController {

    @Autowired
    private AuthService authService;

    @Autowired
    private ApplicationRecordRepository applicationRecordRepository;

    @Autowired
    private SchemeRepository schemeRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ApplicationRecord>>> getApplications(Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        List<ApplicationRecord> apps = applicationRecordRepository.findByUser(user);
        return ResponseEntity.ok(ApiResponse.success("Applications list", apps));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ApplicationRecord>> createApplication(
            @RequestParam Long schemeId,
            @RequestParam String applicationNumber,
            @RequestParam(defaultValue = "Submitted") String status,
            @RequestParam(required = false) String appliedDate,
            @RequestParam(required = false) String notes,
            Authentication authentication) {

        User user = authService.getUserByEmail(authentication.getName());
        Scheme scheme = schemeRepository.findById(schemeId)
                .orElseThrow(() -> new IllegalArgumentException("Scheme not found with ID: " + schemeId));

        ApplicationRecord app = new ApplicationRecord();
        app.setUser(user);
        app.setScheme(scheme);
        app.setApplicationNumber(applicationNumber.trim());
        app.setStatus(status);
        app.setAppliedDate(appliedDate != null ? LocalDate.parse(appliedDate) : LocalDate.now());
        app.setNotes(notes);
        app.setOfficialPortalAcknowledged(true);

        ApplicationRecord saved = applicationRecordRepository.save(app);
        return ResponseEntity.ok(ApiResponse.success("Application tracked successfully", saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ApplicationRecord>> updateApplication(
            @PathVariable Long id,
            @RequestBody ApplicationRecord update,
            Authentication authentication) {

        User user = authService.getUserByEmail(authentication.getName());
        ApplicationRecord app = applicationRecordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application record not found: " + id));

        if (!app.getUser().getId().equals(user.getId())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Unauthorized access to application record"));
        }

        if (update.getStatus() != null) app.setStatus(update.getStatus());
        if (update.getApplicationNumber() != null) app.setApplicationNumber(update.getApplicationNumber());
        if (update.getNotes() != null) app.setNotes(update.getNotes());
        if (update.getVerificationDate() != null) app.setVerificationDate(update.getVerificationDate());

        ApplicationRecord saved = applicationRecordRepository.save(app);
        return ResponseEntity.ok(ApiResponse.success("Application updated", saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteApplication(
            @PathVariable Long id, Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        applicationRecordRepository.findById(id).ifPresent(app -> {
            if (app.getUser().getId().equals(user.getId())) {
                applicationRecordRepository.delete(app);
            }
        });
        return ResponseEntity.ok(ApiResponse.success("Application tracker record deleted", "Deleted"));
    }
}

