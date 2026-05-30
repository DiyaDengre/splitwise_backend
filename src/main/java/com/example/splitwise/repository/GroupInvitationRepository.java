package com.example.splitwise.repository;

import com.example.splitwise.entity.GroupInvitation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupInvitationRepository
        extends JpaRepository<GroupInvitation, Long> {

    List<GroupInvitation> findByEmail(
            String email
    );
}
