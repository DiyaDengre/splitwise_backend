package com.example.splitwise.dto;

public class UserDashboardDTO {

    private Double totalBalance;

    private Double youOwe;

    private Double youGet;

    public UserDashboardDTO() {
    }

    public UserDashboardDTO(
            Double totalBalance,
            Double youOwe,
            Double youGet
    ) {
        this.totalBalance = totalBalance;
        this.youOwe = youOwe;
        this.youGet = youGet;
    }

    public Double getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(Double totalBalance) {
        this.totalBalance = totalBalance;
    }

    public Double getYouOwe() {
        return youOwe;
    }

    public void setYouOwe(Double youOwe) {
        this.youOwe = youOwe;
    }

    public Double getYouGet() {
        return youGet;
    }

    public void setYouGet(Double youGet) {
        this.youGet = youGet;
    }
}