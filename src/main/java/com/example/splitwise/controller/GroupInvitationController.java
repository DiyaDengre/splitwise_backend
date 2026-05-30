package com.example.splitwise.controller;

import com.example.splitwise.entity.GroupInvitation;

import com.example.splitwise.service.GroupInvitationService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/api/invitations")

@CrossOrigin(origins = "http://localhost:5173")

public class GroupInvitationController {

    @Autowired
    private GroupInvitationService
            invitationService;

    @PostMapping("/invite")

    public GroupInvitation inviteMember(

            @RequestParam Long groupId,

            @RequestParam String email

    ) {

        return invitationService.inviteMember(
                groupId,
                email
        );
    }

    @GetMapping("/{email}")

    public List<GroupInvitation>
    getInvitations(

            @PathVariable String email

    ) {

        return invitationService
                .getInvitations(email);
    }
    @PostMapping("/accept")

    public void acceptInvitation(

            @RequestParam Long invitationId

    ) {

        invitationService.acceptInvitation(
                invitationId
        );
    }
    @PostMapping("/reject")

    public void rejectInvitation(

            @RequestParam Long invitationId

    ) {

        invitationService.rejectInvitation(
                invitationId
        );
    }
}
