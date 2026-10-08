package com.nopoverty.platform.controller;

import com.nopoverty.platform.dto.ApiResponse;
import com.nopoverty.platform.model.Notification;
import com.nopoverty.platform.model.User;
import com.nopoverty.platform.repository.NotificationRepository;
import com.nopoverty.platform.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    @Autowired
    private AuthService authService;

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Notification>>> getNotifications(Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        List<Notification> list = notificationRepository.findForUserOrGlobal(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Notifications", list));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<String>> markRead(@PathVariable Long id, Authentication authentication) {
        notificationRepository.findById(id).ifPresent(n -> {
            n.setIsRead(true);
            notificationRepository.save(n);
        });
        return ResponseEntity.ok(ApiResponse.success("Notification marked as read", "Updated"));
    }

    @PutMapping("/read-all")
    public ResponseEntity<ApiResponse<String>> markAllRead(Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        List<Notification> list = notificationRepository.findForUserOrGlobal(user.getId());
        for (Notification n : list) {
            n.setIsRead(true);
        }
        notificationRepository.saveAll(list);
        return ResponseEntity.ok(ApiResponse.success("All notifications marked as read", "Updated"));
    }
}

