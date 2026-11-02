
package com.salaryhub.repository;

import com.salaryhub.entity.Employee;
import com.salaryhub.entity.SalaryRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SalaryRecordRepositoryIntegrationTest {

    @Autowired
    private SalaryRecordRepository salaryRecordRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void shouldSearchSalaryRecordsByEmployeeCode() {
        createTestSalaryRecord();

        Page<SalaryRecord> result =
                salaryRecordRepository.searchSalaryRecords(
                        "INT-TEST-001",
                        null,
                        null,
                        null,
                        null,
                        PageRequest.of(0, 10)
                );

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0)
                .getEmployee().getEmployeeCode())
                .isEqualTo("INT-TEST-001");
    }

    @Test
    void shouldFilterSalaryRecordsByCountryDepartmentAndCurrency() {
        createTestSalaryRecord();

        Page<SalaryRecord> result =
                salaryRecordRepository.searchSalaryRecords(
                        null,
                        null,
                        "Integration Country",
                        "Integration Department",
                        "cad",
                        PageRequest.of(0, 10)
                );

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getCurrency())
                .isEqualTo("CAD");
    }

    @Test
    void shouldReturnCorrectPaginationDetails() {
        createTestSalaryRecord();

        Page<SalaryRecord> result =
                salaryRecordRepository.searchSalaryRecords(
                        "INT-TEST-001",
                        null,
                        null,
                        null,
                        null,
                        PageRequest.of(
                                0,
                                1,
                                Sort.by(
                                        Sort.Direction.DESC,
                                        "effectiveDate"
                                )
                        )
                );

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getTotalPages()).isEqualTo(1);
    }

    @Test
    void shouldReturnSalaryBreakdownByCountry() {
        createTestSalaryRecord();

        List<SalaryBreakdownProjection> result =
                salaryRecordRepository.findSalaryBreakdownByCountry();

        SalaryBreakdownProjection breakdown = result.stream()
                .filter(item ->
                        "Integration Country".equals(item.getGroupName())
                                && "CAD".equals(item.getCurrency()))
                .findFirst()
                .orElseThrow();

        assertThat(breakdown.getEmployeeCount()).isEqualTo(1);
        assertThat(breakdown.getAverageSalary())
                .isEqualByComparingTo("12345.67");
    }

    @Test
    void shouldReturnSalaryBreakdownByDepartment() {
        createTestSalaryRecord();

        List<SalaryBreakdownProjection> result =
                salaryRecordRepository.findSalaryBreakdownByDepartment();

        SalaryBreakdownProjection breakdown = result.stream()
                .filter(item ->
                        "Integration Department".equals(item.getGroupName())
                                && "CAD".equals(item.getCurrency()))
                .findFirst()
                .orElseThrow();

        assertThat(breakdown.getEmployeeCount()).isEqualTo(1);
        assertThat(breakdown.getAverageSalary())
                .isEqualByComparingTo("12345.67");
    }

    private void createTestSalaryRecord() {
        Employee employee = new Employee();

        employee.setEmployeeCode("INT-TEST-001");
        employee.setFirstName("Integration");
        employee.setLastName("Tester");
        employee.setEmail("integration.tester@example.com");
        employee.setCountry("Integration Country");
        employee.setDepartment("Integration Department");
        employee.setJobTitle("Test Engineer");
        employee.setHireDate(LocalDate.of(2025, 1, 1));
        employee.setEmploymentStatus("ACTIVE");

        Employee savedEmployee =
                employeeRepository.saveAndFlush(employee);

        SalaryRecord salaryRecord = new SalaryRecord();

        salaryRecord.setEmployee(savedEmployee);
        salaryRecord.setAmount(new BigDecimal("12345.67"));
        salaryRecord.setCurrency("CAD");
        salaryRecord.setEffectiveDate(LocalDate.now().minusDays(1));
        salaryRecord.setNotes("Integration test record");

        salaryRecordRepository.saveAndFlush(salaryRecord);
    }
}
