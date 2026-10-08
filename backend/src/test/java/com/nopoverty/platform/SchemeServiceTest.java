package com.nopoverty.platform;

import com.nopoverty.platform.model.Scheme;
import com.nopoverty.platform.repository.CategoryRepository;
import com.nopoverty.platform.repository.SchemeRepository;
import com.nopoverty.platform.service.SchemeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SchemeServiceTest {

    @Mock
    private SchemeRepository schemeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private SchemeService schemeService;

    @Test
    void testFindSchemeById() {
        Scheme s = new Scheme();
        s.setId(1L);
        s.setName("PM Kisan");
        when(schemeRepository.findById(1L)).thenReturn(Optional.of(s));

        Optional<Scheme> res = schemeService.getSchemeById(1L);
        assertTrue(res.isPresent());
        assertEquals("PM Kisan", res.get().getName());
    }

    @Test
    void testSearchSchemes() {
        Scheme s = new Scheme();
        s.setName("Ayushman Bharat");
        when(schemeRepository.searchSchemes("Ayushman", null, null)).thenReturn(List.of(s));

        List<Scheme> results = schemeService.searchSchemes("Ayushman", null, null);
        assertEquals(1, results.size());
        assertEquals("Ayushman Bharat", results.get(0).getName());
    }
}

