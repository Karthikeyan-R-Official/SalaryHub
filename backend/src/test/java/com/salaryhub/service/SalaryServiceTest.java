
package com.salaryhub.service;

import com.salaryhub.dto.SalaryRequest;
import com.salaryhub.dto.SalaryResponse;
import com.salaryhub.entity.Employee;
import com.salaryhub.entity.SalaryRecord;
import com.salaryhub.repository.EmployeeRepository;
import com.salaryhub.repository.SalaryRecordRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalaryServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private SalaryRecordRepository salaryRecordRepository;

    @InjectMocks
    private SalaryService salaryService;

    private SalaryRequest createRequest() {
        SalaryRequest request = new SalaryRequest();

        request.setAmount(new BigDecimal("75000.00"));
        request.setCurrency(" inr ");
        request.setEffectiveDate(LocalDate.of(2026, 1, 1));
        request.setNotes("  Annual salary revision  ");

        return request;
    }

    @Test
    void createSalaryShouldSaveNormalizedSalaryRecord() {

        SalaryRequest request = createRequest();
        Employee employee = mock(Employee.class);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(salaryRecordRepository
                .existsByEmployee_IdAndEffectiveDate(
                        1L, request.getEffectiveDate()))
                .thenReturn(false);

        when(employee.getId()).thenReturn(1L);
        when(employee.getEmployeeCode()).thenReturn("EMP-1001");

        when(salaryRecordRepository.save(any(SalaryRecord.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SalaryResponse response =
                salaryService.createSalary(1L, request);

        assertEquals(1L, response.employeeId());
        assertEquals("EMP-1001", response.employeeCode());
        assertEquals(new BigDecimal("75000.00"), response.amount());
        assertEquals("INR", response.currency());
        assertEquals(
                LocalDate.of(2026, 1, 1),
                response.effectiveDate()
        );
        assertEquals("Annual salary revision", response.notes());

        verify(salaryRecordRepository).save(any(SalaryRecord.class));
    }

    @Test
    void createSalaryShouldReturnNotFoundForUnknownEmployee() {

        SalaryRequest request = createRequest();

        when(employeeRepository.findById(999L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> salaryService.createSalary(999L, request)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

        verify(salaryRecordRepository, never())
                .save(any(SalaryRecord.class));
    }

    @Test
    void createSalaryShouldRejectDuplicateEffectiveDate() {

        SalaryRequest request = createRequest();
        Employee employee = mock(Employee.class);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(salaryRecordRepository
                .existsByEmployee_IdAndEffectiveDate(
                        1L, request.getEffectiveDate()))
                .thenReturn(true);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> salaryService.createSalary(1L, request)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());

        verify(salaryRecordRepository, never())
                .save(any(SalaryRecord.class));
    }

    @Test
    void getSalaryHistoryShouldReturnNotFoundForUnknownEmployee() {

        when(employeeRepository.existsById(999L))
                .thenReturn(false);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> salaryService.getSalaryHistory(999L)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

        verify(salaryRecordRepository, never())
                .findByEmployee_IdOrderByEffectiveDateDescIdDesc(anyLong());
    }

    @Test
    void getSalaryHistoryShouldReturnSalaryRecords() {

        Employee employee = mock(Employee.class);

        when(employeeRepository.existsById(1L))
                .thenReturn(true);

        when(employee.getId()).thenReturn(1L);
        when(employee.getEmployeeCode()).thenReturn("EMP-1001");

        SalaryRecord salary = new SalaryRecord();
        salary.setEmployee(employee);
        salary.setAmount(new BigDecimal("75000.00"));
        salary.setCurrency("INR");
        salary.setEffectiveDate(LocalDate.of(2026, 1, 1));
        salary.setNotes("Annual revision");

        when(salaryRecordRepository
                .findByEmployee_IdOrderByEffectiveDateDescIdDesc(1L))
                .thenReturn(List.of(salary));

        List<SalaryResponse> result =
                salaryService.getSalaryHistory(1L);

        assertEquals(1, result.size());
        assertEquals(new BigDecimal("75000.00"), result.get(0).amount());
        assertEquals("INR", result.get(0).currency());
        assertEquals("EMP-1001", result.get(0).employeeCode());
    }

    @Test
    void getAllSalaryRecordsShouldRejectNegativePage() {

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> salaryService.getAllSalaryRecords(
                        null, null, null, null, -1, 10
                )
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

        verify(salaryRecordRepository, never())
                .searchSalaryRecords(
                        any(), any(), any(), any(), any(), any()
                );
    }

    @Test
    void getAllSalaryRecordsShouldRejectPageSizeGreaterThan100() {

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> salaryService.getAllSalaryRecords(
                        null, null, null, null, 0, 101
                )
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

        verify(salaryRecordRepository, never())
                .searchSalaryRecords(
                        any(), any(), any(), any(), any(), any()
                );
    }

    @Test
    void getAllSalaryRecordsShouldNormalizeFilters() {

        when(salaryRecordRepository.searchSalaryRecords(
                eq("Rahul"),
                isNull(),
                eq("India"),
                isNull(),
                eq("INR"),
                any()
        )).thenReturn(Page.empty());

        Page<?> result = salaryService.getAllSalaryRecords(
                " Rahul ",
                " India ",
                " ",
                " INR ",
                0,
                10
        );

        assertNotNull(result);

        verify(salaryRecordRepository).searchSalaryRecords(
                eq("Rahul"),
                isNull(),
                eq("India"),
                isNull(),
                eq("INR"),
                any()
        );
    }
}
