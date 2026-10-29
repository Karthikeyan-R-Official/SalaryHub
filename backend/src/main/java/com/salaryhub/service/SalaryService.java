package com.salaryhub.service;

import com.salaryhub.dto.SalaryRequest;
import com.salaryhub.dto.SalaryResponse;
import com.salaryhub.entity.Employee;
import com.salaryhub.entity.SalaryRecord;
import com.salaryhub.repository.EmployeeRepository;
import com.salaryhub.repository.SalaryRecordRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SalaryService {

    private final EmployeeRepository employeeRepository;
    private final SalaryRecordRepository salaryRecordRepository;

    public SalaryService(
            EmployeeRepository employeeRepository,
            SalaryRecordRepository salaryRecordRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.salaryRecordRepository = salaryRecordRepository;
    }

    @Transactional
    public SalaryResponse createSalary(
            Long employeeId,
            SalaryRequest request
    ) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Employee not found"
                ));

        if (salaryRecordRepository.existsByEmployee_IdAndEffectiveDate(
                employeeId,
                request.getEffectiveDate()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A salary record already exists for this employee and effective date"
            );
        }

        SalaryRecord salary = new SalaryRecord();

        salary.setEmployee(employee);
        salary.setAmount(request.getAmount());
        salary.setCurrency(request.getCurrency().trim().toUpperCase());
        salary.setEffectiveDate(request.getEffectiveDate());
        salary.setNotes(
                request.getNotes() == null || request.getNotes().isBlank()
                        ? null
                        : request.getNotes().trim()
        );

        SalaryRecord savedSalary = salaryRecordRepository.save(salary);

        return SalaryResponse.from(savedSalary);
    }

    @Transactional(readOnly = true)
    public List<SalaryResponse> getSalaryHistory(Long employeeId) {

        if (!employeeRepository.existsById(employeeId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Employee not found"
            );
        }

        return salaryRecordRepository
                .findByEmployee_IdOrderByEffectiveDateDescIdDesc(employeeId)
                .stream()
                .map(SalaryResponse::from)
                .toList();
    }
}