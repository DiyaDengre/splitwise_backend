package com.example.splitwise.service;

import com.example.splitwise.entity.ExpenseGroup;
import com.example.splitwise.entity.GroupInvitation;

import com.example.splitwise.entity.User;
import com.example.splitwise.repository.GroupInvitationRepository;
import com.example.splitwise.repository.GroupRepository;

import com.example.splitwise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class GroupInvitationService {

    @Autowired
    private GroupInvitationRepository
            invitationRepository;

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;

    public GroupInvitation inviteMember(

            Long groupId,

            String email

    ) {

        ExpenseGroup group =
                groupRepository.findById(groupId)
                        .orElseThrow();
        List<GroupInvitation> invitations =

                invitationRepository.findByEmail(
                        email
                );

        for(GroupInvitation invitation :
                invitations) {

            if(
                    invitation.getGroup()
                            .getId()
                            .equals(groupId)
            ) {

                throw new RuntimeException(
                        "Invitation already sent"
                );
            }
        }
        for(User member :
                group.getMembers()) {

            if(
                    member.getEmail().equalsIgnoreCase(email)
            ) {

                throw new RuntimeException(
                        "User already in group"
                );
            }
        }

        GroupInvitation invitation =
                new GroupInvitation();

        invitation.setEmail(email);

        invitation.setGroup(group);

        return invitationRepository.save(
                invitation
        );
    }

    public List<GroupInvitation>
    getInvitations(String email) {

        return invitationRepository
                .findByEmail(email);
    }
    public void acceptInvitation(

            Long invitationId

    ) {

        GroupInvitation invitation =

                invitationRepository
                        .findById(invitationId)
                        .orElseThrow();

        ExpenseGroup group =
                invitation.getGroup();

        User user =
                userRepository.findByEmail(
                        invitation.getEmail()
                );
        if(!group.getMembers().contains(user)) {
            group.getMembers().add(user);
        }

        groupRepository.save(group);

        invitationRepository.delete(
                invitation
        );
    }
    public void rejectInvitation(

            Long invitationId

    ) {

        GroupInvitation invitation =

                invitationRepository
                        .findById(invitationId)
                        .orElseThrow();

        invitationRepository.delete(
                invitation
        );
    }

}