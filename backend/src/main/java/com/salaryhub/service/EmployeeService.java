package com.salaryhub.service;

import com.salaryhub.dto.EmployeeRequest;
import com.salaryhub.dto.EmployeeResponse;
import com.salaryhub.entity.Employee;
import com.salaryhub.repository.EmployeeRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    public Page<EmployeeResponse> getEmployees(
            String search,
            String country,
            String department,
            int page,
            int size
    ) {
        if (page < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Page must be zero or greater"
            );
        }

        if (size < 1 || size > 100) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Page size must be between 1 and 100"
            );
        }

        return employeeRepository.searchEmployees(
                search,
                country,
                department,
                PageRequest.of(page, size)
        ).map(EmployeeResponse::from);
    }

    @Transactional(readOnly = true)
    public EmployeeResponse getEmployee(Long id) {
        Employee employee = findEmployee(id);
        return EmployeeResponse.from(employee);
    }

    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        if (employeeRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Employee code already exists"
            );
        }

        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Email already exists"
            );
        }

        Employee employee = new Employee();
        copyRequestToEmployee(request, employee);

        return EmployeeResponse.from(employeeRepository.save(employee));
    }

    @Transactional
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        Employee employee = findEmployee(id);

        if (employeeRepository.existsByEmployeeCodeAndIdNot(
                request.getEmployeeCode(), id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Employee code already exists"
            );
        }

        if (employeeRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Email already exists"
            );
        }

        copyRequestToEmployee(request, employee);

        return EmployeeResponse.from(employeeRepository.save(employee));
    }

    private Employee findEmployee(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Employee not found"
                ));
    }

    private void copyRequestToEmployee(
            EmployeeRequest request,
            Employee employee
    ) {
        employee.setEmployeeCode(request.getEmployeeCode().trim());
        employee.setFirstName(request.getFirstName().trim());
        employee.setLastName(request.getLastName().trim());
        employee.setEmail(request.getEmail().trim().toLowerCase());
        employee.setCountry(request.getCountry().trim());
        employee.setDepartment(request.getDepartment().trim());
        employee.setJobTitle(request.getJobTitle().trim());
        employee.setHireDate(request.getHireDate());

        String status = request.getEmploymentStatus();

        employee.setEmploymentStatus(
                status == null || status.isBlank()
                        ? "ACTIVE"
                        : status.trim().toUpperCase()
        );
    }
}