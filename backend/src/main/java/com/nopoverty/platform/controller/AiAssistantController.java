package com.nopoverty.platform.controller;

import com.nopoverty.platform.dto.AiQuestionRequest;
import com.nopoverty.platform.dto.ApiResponse;
import com.nopoverty.platform.service.AiAssistantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AiAssistantController {

    @Autowired
    private AiAssistantService aiAssistantService;

    @PostMapping("/ask")
    public ResponseEntity<ApiResponse<Map<String, Object>>> askAi(@RequestBody AiQuestionRequest request) {
        Map<String, Object> result = aiAssistantService.answerQuestion(request);
        return ResponseEntity.ok(ApiResponse.success("AI response", result));
    }
}

