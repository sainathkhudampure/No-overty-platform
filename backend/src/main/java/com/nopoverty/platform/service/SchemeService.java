package com.nopoverty.platform.service;

import com.nopoverty.platform.model.Category;
import com.nopoverty.platform.model.Scheme;
import com.nopoverty.platform.repository.CategoryRepository;
import com.nopoverty.platform.repository.SchemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class SchemeService {

    @Autowired
    private SchemeRepository schemeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Scheme> getAllActiveSchemes() {
        return schemeRepository.findByIsActiveTrue();
    }

    public List<Scheme> searchSchemes(String keyword, String categoryCode, String state) {
        String cleanKeyword = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        String cleanCategory = (categoryCode != null && !categoryCode.trim().isEmpty() && !categoryCode.equalsIgnoreCase("ALL")) ? categoryCode.trim() : null;
        String cleanState = (state != null && !state.trim().isEmpty() && !state.equalsIgnoreCase("All India")) ? state.trim() : null;

        return schemeRepository.searchSchemes(cleanKeyword, cleanCategory, cleanState);
    }

    public Optional<Scheme> getSchemeById(Long id) {
        return schemeRepository.findById(id);
    }

    public Optional<Scheme> getSchemeByCode(String code) {
        return schemeRepository.findByCode(code);
    }

    @Transactional
    public void incrementViews(Long id) {
        schemeRepository.findById(id).ifPresent(s -> {
            s.setViewsCount(s.getViewsCount() != null ? s.getViewsCount() + 1 : 1);
            schemeRepository.save(s);
        });
    }

    @Transactional
    public Scheme createOrUpdateScheme(Scheme scheme, List<String> categoryCodes) {
        if (categoryCodes != null && !categoryCodes.isEmpty()) {
            Set<Category> categories = new HashSet<>();
            for (String code : categoryCodes) {
                categoryRepository.findByCode(code).ifPresent(categories::add);
            }
            scheme.setCategories(categories);
        }
        return schemeRepository.save(scheme);
    }

    @Transactional
    public void deleteScheme(Long id) {
        schemeRepository.deleteById(id);
    }

    public List<Scheme> getMostViewedSchemes() {
        return schemeRepository.findTop5ByIsActiveTrueOrderByViewsCountDesc();
    }

    public List<Scheme> getRecentSchemes() {
        return schemeRepository.findTop5ByIsActiveTrueOrderByCreatedAtDesc();
    }
}

