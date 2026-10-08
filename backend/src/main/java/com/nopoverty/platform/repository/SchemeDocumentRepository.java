package com.nopoverty.platform.repository;

import com.nopoverty.platform.model.SchemeDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SchemeDocumentRepository extends JpaRepository<SchemeDocument, Long> {
    List<SchemeDocument> findBySchemeId(Long schemeId);
}

