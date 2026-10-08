package com.nopoverty.platform.controller;

import com.nopoverty.platform.dto.ApiResponse;
import com.nopoverty.platform.model.User;
import com.nopoverty.platform.model.UserDocument;
import com.nopoverty.platform.repository.UserDocumentRepository;
import com.nopoverty.platform.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "*")
public class DocumentController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserDocumentRepository userDocumentRepository;

    private static final String UPLOAD_DIR = "uploads/docs/";

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserDocuments(Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        List<UserDocument> docs = userDocumentRepository.findByUser(user);

        // Standard core document list
        List<String> coreDocNames = List.of(
                "Aadhaar Card",
                "Income Certificate",
                "Bank Passbook / Details",
                "Residence / Domicile Certificate",
                "Caste Certificate",
                "Land Ownership Record",
                "Education Marksheet / Certificate"
        );

        Set<String> uploadedNames = new HashSet<>();
        for (UserDocument d : docs) {
            uploadedNames.add(d.getDocumentName());
        }

        List<String> missingDocs = new ArrayList<>();
        for (String c : coreDocNames) {
            if (!uploadedNames.contains(c)) {
                missingDocs.add(c);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("documents", docs);
        result.put("uploadedCount", docs.size());
        result.put("missingDocuments", missingDocs);

        return ResponseEntity.ok(ApiResponse.success("Document checklist", result));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserDocument>> markDocument(
            @RequestBody UserDocument docRequest, Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        docRequest.setUser(user);
        if (docRequest.getStatus() == null) docRequest.setStatus("AVAILABLE");
        UserDocument saved = userDocumentRepository.save(docRequest);
        return ResponseEntity.ok(ApiResponse.success("Document status recorded", saved));
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<UserDocument>> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("documentName") String documentName,
            @RequestParam(value = "documentType", required = false) String documentType,
            @RequestParam(value = "notes", required = false) String notes,
            Authentication authentication) throws IOException {

        User user = authService.getUserByEmail(authentication.getName());

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("File is empty"));
        }

        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "document.pdf";
        String cleanName = originalFilename.replaceAll("[^a-zA-Z0-9.-]", "_");
        String storedFilename = user.getId() + "_" + System.currentTimeMillis() + "_" + cleanName;
        Path targetPath = uploadPath.resolve(storedFilename);

        Files.copy(file.getInputStream(), targetPath);

        UserDocument doc = new UserDocument();
        doc.setUser(user);
        doc.setDocumentName(documentName);
        doc.setDocumentType(documentType != null ? documentType : "Identity/Proof");
        doc.setFilePath("/uploads/docs/" + storedFilename);
        doc.setFileSizeBytes(file.getSize());
        doc.setStatus("VERIFIED");
        doc.setIsVerified(true);
        doc.setNotes(notes);

        UserDocument saved = userDocumentRepository.save(doc);
        return ResponseEntity.ok(ApiResponse.success("Document uploaded securely", saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteDocument(
            @PathVariable Long id, Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        userDocumentRepository.findById(id).ifPresent(doc -> {
            if (doc.getUser().getId().equals(user.getId())) {
                userDocumentRepository.delete(doc);
            }
        });
        return ResponseEntity.ok(ApiResponse.success("Document deleted", "Deleted"));
    }
}

