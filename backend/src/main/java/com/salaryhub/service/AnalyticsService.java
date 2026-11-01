package com.salaryhub.service;

import com.salaryhub.dto.AnalyticsOverviewResponse;
import com.salaryhub.dto.SalaryBreakdownResponse;
import com.salaryhub.repository.EmployeeRepository;
import com.salaryhub.repository.SalaryBreakdownProjection;
import com.salaryhub.repository.SalaryRecordRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnalyticsService {

    private final EmployeeRepository employeeRepository;
    private final SalaryRecordRepository salaryRecordRepository;

    public AnalyticsService(
            EmployeeRepository employeeRepository,
            SalaryRecordRepository salaryRecordRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.salaryRecordRepository = salaryRecordRepository;
    }

    @Transactional(readOnly = true)
    public AnalyticsOverviewResponse getOverview() {
        return new AnalyticsOverviewResponse(
                employeeRepository.count(),
                employeeRepository.countByEmploymentStatusIgnoreCase("ACTIVE"),
                employeeRepository.countByEmploymentStatusIgnoreCase("INACTIVE"),
                salaryRecordRepository.count()
        );
    }

    @Transactional(readOnly = true)
    public List<SalaryBreakdownResponse> getSalaryBreakdownByCountry() {
        return salaryRecordRepository.findSalaryBreakdownByCountry()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SalaryBreakdownResponse> getSalaryBreakdownByDepartment() {
        return salaryRecordRepository.findSalaryBreakdownByDepartment()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private SalaryBreakdownResponse toResponse(
            SalaryBreakdownProjection projection
    ) {
        return new SalaryBreakdownResponse(
                projection.getGroupName(),
                projection.getCurrency(),
                projection.getEmployeeCount(),
                projection.getAverageSalary()
        );
    }
}