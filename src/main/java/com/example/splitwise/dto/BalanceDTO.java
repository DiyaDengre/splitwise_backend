package com.example.splitwise.dto;

public class BalanceDTO {

    private String owesUser;

    private String getsUser;

    private Double amount;

    public BalanceDTO() {
    }

    public BalanceDTO(

            String owesUser,

            String getsUser,

            Double amount

    ) {

        this.owesUser = owesUser;

        this.getsUser = getsUser;

        this.amount = amount;
    }

    public String getOwesUser() {
        return owesUser;
    }

    public void setOwesUser(
            String owesUser
    ) {
        this.owesUser = owesUser;
    }

    public String getGetsUser() {
        return getsUser;
    }

    public void setGetsUser(
            String getsUser
    ) {
        this.getsUser = getsUser;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(
            Double amount
    ) {
        this.amount = amount;
    }
}