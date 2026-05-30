package com.example.splitwise.controller;
import com.example.splitwise.dto.*;

import java.util.ArrayList;
import java.util.List;
import com.example.splitwise.dto.UserDashboardDTO;
import com.example.splitwise.entity.Expense;
import com.example.splitwise.entity.ExpenseSplit;
import com.example.splitwise.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "http://localhost:5173")              // LINE 16: NAYA - CrossOrigin add kiya

public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping("/add")
    public Expense addExpense(
            @Valid @RequestBody ExpenseRequestDTO request
    ) {

        return expenseService.addExpense(request);
    }
    @GetMapping("/balances")
    public List<BalanceDTO> getBalances(
            @RequestParam Long groupId
    ) {

        return expenseService.getBalances(groupId);
    }
    @PostMapping("/settle")
    public ExpenseSplit settleExpense(
            @RequestParam Long splitId,
            @RequestParam Double paidAmount
    ) {

        return expenseService.settleExpense(
                splitId,
                paidAmount
        );
    }
    @GetMapping("/group/{groupId}")
    public List<ExpenseDTO> getGroupExpenses(
            @PathVariable Long groupId
    ) {

        return expenseService.getGroupExpenses(groupId);
    }

    @GetMapping("/dashboard/{userId}")

    public UserDashboardDTO getDashboard(

            @PathVariable Long userId

    ) {

        return expenseService
                .getUserDashboard(userId);
    }
}