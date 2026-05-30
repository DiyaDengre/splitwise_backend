package com.example.splitwise.dto;

public class ExpenseDTO {

    private String description;

    private Double amount;

    private String paidBy;

    public ExpenseDTO() {
    }

    public ExpenseDTO(
            String description,
            Double amount,
            String paidBy
    ) {
        this.description = description;
        this.amount = amount;
        this.paidBy = paidBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }
}