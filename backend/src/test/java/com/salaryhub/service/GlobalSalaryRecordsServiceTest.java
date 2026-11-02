
package com.salaryhub.service;

import com.salaryhub.dto.SalaryRecordListResponse;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalSalaryRecordsServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private SalaryRecordRepository salaryRecordRepository;

    @InjectMocks
    private SalaryService salaryService;

    @Test
    void shouldReturnSalaryRecordsWithEmployeeDetails() {

        Employee employee = mock(Employee.class);

        when(employee.getId()).thenReturn(1L);
        when(employee.getEmployeeCode()).thenReturn("EMP-1001");
        when(employee.getFirstName()).thenReturn("Rahul");
        when(employee.getLastName()).thenReturn("Sharma");
        when(employee.getCountry()).thenReturn("India");
        when(employee.getDepartment()).thenReturn("Engineering");
        when(employee.getJobTitle()).thenReturn("Software Engineer");

        SalaryRecord salary = new SalaryRecord();
        salary.setEmployee(employee);
        salary.setAmount(new BigDecimal("75000.00"));
        salary.setCurrency("INR");
        salary.setEffectiveDate(LocalDate.of(2026, 1, 1));
        salary.setNotes("Annual revision");

        Page<SalaryRecord> salaryPage = new PageImpl<>(
                List.of(salary),
                PageRequest.of(0, 10),
                1
        );

        when(salaryRecordRepository.searchSalaryRecords(
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                any()
        )).thenReturn(salaryPage);

        Page<SalaryRecordListResponse> result =
                salaryService.getAllSalaryRecords(
                        null, null, null, null, 0, 10
                );

        assertEquals(1, result.getTotalElements());

        SalaryRecordListResponse response = result.getContent().get(0);

        assertEquals("EMP-1001", response.employeeCode());
        assertEquals("Rahul Sharma", response.employeeName());
        assertEquals("India", response.country());
        assertEquals("Engineering", response.department());
        assertEquals("Software Engineer", response.jobTitle());
        assertEquals(new BigDecimal("75000.00"), response.amount());
        assertEquals("INR", response.currency());
    }

    @Test
    void shouldSearchByNumericEmployeeId() {

        when(salaryRecordRepository.searchSalaryRecords(
                eq("1001"),
                eq(1001L),
                isNull(),
                isNull(),
                isNull(),
                any()
        )).thenReturn(Page.empty());

        Page<SalaryRecordListResponse> result =
                salaryService.getAllSalaryRecords(
                        "1001", null, null, null, 0, 10
                );

        assertNotNull(result);

        verify(salaryRecordRepository).searchSalaryRecords(
                eq("1001"),
                eq(1001L),
                isNull(),
                isNull(),
                isNull(),
                any()
        );
    }

    @Test
    void shouldSearchByEmployeeName() {

        when(salaryRecordRepository.searchSalaryRecords(
                eq("Rahul Sharma"),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                any()
        )).thenReturn(Page.empty());

        salaryService.getAllSalaryRecords(
                "Rahul Sharma", null, null, null, 0, 10
        );

        verify(salaryRecordRepository).searchSalaryRecords(
                eq("Rahul Sharma"),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                any()
        );
    }

    @Test
    void shouldApplyCountryDepartmentAndCurrencyFilters() {

        when(salaryRecordRepository.searchSalaryRecords(
                isNull(),
                isNull(),
                eq("India"),
                eq("Engineering"),
                eq("INR"),
                any()
        )).thenReturn(Page.empty());

        salaryService.getAllSalaryRecords(
                null, "India", "Engineering", "INR", 0, 10
        );

        verify(salaryRecordRepository).searchSalaryRecords(
                isNull(),
                isNull(),
                eq("India"),
                eq("Engineering"),
                eq("INR"),
                any()
        );
    }

    @Test
    void shouldUseRequestedPageSizeAndDescendingSort() {

        when(salaryRecordRepository.searchSalaryRecords(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
        )).thenReturn(Page.empty());

        salaryService.getAllSalaryRecords(
                null, null, null, null, 2, 25
        );

        verify(salaryRecordRepository).searchSalaryRecords(
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                argThat(pageable -> {
                    if (pageable.getPageNumber() != 2
                            || pageable.getPageSize() != 25) {
                        return false;
                    }

                    Sort sort = pageable.getSort();

                    return sort.getOrderFor("effectiveDate") != null
                            && sort.getOrderFor("effectiveDate").isDescending()
                            && sort.getOrderFor("id") != null
                            && sort.getOrderFor("id").isDescending();
                })
        );
    }

    @Test
    void shouldTreatVeryLargeNumericSearchAsText() {

        String search = "999999999999999999999999999999";

        when(salaryRecordRepository.searchSalaryRecords(
                eq(search),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                any()
        )).thenReturn(Page.empty());

        salaryService.getAllSalaryRecords(
                search, null, null, null, 0, 10
        );

        verify(salaryRecordRepository).searchSalaryRecords(
                eq(search),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                any()
        );
    }
}
