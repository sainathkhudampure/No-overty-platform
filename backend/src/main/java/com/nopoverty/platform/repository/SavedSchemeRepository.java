package com.nopoverty.platform.repository;

import com.nopoverty.platform.model.SavedScheme;
import com.nopoverty.platform.model.Scheme;
import com.nopoverty.platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SavedSchemeRepository extends JpaRepository<SavedScheme, Long> {
    List<SavedScheme> findByUser(User user);
    List<SavedScheme> findByUserId(Long userId);
    Optional<SavedScheme> findByUserAndScheme(User user, Scheme scheme);
    Optional<SavedScheme> findByUserIdAndSchemeId(Long userId, Long schemeId);
    boolean existsByUserIdAndSchemeId(Long userId, Long schemeId);
    void deleteByUserIdAndSchemeId(Long userId, Long schemeId);
}

