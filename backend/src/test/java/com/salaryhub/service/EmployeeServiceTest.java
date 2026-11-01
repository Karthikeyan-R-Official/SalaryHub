package com.salaryhub.service;

import com.salaryhub.dto.EmployeeRequest;
import com.salaryhub.dto.EmployeeResponse;
import com.salaryhub.entity.Employee;
import com.salaryhub.repository.EmployeeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private EmployeeRequest request;

    @BeforeEach
    void setUp() {
        request = new EmployeeRequest();
        request.setEmployeeCode("EMP-2001");
        request.setFirstName("  Rahul ");
        request.setLastName(" Sharma ");
        request.setEmail(" RAHUL.SHARMA@example.com ");
        request.setCountry("India");
        request.setDepartment("Engineering");
        request.setJobTitle("Software Engineer");
        request.setHireDate(LocalDate.of(2025, 1, 15));
        request.setEmploymentStatus(null);
    }

    @Test
    void createEmployeeShouldNormalizeInputAndSetDefaultStatus() {
        when(employeeRepository.existsByEmployeeCode("EMP-2001"))
                .thenReturn(false);
        when(employeeRepository.existsByEmail(" RAHUL.SHARMA@example.com "))
                .thenReturn(false);
        when(employeeRepository.save(any(Employee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        EmployeeResponse response = employeeService.createEmployee(request);

        assertEquals("EMP-2001", response.employeeCode());
        assertEquals("Rahul", response.firstName());
        assertEquals("Sharma", response.lastName());
        assertEquals("rahul.sharma@example.com", response.email());
        assertEquals("ACTIVE", response.employmentStatus());

        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void createEmployeeShouldRejectDuplicateEmployeeCode() {
        when(employeeRepository.existsByEmployeeCode("EMP-2001"))
                .thenReturn(true);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> employeeService.createEmployee(request)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void createEmployeeShouldRejectDuplicateEmail() {
        when(employeeRepository.existsByEmployeeCode("EMP-2001"))
                .thenReturn(false);
        when(employeeRepository.existsByEmail(" RAHUL.SHARMA@example.com "))
                .thenReturn(true);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> employeeService.createEmployee(request)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void getEmployeeShouldReturnNotFoundForUnknownId() {
        when(employeeRepository.findById(999L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> employeeService.getEmployee(999L)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void getEmployeesShouldRejectNegativePage() {
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> employeeService.getEmployees(null, null, null, -1, 10)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(employeeRepository, never())
                .searchEmployees(any(), any(), any(), any());
    }

    @Test
    void getEmployeesShouldRejectPageSizeGreaterThan100() {
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> employeeService.getEmployees(null, null, null, 0, 101)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(employeeRepository, never())
                .searchEmployees(any(), any(), any(), any());
    }

    @Test
    void getEmployeesShouldReturnPaginatedResults() {
        Employee employee = new Employee();
        employee.setEmployeeCode("EMP-2001");
        employee.setFirstName("Rahul");
        employee.setLastName("Sharma");
        employee.setEmail("rahul.sharma@example.com");
        employee.setCountry("India");
        employee.setDepartment("Engineering");
        employee.setJobTitle("Software Engineer");
        employee.setHireDate(LocalDate.of(2025, 1, 15));
        employee.setEmploymentStatus("ACTIVE");

        Page<Employee> employeePage = new PageImpl<>(
                List.of(employee),
                PageRequest.of(0, 10),
                1
        );

        when(employeeRepository.searchEmployees(
                null, null, null, PageRequest.of(0, 10)
        )).thenReturn(employeePage);

        Page<EmployeeResponse> result =
                employeeService.getEmployees(null, null, null, 0, 10);

        assertEquals(1, result.getTotalElements());
        assertEquals("EMP-2001", result.getContent().get(0).employeeCode());
        assertEquals("Rahul", result.getContent().get(0).firstName());
    }
}
