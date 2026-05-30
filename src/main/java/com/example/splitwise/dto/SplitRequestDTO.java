package com.example.splitwise.dto;

public class SplitRequestDTO {

    private Long userId;

    private Double amount;

    public SplitRequestDTO() {
    }

    public SplitRequestDTO(
            Long userId,
            Double amount
    ) {
        this.userId = userId;
        this.amount = amount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
