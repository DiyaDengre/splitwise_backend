package com.example.splitwise.dto;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ExpenseRequestDTO {

    @NotBlank
    private String description;

    @NotNull
    @Positive
    private Double amount;

    private Long paidByUserId;

    private Long groupId;

    @NotBlank
    private String splitType;

    private List<SplitRequestDTO> splits;

    public ExpenseRequestDTO() {
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

    public Long getPaidByUserId() {
        return paidByUserId;
    }

    public void setPaidByUserId(Long paidByUserId) {
        this.paidByUserId = paidByUserId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getSplitType() {
        return splitType;
    }

    public void setSplitType(String splitType) {
        this.splitType = splitType;
    }

    public List<SplitRequestDTO> getSplits() {
        return splits;
    }

    public void setSplits(List<SplitRequestDTO> splits) {
        this.splits = splits;
    }
}