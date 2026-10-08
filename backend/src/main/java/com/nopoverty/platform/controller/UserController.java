package com.nopoverty.platform.controller;

import com.nopoverty.platform.dto.ApiResponse;
import com.nopoverty.platform.model.User;
import com.nopoverty.platform.model.UserProfile;
import com.nopoverty.platform.repository.UserProfileRepository;
import com.nopoverty.platform.repository.UserRepository;
import com.nopoverty.platform.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> getMe(Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("User fetched successfully", user));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<User>> updateMe(@RequestBody User update, Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        if (update.getFullName() != null) user.setFullName(update.getFullName());
        if (update.getMobile() != null) user.setMobile(update.getMobile());
        if (update.getState() != null) user.setState(update.getState());
        if (update.getDistrict() != null) user.setDistrict(update.getDistrict());
        if (update.getDateOfBirth() != null) user.setDateOfBirth(update.getDateOfBirth());
        if (update.getPreferredLanguage() != null) user.setPreferredLanguage(update.getPreferredLanguage());

        User saved = userRepository.save(user);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", saved));
    }
}

