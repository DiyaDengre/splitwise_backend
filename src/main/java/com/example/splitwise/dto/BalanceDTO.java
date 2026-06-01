package com.example.splitwise.dto;

public class BalanceDTO {

    private String owesUser;
    private String getsUser;
    private Double amount;
    private Long splitId;
    private String expenseDescription;

    public BalanceDTO() {}

    public BalanceDTO(String owesUser, String getsUser, Double amount, Long splitId, String expenseDescription) {
        this.owesUser = owesUser;
        this.getsUser = getsUser;
        this.amount = amount;
        this.splitId = splitId;
        this.expenseDescription = expenseDescription;
    }

    public String getOwesUser() { return owesUser; }
    public void setOwesUser(String owesUser) { this.owesUser = owesUser; }
    public String getGetsUser() { return getsUser; }
    public void setGetsUser(String getsUser) { this.getsUser = getsUser; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public Long getSplitId() { return splitId; }
    public void setSplitId(Long splitId) { this.splitId = splitId; }
    public String getExpenseDescription() { return expenseDescription; }
    public void setExpenseDescription(String expenseDescription) { this.expenseDescription = expenseDescription; }
}