package com.nopoverty.platform.repository;

import com.nopoverty.platform.model.Notification;
import com.nopoverty.platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreatedAtDesc(User user);

    @Query("SELECT n FROM Notification n WHERE (n.user.id = :userId OR n.user IS NULL) ORDER BY n.createdAt DESC")
    List<Notification> findForUserOrGlobal(@Param("userId") Long userId);

    long countByUserIdAndIsReadFalse(Long userId);
}

