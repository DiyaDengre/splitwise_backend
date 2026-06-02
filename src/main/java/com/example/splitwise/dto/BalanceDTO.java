package com.example.splitwise.dto;

public class BalanceDTO {

    private String owesUser;
    private String getsUser;
    private Double amount;
    private Long splitId;
    private Long owesUserId;
    private String expenseDescription;

    public BalanceDTO() {}

    public BalanceDTO(String owesUser, String getsUser, Double amount, Long splitId, Long owesUserId, String expenseDescription) {
        this.owesUser = owesUser;
        this.getsUser = getsUser;
        this.amount = amount;
        this.splitId = splitId;
        this.owesUserId = owesUserId;
        this.expenseDescription = expenseDescription;
    }

    public String getOwesUser() { return owesUser; }
    public void setOwesUser(String v) { this.owesUser = v; }
    public String getGetsUser() { return getsUser; }
    public void setGetsUser(String v) { this.getsUser = v; }
    public Double getAmount() { return amount; }
    public void setAmount(Double v) { this.amount = v; }
    public Long getSplitId() { return splitId; }
    public void setSplitId(Long v) { this.splitId = v; }
    public Long getOwesUserId() { return owesUserId; }
    public void setOwesUserId(Long v) { this.owesUserId = v; }
    public String getExpenseDescription() { return expenseDescription; }
    public void setExpenseDescription(String v) { this.expenseDescription = v; }
}