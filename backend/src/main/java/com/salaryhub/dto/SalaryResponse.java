package com.salaryhub.dto;

import com.salaryhub.entity.SalaryRecord;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record SalaryResponse(
        Long id,
        Long employeeId,
        String employeeCode,
        BigDecimal amount,
        String currency,
        LocalDate effectiveDate,
        String notes,
        LocalDateTime createdAt
) {

    public static SalaryResponse from(SalaryRecord salary) {
        return new SalaryResponse(
                salary.getId(),
                salary.getEmployee().getId(),
                salary.getEmployee().getEmployeeCode(),
                salary.getAmount(),
                salary.getCurrency(),
                salary.getEffectiveDate(),
                salary.getNotes(),
                salary.getCreatedAt()
        );
    }
}