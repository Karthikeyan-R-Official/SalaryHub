package com.salaryhub.dto;

import java.math.BigDecimal;

public record SalaryBreakdownResponse(
        String groupName,
        String currency,
        Long employeeCount,
        BigDecimal averageSalary
) {
}