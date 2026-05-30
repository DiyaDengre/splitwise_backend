package com.example.splitwise.controller;

import com.example.splitwise.entity.ExpenseGroup;
import com.example.splitwise.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping("/create")
    public ExpenseGroup createGroup(@RequestBody ExpenseGroup expenseGroup) {

        return groupService.createGroup(expenseGroup);
    }
    @PostMapping("/addMember")
    public ExpenseGroup addMember(
            @RequestParam Long groupId,
            @RequestParam Long userId
    ) {

        return groupService.addMember(groupId, userId);
    }
    @GetMapping("/user/{userId}")
    public List<ExpenseGroup> getUserGroups(
            @PathVariable Long userId
    ) {

        return groupService.getUserGroups(
                userId
        );
    }
}
