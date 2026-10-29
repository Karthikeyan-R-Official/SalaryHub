package com.salaryhub.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SalaryRequest {

    @NotNull(message = "Salary amount is required")
    @DecimalMin(
            value = "0.01",
            message = "Salary amount must be greater than zero"
    )
    @Digits(
            integer = 17,
            fraction = 2,
            message = "Salary must have at most 17 integer digits and 2 decimal places"
    )
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    @Pattern(
            regexp = "[A-Za-z]{3}",
            message = "Currency must be a three-letter code, such as INR or USD"
    )
    private String currency;

    @NotNull(message = "Effective date is required")
    private LocalDate effectiveDate;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}