package com.nopoverty.platform.repository;

import com.nopoverty.platform.model.Scheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SchemeRepository extends JpaRepository<Scheme, Long> {
    Optional<Scheme> findByCode(String code);
    List<Scheme> findByIsActiveTrue();

    @Query("SELECT DISTINCT s FROM Scheme s LEFT JOIN s.categories c WHERE s.isActive = true AND " +
           "(:keyword IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.shortDescription) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.benefits) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.department) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:categoryCode IS NULL OR c.code = :categoryCode) AND " +
           "(:state IS NULL OR s.stateAvailability = 'All India' OR LOWER(s.stateAvailability) LIKE LOWER(CONCAT('%', :state, '%')))")
    List<Scheme> searchSchemes(
        @Param("keyword") String keyword,
        @Param("categoryCode") String categoryCode,
        @Param("state") String state
    );

    List<Scheme> findTop5ByIsActiveTrueOrderByViewsCountDesc();
    List<Scheme> findTop5ByIsActiveTrueOrderByCreatedAtDesc();
}

