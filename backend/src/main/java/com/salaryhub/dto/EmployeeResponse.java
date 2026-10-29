package com.salaryhub.dto;

import com.salaryhub.entity.Employee;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record EmployeeResponse(
        Long id,
        String employeeCode,
        String firstName,
        String lastName,
        String email,
        String country,
        String department,
        String jobTitle,
        LocalDate hireDate,
        String employmentStatus,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static EmployeeResponse from(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getEmployeeCode(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getCountry(),
                employee.getDepartment(),
                employee.getJobTitle(),
                employee.getHireDate(),
                employee.getEmploymentStatus(),
                employee.getCreatedAt(),
                employee.getUpdatedAt()
        );
    }
}