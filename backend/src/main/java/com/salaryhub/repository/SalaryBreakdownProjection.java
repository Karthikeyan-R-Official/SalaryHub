package com.salaryhub.repository;

import java.math.BigDecimal;

public interface SalaryBreakdownProjection {

    String getGroupName();

    String getCurrency();

    Long getEmployeeCount();

    BigDecimal getAverageSalary();
}