package com.example.splitwise.dto;

public class UserBalanceDTO {

    private String userName;

    private Double totalOwed;

    public UserBalanceDTO() {
    }

    public UserBalanceDTO(
            String userName,
            Double totalOwed
    ) {
        this.userName = userName;
        this.totalOwed = totalOwed;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Double getTotalOwed() {
        return totalOwed;
    }

    public void setTotalOwed(Double totalOwed) {
        this.totalOwed = totalOwed;
    }
}
