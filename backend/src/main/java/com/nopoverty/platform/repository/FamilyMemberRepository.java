package com.nopoverty.platform.repository;

import com.nopoverty.platform.model.FamilyMember;
import com.nopoverty.platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long> {
    List<FamilyMember> findByUser(User user);
    List<FamilyMember> findByUserId(Long userId);
}

