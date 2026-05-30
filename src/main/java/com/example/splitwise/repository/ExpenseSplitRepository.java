package com.example.splitwise.repository;
import java.util.List;

import com.example.splitwise.entity.ExpenseSplit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseSplitRepository
        extends JpaRepository<ExpenseSplit, Long> {
    List<ExpenseSplit> findByExpense_Group_Id(Long groupId);
    List<ExpenseSplit> findByUser_Id(Long userId);
    List<ExpenseSplit> findByExpense_PaidBy_Id(Long userId);

}