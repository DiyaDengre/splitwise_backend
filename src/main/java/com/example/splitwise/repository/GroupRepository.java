package com.example.splitwise.repository;

import com.example.splitwise.entity.ExpenseGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<ExpenseGroup, Long> {
    List<ExpenseGroup> findByMembers_Id(
            Long userId
    );
}