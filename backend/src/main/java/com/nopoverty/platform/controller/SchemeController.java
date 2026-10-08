package com.nopoverty.platform.controller;

import com.nopoverty.platform.dto.ApiResponse;
import com.nopoverty.platform.dto.EligibilityRequest;
import com.nopoverty.platform.dto.EligibilityResultDto;
import com.nopoverty.platform.model.Scheme;
import com.nopoverty.platform.service.EligibilityService;
import com.nopoverty.platform.service.SchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schemes")
@CrossOrigin(origins = "*")
public class SchemeController {

    @Autowired
    private SchemeService schemeService;

    @Autowired
    private EligibilityService eligibilityService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Scheme>>> getSchemes(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String state) {
        List<Scheme> schemes = schemeService.searchSchemes(keyword, category, state);
        return ResponseEntity.ok(ApiResponse.success("Schemes retrieved", schemes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Scheme>> getSchemeById(@PathVariable Long id) {
        return schemeService.getSchemeById(id)
                .map(scheme -> {
                    schemeService.incrementViews(id);
                    return ResponseEntity.ok(ApiResponse.success("Scheme details retrieved", scheme));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<Scheme>> getSchemeByCode(@PathVariable String code) {
        return schemeService.getSchemeByCode(code)
                .map(scheme -> ResponseEntity.ok(ApiResponse.success("Scheme details retrieved", scheme)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/eligibility")
    public ResponseEntity<ApiResponse<EligibilityResultDto>> checkSchemeEligibility(
            @PathVariable Long id, @RequestBody EligibilityRequest request) {
        EligibilityResultDto result = eligibilityService.evaluateEligibility(id, request);
        return ResponseEntity.ok(ApiResponse.success("Eligibility evaluated", result));
    }

    @PostMapping("/check-eligibility")
    public ResponseEntity<ApiResponse<List<EligibilityResultDto>>> checkAllEligibility(
            @RequestBody EligibilityRequest request) {
        List<EligibilityResultDto> results = eligibilityService.evaluateAllSchemes(request);
        return ResponseEntity.ok(ApiResponse.success("Eligibility evaluated across all schemes", results));
    }

    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<List<Scheme>>> getPopularSchemes() {
        return ResponseEntity.ok(ApiResponse.success("Popular schemes", schemeService.getMostViewedSchemes()));
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<Scheme>>> getRecentSchemes() {
        return ResponseEntity.ok(ApiResponse.success("Recently added schemes", schemeService.getRecentSchemes()));
    }
}

