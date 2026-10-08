package com.nopoverty.platform.controller;

import com.nopoverty.platform.dto.ApiResponse;
import com.nopoverty.platform.model.Category;
import com.nopoverty.platform.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Category>>> getAllCategories() {
        List<Category> categories = categoryRepository.findByIsActiveTrueOrderByDisplayOrderAsc();
        return ResponseEntity.ok(ApiResponse.success("Categories retrieved", categories));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> getCategory(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .map(cat -> ResponseEntity.ok(ApiResponse.success("Category retrieved", cat)))
                .orElse(ResponseEntity.notFound().build());
    }
}

