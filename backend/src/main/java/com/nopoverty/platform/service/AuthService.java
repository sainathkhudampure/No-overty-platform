package com.nopoverty.platform.service;

import com.nopoverty.platform.config.JwtTokenProvider;
import com.nopoverty.platform.dto.AuthRequest;
import com.nopoverty.platform.dto.AuthResponse;
import com.nopoverty.platform.dto.SignupRequest;
import com.nopoverty.platform.model.Role;
import com.nopoverty.platform.model.User;
import com.nopoverty.platform.model.UserProfile;
import com.nopoverty.platform.repository.RoleRepository;
import com.nopoverty.platform.repository.UserProfileRepository;
import com.nopoverty.platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public AuthResponse login(AuthRequest request) {
        if (request == null || request.getPassword() == null) {
            throw new BadCredentialsException("Identifier and password are required");
        }
        String identifier = request.getIdentifier() != null ? request.getIdentifier().trim() : "";
        if (identifier.isEmpty()) {
            throw new BadCredentialsException("Email or mobile number is required");
        }

        Optional<User> userOpt = userRepository.findByEmailOrMobile(identifier, identifier);
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByEmail(identifier);
            if (userOpt.isEmpty()) {
                userOpt = userRepository.findByMobile(identifier);
            }
        }

        if (userOpt.isEmpty()) {
            throw new BadCredentialsException("Invalid email or password");
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        Set<String> roleNames = new HashSet<>();
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            for (Role role : user.getRoles()) {
                if (role != null && role.getName() != null) {
                    roleNames.add(role.getName());
                }
            }
        }
        if (roleNames.isEmpty()) {
            roleNames.add("ROLE_USER");
        }

        String rolesString = String.join(",", roleNames);
        String token = jwtTokenProvider.generateTokenForUsername(user.getEmail(), rolesString);

        return new AuthResponse(
                token,
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getMobile(),
                user.getState(),
                user.getDistrict(),
                user.getPreferredLanguage(),
                roleNames
        );
    }

    @Transactional
    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail().trim())) {
            throw new IllegalArgumentException("Email is already registered");
        }
        if (userRepository.existsByMobile(request.getMobile().trim())) {
            throw new IllegalArgumentException("Mobile number is already registered");
        }

        User user = new User();
        user.setFullName(request.getFullName().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setMobile(request.getMobile().trim());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setState(request.getState());
        user.setDistrict(request.getDistrict());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setPreferredLanguage(request.getPreferredLanguage() != null ? request.getPreferredLanguage() : "en");
        user.setStatus("ACTIVE");

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER", "Citizen Beneficiary")));
        roles.add(userRole);

        // Secure Admin guard: public users cannot freely create admin accounts
        if (request.getRequestedRole() != null && request.getRequestedRole().contains("ADMIN")) {
            if ("SDG1_SECURE_ADMIN_PASS_2026".equals(request.getAdminSecretKey())) {
                String roleToAssign = request.getRequestedRole().contains("DEVELOPER") ? "ROLE_DEVELOPER_ADMIN" : "ROLE_ADMIN";
                Role adminRole = roleRepository.findByName(roleToAssign)
                        .orElseGet(() -> roleRepository.save(new Role(roleToAssign, "System Administrator")));
                roles.add(adminRole);
            }
        }

        user.setRoles(roles);
        User savedUser = userRepository.save(user);

        // Create initial empty profile
        UserProfile profile = new UserProfile();
        profile.setUser(savedUser);
        profile.setCompletionPercentage(25);
        userProfileRepository.save(profile);

        Set<String> roleNames = roles.stream().map(Role::getName).collect(Collectors.toSet());
        String token = jwtTokenProvider.generateTokenForUsername(savedUser.getEmail(), String.join(",", roleNames));

        return new AuthResponse(
                token,
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                savedUser.getMobile(),
                savedUser.getState(),
                savedUser.getDistrict(),
                savedUser.getPreferredLanguage(),
                roleNames
        );
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }
}
