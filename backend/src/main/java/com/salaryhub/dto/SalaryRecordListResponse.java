package com.salaryhub.dto;

import com.salaryhub.entity.SalaryRecord;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record SalaryRecordListResponse(
        Long id,
        Long employeeId,
        String employeeCode,
        String employeeName,
        String country,
        String department,
        String jobTitle,
        BigDecimal amount,
        String currency,
        LocalDate effectiveDate,
        String notes,
        LocalDateTime createdAt
) {

    public static SalaryRecordListResponse from(SalaryRecord salary) {
        var employee = salary.getEmployee();

        return new SalaryRecordListResponse(
                salary.getId(),
                employee.getId(),
                employee.getEmployeeCode(),
                employee.getFirstName() + " " + employee.getLastName(),
                employee.getCountry(),
                employee.getDepartment(),
                employee.getJobTitle(),
                salary.getAmount(),
                salary.getCurrency(),
                salary.getEffectiveDate(),
                salary.getNotes(),
                salary.getCreatedAt()
        );
    }
}