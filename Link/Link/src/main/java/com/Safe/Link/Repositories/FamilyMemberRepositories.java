package com.Safe.Link.Repositories;

import com.Safe.Link.Entities.FamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FamilyMemberRepositories extends JpaRepository<FamilyMember, Long> {

    List<FamilyMember> findByUserId(Long userId);

}
