package com.nopoverty.platform.controller;

import com.nopoverty.platform.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, Object>>> health() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("app", "NO POVERTY Welfare Assistance Platform");
        status.put("sdg", "SDG 1: NO POVERTY");
        status.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(ApiResponse.success("System is operational", status));
    }
}

