package com.nopoverty.platform.controller;

import com.nopoverty.platform.dto.AnalyticsDto;
import com.nopoverty.platform.dto.ApiResponse;
import com.nopoverty.platform.model.*;
import com.nopoverty.platform.repository.*;
import com.nopoverty.platform.service.AnalyticsService;
import com.nopoverty.platform.service.SchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DEVELOPER_ADMIN')")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AnalyticsService analyticsService;

    @Autowired
    private SchemeService schemeService;

    @Autowired
    private SchemeRepository schemeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationRecordRepository applicationRecordRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/analytics")
    public ResponseEntity<ApiResponse<AnalyticsDto>> getAnalytics() {
        AnalyticsDto analytics = analyticsService.computeAnalytics();
        return ResponseEntity.ok(ApiResponse.success("Analytics retrieved", analytics));
    }

    @GetMapping("/schemes")
    public ResponseEntity<ApiResponse<List<Scheme>>> getAllSchemes() {
        List<Scheme> list = schemeRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success("All schemes retrieved", list));
    }

    @PostMapping("/schemes")
    public ResponseEntity<ApiResponse<Scheme>> createScheme(
            @RequestBody Scheme scheme,
            @RequestParam(required = false) List<String> categories) {
        Scheme created = schemeService.createOrUpdateScheme(scheme, categories);
        return ResponseEntity.ok(ApiResponse.success("Scheme created successfully", created));
    }

    @PutMapping("/schemes/{id}")
    public ResponseEntity<ApiResponse<Scheme>> updateScheme(
            @PathVariable Long id,
            @RequestBody Scheme update,
            @RequestParam(required = false) List<String> categories) {
        Scheme existing = schemeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Scheme not found with ID: " + id));

        if (update.getName() != null) existing.setName(update.getName());
        if (update.getDepartment() != null) existing.setDepartment(update.getDepartment());
        if (update.getShortDescription() != null) existing.setShortDescription(update.getShortDescription());
        if (update.getFullDescription() != null) existing.setFullDescription(update.getFullDescription());
        if (update.getObjective() != null) existing.setObjective(update.getObjective());
        if (update.getBenefits() != null) existing.setBenefits(update.getBenefits());
        if (update.getApplicationProcess() != null) existing.setApplicationProcess(update.getApplicationProcess());
        if (update.getStateAvailability() != null) existing.setStateAvailability(update.getStateAvailability());
        if (update.getOfficialPortalUrl() != null) existing.setOfficialPortalUrl(update.getOfficialPortalUrl());
        if (update.getOfficialApplicationUrl() != null) existing.setOfficialApplicationUrl(update.getOfficialApplicationUrl());
        if (update.getIsActive() != null) existing.setIsActive(update.getIsActive());

        Scheme saved = schemeService.createOrUpdateScheme(existing, categories);
        return ResponseEntity.ok(ApiResponse.success("Scheme updated successfully", saved));
    }

    @DeleteMapping("/schemes/{id}")
    public ResponseEntity<ApiResponse<String>> deleteScheme(@PathVariable Long id) {
        schemeService.deleteScheme(id);
        return ResponseEntity.ok(ApiResponse.success("Scheme deleted successfully", "Deleted"));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success("All users retrieved", users));
    }

    @PutMapping("/users/{id}/status")
    public ResponseEntity<ApiResponse<User>> updateUserStatus(
            @PathVariable Long id, @RequestBody Map<String, String> statusBody) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        String status = statusBody.get("status");
        if (status != null) {
            user.setStatus(status);
            userRepository.save(user);
        }
        return ResponseEntity.ok(ApiResponse.success("User status updated", user));
    }

    @GetMapping("/applications")
    public ResponseEntity<ApiResponse<List<ApplicationRecord>>> getAllApplications() {
        List<ApplicationRecord> apps = applicationRecordRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success("All applications retrieved", apps));
    }

    @PutMapping("/applications/{id}/status")
    public ResponseEntity<ApiResponse<ApplicationRecord>> updateApplicationStatus(
            @PathVariable Long id, @RequestBody Map<String, String> statusBody) {
        ApplicationRecord app = applicationRecordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application not found with ID: " + id));
        String status = statusBody.get("status");
        if (status != null) {
            app.setStatus(status);
            applicationRecordRepository.save(app);
        }
        return ResponseEntity.ok(ApiResponse.success("Application status updated", app));
    }

    @PostMapping("/notifications/broadcast")
    public ResponseEntity<ApiResponse<Notification>> broadcastNotification(
            @RequestBody Notification notification) {
        notification.setUser(null); // global broadcast
        Notification saved = notificationRepository.save(notification);
        return ResponseEntity.ok(ApiResponse.success("Broadcast sent to all citizens", saved));
    }
}

