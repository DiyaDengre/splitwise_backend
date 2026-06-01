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

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ExpenseSplitRepository expenseSplitRepository;

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
        Expense savedExpense = expenseRepository.save(expense);

        String splitType = request.getSplitType();

        if ("CUSTOM".equalsIgnoreCase(splitType)) {

            if (request.getSplits() == null || request.getSplits().isEmpty())
                throw new RuntimeException("Custom split needs splits list");

            double totalSplit = request.getSplits().stream()
                    .mapToDouble(SplitRequestDTO::getAmount).sum();

            if (Math.abs(totalSplit - request.getAmount()) > 0.01)
                throw new RuntimeException("Split amounts don't add up to total");

            for (SplitRequestDTO splitRequest : request.getSplits()) {
                User splitUser = userRepository.findById(splitRequest.getUserId()).orElse(null);
                if (splitUser == null || splitUser.getId().equals(user.getId())) continue;

                ExpenseSplit split = new ExpenseSplit();
                split.setExpense(savedExpense);
                split.setUser(splitUser);
                split.setAmountOwed(splitRequest.getAmount());
                expenseSplitRepository.save(split);
            }

        } else if ("EQUAL".equalsIgnoreCase(splitType)) {

            long nonPayerCount = group.getMembers().stream()
                    .filter(m -> !m.getId().equals(user.getId())).count();

            if (nonPayerCount <= 0) throw new RuntimeException("No members to split with");

            double splitAmount = request.getAmount() / nonPayerCount;

            for (User member : group.getMembers()) {
                if (member.getId().equals(user.getId())) continue;

                ExpenseSplit split = new ExpenseSplit();
                split.setExpense(savedExpense);
                split.setUser(member);
                split.setAmountOwed(splitAmount);
                expenseSplitRepository.save(split);
            }

        } else {
            throw new RuntimeException("splitType must be EQUAL or CUSTOM");
        }

        return savedExpense;
    }

    public List<BalanceDTO> getBalances(Long groupId) {

        List<ExpenseSplit> splits = expenseSplitRepository.findByExpense_Group_Id(groupId);
        List<BalanceDTO> balances = new ArrayList<>();

        for (ExpenseSplit split : splits) {
            if (split.getSettled()) continue;

            BalanceDTO dto = new BalanceDTO(
                    split.getUser().getName(),
                    split.getExpense().getPaidBy().getName(),
                    split.getAmountOwed(),
                    split.getId(),
                    split.getExpense().getDescription()
            );
            balances.add(dto);
        }

        return balances;
    }

    public ExpenseSplit settleExpense(Long splitId, Double paidAmount) {

        ExpenseSplit split = expenseSplitRepository.findById(splitId).orElse(null);
        if (split == null) return null;

        double remainingAmount = split.getAmountOwed() - paidAmount;

        if (remainingAmount <= 0) {
            split.setAmountOwed(0.0);
            split.setSettled(true);
        } else {
            split.setAmountOwed(remainingAmount);
        }

        return expenseSplitRepository.save(split);
    }

    public List<ExpenseDTO> getGroupExpenses(Long groupId) {

        List<Expense> expenses = expenseRepository.findByGroup_Id(groupId);
        List<ExpenseDTO> expenseDTOList = new ArrayList<>();

        for (Expense expense : expenses) {
            ExpenseDTO dto = new ExpenseDTO();
            dto.setDescription(expense.getDescription());
            dto.setAmount(expense.getAmount());
            dto.setPaidBy(expense.getPaidBy().getName());
            expenseDTOList.add(dto);
        }

        return expenseDTOList;
    }

    public UserDashboardDTO getUserDashboard(Long userId) {

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) throw new RuntimeException("User not found");

        double youOwe = 0.0;
        double youGet = 0.0;

        List<ExpenseSplit> oweSplits = expenseSplitRepository.findByUser_Id(userId);
        List<ExpenseSplit> getSplits = expenseSplitRepository.findByExpense_PaidBy_Id(userId);

        for (ExpenseSplit split : oweSplits) {
            if (!split.getSettled()) youOwe += split.getAmountOwed();
        }
        for (ExpenseSplit split : getSplits) {
            if (!split.getSettled()) youGet += split.getAmountOwed();
        }

        return new UserDashboardDTO(youGet - youOwe, youOwe, youGet);
    }
}