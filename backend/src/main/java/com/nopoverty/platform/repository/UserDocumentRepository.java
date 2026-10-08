package com.nopoverty.platform.repository;

import com.nopoverty.platform.model.User;
import com.nopoverty.platform.model.UserDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserDocumentRepository extends JpaRepository<UserDocument, Long> {
    List<UserDocument> findByUser(User user);
    List<UserDocument> findByUserId(Long userId);
}

