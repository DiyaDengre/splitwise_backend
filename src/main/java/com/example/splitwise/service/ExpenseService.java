package com.example.splitwise.service;

import com.example.splitwise.dto.*;
import java.util.ArrayList;
import java.util.List;
import com.example.splitwise.entity.Expense;
import com.example.splitwise.entity.ExpenseGroup;
import com.example.splitwise.entity.User;
import com.example.splitwise.repository.ExpenseRepository;
import com.example.splitwise.repository.GroupRepository;
import com.example.splitwise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.splitwise.entity.ExpenseSplit;
import com.example.splitwise.repository.ExpenseSplitRepository;

@Service
public class ExpenseService {

    @Autowired private ExpenseRepository expenseRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private GroupRepository groupRepository;
    @Autowired private ExpenseSplitRepository expenseSplitRepository;

    @Transactional
    public Expense addExpense(ExpenseRequestDTO request) {
        User user = userRepository.findById(request.getPaidByUserId()).orElse(null);
        ExpenseGroup group = groupRepository.findById(request.getGroupId()).orElse(null);

        if (user == null) throw new RuntimeException("User not found");
        if (group == null) throw new RuntimeException("Group not found");

        boolean payerInGroup = group.getMembers().stream()
                .anyMatch(m -> m.getId().equals(user.getId()));
        if (!payerInGroup) throw new RuntimeException("Payer is not a group member");

        Expense expense = new Expense();
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setPaidBy(user);
        expense.setGroup(group);
        Expense saved = expenseRepository.save(expense);

        String splitType = request.getSplitType();

        if ("CUSTOM".equalsIgnoreCase(splitType)) {
            if (request.getSplits() == null || request.getSplits().isEmpty())
                throw new RuntimeException("Custom split needs splits list");

            double totalSplit = request.getSplits().stream()
                    .mapToDouble(SplitRequestDTO::getAmount).sum();

            // FIXED: only reject if split total EXCEEDS expense amount
            // Payer can cover the remaining difference themselves
            if (totalSplit > request.getAmount() + 0.01)
                throw new RuntimeException("Split total cannot exceed expense amount");

            for (SplitRequestDTO s : request.getSplits()) {
                User splitUser = userRepository.findById(s.getUserId()).orElse(null);
                if (splitUser == null || splitUser.getId().equals(user.getId())) continue;
                ExpenseSplit split = new ExpenseSplit();
                split.setExpense(saved);
                split.setUser(splitUser);
                split.setAmountOwed(s.getAmount());
                expenseSplitRepository.save(split);
            }

        } else if ("EQUAL".equalsIgnoreCase(splitType)) {
            // FIXED: divide among ALL members (including payer counts in total)
            // Each person owes: totalAmount / totalMembers
            // But we only save splits for NON-payers (payer already paid)
            List<User> nonPayers = new ArrayList<>();
            for (User m : group.getMembers()) {
                if (!m.getId().equals(user.getId())) nonPayers.add(m);
            }
            if (nonPayers.isEmpty()) throw new RuntimeException("No members to split with");

            int totalMembers = group.getMembers().size(); // includes payer
            double splitAmount = request.getAmount() / totalMembers; // each person's share

            for (User member : nonPayers) {
                ExpenseSplit split = new ExpenseSplit();
                split.setExpense(saved);
                split.setUser(member);
                split.setAmountOwed(splitAmount); // each non-payer owes this amount
                expenseSplitRepository.save(split);
            }

        } else {
            throw new RuntimeException("splitType must be EQUAL or CUSTOM");
        }

        return saved;
    }

    public List<BalanceDTO> getBalances(Long groupId) {
        List<ExpenseSplit> splits = expenseSplitRepository.findByExpense_Group_Id(groupId);
        List<BalanceDTO> balances = new ArrayList<>();
        for (ExpenseSplit split : splits) {
            if (split.getSettled()) continue;
            balances.add(new BalanceDTO(
                    split.getUser().getName(),
                    split.getExpense().getPaidBy().getName(),
                    split.getAmountOwed(),
                    split.getId(),
                    split.getUser().getId(),
                    split.getExpense().getDescription()
            ));
        }
        return balances;
    }

    public ExpenseSplit settleExpense(Long splitId, Double paidAmount) {
        ExpenseSplit split = expenseSplitRepository.findById(splitId).orElse(null);
        if (split == null) return null;
        double remaining = split.getAmountOwed() - paidAmount;
        if (remaining <= 0) {
            split.setAmountOwed(0.0);
            split.setSettled(true);
        } else {
            split.setAmountOwed(remaining);
        }
        return expenseSplitRepository.save(split);
    }

    public List<ExpenseDTO> getGroupExpenses(Long groupId) {
        List<Expense> expenses = expenseRepository.findByGroup_Id(groupId);
        List<ExpenseDTO> list = new ArrayList<>();
        for (Expense e : expenses) {
            ExpenseDTO dto = new ExpenseDTO();
            dto.setDescription(e.getDescription());
            dto.setAmount(e.getAmount());
            dto.setPaidBy(e.getPaidBy().getName());
            list.add(dto);
        }
        return list;
    }

    public UserDashboardDTO getUserDashboard(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) throw new RuntimeException("User not found");

        double youOwe = 0.0, youGet = 0.0;

        for (ExpenseSplit s : expenseSplitRepository.findByUser_Id(userId))
            if (!s.getSettled()) youOwe += s.getAmountOwed();

        for (ExpenseSplit s : expenseSplitRepository.findByExpense_PaidBy_Id(userId))
            if (!s.getSettled()) youGet += s.getAmountOwed();

        return new UserDashboardDTO(youGet - youOwe, youOwe, youGet);
    }
}