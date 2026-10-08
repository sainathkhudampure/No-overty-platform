package com.nopoverty.platform.repository;

import com.nopoverty.platform.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findBySchemeId(Long schemeId);

    @Query("SELECT AVG(f.rating) FROM Feedback f")
    Double getAverageRating();
}

