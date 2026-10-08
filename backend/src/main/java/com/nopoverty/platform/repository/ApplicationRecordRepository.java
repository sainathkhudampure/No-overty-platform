package com.nopoverty.platform.repository;

import com.nopoverty.platform.model.ApplicationRecord;
import com.nopoverty.platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public interface ApplicationRecordRepository extends JpaRepository<ApplicationRecord, Long> {
    List<ApplicationRecord> findByUser(User user);
    List<ApplicationRecord> findByUserId(Long userId);
    List<ApplicationRecord> findByStatus(String status);

    @Query("SELECT a.status as status, COUNT(a) as count FROM ApplicationRecord a GROUP BY a.status")
    List<Map<String, Object>> countApplicationsByStatus();
}

