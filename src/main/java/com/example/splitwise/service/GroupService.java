package com.example.splitwise.service;

import com.example.splitwise.entity.ExpenseGroup;
import com.example.splitwise.entity.User;
import com.example.splitwise.repository.GroupRepository;
import com.example.splitwise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;

    public ExpenseGroup createGroup(ExpenseGroup expenseGroup) {

        return groupRepository.save(expenseGroup);
    }
    public ExpenseGroup addMember(Long groupId, Long userId) {

        ExpenseGroup group =
                groupRepository.findById(groupId).orElse(null);

        User user =
                userRepository.findById(userId).orElse(null);

        if(group == null || user == null) {
            return null;
        }

        group.getMembers().add(user);

        System.out.println(group.getMembers());

        return groupRepository.save(group);
    }
    public List<ExpenseGroup> getUserGroups(
            Long userId
    ) {

        return groupRepository.findByMembers_Id(
                userId
        );
    }
}
