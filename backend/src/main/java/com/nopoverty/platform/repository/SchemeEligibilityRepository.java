package com.nopoverty.platform.repository;

import com.nopoverty.platform.model.Scheme;
import com.nopoverty.platform.model.SchemeEligibilityCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SchemeEligibilityRepository extends JpaRepository<SchemeEligibilityCriteria, Long> {
    Optional<SchemeEligibilityCriteria> findByScheme(Scheme scheme);
    Optional<SchemeEligibilityCriteria> findBySchemeId(Long schemeId);
}

